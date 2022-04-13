let edit_table = null, department_edit_table = null, project_edit_table = null;
let selected_edit_department = null, selected_edit_project = null;

$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $("#edit-task").click(function (event) {
    event.preventDefault();
    hideAllContent();
    $("#content-edit-task").show();
    if ($("#content-edit-task #department_edit_task_table").length != 0) {
      $("#content-edit-task #div_project_edit_task_table").hide();
      $("#content-edit-task #div_edit_tasks_table").hide();
      $("#content-edit-task #div_department_edit_task_table").show();
      if (department_edit_table != null) department_edit_table.destroy();
      loadDepartmenteditTable("#content-edit-task #department_edit_task_table", "/api/department/get_all/open");

      $("body").on("click", "#content-edit-task #select_department_edit", function (e) {
        e.stopPropagation();
        e.stopImmediatePropagation();
        selected_edit_department = $(this).val();
        showProjectSelectEditTable($(this).val());
      });
    } else showProjectSelectEditTable();

    $("body").on("click", "#content-edit-task #departmentBtnToEditTask", function () {
      $("#content-edit-task #div_edit_tasks_table").hide();
      $("#content-edit-task #div_project_edit_task_table").hide();
      $("#content-edit-task #div_department_edit_task_table").show();
      if (department_edit_table != null) department_edit_table.destroy();
      loadDepartmenteditTable("#content-edit-task #department_edit_task_table", "/api/department/get_all/open");

      $("body").on("click", "#content-edit-task #select_department_edit", function () {
        selected_edit_department = $(this).val();
        showProjectSelectEditTable($(this).val());
      });
    });

    $("body").on("click", "#content-edit-task #projectBtnToEditTask", function () {
      $("#content-edit-task #div_edit_tasks_table").hide();
      $("#content-edit-task #div_project_edit_task_table").show();
      $("#content-edit-task #div_department_edit_task_table").hide();
      showProjectSelectEditTable(selected_edit_department);
    });

    $("body").on("click", "#content-edit-task #open_tasks_btn", function () {
      if (edit_table != null) edit_table.destroy();
      loadeditTable("#content-edit-task #edit_tasks_table",
        "/api/task/get_all/project/" + selected_edit_project + "/by_status/OPEN");
    });

    $("body").on("click", "#content-edit-task #in_progress_tasks_btn", function () {
      if (edit_table != null) edit_table.destroy();
      loadeditTable("#content-edit-task #edit_tasks_table",
        "/api/task/get_all/project/" + selected_edit_project + "/by_status/IN_PROGRESS");
    });

    $("body").on("click", "#content-edit-task #done_tasks_btn", function () {
      if (edit_table != null) edit_table.destroy();
      loadeditTable("#content-edit-task #edit_tasks_table",
        "/api/task/get_all/project/" + selected_edit_project + "/by_status/DONE");
    });

    $("body").on("click", "#content-edit-task #closed_tasks_btn", function () {
      if (edit_table != null) edit_table.destroy();
      loadeditTable("#content-edit-task #edit_tasks_table",
        "/api/task/get_all/project/" + selected_edit_project + "/by_status/CLOSED");
    });

    let current_row = null;
    $("body").on('show.bs.modal', "#task_edit_modal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);

      let task_id = current_row.find('.task_id').text();
      modal.find('#modal_input_name').val(current_row.find('.task_name').text());
      modal.find('#modal_input_description').val(current_row.find('.task_description').text());
      modal.find('#modal_input_status').val(current_row.find('.task_status').text());

      $("#modal_task_save").click(function () {
        edit_task(task_id, $("#modal_input_name").val(), $("#modal_input_description").val(),
          $("#modal_input_status option:selected").text());
      })
    });
  });
});

function showProjectSelectEditTable(department_id) {
  $("#content-edit-task #div_department_edit_task_table").hide();
  $("#content-edit-task #div_project_edit_task_table").show();
  if (department_id == null) department_id = 0;
  if (project_edit_table != null) project_edit_table.destroy();
  loadProjecteditTable("#content-edit-task #project_edit_task_table",
    "/api/project/by_department/open/" + department_id);

  $("body").on("click", "#content-edit-task #select_project_edit", function () {
    selected_edit_project = $(this).val();
    show_edit_task($(this).val(), "OPEN");
  });
}

function show_edit_task(project_id, status) {
  $("#content-edit-task #div_project_edit_task_table").hide();
  $("#content-edit-task #div_edit_tasks_table").show();
  if (edit_table != null) edit_table.destroy();
  loadeditTable("#content-edit-task #edit_tasks_table", "/api/task/get_all/project/" + project_id + "/by_status/" + status);
}

function loadProjecteditTable(table_id, req_url) {
  project_edit_table = $(table_id).DataTable({
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
          return '<button type="button" class="btn btn-primary" id="select_project_edit"' +
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

function loadDepartmenteditTable(table_id, req_url) {
  department_edit_table = $(table_id).DataTable({
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
          return '<button type="button" class="btn btn-primary" id="select_department_edit"' +
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

function loadeditTable(table_id, req_url) {
  edit_table = $(table_id).DataTable({
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
      { "data": "id", "sClass": "task_id" },
      { "data": "name", "sClass": "task_name" },
      { "data": "description", "sClass": "task_description" },
      { "data": "status", "sClass": "task_status" },
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
      {
        "mData": null,
        "bSortable": false,
        "mRender": function (data) {
          return '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0"' +
            'data-toggle="modal" data-target="#task_edit_modal">Изменить</button>'
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function edit_task(task_id, task_name, task_description, task_status) {
  let task = {};
  task.id = task_id
  task.name = task_name;
  task.description = task_description;
  task.status = task_status;
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/task/edit/" + task.id,
    data: JSON.stringify(task),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data == "") {
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Задача с ID ` + task.id + ` изменена</div>`);
        edit_table.destroy();
        loadeditTable("#content-edit-task #edit_tasks_table",
          "/api/task/get_all/project/" + selected_edit_project + "/by_status/DONE");
      } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
}