$(document).ready(function () {
  $("#add-project").click(function (event) {
    event.preventDefault();
    clearHelloBlock();
    show_add_project();

    $("body").on("click", "#project_add_btn", function (event) {
      event.preventDefault();
      add_project();
    });
  });

  $("#edit-project").click(function (event) {
    event.preventDefault();
    clearHelloBlock();
    show_edit_project();

    let current_row = null;
    $("body").on('show.bs.modal', "#projectEditModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);

      modal.find('#project_modal_name').val(current_row.find('#name').text());
      let value = current_row.find('#selected_department').text();
      $("#departmentSelectEditModal option[value='" + value + "']").attr("selected", "selected");

      $("#save_project_modal_btn").click(function () {
        current_row.find('#name').text($("#project_modal_name").val());
        current_row.find('#selected_department').text($("#departmentSelectEditModal option:selected").val());
        edit_project(current_row);
      })
    });

    $("body").on("click", "#remove_project_btn", function () {
      close_project($($(this).parent()).parent().find("#projectId").text(), $($(this).parent()).parent());
    });
  });

  $("#activate-project").click(function (event) {
    event.preventDefault();
    clearHelloBlock();
    show_activate_project();

    let current_row = null;
    $("body").on('click', "#activate_project_btn", function () {
      current_row = $($(this).parent()).parent();
      activate_project(current_row);
    });
  });

  $("#view-project").click(function (event) {
    event.preventDefault();
    clearHelloBlock();
    show_view_open_project();

    $("body").on('click', 'input[name="project_radio"]', function () {
      if ($(this).val() == "1") show_view_open_project()
      else show_view_closed_project();
    });
  });
});

function clearHelloBlock() {
  $("#content").empty();
}

function close_project(project_id, current_element) {
  let project = {};
  project.id = project_id;
  $.ajax({
    type: "DELETE",
    contentType: "application/json",
    url: "/api/project/close/" + project_id,
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
        Проект с ID ` + project.id + ` закрыт</div>`);
      current_element.remove();
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      if (error.status == 423)
        $('.alert').append(`<div class="alert alert-danger" role = "alert">
           Данный проект активен и недоступен для закрытия!</div>`)
      else if (error.status == 409)
        $('.alert').append(`<div class="alert alert-danger" role = "alert">
           В данном проекте существуют незакрытые задачи и недоступен для закрытия!</div>`)
      else
        $('.alert').append(`<div class="alert alert-danger" role = "alert">
           Ошибка закрытия проекта!</div>`)
    }
  });
}

function show_add_project() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/open",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      if (data == '') {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Добавление проекта</h3>
          <div class="alert alert-warning" role="alert">Список отделов пуст, поэтому невозможно добавлять проекты!</div>
          </div>`;
      } else {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Добавление проекта</h3>
          <div class="alert"></div>
          <div class="row">
            <div class="col-7">
              <form>
                <div class="form-group">
                  <label>Наименование</label>
                  <input type="text" class="form-control" id="projectNameInput" placeholder="Отдел Java разработки">
                </div>
                <div class="form-group">
                  <label>Отдел</label><br>
                  <select class="form-control" id='departmentSelect'>`;
        for (let pair of data.entries()) {
          let department = pair[1];
          content += `<option value="` + (pair[0] + 1) + `">` + department.id + `-` + department.name + `</option>`;
        }
        content += `</select></div>
          <div class="form-group" style="padding-top:5%;">
                  <button class="btn btn-primary btn-block" type="submit" id="project_add_btn">Сохранить</button>
                </div>
            </div>
          </div></div>`;
      }
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

function add_project() {
  let project = {}, department = {};
  Object.assign(project, { department });
  project.name = $('#projectNameInput').val();
  let value = $("#departmentSelect option:selected").text();
  project.department.id = value.split("-")[0];
  project.department.name = value.split("-")[1];
  project.active = false;
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/project/add",
    data: JSON.stringify(project),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
         Проект "` + data.name + `" с ID ` + data.id + ` создан</div>`);
      $('#projectNameInput').val('');
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      error.status == 423
        ? $('.alert').append(`<div class="alert alert-danger" role = "alert">
           Данный проект уже существует или отдел неактивен!</div>`)
        : $('.alert').append(`<div class="alert alert-danger" role = "alert">
            Ошибка добавления проекта!</div>`);
    }
  });
}

