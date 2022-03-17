$(document).ready(function () {
  $("#add-department").click(function (event) {
    event.preventDefault();
    clearHelloBlock();
    show_add_department();

    $("body").on("click", "#department_save_btn", function (event) {
      event.preventDefault();
      add_department();
    });
  });

  $("#edit-department").click(function (event) {
    event.preventDefault();
    clearHelloBlock();
    show_edit_department();

    let current_row = null;
    $("body").on('show.bs.modal', "#departmentEditModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);

      modal.find('#department_modal_name').val(current_row.find('#name').text());

      $("#save_department_modal_btn").click(function () {
        current_row.find('#name').text($("#department_modal_name").val());
        edit_department(current_row);
      })
    });

    $("body").on("click", "#close_department_btn", function () {
      close_department($($(this).parent()).parent().find("#departmentId").text(), $($(this).parent()).parent());
    });
  });

  $("#activate-department").click(function (event) {
    event.preventDefault();
    clearHelloBlock();
    show_activate_department();

    let current_row = null;
    $("body").on('click', "#activate_department_btn", function () {
      current_row = $($(this).parent()).parent();
      activate_department(current_row);
    });
  });

  $("#view-department").click(function (event) {
    event.preventDefault();
    clearHelloBlock();
    show_view_open_department();

    $("body").on('click', 'input[name="department_radio"]', function () {
      if ($(this).val() == "1") show_view_open_department()
      else show_view_closed_department();
    });
  });
});

function clearHelloBlock() {
  $("#content").empty();
}

function show_edit_department() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/open",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = `<div class="container-fluid">
      <h3 class="mb-4">Редактирование отделов</h3>
      <div class="alert"></div>
      <div class="row">
        <div class="col">
          <table class="table table-hover">
            <thead>
              <tr>
                <th class="col-1">#</th>
                <th class="col-4">Наименование</th>
                <th class="col-2">Дата открытия</th>
                <th class="col-2">Активация</th>
                <th class="col-2">Действие</th>
              </tr>
            </thead>
            <tbody>`;
      for (let pair of data.entries()) {
        let department = pair[1];
        content += `<tr><th scope="row" id='departmentId'>` + department.id + `</th>`;
        content += `<td id='name'>` + department.name + `</td>`;
        department.start_date != null
          ? content += `<td>` + department.start_date + `</td>`
          : content += `<td>Отдел неактивен</td> `;
        if (department.active == false)
          content += `<td id='isActive'>-</td>`;
        else
          content += `<td id = 'isActive'> +</td> `;
        content +=
          `<td>
            <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
              data-toggle="modal" data-target="#departmentEditModal">Изменить</button>
            <button type="button" class="btn btn-danger btn-rounded btn-sm my-0" 
              id='close_department_btn'>Закрыть
            </button>
          </td>`;
        content += `</tr>`
      }
      content += `</tbody></table></div></div></div>`;
      content +=
        `<div class="modal fade" id="departmentEditModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle"
            aria-hidden="true">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title">Изменение отдела</h5>
              </div>
              <div class="modal-body">
                <form>
                  <div class="form-group">
                    <label>Наименование отдела</label>
                    <input type="text" class="form-control" id="department_modal_name" placeholder="Отдел Java разработки">
                  </div>
                </form>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                <button type="button" class="btn btn-primary" id="save_department_modal_btn" data-dismiss="modal">Сохранить</button>
              </div>
            </div>
          </div>
        </div>`;
      $("#content").append(content);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
}

function close_department(departmentId, current_element) {
  let department = {};
  department.id = departmentId;
  $.ajax({
    type: "DELETE",
    contentType: "application/json",
    url: "/api/department/close/" + departmentId,
    data: JSON.stringify(department),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
        Отдел с ID ` + department.id + ` удалён</div>`);
      current_element.remove();
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      if (error.status == 423)
        $('.alert').append(`<div class="alert alert-danger" role = "alert">
           Данный отдел активен и недоступен  для закрытия!</div>`)
      else if (error.status == 409)
        $('.alert').append(`<div class="alert alert-danger" role = "alert">
           В данном отделе существуют незакрытые проекты и недоступен  для закрытия!</div>`)
      else
        $('.alert').append(`<div class="alert alert-danger" role = "alert">
           Ошибка закрытия отдела!</div>`)
    }
  });

}

