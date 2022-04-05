current_task_status = Object.keys(task_status).find(item => task_status[item] == "Открыта");

$(document).ready(function () {
  $("#edit-task").click(function (event) {
    event.preventDefault();
    show_edit_task();

    $("#content-edit-task #open_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Открыта");
      show_tasks("2", "#content-edit-task", "#projectSelectToEditTask", "#edit_tasks_table");
    });

    $("#content-edit-task #in_progress_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Выполняется");
      show_tasks("2", "#content-edit-task", "#projectSelectToEditTask", "#edit_tasks_table");
    });

    $("#content-edit-task #done_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Выполнена");
      show_tasks("2", "#content-edit-task", "#projectSelectToEditTask", "#edit_tasks_table");
    });

    $("#content-edit-task #closed_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Завершена");
      show_tasks("2", "#content-edit-task", "#projectSelectToEditTask", "#edit_tasks_table");
    });

    let current_row = null;
    $("body").on('show.bs.modal', function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);

      modal.find('#modal_input_name').val(current_row.find('#name').text());
      modal.find('#modal_input_description').val(current_row.find('#description').text());
      modal.find('#modal_input_status').val(current_row.find('#status').text());

      $("#modal_task_save").click(function () {
        current_row.find('#name').text($("#modal_input_name").val());
        current_row.find('#description').text($("#modal_input_description").val());
        current_row.find('#status').text($("#modal_input_status option:selected").text());
        edit_task(current_row);
      })
    });
  });

});

function show_edit_task() {
  hideAllContent();
  $("#content-edit-task").show();
  $("#edit_tasks_div").hide();
  if ($("#content-edit-task #departmentSelectToEditTask").length) {
    get_departments();
    if (show_department_alert(departments) == true) {
      let content = ``;
      for (let pair of departments.entries()) {
        let department = pair[1];
        content += `<option value="` + (pair[0] + 1) + `">` + department.id + `-` + department.name + `</option>`;
      }
      $("#content-edit-task #departmentSelectToEditTask").empty();
      $("#content-edit-task #departmentSelectToEditTask").append(content);
      get_projects($("#content-edit-task #departmentSelectToEditTask option:selected").text().split("-")[0]);
    }
  }
  else {
    $(".alert").replaceWith(`<div class="alert"></div>`);
    get_projects(0);
  }
  show_project_select("#content-edit-task #projectSelectToEditTask");

  if (projects != "") {
    let project_id = $("#content-edit-task #projectSelectToEditTask option:selected").text().split("-")[0];
    get_tasks(project_id, current_task_status);
    if (show_task_alert(tasks) == true) {
      $("#edit_tasks_div").show();
      $("#content-edit-task #edit_tasks_table").show();
      let content = ``;
      for (let pair of tasks.entries()) {
        let task = pair[1];
        content += `<tr><th scope="row" id="task_id">` + task.id + `</th>`;
        content += `<td id="name">` + task.name + `</td>`;
        content += `<td id="description">` + task.description + `</td>`;
        content += `<td id="status">` + task_status[task.status] + `</td>`;
        content += `<td>` + task.create_date + `</td>`;
        content += `<td>` + task.project.id + `-` + task.project.name + `</td>`;
        content += `<td>` + task.assignee.id + `-` + task.assignee.name + `</td>`;
        content +=
          `<td>
          <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
            data-toggle="modal" data-target="#task_edit_modal">
            Изменить
          </button>
        </td></tr>`;
      }
      $("#content-edit-task #edit_tasks_table tbody").empty();
      $("#content-edit-task #edit_tasks_table tbody").append(content);
    }
  } else $("#edit_tasks_div").hide();

  $('#content-edit-task #departmentSelectToEditTask').on('change', function (e) {
    $('.alert').empty();
    get_projects($("option:selected", this).text().split("-")[0]);
    if (projects != "") {
      show_project_select("#content-edit-task #projectSelectToEditTask");
      let project_id = $("#content-edit-task #projectSelectToEditTask option:selected").text().split("-")[0];
      get_tasks(project_id, current_task_status);
      if (show_task_alert(tasks) == true) {
        $("#content-edit-task #edit_tasks_table").show();
        $("#edit_tasks_div").show();
        let content = ``;
        for (let pair of tasks.entries()) {
          let task = pair[1];
          content += `<tr><th scope="row" id="task_id">` + task.id + `</th>`;
          content += `<td id="name">` + task.name + `</td>`;
          content += `<td id="description">` + task.description + `</td>`;
          content += `<td id="status">` + task_status[task.status] + `</td>`;
          content += `<td>` + task.create_date + `</td>`;
          content += `<td>` + task.project.id + `-` + task.project.name + `</td>`;
          content += `<td>` + task.assignee.id + `-` + task.assignee.name + `</td>`;
          content +=
            `<td>
          <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
            data-toggle="modal" data-target="#task_edit_modal">
            Изменить
          </button>
        </td></tr>`;
        }
        $("#content-edit-task #edit_tasks_table tbody").empty();
        $("#content-edit-task #edit_tasks_table tbody").append(content);
      }
    } else $("#edit_tasks_div").hide();
  });

  $('#content-edit-task #projectSelectToEditTask').on('change', function (e) {
    let project_id = $("#content-edit-task #projectSelectToEditTask option:selected").text().split("-")[0];
    get_tasks(project_id, current_task_status);
    if (show_task_alert(tasks) == true) {
      $("#content-edit-task #edit_tasks_table").show();
      $("#edit_tasks_table").show();
      let content = ``;
      for (let pair of tasks.entries()) {
        let task = pair[1];
        content += `<tr><th scope="row" id="task_id">` + task.id + `</th>`;
        content += `<td id="name">` + task.name + `</td>`;
        content += `<td id="description">` + task.description + `</td>`;
        content += `<td id="status">` + task_status[task.status] + `</td>`;
        content += `<td>` + task.create_date + `</td>`;
        content += `<td>` + task.project.id + `-` + task.project.name + `</td>`;
        content += `<td>` + task.assignee.id + `-` + task.assignee.name + `</td>`;
        content += `<td>` + task.assignee.id + `-` + task.assignee.name + `</td>`;
        content +=
          `<td>
          <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
            data-toggle="modal" data-target="#task_edit_modal">
            Изменить
          </button>
        </td></tr>`;
      }
      $("#content-edit-task #edit_tasks_table tbody").empty();
      $("#content-edit-task #edit_tasks_table tbody").append(content);
    }
    else $("#edit_tasks_div").hide();
  });
}

function edit_task(current_row) {
  let task = {};
  task.id = current_row.find("#task_id").text();
  task.name = current_row.find("#name").text();
  task.description = current_row.find("#description").text();
  task.status = Object.keys(task_status)
    .find(item => task_status[item] == current_row.find("#status").text());
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/task/edit/" + task.id,
    data: JSON.stringify(task),
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Задача с ID ` + task.id + ` изменена</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
}