function show_edit_project() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/project/get_all/open",
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      if (data == '') {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Редактирование проектов</h3>
          <div class="alert alert-warning" role="alert">Список запущенных проектов пуст!</div>
          </div>`;
      } else {
        content = `<div class="container-fluid">
        <h3 class="mb-4">Редактирование проектов</h3>
        <div class="alert"></div>
        <div class="row">
          <div class="col">
            <table class="table table-hover">
              <thead>
                <tr>
                  <th class="col-1">#</th>
                  <th class="col-4">Наименование</th>
                  <th class="col-4">Отдел</th>
                  <th class="col-1" style="width: 15%">Дата создания</th>
                  <th class="col-1">Активация</th>
                  <th class="col-2">Действие</th>
                </tr>
              </thead>
              <tbody>`;

        for (let pair of data.entries()) {
          let project = pair[1];
          content += `<tr><th scope="row" id='projectId'>` + project.id + `</th>`;
          content += `<td id='name'>` + project.name + `</td>`;
          content += `<td id='selected_department'>` + project.department.id + `-` + project.department.name + `</td>`;
          project.start_date != null
            ? content += `<td>` + project.start_date + `</td>`
            : content += `<td>Проект неактивен</td> `;
          if (project.active == false)
            content += `<td>-</td>`;
          else
            content += `<td>+</td>`;
          content +=
            `<td>
              <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
                data-toggle="modal" data-target="#projectEditModal">
                Изменить
              </button>
              <button type="button" class="btn btn-danger btn-rounded btn-sm my-0" id='remove_project_btn'>
                Закрыть
              </button>
            </td>`;
          content += `</tr>`
        }
        content += `</tbody></table></div></div></div>`;
        content +=
          `<div class="modal fade" id="projectEditModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle"
              aria-hidden="true">
            <div class="modal-dialog" role="document">
              <div class="modal-content">
                <div class="modal-header">
                  <h5 class="modal-title">Изменение отдела</h5>
                </div>
                <div class="modal-body">
                  <form>
                    <div class="form-group">
                      <label>Наименование проекта</label>
                      <input type="text" class="form-control" id="project_modal_name" placeholder="Отдел Java разработки">
                    </div>
                    <div class="form-group">
                      <label>Отдел</label><br>
                      <select class="form-control" id="departmentSelectEditModal">`;
        load_all_departments();
        content += `</select>
                    </div>
                  </form>
                </div>
                <div class="modal-footer">
                  <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
                  <button type="button" class="btn btn-primary" id="save_project_modal_btn" data-dismiss="modal">Сохранить</button>
                </div>
              </div>
            </div>
          </div>`;
      }
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

function load_all_departments() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/open",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      for (let pair of data.entries()) {
        let department = pair[1];
        content += `<option value="` + department.id + `-` + department.name + `">` + department.id + `-` + department.name + `</option>`;
      }
      $('#departmentSelectEditModal').append(content);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div div class="alert alert-danger"role = "alert" >
          Ошибка получения списка отделов!</div>`);
    }
  });
}

function edit_project(current_row) {
  let project = {}, department = {};
  Object.assign(project, { department });
  project.name = current_row.find('#name').text();
  project.id = current_row.find('#projectId').text();
  project.department.id = $("#departmentSelectEditModal option:selected").val().split('-')[0];
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/project/edit/" + project.id,
    data: JSON.stringify(project),
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
        Проект с ID ` + department.id + ` изменён</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      if (error.status == 409)
        $('.alert').append(`<div class="alert alert-danger" role = "alert">
           Данный проект невозможно перенести в данный отдел!</div>`)
      else
        $('.alert').append(`<div class="alert alert-danger"role="alert">
          Ошибка изменения проекта!</div>`);


    }
  });
}

function show_activate_project() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/project/get_all/open",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      if (data == '') {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Активация проектов</h3>
          <div class="alert alert-warning" role="alert">Список запущенных проектов пуст!</div>
          </div>`;
      }
      else {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Активация проектов</h3>
          <div class="alert"></div>
          <div class="row">
            <div class="col">
              <table class="table table-hover">
                <thead>
                  <tr>
                    <th class="col-1">#</th>
                    <th class="col-4">Наименование</th>
                    <th class="col-4">Отдел</th>
                    <th class="col-1" style="width: 15%">Дата создания</th>
                    <th class="col-1">Активация</th>
                    <th class="col-1">Действие</th>
                  </tr>
                </thead>
                <tbody>`;
        for (let pair of data.entries()) {
          let project = pair[1];
          content += `<tr><th scope="row" id='projectId'>` + project.id + `</th>`;
          content += `<td>` + project.name + `</td>`;
          content += `<td>` + project.department.id + `-` + project.department.name + `</td>`;
          project.start_date != null
            ? content += `<td id='date'>` + project.start_date + `</td>`
            : content += `<td id='date'>Проект неактивен</td> `;
          if (project.active == false)
            content +=
              `<td id='isActive'>-</td>
            <td>
              <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
                  id="activate_project_btn">Активировать</button>
            </td>`;
          else
            content +=
              `<td id='isActive'>+</td>
            <td>
              <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
                  id="activate_project_btn">Деактивировать</button>
            </td>`;
          content += `</tr>`
        }
        content += `</tbody></table></div></div></div>`;
      }
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