function edit_department(current_row) {
  let department = {};
  department.name = current_row.find('#name').text();
  department.id = current_row.find('#departmentId').text()
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/department/edit/" + department.id,
    data: JSON.stringify(department),
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
        Отдел с ID ` + department.id + ` изменён</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка изменения отдела!</div>`);
    }
  });
}

function show_activate_department() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/open",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = `<div class="container-fluid">
      <h3 class="mb-4">Активация отделов</h3>
      <div class="alert"></div>
      <div class="row">
        <div class="col">
          <table class="table table-hover">
            <thead>
              <tr>
                <th class="col-1">#</th>
                <th class="col-4">Наименование</th>
                <th class="col-2">Дата открытия</th>
                <th class="col-2">Активация</th>
                <th class="col-2">Действие</th>
              </tr>
            </thead>
            <tbody>`;
      for (let pair of data.entries()) {
        let department = pair[1];
        content += `<tr><th scope="row" id='departmentId'>` + department.id + `</th>`;
        content += `<td>` + department.name + `</td>`;
        department.start_date != null
          ? content += `<td>` + department.start_date + `</td>`
          : content += `<td>Отдел неактивен</td> `;
        if (department.active == false)
          content +=
            `<td id='isActive'>-</td>
            <td>
              <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
                  id="activate_department_btn">Активировать</button>
            </td>`;
        else
          content +=
            `<td id='isActive'>+</td>
            <td>
              <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
                  id="activate_department_btn">Деактивировать</button>
            </td>`;
        content += `</tr>`
      }
      content += `</tbody></table></div></div></div> `;
      $("#content").append(content);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
}

function activate_department(current_row) {
  let action = current_row.find('#activate_department_btn').text()
  let department = {};
  department.id = current_row.find('#departmentId').text();
  if (action === "Активировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/department/activate/" + department.id,
      data: JSON.stringify(department),
      cache: false,
      timeout: 600000,
      success: function () {
        current_row.find('#isActive').text('+');
        current_row.find('#activate_department_btn').text('Деактивировать');
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-success" role="alert">
          Отдел с ID ` + department.id + ` активирован</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-danger"role="alert">
          Ошибка активации!</div>`);
      }
    });
  }
  else if (action === "Деактивировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/department/inactivate/" + department.id,
      data: JSON.stringify(department),
      cache: false,
      timeout: 600000,
      success: function () {
        current_row.find('#isActive').text('-');
        current_row.find('#activate_department_btn').text('Активировать');
        $('.alert').empty();
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-success" role="alert">
          Отдел с ID ` + department.id + ` деактивирован</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-danger"role="alert">
          Ошибка активации!</div>`);
      }
    });
  }
}

function show_view_open_department() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/open",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      if (data == "") {
        content =
          `<div class="container-fluid">
            <h3 class="mb-4">Просмотр отделов</h3>
            <div class="alert alert-warning" role="alert">Список пуст!</div>
          </div>
          <div class="row">
              <div class="col">
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="department_radio" id="getOpenDepartmentsRadio" value="1"
                    checked>
                  <label class="form-check-label" for="getOpenDepartmentsRadio">Открытые отделы</label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="department_radio" id="getClosedDepartmentsRadio"
                    value="2">
                  <label class="form-check-label" for="getClosedDepartmentsRadio">Закрытые отделы</label>
                </div>
              </div>
            </div>`;
      }
      else {
        content =
          `<div class="container-fluid">
            <h3 class="mb-4">Просмотр отделов</h3>
            <div class="alert"></div>
            <div class="row">
              <div class="col">
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="department_radio" id="getOpenDepartmentsRadio" value="1"
                    checked>
                  <label class="form-check-label" for="getOpenDepartmentsRadio">Открытые отделы</label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="department_radio" id="getClosedDepartmentsRadio"
                    value="2">
                  <label class="form-check-label" for="getClosedDepartmentsRadio">Закрытые отделы</label>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th class="col-1">#</th>
                      <th class="col-4">Наименование</th>
                      <th class="col-2">Активация</th>
                      <th class="col-2">Дата открытия</th>
                    </tr>
                  </thead>
                  <tbody>`;
        for (let pair of data.entries()) {
          let department = pair[1];
          content += `<tr><th scope="row">` + department.id + `</th>`;
          content += `<td>` + department.name + `</td>`;
          if (department.active == false)
            content += `<td>-</td>`;
          else
            content += `<td>+</td>`;
          department.start_date != null
            ? content += `<td>` + department.start_date + `</td>`
            : content += `<td>Отдел неактивен</td> `;
          content += `</tr>`
        }
        content += `</tbody></table></div></div></div>`;
      }
      clearHelloBlock();
      $("#content").append(content);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div div class="alert alert-danger"role = "alert" >
          Ошибка!</div>`);
    }
  });
}

