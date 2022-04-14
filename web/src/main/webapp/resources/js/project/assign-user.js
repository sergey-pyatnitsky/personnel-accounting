let assign_user_table = null, department_assign_table = null, project_modal_table = null, positions = null;
let current_url_for_assign_user_table = "/api/employee/get_all/free";
let mode = "1", selected_department_project_assign;

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

    let current_row = null;
    $("body").on('show.bs.modal', "#assignUserModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);
      if (current_row.find("#assign_user_btn").text() == "Назначить") {
        get_positions();
        content = ``;
        for (let pair of positions.entries()) {
          let position = pair[1];
          content += `<option value="` + (pair[0] + 1) + `">` + position.id + `-` + position.name + `</option>`;
        }
        $("#positionSelect").empty();
        $("#positionSelect").append(content);
        $("#positionSelectDiv").show();
        if (project_modal_table != null) project_modal_table.destroy();
        loadProjectModalTable("#projectAssignModelTable", "/api/department/projects/open/" +
          current_row.find(".department_cell").text().split("-")[0]);
        modal.find("#modal_header_title").text("Назначение пользователя на проект");
      } else {
        if (project_modal_table != null) project_modal_table.destroy();
        loadProjectModalTable("#projectAssignModelTable", "/api/project/by_employee/open/" +
          current_row.find(".employee_id").text());
        modal.find("#modal_header_title").text("Снять пользователя с проекта");
        $("#positionSelectDiv").hide();
        $("#assign_modal_save_btn").hide();
      }

      $("body").on("click", "#content-assign-user #save_assign_project_modal_btn", function () {
        if (current_row.find("#assign_user_btn").text() == "Назначить")
          assign_user(current_row.find(".employee_id").text(), $(this).val(),
            $("#positionSelect option:selected").text().split("-")[0]);
        else
          cancel_user(current_row.find(".employee_id").text(), $(this).val());
      });
    });
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

function loadProjectModalTable(table_id, req_url) {
  project_modal_table = $(table_id).DataTable({
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
          return '<button type="button" class="btn btn-primary" id="save_assign_project_modal_btn"' +
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
        console.log(123);
        console.log(JSON.stringify(d));
        return JSON.stringify(d);
      }
    },
    "columns": [
      { "data": "id", "sClass": "employee_id" },
      { "data": "user.username" },
      { "data": "name" },
      { "data": "user.authority.role" },
      {
        "data": "department", render: function (data) {
          return data.id + "-" + data.name;
        }, "sClass": "department_cell"
      },
      {
        "mData": null,
        "bSortable": false,
        "mRender": function (data) {
          return mode == "1"
            ? '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal"' +
            'data-target="#assignUserModal" id="assign_user_btn" value="' + data.id + '">Назначить</button>'
            : '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal"' +
            'data-target="#assignUserModal" id="assign_user_btn" value="' + data.id + '">Снять</button>';
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

function assign_user(employee_id, project_id, position_id) {
  let employee_position = {}, project = {}, employee = {}, position = {};
  Object.assign(employee_position, { project });
  Object.assign(employee_position, { employee });
  Object.assign(employee_position, { position });
  employee_position.employee.id = employee_id;
  employee_position.project.id = project_id;
  employee_position.position.id = position_id;
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/project/assign/employee",
    data: JSON.stringify(employee_position),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data == "") {
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Пользователь с ID `+ employee_position.employee.id +
          ` назначен на проект с ID ` + employee_position.project.id + `</div>`);
        assign_user_table.destroy();
        loadAssignUserTable("#content-assign-user #assign_users_table", current_url_for_assign_user_table);
      } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
}

function cancel_user(employee_id, project_id) {
  let employee_position = {}, project = {}, employee = {};
  Object.assign(employee_position, { project });
  Object.assign(employee_position, { employee });
  employee_position.employee.id = employee_id;
  employee_position.project.id = project_id;
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/project/cancel/employee",
    data: JSON.stringify(employee_position),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data == "") {
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Пользователь с ID `+ employee_position.employee.id +
          ` снят с проекта с ID ` + employee_position.project.id + `</div>`);
        assign_user_table.destroy();
        loadAssignUserTable("#content-assign-user #assign_users_table", current_url_for_assign_user_table);
      } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
}