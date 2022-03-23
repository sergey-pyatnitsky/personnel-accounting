const role_enum = Object.freeze({
  ADMIN: 'Администратор',
  DEPARTMENT_HEAD: 'Глава отдела',
  PROJECT_MANAGER: 'Проектный менеджер',
  EMPLOYEE: 'Сотрудник'
});

let employees = null, admins = null;

$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $("#activate-user").click(function (event) {
    event.preventDefault();
    get_employees();
    show_activate_user_content("1", employees);

    $("body").on('click', 'input[name="user_radio"]', function () {
      if ($(this).val() == "1") {
        get_employees();
        show_activate_user_content("1", employees);
      }
      else {
        get_admins();
        show_activate_user_content("2", admins);
      }
    });

    $("body").on('click', "#activate_user_btn", function () {
      activate_user($($(this).parent()).parent());
    });
  });

  $("#edit-user").click(function (event) {
    event.preventDefault();
    get_employees();
    show_edit_user_content("1", employees);

    $("body").on('click', 'input[name="user_radio"]', function () {
      if ($(this).val() == "1") {
        get_employees();
        show_edit_user_content("1", employees);
      }
      else {
        get_admins();
        show_edit_user_content("2", admins);
      }
    });

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

      $("body").on('hidden.bs.modal', "#editUserModal", function () {
        let radioValue = $('#content-edit-user input[name="user_radio"]:checked').val();
        if (radioValue != null && radioValue == "2") {
          get_admins();
          show_edit_user_content("2", admins);
        }
        else {
          get_employees();
          show_edit_user_content("1", employees);
        }

      });
    });

    $("body").on("click", "#removeUserBtn", function () {
      delete_employee($($(this).parent()).parent().find("#employeeId").text());
      $($(this).parent()).parent().remove();
    });
  });

  $("#view-user").click(function (event) {
    event.preventDefault();
    get_employees();
    show_view_user_content("1", employees);

    $("body").on('click', 'input[name="user_radio"]', function () {
      if ($(this).val() == "1") {
        get_employees();
        show_view_user_content("1", employees);
      }
      else {
        get_admins();
        show_view_user_content("2", admins);
      }
    });
  });
});

function show_activate_user_content(radio, array) {
  hideAllContent();
  $("#content-activate-user").show();
  $("#activate_users_table").hide();
  set_radio_checked(radio, "content-activate-user");
  if (show_alert(radio, array) == true) {
    $("#activate_users_table").show();
    let content = ``;
    for (let pair of array.entries()) {
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
        </td></tr>`;
    }
    $("#activate_users_table tbody").empty();
    $("#activate_users_table tbody").append(content);
  }
}

function show_edit_user_content(radio, array) {
  hideAllContent();
  $("#content-edit-user").show();
  $("#edit_users_table").hide();
  set_radio_checked(radio, "content-edit-user");
  if (show_alert(radio, array) == true) {
    $("#edit_users_table").show();
    let content = ``;
    for (let pair of array.entries()) {
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
            data-toggle="modal" data-target="#editUserModal">Изменить</button>
          <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
            id="removeUserBtn">Удалить</button>
        </td></tr>`;
    }
    $("#edit_users_table tbody").empty();
    $("#edit_users_table tbody").append(content);
  }
}

function show_view_user_content(radio, array) {
  hideAllContent();
  $("#content-view-user").show();
  $("#view_users_table").hide();
  set_radio_checked(radio, "content-view-user");
  if (show_alert(radio, array) == true) {
    $("#view_users_table").show();
    let content = ``;
    for (let pair of array.entries()) {
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
    $("#view_users_table tbody").empty();
    $("#view_users_table tbody").append(content);
  }
}

function show_alert(radio, array) {
  if (radio == "1" && array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список пользователей пуст!</div>`);
    return false;
  }
  else if (radio == "2" && array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список администраторов пуст!</div>`);
    return false;
  }
  else if (array == null) {
    $(".alert").replaceWith(`
      <div class="alert alert-danger"role="alert">Ошибка!</div>`);
    return false;
  }
  else return true;
}

function set_radio_checked(radio, context) {
  radio == "1"
    ? $('#' + context + ' input[name=user_radio][value="1"]').prop("checked", true)
    : $('#' + context + ' input[name=user_radio][value="2"]').prop("checked", true);
}

function get_employees() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all",
    async: false,
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      employees = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function get_admins() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all/admins",
    async: false,
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      admins = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function delete_employee(employeeId) {
  show_preloader();
  let employee = {};
  employee.id = employeeId;
  $.ajax({
    type: "DELETE",
    contentType: "application/json",
    url: "/api/employee/remove/" + employeeId,
    data: JSON.stringify(employee),
    async: false,
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Выполнено</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
  hide_preloader();
}

function edit_user(current_row) {
  show_preloader();
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
    async: false,
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Пользователь с ID ` + employee.id + ` изменён</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
  hide_preloader();
}

function activate_user(current_row) {
  show_preloader();
  let action = current_row.find('#activate_user_btn').text()
  let username = current_row.find('#username').text();
  if (action === "Активировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/employee/activate/" + username,
      async: false,
      cache: false,
      timeout: 600000,
      success: function () {
        current_row.find('#isActive').text('+');
        current_row.find('#activate_user_btn').text('Деактивировать');
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
          Пользователь с username ` + username + ` активирован</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').replaceWith(`<div class="alert alert-danger"role="alert">
        Ошибка активации!</div>`);
      }
    });
  }
  else if (action === "Деактивировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/employee/inactivate/" + username,
      cache: false,
      timeout: 600000,
      success: function () {
        current_row.find('#isActive').text('-');
        current_row.find('#activate_user_btn').text('Активировать');
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
          Пользователь с username ` + username + ` деактивирован</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').replaceWith(`<div class="alert alert-danger"role="alert">
        Ошибка деактивации!</div>`);
      }
    });
  }
  hide_preloader();
}

function hideAllContent() {
  $("#content-view-user").hide();
  $("#content-edit-user").hide();
  $("#content-activate-user").hide();
}