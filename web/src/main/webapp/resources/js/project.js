let open_projects = null, close_projects = null, departments = null, employee = null;

$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $(function () {
    $('#departmentDivSelectEditModal').trigger('onload');
  });

  $("#assign-user").click(function (event) {
    event.preventDefault();
    $("#content-assign-user").show();
    $("#assign_user_to_project").show();
    getEmployeeDataForAssign("1");
    show_assign_user_content("1");
    $("body").on('click', 'input[name="user_radio"]', function () {
      if ($(this).val() == "1") {
        $("#assign_user_to_project").show();
        $("#remove_user_from_project").hide();
        getEmployeeDataForAssign("1");
        show_assign_user_content("1")
      }
      else {
        $("#remove_user_from_project").show();
        $("#assign_user_to_project").hide();
        getEmployeeDataForAssign("2");
        show_assign_user_content("2");
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

  $("#add-project").click(function (event) {
    event.preventDefault();
    show_add_project_content();

    $("body").on("click", "#project_add_btn", function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      add_project();
    });
  });

  $("#edit-project").click(function (event) {
    event.preventDefault();
    get_open_project();
    show_edit_project_content("1", open_projects);

    let current_row = null;
    $("body").on('show.bs.modal', "#projectEditModal", function (event) {
      get_departments();
      if (show_department_alert(departments) == true) {
        let content = ``;
        for (let pair of departments.entries()) {
          let department = pair[1];
          content += `<option value="` + (pair[0] + 1) + `">` + department.id + `-` + department.name + `</option>`;
        }
        $("#departmentSelectEditModal").empty();
        $("#departmentSelectEditModal").append(content);
      }

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
    get_open_project();
    show_activate_project_content("1", open_projects);

    $("body").on('click', "#activate_project_btn", function () {
      activate_project($($(this).parent()).parent());
    });
  });

  $("#view-project").click(function (event) {
    event.preventDefault();
    get_open_project();
    show_view_project_content("1", open_projects);

    $("body").on('click', 'input[name="project_radio"]', function () {
      if ($(this).val() == "1") {
        get_open_project();
        show_view_project_content("1", open_projects)
      }
      else {
        get_close_project();
        show_view_project_content("2", close_projects);
      }
    });
  });
});

function getEmployeeDataForAssign(value) {
  if ($("#get_employee_btn").length != 0) {
    $("#get_employee_btn").click(function () {
      let department_id = $("#departmentSelect option:selected").text().split("-")[0];
      value == "1" ? getEmployeeByDepartment(department_id) : getEmployeeWithProjectByDepartment(department_id);
    })
  } else {
    value == "1" ? getEmployeeByDepartment(0) : getEmployeeWithProjectByDepartment(0);
  }
}

function show_assign_user_content(radio, array) {
  hideAllContent();
  $("#content-assign-user").show();
  $("#assign_users_table").hide();
  set_radio_checked(radio, "content-assign-user", "user_radio");
  if ($("alert").is(':empty')) {
    $("#assign_users_table").show();
    let content = ``;
    for (let pair of array.entries()) {
      let employee = pair[1];
      content += `<tr><th scope="row" id = "employeeId">` + employee.id + `</th>`;
      content += `<td id="username">` + employee.user.username + `</td>`;
      content += `<td>` + employee.name + `</td>`;
      content += `<td>` + role_enum[employee.user.role] + `</td>`;
      content += `<td id="department_cell">` + employee.department.id + `-` + employee.department.name + `</td>`;
      content += `<td><button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal" 
          data-target="#assignUserModal" id="assign_user_btn">`;
      radio == "1" ? content += `Перевод` : content += `Снять`;
      content += `</button></td>`;

    }
    $("#assign_users_table tbody").empty();
    $("#assign_users_table tbody").append(content);
  }
}

function show_edit_project_content(radio, array) {
  hideAllContent();
  $("#content-edit-project").show();
  $("#edit_project_table").hide();
  set_radio_checked(radio, "content-edit-project");
  if (show_alert(radio, array) == true) {
    $("#edit_project_table").show();
    let content = ``;
    for (let pair of array.entries()) {
      let project = pair[1];
      content += `<tr><th scope="row" id='projectId'>` + project.id + `</th>`;
      content += `<td id='name'>` + project.name + `</td>`;
      content += `<td id='selected_department'>` + project.department.id + `-` + project.department.name + `</td>`;
      project.start_date != null
        ? content += `<td>` + project.start_date + `</td>`
        : content += `<td>Проект неактивен</td> `;
      project.active == false ? content += `<td>-</td>` : content += `<td>+</td>`;
      content +=
        `<td>
          <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
            data-toggle="modal" data-target="#projectEditModal">
            Изменить
          </button>
          <button type="button" class="btn btn-danger btn-rounded btn-sm my-0" id='remove_project_btn'>
            Закрыть
          </button>
        </td></tr>`;
    }
    $("#edit_project_table tbody").empty();
    $("#edit_project_table tbody").append(content);
  }
}

function show_activate_project_content(radio, array) {
  hideAllContent();
  $("#content-activate-project").show();
  $("#activate_departments_table").hide();
  set_radio_checked(radio, "content-activate-project");
  if (show_alert(radio, array) == true) {
    $("#activate_project_table").show();
    let content = ``;
    for (let pair of array.entries()) {
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
          </td></tr>`;
    }
    $("#activate_project_table tbody").empty();
    $("#activate_project_table tbody").append(content);
  }
}

function show_view_project_content(radio, array) {
  hideAllContent();
  $("#content-view-project").show();
  $("#view_projects_table").hide();
  set_radio_checked(radio, "content-view-project");
  if (show_alert(radio, array) == true) {
    $("#view_projects_table").show();
    let content = ``;
    for (let pair of array.entries()) {
      let project = pair[1];
      content += `<tr><th scope="row">` + project.id + `</th>`;
      content += `<td>` + project.name + `</td>`;
      content += `<td>` + project.department.id + `-` + project.department.name + `</td>`;
      project.active == true
        ? content += `<td>+</td>`
        : content += `<td>-</td>`;
      project.start_date != null
        ? content += `<td id='date'>` + project.start_date + `</td>`
        : content += `<td id='date'>Проект не стартовал</td>`;
      project.end_date != null
        ? content += `<td>` + project.end_date + `</td></tr>`
        : content += `<td>Проект активен</td></tr>`;
    }
    $("#view_projects_table tbody").empty();
    $("#view_projects_table tbody").append(content);
  }
}

function show_add_project_content() {
  hideAllContent();
  $("#content-add-project").show();
  $('#projectNameInput').val('');
  if ($("#departmentSelect").length) {
    if (show_department_alert(departments) == true) {
      let content = ``;
      for (let pair of departments.entries()) {
        let department = pair[1];
        content += `<option value="` + (pair[0] + 1) + `">` + department.id + `-` + department.name + `</option>`;
      }
      $("#departmentSelect").empty();
      $("#departmentSelect").append(content);
    }
  }
  else $(".alert").replaceWith(`<div class="alert"></div>`);
}

function activate_project(current_row) {
  show_preloader();
  let action = current_row.find('#activate_project_btn').text()
  let project = {};
  project.id = current_row.find('#projectId').text();
  if (action === "Активировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/project/activate/" + project.id,
      async: false,
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
      async: false,
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
  hide_preloader();
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

function edit_project(current_row) {
  let project = {}, department = {};
  Object.assign(project, { department });
  project.name = current_row.find('#name').text();
  project.id = current_row.find('#projectId').text();
  if ($("#departmentSelectEditModal").length != null)
    project.department.id = $("#departmentSelectEditModal option:selected").text();
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
        Проект с ID ` + project.id + ` изменён</div>`);
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

function get_departments() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/open",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      departments = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div div class="alert alert-danger"role = "alert" >
          Ошибка!</div>`);
    }
  });
  hide_preloader();
}

function get_open_project() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/project/get_all/open",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      open_projects = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function get_close_project() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/project/get_all/closed",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      close_projects = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function getEmployeeByDepartment(selected_department) {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all/department/" + selected_department,
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      employee = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      error.status == 409
        ? $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Список сотрудников с проектавми пуст!</div>`)
        : $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
    }
  });
  hide_preloader();
}

function getEmployeeWithProjectByDepartment(selected_department) {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_with_project/department/" + selected_department,
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      employee = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      error.status == 409
        ? $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Список сотрудников с проектавми пуст!</div>`)
        : $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
    }
  });
  hide_preloader();
}

function add_project() {
  show_preloader();
  let project = {}, department = {};
  Object.assign(project, { department });
  project.name = $('#projectNameInput').val();
  let value = $("#departmentSelect option:selected").text();
  if (value != null) {
    project.department.id = value.split("-")[0];
    project.department.name = value.split("-")[1];
  }
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
  hide_preloader();
}

function show_alert(radio, array) {
  $(".alert").replaceWith(`<div class="alert"></div>`);
  if (radio == "1" && array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список запущенных проектов пуст!</div>`);
    return false;
  }
  else if (radio == "2" && array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список закрытых проектов пуст!</div>`);
    return false;
  }
  else if (array == null) {
    $(".alert").replaceWith(`
      <div class="alert alert-danger"role="alert">Ошибка!</div>`);
    return false;
  }
  else return true;
}

function show_department_alert(array) {
  $(".alert").replaceWith(`<div class="alert"></div>`);
  if (array == "") {
    $(".alert").replaceWith(`
      <div class="alert alert-warning" role="alert">Список отделов пуст!</div>`);
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
    ? $('#' + context + ' input[name=project_radio][value="1"]').prop("checked", true)
    : $('#' + context + ' input[name=project_radio][value="2"]').prop("checked", true);
}

function hideAllContent() {
  $("#content-add-project").hide();
  $("#content-edit-project").hide();
  $("#content-activate-project").hide();
  $("#content-view-project").hide();
  $("#content-assign-user").hide();
}