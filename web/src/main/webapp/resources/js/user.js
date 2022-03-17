const role_enum = Object.freeze({
  SUPER_ADMIN: 'Супервользователь',
  ADMIN: 'Администратор',
  DEPARTMENT_HEAD: 'Глава отдела',
  PROJECT_MANAGER: 'Проектный менеджер',
  EMPLOYEE: 'Сотрудник'
});

$(document).ready(function () {
  $("#activate-user").click(function (event) {
    event.preventDefault();
    clearHello();
    show_activate_user();

    let current_row = null;
    $("body").on('show.bs.modal', "#activateUserModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      activate_user(current_row);
    });
  });

  $("#edit-user").click(function (event) {
    event.preventDefault();
    clearHello();
    show_edit_user();

    let current_row = null;
    $("body").on('show.bs.modal', "#editUserModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);

      modal.find('#inputNameEditModal').val(current_row.find('#name').text());
      modal.find('#roleSelectEditModal').val(current_row.find('#role').text());

      $("#modal_save").click(function () {
        current_row.find('#name').text($("#inputNameEditModal").val());
        current_row.find('#role').text($("#roleSelectEditModal").val());
        edit_user(current_row);
      })
    });

    $("body").on("click", "#removeUserBtn", function (event) {
      delete_employee($($(this).parent()).parent().find("#employeeId").text());
      $($(this).parent()).parent().remove();
    });
  });

  $("#view-user").click(function (event) {
    event.preventDefault();
    clearHello();
    show_view_user();
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

function clearHello() {
  $("#content").empty();
}

function show_view_user() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all",
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = '';
      if (data == "") {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Просмотр пользователей</h3>
          <div class="alert alert-warning" role="alert">Список пуст!</div>
          </div>`;
      }
      else {
        content =
          `<div class="container-fluid">
          <h3 class="mb-4">Просмотр пользователей</h3>
          <div class="alert"></div>
          <div class="row">
            <div class="col">
              <table class="table table-hover">
                <thead>
                  <tr>
                    <th class="col-1">#</th>
                    <th class="col-2">Логин</th>
                    <th class="col-4">ФИО</th>
                    <th class="col-2">Роль</th>
                    <th class="col-1">Активация</th>
                  </tr>
                </thead>
                <tbody>`;

        for (let pair of data.entries()) {
          let employee = pair[1];
          content += `<tr><th scope="row">` + employee.id + `</th>`;
          content += `<td>` + employee.user.username + `</td>`;
          content += `<td>` + employee.name + `</td>`;
          content += `<td>` + role_enum[employee.user.role] + `</td>`;
          if (employee.user.active == false)
            content += `<td>-</td>`;
          else
            content += `<td>+</td>`;
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

function show_edit_user() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all",
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = `<div class="container-fluid">
      <h3 class="mb-4">Редактирование пользователей</h3>
      <div class="alert"></div>
      <div class="row">
        <div class="col">
          <table class="table table-hover">
            <thead>
              <tr>
                <th class="col-1">#</th>
                <th class="col-2">Логин</th>
                <th class="col-3">ФИО</th>
                <th class="col-2">Роль</th>
                <th class="col-1">Активация</th>
                <th class="col-2">Действие</th>
              </tr>
            </thead>
            <tbody>`;

      for (let pair of data.entries()) {
        let employee = pair[1];
        content += `<tr><th scope="row" id="employeeId">` + employee.id + `</th>`;
        content += `<td id="username">` + employee.user.username + `</td>`;
        content += `<td id="name">` + employee.name + `</td>`;
        content += `<td id="role">` + role_enum[employee.user.role] + `</td>`;
        if (employee.user.active == false)
          content += `<td>-</td>`;
        else
          content += `<td>+</td>`;
        content +=
          `<td>
            <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
                data-toggle="modal" data-target="#editUserModal">
              Изменить
            </button>
            <button type="button" class="btn btn-danger btn-rounded btn-sm my-0" id="removeUserBtn">
              Удалить
            </button></td>`;
        content += `</tr>`
      }
      content += `</tbody></table></div></div></div>
      <div class="modal fade" id="editUserModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLongTitle"
        aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLongTitle">Изменение пользователя</h5>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
                <label>ФИО</label>
                <input type="text" class="form-control" id="inputNameEditModal" placeholder="Отдел Java разработки">
              </div>
              <div class="form-group">
                <label>Роль пользователя</label><br>
                <select class="form-control" id="roleSelectEditModal">
                  <option value="Супервользователь">Супервользователь</option>
                  <option value="Администратор">Администратор</option>
                  <option value="Глава отдела">Глава отдела</option>
                  <option value="Проектный менеджер">Проектный менеджер</option>
                  <option value="Сотрудник">Сотрудник</option>
                </select>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
            <button type="button" class="btn btn-primary" id="modal_save" data-dismiss="modal">Сохранить</button>
          </div>
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

function delete_employee(employeeId) {
  let employee = {};
  employee.id = employeeId;
  $.ajax({
    type: "DELETE",
    contentType: "application/json",
    url: "/api/employee/remove/" + employeeId,
    data: JSON.stringify(employee),
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
        Выполнено</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
}

function edit_user(current_row) {
  let employee = {};
  employee.name = current_row.find('#name').text();
  Object.assign(employee, {
    user: {
      role: Object.keys(role_enum)
        .find(item => role_enum[item] == current_row.find('#role').text())
    }
  });
  employee.id = current_row.find('#employeeId').text()
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/employee/edit",
    data: JSON.stringify(employee),
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
        Выполнено</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
}

function show_activate_user() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all",
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      let content = `<div class="container-fluid">
      <h3 class="mb-4">Просмотр пользователей</h3>
      <div class="alert"></div>
      <div class="row">
        <div class="col">
          <table class="table table-hover">
            <thead>
              <tr>
                <th class="col-1">#</th>
                <th class="col-2">Логин</th>
                <th class="col-4">ФИО</th>
                <th class="col-2">Роль</th>
                <th class="col-2">Активация</th>
                <th class="col-1">Действие</th>
              </tr>
            </thead>
            <tbody>`;

      for (let pair of data.entries()) {
        let employee = pair[1];
        content += `<tr><th scope="row">` + employee.id + `</th>`;
        content += `<td id="username">` + employee.user.username + `</td>`;
        content += `<td>` + employee.name + `</td>`;
        content += `<td>` + role_enum[employee.user.role] + `</td>`;
        if (employee.user.active == false)
          content +=
            `<td id='isActive'>-</td>
            <td>
              <button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal" data-target="#activateUserModal"
                  id="activate_user_btn">Активировать</button>
            </td>`;
        else
          content +=
            `<td id='isActive'>+</td>
            <td>
              <button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal" data-target="#activateUserModal"
                  id="activate_user_btn">Деактивировать</button>
            </td>`;
        content += `</tr>`
      }
      content += `</tbody></table></div></div></div>`;
      content +=
        `<div class="modal fade" id="activateUserModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
          <div class="modal-dialog" role="document">
            <div class="modal-content">
              <div class="modal-header">
                <h5 class="modal-title" id="exampleModalLabel">Активация</h5>
              </div>
              <div class="modal-body">
                <p>Выполнено</p>
              </div>
              <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">Закрыть</button>
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

function activate_user(current_row) {
  let action = current_row.find('#activate_user_btn').text()
  let username = current_row.find('#username').text();
  if (action === "Активировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/employee/activate/" + username,
      cache: false,
      timeout: 600000,
      success: function () {
        current_row.find('#isActive').text('+');
        current_row.find('#activate_user_btn').text('Деактивировать');
        $('.alert').append(`<div class="alert alert-success" role="alert">
          Пользователь с username ` + username + ` активирован</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка активации!</div>`);
      }
    });
  }
  else if (action === "Деактивировать")
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/employee/inactivate/" + username,
      cache: false,
      timeout: 600000,
      success: function () {
        current_row.find('#isActive').text('-');
        current_row.find('#activate_user_btn').text('Активировать');
        $('.alert').append(`<div class="alert alert-success" role="alert">
          Пользователь с username ` + username + ` деактивирован</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка деактивации!</div>`);
      }
    });
}