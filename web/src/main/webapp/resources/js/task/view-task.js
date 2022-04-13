let view_table = null, department_view_table = null, project_view_table = null;
let selected_department = null, selected_project = null;

$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $("#view-task").click(function (event) {
    event.preventDefault();
    hideAllContent();
    $("#content-view-task").show();
    if ($("#content-view-task #department_view_task_table").length != 0) {
      $("#content-view-task #div_project_view_task_table").hide();
      $("#content-view-task #div_view_tasks_table").hide();
      $("#content-view-task #div_department_view_task_table").show();
      if (department_view_table != null) department_view_table.destroy();
      loadDepartmentViewTable("#content-view-task #department_view_task_table", "/api/department/get_all/open");

      $("body").on("click", "#content-view-task #select_department_view", function () {
        selected_department = $(this).val();
        showProjectSelectTable($(this).val());
      });
    } else showProjectSelectTable();

    $("body").on("click", "#content-view-task #departmentBtnToAddTask", function () {
      $("#content-view-task #div_view_tasks_table").hide();
      $("#content-view-task #div_project_view_task_table").hide();
      $("#content-view-task #div_department_view_task_table").show();
      if (department_view_table != null) department_view_table.destroy();
      loadDepartmentViewTable("#content-view-task #department_view_task_table", "/api/department/get_all/open");

      $("body").on("click", "#content-view-task #select_department_view", function () {
        selected_department = $(this).val();
        showProjectSelectTable($(this).val());
      });
    });

    $("body").on("click", "#content-view-task #projectBtnToAddTask", function () {
      $("#content-view-task #div_view_tasks_table").hide();
      $("#content-view-task #div_project_view_task_table").show();
      $("#content-view-task #div_department_view_task_table").hide();
      showProjectSelectTable(selected_department);
    });

    $("body").on("click", "#content-view-task #open_tasks_btn", function () {
      if (view_table != null) view_table.destroy();
      loadViewTable("#content-view-task #view_tasks_table",
        "/api/task/get_all/project/" + selected_project + "/by_status/OPEN");
    });

    $("body").on("click", "#content-view-task #in_progress_tasks_btn", function () {
      if (view_table != null) view_table.destroy();
      loadViewTable("#content-view-task #view_tasks_table",
        "/api/task/get_all/project/" + selected_project + "/by_status/IN_PROGRESS");
    });

    $("body").on("click", "#content-view-task #done_tasks_btn", function () {
      if (view_table != null) view_table.destroy();
      loadViewTable("#content-view-task #view_tasks_table",
        "/api/task/get_all/project/" + selected_project + "/by_status/DONE");
    });

    $("body").on("click", "#content-view-task #closed_tasks_btn", function () {
      if (view_table != null) view_table.destroy();
      loadViewTable("#content-view-task #view_tasks_table",
        "/api/task/get_all/project/" + selected_project + "/by_status/CLOSED");
    });
  });
});

function showProjectSelectTable(department_id) {
  $("#content-view-task #div_department_view_task_table").hide();
  $("#content-view-task #div_project_view_task_table").show();
  if (department_id == null) department_id = 0;
  if (project_view_table != null) project_view_table.destroy();
  loadProjectViewTable("#content-view-task #project_view_task_table",
    "/api/project/by_department/open/" + department_id);

  $("body").on("click", "#content-view-task #select_project_view", function () {
    selected_project = $(this).val();
    show_view_task($(this).val(), "OPEN");
  });
}

function show_view_task(project_id, status) {
  $("#content-view-task #div_project_view_task_table").hide();
  $("#content-view-task #div_view_tasks_table").show();
  if (view_table != null) view_table.destroy();
  loadViewTable("#content-view-task #view_tasks_table", "/api/task/get_all/project/" + project_id + "/by_status/" + status);
}

function loadProjectViewTable(table_id, req_url) {
  project_view_table = $(table_id).DataTable({
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
          return '<button type="button" class="btn btn-primary" id="select_project_view"' +
            'value="' + data.id + '">Выбрать</button>'
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function loadDepartmentViewTable(table_id, req_url) {
  department_view_table = $(table_id).DataTable({
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
          return '<button type="button" class="btn btn-primary" id="select_department_view"' +
            'value="' + data.id + '">Выбрать</button>'
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function loadViewTable(table_id, req_url) {
  view_table = $(table_id).DataTable({
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
      { "data": "description" },
      { "data": "status" },
      { "data": "create_date" },
      {
        "data": "project", render: function (data) {
          return data.id + "-" + data.name;
        }
      },
      {
        "data": "assignee", render: function (data) {
          return data.id + "-" + data.name;
        }
      },
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}