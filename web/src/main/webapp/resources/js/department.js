let open_departments = null, close_departments = null;
let free_employee = null, assigned_employee = null;

$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $("#assign-user").click(function (event) {
    event.preventDefault();
    get_free_employee();
    $("#department_column").hide();
    show_assign_user_content("1", free_employee);

    $("body").on('click', 'input[name="user_radio"]', function () {
      if ($(this).val() == "1") {
        $("#department_column").hide();
        get_free_employee();
        show_assign_user_content("1", free_employee)
      }
      else {
        $("#department_column").show();
        get_assigned_employee();
        show_assign_user_content("2", assigned_employee);
      }
    });

    let current_row = null;
    $("body").on('show.bs.modal', "#assignUserModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);
      get_open_department();
      let content = ``;
      for (let pair of open_departments.entries()) {
        let department = pair[1];
        content += `<option value="` + (pair[0] + 1) + `">` + department.id + `-` + department.name + `</option>`;
      }
      $("#departmentSelect").empty();
      $("#departmentSelect").append(content);

      current_row.find('#department_column').is(':visible') ?
        modal.find('#departmentSelect select').val(current_row.find('#department_column').text()) : null;

      $("#assign_modal_save_btn").click(function () {
        current_row.find('#department_column').text($("#departmentSelect option:selected").text());
        assign_user(current_row, $("#departmentSelect option:selected").text());
      })
    });
  });

  $("#add-department").click(function (event) {
    event.preventDefault();
    show_add_department_content();

    $("body").on("click", "#department_save_btn", function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      add_department();
    });
  });

  $("#add-position").click(function (event) {
    event.preventDefault();
    show_add_position_content();

    $("body").on("click", "#position_save_btn", function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      add_position();
    });
  });

  $("#edit-department").click(function (event) {
    event.preventDefault();
    get_open_department();
    show_edit_department_content("1", open_departments);

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
      close_department($($(this).parent()).parent().find("#departmentId").text());
      $($(this).parent()).parent().remove();
    });
  });

  $("#activate-department").click(function (event) {
    event.preventDefault();
    event.stopImmediatePropagation();
    get_open_department();
    show_activate_department_content("1", open_departments);

    $("body").on('click', "#activate_department_btn", function () {
      activate_department($($(this).parent()).parent());
    });
  });

  $("#view-department").click(function (event) {
    event.preventDefault();
    get_open_department();
    show_view_department_content("1", open_departments);

    $("body").on('click', 'input[name="department_radio"]', function () {
      if ($(this).val() == "1") {
        get_open_department();
        show_view_department_content("1", open_departments)
      }
      else {
        get_close_department();
        show_view_department_content("2", close_departments);
      }
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

function show_add_department_content() {
  hideAllContent();
  $("#content-add-department").show();
  $('#departmentNameInput').val('');
  $(".alert").replaceWith(`<div class="alert"></div>`);
}

function show_add_position_content() {
  hideAllContent();
  $("#content-add-position").show();
  $('#positionNameInput').val('');
  $(".alert").replaceWith(`<div class="alert"></div>`);
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

function show_view_department_content(radio, array) {
  hideAllContent();
  $("#content-view-department").show();
  $("#view_departments_table").hide();
  set_radio_checked(radio, "content-view-department", "department_radio");
  if (show_alert(radio, array) == true) {
    $("#view_departments_table").show();
    let content = ``;
    for (let pair of array.entries()) {
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
    $("#view_departments_table tbody").empty();
    $("#view_departments_table tbody").append(content);
  }
}

function add_department() {
  show_preloader();
  let department = {};
  department.name = $('#departmentNameInput').val();
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/department/add",
    data: JSON.stringify(department),
    async: false,
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
  hide_preloader();
}

function add_position() {
  let position = {};
  position.name = $('#positionNameInput').val();
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/position/add",
    data: JSON.stringify(department),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
         Должность "` + data.name + `" с ID ` + data.id + ` создана</div>`);
      $('#positionNameInput').val('');
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      error.status == 423
        ? $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Данная должность уже существует!</div>`)
        : $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка добавления должности!</div>`);
    }
  });
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

function get_open_department() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/open",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      open_departments = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function get_close_department() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/closed",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      close_departments = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function edit_department(current_row) {
  show_preloader();
  let department = {};
  department.name = current_row.find('#name').text();
  department.id = current_row.find('#departmentId').text()
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/department/edit/" + department.id,
    data: JSON.stringify(department),
    async: false,
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
  hide_preloader();
}

function assign_user(current_row, department_text,) {
  let employee = {}, department = {};
  Object.assign(employee, { department });
  employee.id = current_row.find('#employeeId').text();
  employee.department.id = department_text.split("-")[0];
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/employee/assign/department",
    data: JSON.stringify(employee),
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
        Пользователь с ID `+ employee.id + ` назначен на отдел с ID ` + employee.department.id + `</div>`);
      current_row.find('#department_cell').text($("#departmentSelect option:selected").text());
      if ($("#transfer_user_btn").text() != "Перевод")
        current_row.remove();
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка назначение пользователя!</div>`);
    }
  });
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

function close_department(departmentId) {
  show_preloader();
  let department = {};
  department.id = departmentId;
  $.ajax({
    type: "DELETE",
    contentType: "application/json",
    url: "/api/department/close/" + departmentId,
    data: JSON.stringify(department),
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
        Отдел с ID ` + department.id + ` удалён</div>`);
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