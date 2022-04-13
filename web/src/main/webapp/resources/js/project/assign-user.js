let assign_user_table = null, department_assign_table = null;
let current_url_for_assign_user_table = "/api/employee/get_all/free";
let mode = "1";

$(document).ready(function () {
  $("#assign-user").click(function (event) {
    event.preventDefault();
    $('.alert').replaceWith(`<div class="alert"></div>`);
    hideAllContent();
    $("#content-assign-user").show();
    if ($("#content-assign-user #department_assign_users_table").length != 0) {
      $("#content-assign-user #div_assign_users_table").hide();
      $("#content-assign-user #tabs_to_assign_user").hide();
      $("#content-assign-user #div_department_assign_users_table").show();
      if (department_assign_table != null) department_assign_table.destroy();
      loadDepartmentAssignTable("#content-assign-user #department_assign_users_table", "/api/department/get_all/open");

      $("body").on("click", "#content-assign-user #select_department_to_assign", function () {
        showAssignUserTable($(this).val());
      });

      $("body").on("click", "#content-assign-user #selectDepartmentBtn", function () {
        $("#content-assign-user #div_assign_users_table").hide();
        $("#content-assign-user #tabs_to_assign_user").hide();
        $("#content-assign-user #div_department_assign_users_table").show();
      });
    } else showAssignUserTable();
  });
});

function showAssignUserTable(department_id) {
  $("#content-assign-user #div_department_assign_users_table").hide();
  if (department_id == null) department_id = 0;
  if (assign_user_table != null) assign_user_table.destroy();
  current_url_for_assign_user_table = "/api/employee/get_all/department/" + department_id;
  loadAssignUserTable("#content-assign-user #assign_users_table", current_url_for_assign_user_table);

  $("#content-assign-user #assignEmployeeBtn").click(function () {
    assign_user_table.destroy();
    current_url_for_assign_user_table = "/api/employee/get_all/department/" + department_id;
    mode = "1";
    loadAssignUserTable("#content-assign-user #assign_users_table", current_url_for_assign_user_table);
  });

  $("#content-assign-user #removeEmployeeBtn").click(function () {
    assign_user_table.destroy();
    current_url_for_assign_user_table = "/api/employee/get_with_project/department/" + department_id;
    mode = "2";
    loadAssignUserTable("#content-assign-user #assign_users_table", current_url_for_assign_user_table);
  });
}

function loadDepartmentAssignTable(table_id, req_url) {
  department_assign_table = $(table_id).DataTable({
    "processing": true,
    "serverSide": true,
    "pagingType": "full_numbers",
    "ajax": {
      "url": req_url,
      "type": "POST",
      "dataType": "json",
      "contentType": "application/json",
      "data": function (d) {
        return JSON.stringify(d);
      }
    },
    "columns": [
      { "data": "id" },
      { "data": "name" },
      {
        "mData": null,
        "bSortable": false,
        "mRender": function (data) {
          return '<button type="button" class="btn btn-primary" id="select_department_to_assign"' +
            'data-dismiss="modal" value="' + data.id + '">Выбрать</button>'
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function loadAssignUserTable(table_id, req_url) {
  $("#tabs_to_assign_user").show();
  $("#div_assign_users_table").show();
  assign_user_table = $(table_id).DataTable({
    "processing": true,
    "serverSide": true,
    "pagingType": "full_numbers",
    "ajax": {
      "url": req_url,
      "type": "POST",
      "dataType": "json",
      "contentType": "application/json",
      "data": function (d) {
        console.log(JSON.stringify(d));
        return JSON.stringify(d);
      }
    },
    "columns": [
      { "data": "id" },
      { "data": "user.username" },
      { "data": "name" },
      { "data": "user.authority.role" },
      {
        "data": "department", render: function (data) {
          return data.id + "-" + data.name;
        }
      },
      {
        "mData": null,
        "bSortable": false,
        "mRender": function (data) {
          return mode == "1"
            ? '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal"' +
            'data-target="#assignUserModal" id="assign_user_btn" value="' + data.id + '">Перевод</button>'
            : '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal"' +
            'data-target="#assignUserModal" id="assign_user_btn" value="' + data.id + '">Назначить</button>';
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function get_positions() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/position/get_all",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      data != "" ? positions = data : positions = "";
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function get_projects_by_department(department_id) {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/projects/open/" + department_id,
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      open_projects = data;
    },
    error: function (error) {
      console.log(error);
      $('#modal_alert').empty();
      error.status == 409
        ? $('#modal_alert').append(`<div class="alert alert-danger" role = "alert">
      Список проектов в отделе пуст!</div>`)
        : $('#modal_alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
    }
  });
  hide_preloader();
}

function get_projects_by_employee(employee_id) {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/project/by_employee/open/" + employee_id,
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      data != "" ? open_projects = data : open_projects = "";
    },
    error: function (error) {
      console.log(error);
      $('#modal_alert').empty();
      error.status == 409
        ? $('#modal_alert').append(`<div class="alert alert-danger" role = "alert">
      Список проектов пользователя пуст!</div>`)
        : $('#modal_alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
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

function assign_user(employee_id, department_id) {
  let employee = {}, department = {};
  Object.assign(employee, { department });
  employee.id = employee_id;
  employee.department.id = department_id;
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/employee/assign/department",
    data: JSON.stringify(employee),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data == "") {
        $('.alert').replaceWith(`<div div class="alert alert-success" role = "alert" >
            Пользователь с ID `+ employee.id + ` назначен на отдел с ID ` + employee.department.id + `</div > `);
        assign_user_table.destroy();
        loadAssignTable("#content-assign-user #edit_departments_table", current_url_for_assign_user_table);
      } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
}