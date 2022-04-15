let department_my_task_table = null, project_my_task_table = null;
let selected_my_task_department = null, selected_my_task_project = null;
let tasks = null;

$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $("#my-task").click(function (event) {
    event.preventDefault();
    hideAllContent();
    $("#content-my-task").show();
    $('.alert').replaceWith(`<div class="alert"></div>`);
    if ($("#content-my-task #department_my_task_task_table").length != 0) {
      $("#content-my-task #div_project_my_task_task_table").hide();
      $("#content-my-task #div_my_task_tasks_table").hide();
      $("#content-my-task #div_department_my_task_task_table").show();
      if (department_my_task_table != null) department_my_task_table.destroy();
      loadDepartmentmy_taskTable("#content-my-task #department_my_task_task_table", "/api/department/get_all/open");

      $("body").on("click", "#content-my-task #select_department_my_task", function (e) {
        e.stopPropagation();
        e.stopImmediatePropagation();
        selected_my_task_department = $(this).val();
        showProjectSelectmy_taskTable($(this).val());
      });
    } else showProjectSelectmy_taskTable();

    $("body").on("click", "#content-my-task #departmentBtnTomy_taskTask", function () {
      $("#content-my-task #div_my_task_tasks_table").hide();
      $("#content-my-task #div_project_my_task_task_table").hide();
      $("#content-my-task #div_department_my_task_task_table").show();
      if (department_my_task_table != null) department_my_task_table.destroy();
      loadDepartmentmy_taskTable("#content-my-task #department_my_task_task_table", "/api/department/get_all/open");

      $("body").on("click", "#content-my-task #select_department_my_task", function () {
        selected_my_task_department = $(this).val();
        showProjectSelectmy_taskTable($(this).val());
      });
    });

    $("body").on("click", "#content-my-task #projectBtnTomy_taskTask", function () {
      $("#content-my-task #div_my_task_tasks_table").hide();
      $("#content-my-task #div_project_my_task_task_table").show();
      $("#content-my-task #div_department_my_task_task_table").hide();
      showProjectSelectmy_taskTable(selected_my_task_department);
    });

    $("body").on("click", "#content-my-task #open_tasks_btn", function () {
      show_task_div(selected_my_task_project, "OPEN");
    });

    $("body").on("click", "#content-my-task #in_progress_tasks_btn", function () {
      show_task_div(selected_my_task_project, "IN_PROGRESS");
    });

    $("body").on("click", "#content-my-task #done_tasks_btn", function () {
      show_task_div(selected_my_task_project, "DONE");
    });

    $("body").on("click", "#content-my-task #closed_tasks_btn", function () {
      show_task_div(selected_my_task_project, "CLOSED");
    });

    $("body").on("click", "#content-my-task #edit_task_status_btn", function () {
      edit_task_status($(this).attr("value"));
    });
  });
});

function showProjectSelectmy_taskTable(department_id) {
  $("#content-my-task #div_department_my_task_task_table").hide();
  $("#content-my-task #div_project_my_task_task_table").show();
  if (department_id == null) department_id = 0;
  if (project_my_task_table != null) project_my_task_table.destroy();
  loadProjectmy_taskTable("#content-my-task #project_my_task_task_table",
    "/api/project/by_department/open/" + department_id);

  $("body").on("click", "#content-my-task #select_project_my_task", function () {
    selected_my_task_project = $(this).val();
    show_task_div($(this).val(), "OPEN");
  });
}

function loadProjectmy_taskTable(table_id, req_url) {
  project_my_task_table = $(table_id).DataTable({
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
          let message = get_message(localStorage.getItem("lang"),
            "task.alert.button.choice");
          return '<button type="button" class="btn btn-primary" id="select_project_my_task"' +
            'value="' + data.id + '">' + message + '</button>'
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function loadDepartmentmy_taskTable(table_id, req_url) {
  department_my_task_table = $(table_id).DataTable({
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
          let message = get_message(localStorage.getItem("lang"),
            "task.alert.button.choice");
          return '<button type="button" class="btn btn-primary" id="select_department_my_task"' +
            'value="' + data.id + '">' + message + '</button>'
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function edit_task_status(task_id) {
  let time = null;
  if ($("#time_input").length != 0) time = $("#time_input").val();
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/task/edit/status/" + task_id,
    data: JSON.stringify(time),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data == "")
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
      Статус изменён!</div>`);
      else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
}

function get_tasks_by_employee(project_id, status) {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/task/get_all/project/" + project_id + "/by_status/" + status + "/employee",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data.error != undefined) {
        $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
        tasks = null;
      }
      else tasks = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
  hide_preloader();
}

function show_task_div(project_id, task_status) {
  $('.alert').replaceWith(`<div class="alert"></div>`);
  $("#content-my-task #div_project_my_task_task_table").hide();
  $("#content-my-task #div_my_task_tasks_table").show();
  $("#content-my-task #div_my_task_tasks_table #task_blocks_div").show();
  get_tasks_by_employee(project_id, task_status);
  if (tasks != null) {
    let content = ``;
    for (let pair of tasks.entries()) {
      let task = pair[1];
      content +=
        `<a href="#" class="list-group-item list-group-item-action flex-column align-items-start" scope="row">
          <div class="d-flex w-100 justify-content-between">
            <h5 class="mb-1">`;
      content += task.name + `</h5><small>`;
      content += task.create_date + `</small></div><p class="mb-1">`;
      content += task.description + `</p>`;
      content += `<div class="row"><div class="col"><small>`
        + task.project.department.name + ` - ` + task.project.name + `</small></div>`;
      if (task.status == "DONE")
        content += `<div class="col-2"><input type="time" id="time_input" class="form-control"/></div>`;
      if (task.status != "CLOSED") {
        let message = get_message(localStorage.getItem("lang"),
          "task.alert.button.edit.status");
        content += `<div class="col-2">
        <button type="button" class="btn btn-secondary" id="edit_task_status_btn" 
        style="float: right" value="` + task.id + `">` + message + `</button></div>`;
      }
      content += `</div></a>`;
    }
    $("#content-my-task #div_my_task_tasks_table #task_blocks_div").empty();
    $("#content-my-task #div_my_task_tasks_table #task_blocks_div").append(content);
  }
  else $("#content-my-task #div_my_task_tasks_table #task_blocks_div").hide();
}