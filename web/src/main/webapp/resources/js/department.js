let open_departments = null, close_departments = null;
let free_employee = null, assigned_employee = null;

$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  

  $("#activate-department").click(function (event) {
    event.preventDefault();
    event.stopImmediatePropagation();
    get_open_department();
    show_activate_department_content("1", open_departments);

    $("body").on('click', "#activate_department_btn", function () {
      activate_department($($(this).parent()).parent());
    });
  });
});

function show_assign_user_content(radio, array) {
  hideAllContent();
  $("#content-assign-user").show();
  $("#assign_users_table").hide();
  set_radio_checked(radio, "content-assign-user", "user_radio");
  if (show_user_alert(radio, array) == true) {
    $("#assign_users_table").show();
    let content = ``;
    for (let pair of array.entries()) {
      let employee = pair[1];
      content += `<tr><th scope="row" id = "employeeId">` + employee.id + `</th>`;
      content += `<td id="username">` + employee.user.username + `</td>`;
      content += `<td>` + employee.name + `</td>`;
      content += `<td>` + role_enum[employee.user.role] + `</td>`;
      employee.department != null
        ? content += `<td id="department_cell">` + employee.department.id + `-` + employee.department.name + `</td>` +
        `<td><button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal" 
          data-target="#assignUserModal" id="transfer_user_btn">Перевод</button></td>`
        : content +=
        `<td><button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal" 
          data-target="#assignUserModal" id="transfer_user_btn">Назначить</button></td>`;
    }
    $("#assign_users_table tbody").empty();
    $("#assign_users_table tbody").append(content);
  }
}

function show_edit_department_content(radio, array) {
  hideAllContent();
  $("#content-edit-department").show();
  $("#edit_departments_table").hide();
  set_radio_checked(radio, "content-edit-department", "department_radio");
  if (show_alert(radio, array) == true) {
    $("#edit_departments_table").show();
    let content = ``;
    for (let pair of array.entries()) {
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
        </td></tr>`;
    }
    $("#edit_departments_table tbody").empty();
    $("#edit_departments_table tbody").append(content);
  }
}

function show_activate_department_content(radio, array) {
  hideAllContent();
  $("#content-activate-department").show();
  $("#activate_departments_table").hide();
  set_radio_checked(radio, "content-activate-department", "department_radio");
  if (show_alert(radio, array) == true) {
    $("#activate_departments_table").show();
    let content = ``;
    for (let pair of array.entries()) {
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
          </td></tr>`;
    }
    $("#activate_departments_table tbody").empty();
    $("#activate_departments_table tbody").append(content);
  }
}

function get_free_employee() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all/free",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      free_employee = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function get_assigned_employee() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all/assigned",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      assigned_employee = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function activate_department(current_row) {
  show_preloader();
  let action = current_row.find('#activate_department_btn').text()
  let department = {};
  department.id = current_row.find('#departmentId').text();
  if (action === "Активировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/department/activate/" + department.id,
      data: JSON.stringify(department),
      async: false,
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
      async: false,
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
  hide_preloader();
}

function show_user_alert(radio, array) {
  $(".alert").replaceWith(`<div class="alert"></div>`);
  if (radio == "1" && array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список свободных сотрудников пуст!</div>`);
    return false;
  }
  else if (radio == "2" && array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список пользователей пуст!</div>`);
    return false;
  }
  else if (array == null) {
    $(".alert").replaceWith(`
      <div class="alert alert-danger"role="alert">Ошибка!</div>`);
    return false;
  }
  else return true;
}

function show_alert(radio, array) {
  $(".alert").replaceWith(`<div class="alert"></div>`);
  if (radio == "1" && array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список открытых отделов пуст!</div>`);
    return false;
  }
  else if (radio == "2" && array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список закрытых отделов пуст!</div>`);
    return false;
  }
  else if (array == null) {
    $(".alert").replaceWith(`
      <div class="alert alert-danger"role="alert">Ошибка!</div>`);
    return false;
  }
  else return true;
}

function set_radio_checked(radio, context, radioID) {
  radio == "1"
    ? $('#' + context + ' input[name=' + radioID + '][value="1"]').prop("checked", true)
    : $('#' + context + ' input[name=' + radioID + '][value="2"]').prop("checked", true);
}

function hideAllContent() {
  $("#content-add-department").hide();
  $("#content-edit-department").hide();
  $("#content-activate-department").hide();
  $("#content-view-department").hide();
  $("#content-assign-user").hide();
  $("#content-add-position").hide();
}