function show_view_closed_department() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/closed",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      if (data == "") {
        content =
          `<div class="container-fluid">
            <h3 class="mb-4">Просмотр отделов</h3>
            <div class="alert alert-warning" role="alert">Список пуст!</div>
          </div>
          <div class="row">
              <div class="col">
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="department_radio" id="getOpenDepartmentsRadio" 
                    value="1">
                  <label class="form-check-label" for="getOpenDepartmentsRadio">Открытые отделы</label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="department_radio" id="getClosedDepartmentsRadio"
                    value="2" checked>
                  <label class="form-check-label" for="getClosedDepartmentsRadio">Закрытые отделы</label>
                </div>
              </div>
            </div>`;
      }
      else {
        content =
          `<div class="container-fluid">
            <h3 class="mb-4">Просмотр отделов</h3>
            <div class="alert"></div>
            <div class="row">
              <div class="col">
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="department_radio" id="getOpenDepartmentsRadio" 
                    value="1">
                  <label class="form-check-label" for="getOpenDepartmentsRadio">Открытые отделы</label>
                </div>
                <div class="form-check">
                  <input class="form-check-input" type="radio" name="department_radio" id="getClosedDepartmentsRadio"
                    value="2" checked>
                  <label class="form-check-label" for="getClosedDepartmentsRadio">Закрытые отделы</label>
                </div>
              </div>
            </div>
            <div class="row">
              <div class="col">
                <table class="table table-hover">
                  <thead>
                    <tr>
                      <th class="col-1">#</th>
                      <th class="col-4">Наименование</th>
                      <th class="col-2">Дата открытия</th>
                      <th class="col-2">Дата закрытия</th>
                    </tr>
                  </thead>
                  <tbody>`;
        for (let pair of data.entries()) {
          let department = pair[1];
          content += `<tr><th scope="row" id='departmentId'>` + department.id + `</th>`;
          content += `<td id='name'>` + department.name + `</td>`;
          content += `<td>` + department.start_date + `</td> `;
          content += `<td>` + department.end_date + `</td> `;
          content += `</tr></tbody>`
        }
        content += `</tbody></table></div></div></div>`;
      }
      clearHelloBlock();
      $("#content").append(content);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div div class="alert alert-danger"role = "alert" >
          Ошибка!</div>`);
    }
  });
}

function show_add_department() {
  $("#content").append(`<div class="container-fluid">
  <h3 class="mb-4">Добавление отдела</h3>
  <div class="row alert"></div>
  <div class="row">
    <div class="col-5">
      <form>
        <div class="form-group">
          <label>Наименование</label>
          <input type="text" class="form-control" id="departmentNameInput"
            placeholder="Отдел Java разработки">
        </div>
        <div class="form-group" style="padding-top:5%;">
          <button class="btn btn-primary btn-block" type="submit" id='department_save_btn'>Сохранить</button>
        </div>
      </form>
    </div>
  </div>
</div>`);
}

function add_department() {
  let department = {};
  department.name = $('#departmentNameInput').val();
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/department/add",
    data: JSON.stringify(department),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
         Отдел "` + data.name + `" с ID ` + data.id + ` создан</div>`);
      $('#departmentNameInput').val('');
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      error.status == 423
        ? $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Данный отдел уже существует!</div>`)
        : $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка добавления отдела!</div>`);
    }
  });
}