function activate_project(current_row) {
  let action = current_row.find('#activate_project_btn').text()
  let project = {};
  project.id = current_row.find('#projectId').text();
  if (action === "Активировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/project/activate/" + project.id,
      cache: false,
      timeout: 600000,
      success: function (data) {
        current_row.find('#isActive').text('+');
        current_row.find('#activate_project_btn').text('Деактивировать');
        current_row.find('#date').text(data);
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-success" role="alert">
          Проект с ID ` + project.id + ` активирован</div>`);

      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        error.status == 409
          ? $('.alert').append(`<div class="alert alert-danger" role = "alert">
            Ошибка активации проекта! Проверьте отсутствие незавершённых задач</div>`)
          : $('.alert').append(`<div class="alert alert-danger" role = "alert">
            Ошибка активации проекта!</div>`);
      }
    });
  }
  else if (action === "Деактивировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/project/inactivate/" + project.id,
      data: JSON.stringify(project),
      cache: false,
      timeout: 600000,
      success: function () {
        current_row.find('#isActive').text('-');
        current_row.find('#activate_project_btn').text('Активировать');
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-success" role="alert">
          Проект с ID ` + project.id + ` деактивирован</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        error.status == 409
          ? $('.alert').append(`<div class="alert alert-danger" role = "alert">
            Ошибка деактивации проекта! Проверьте отсутствие незавершённых задач</div>`)
          : $('.alert').append(`<div class="alert alert-danger" role = "alert">
            Ошибка деактивации проекта!</div>`);
      }
    });
  }
}

function show_view_open_project() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/project/get_all/open",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      if (data == '') {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Просмотр проектов</h3>
          <div class="alert alert-warning" role="alert">Список запущенных проектов пуст!</div>
          </div>
          <div class="row">
            <div class="col">
              <div class="form-check">
                <input class="form-check-input" type="radio" name="project_radio" id="getOpenProjectsRadio" 
                  value="1" checked>
                <label class="form-check-label" for="getOpenProjectsRadio">Запущенные проекты</label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="radio" name="project_radio" id="getClosedProjectsRadio"
                  value="2">
                <label class="form-check-label" for="getClosedProjectsRadio">Закрытые проекты</label>
              </div>
            </div>
          </div>`;
      } else {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Просмотр проектов</h3>
          <div class="alert"></div>
          <div class="row">
            <div class="col">
              <div class="form-check">
                <input class="form-check-input" type="radio" name="project_radio" id="getOpenProjectsRadio" 
                  value="1" checked>
                <label class="form-check-label" for="getOpenProjectsRadio">Запущенные проекты</label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="radio" name="project_radio" id="getClosedProjectsRadio"
                  value="2">
                <label class="form-check-label" for="getClosedProjectsRadio">Закрытые проекты</label>
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
                    <th class="col-4">Отдел</th>
                    <th class="col-2">Дата открытия</th>
                    <th class="col-1">Активация</th>
                  </tr>
                </thead>
                <tbody>`;
        for (let pair of data.entries()) {
          let project = pair[1];
          content += `<tr><th scope="row">` + project.id + `</th>`;
          content += `<td>` + project.name + `</td>`;
          content += `<td>` + project.department.id + `-` + project.department.name + `</td>`;
          project.start_date != null
            ? content += `<td>` + project.start_date + `</td>`
            : content += `<td>Проект неактивен</td> `;
          project.active == false
            ? content += `<td>-</td>`
            : content += `<td>+</td>`;
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
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
}

function show_view_closed_project() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/project/get_all/closed",
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      if (data == '') {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Просмотр проектов</h3>
          <div class="alert alert-warning" role="alert">Список закрытых проектов пуст!</div>
          </div>
          <div class="row">
            <div class="col">
              <div class="form-check">
                <input class="form-check-input" type="radio" name="project_radio" id="getOpenProjectsRadio" 
                  value="1">
                <label class="form-check-label" for="getOpenProjectsRadio">Запущенные проекты</label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="radio" name="project_radio" id="getClosedProjectsRadio"
                  value="2" checked>
                <label class="form-check-label" for="getClosedProjectsRadio">Закрытые проекты</label>
              </div>
            </div>
          </div>`;
      } else {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Просмотр проектов</h3>
          <div class="alert"></div>
          <div class="row">
            <div class="col">
              <div class="form-check">
                <input class="form-check-input" type="radio" name="project_radio" id="getOpenProjectsRadio" 
                  value="1">
                <label class="form-check-label" for="getOpenProjectsRadio">Запущенные проекты</label>
              </div>
              <div class="form-check">
                <input class="form-check-input" type="radio" name="project_radio" id="getClosedProjectsRadio"
                  value="2" checked>
                <label class="form-check-label" for="getClosedProjectsRadio">Закрытые проекты</label>
              </div>
            </div>
          </div>
          <div class="row">
            <div class="col">
              <table class="table table-hover">
                <thead>
                  <tr>
                    <th class="col-1">#</th>
                    <th class="col-3">Наименование</th>
                    <th class="col-4">Отдел</th>
                    <th class="col-2">Дата открытия</th>
                    <th class="col-2">Дата закрытия</th>
                  </tr>
                </thead>
                <tbody>`;
        for (let pair of data.entries()) {
          let project = pair[1];
          content += `<tr><th scope="row">` + project.id + `</th>`;
          content += `<td>` + project.name + `</td>`;
          content += `<td>` + project.department.id + `-` + project.department.name + `</td>`;
          content += `<td>` + project.start_date + `</td> `;
          content += `<td>` + project.end_date + `</td> `;
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
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
}