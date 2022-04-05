current_task_status = Object.keys(task_status).find(item => task_status[item] == "Открыта");

$(document).ready(function () {
  $("#view-task").click(function (event) {
    event.preventDefault();
    show_view_task();

    $("#content-view-task #open_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Открыта");
      show_tasks("1", "#content-view-task", "#projectSelectToAddTask", "#view_tasks_table");
    });

    $("#content-view-task #in_progress_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Выполняется");
      show_tasks("1", "#content-view-task", "#projectSelectToAddTask", "#view_tasks_table");
    });

    $("#content-view-task #done_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Выполнена");
      show_tasks("1", "#content-view-task", "#projectSelectToAddTask", "#view_tasks_table");
    });

    $("#content-view-task #closed_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Завершена");
      show_tasks("1", "#content-view-task", "#projectSelectToAddTask", "#view_tasks_table");
    });
  });
});

function show_view_task() {
  hideAllContent();
  $("#content-view-task").show();
  $("#view_tasks_div").hide();
  if ($("#content-view-task #departmentSelectToAddTask").length) {
    get_departments();
    if (show_department_alert(departments) == true) {
      let content = ``;
      for (let pair of departments.entries()) {
        let department = pair[1];
        content += `<option value="` + (pair[0] + 1) + `">` + department.id + `-` + department.name + `</option>`;
      }
      $("#content-view-task #departmentSelectToAddTask").empty();
      $("#content-view-task #departmentSelectToAddTask").append(content);
      get_projects($("#content-view-task #departmentSelectToAddTask option:selected").text().split("-")[0]);
    }
  }
  else {
    $(".alert").replaceWith(`<div class="alert"></div>`);
    get_projects(0);
  }
  show_project_select("#content-view-task #projectSelectToAddTask");

  if (projects != "") {
    let project_id = $("#content-view-task #projectSelectToAddTask option:selected").text().split("-")[0];
    get_tasks(project_id, current_task_status);
    if (show_task_alert(tasks) == true) {
      $("#view_tasks_div").show();
      $("#content-view-task #view_tasks_table").show();
      let content = ``;
      for (let pair of tasks.entries()) {
        let task = pair[1];
        content += `<tr><th scope="row">` + task.id + `</th>`;
        content += `<td>` + task.name + `</td>`;
        content += `<td>` + task.description + `</td>`;
        content += `<td>` + task_status[task.status] + `</td>`;
        content += `<td>` + task.create_date + `</td>`;
        content += `<td>` + task.project.id + `-` + task.project.name + `</td>`;
        content += `<td>` + task.assignee.id + `-` + task.assignee.name + `</td>`;
      }
      $("#view_tasks_table tbody").empty();
      $("#view_tasks_table tbody").append(content);
    }
  } else $("#view_tasks_div").hide();

  $('#content-view-task #departmentSelectToAddTask').on('change', function (e) {
    $('.alert').empty();
    get_projects($("option:selected", this).text().split("-")[0]);
    if (projects != "") {
      show_project_select("#content-view-task #projectSelectToAddTask");
      let project_id = $("#content-view-task #projectSelectToAddTask option:selected").text().split("-")[0];
      get_tasks(project_id, current_task_status);
      if (show_task_alert(tasks) == true) {
        $("#content-view-task #view_tasks_table").show();
        $("#view_tasks_div").show();
        let content = ``;
        for (let pair of tasks.entries()) {
          let task = pair[1];
          content += `<tr><th scope="row">` + task.id + `</th>`;
          content += `<td>` + task.name + `</td>`;
          content += `<td>` + task.description + `</td>`;
          content += `<td>` + task_status[task.status] + `</td>`;
          content += `<td>` + task.create_date + `</td>`;
          content += `<td>` + task.project.id + `-` + task.project.name + `</td>`;
          content += `<td>` + task.assignee.id + `-` + task.assignee.name + `</td>`;
        }
        $("#view_tasks_table tbody").empty();
        $("#view_tasks_table tbody").append(content);
      }
    } else $("#view_tasks_div").hide();
  });

  $('#content-view-task #projectSelectToAddTask').on('change', function (e) {
    let project_id = $("#content-view-task #projectSelectToAddTask option:selected").text().split("-")[0];
    get_tasks(project_id, current_task_status);
    if (show_task_alert(tasks) == true) {
      $("#content-view-task #view_tasks_table").show();
      $("#view_tasks_div").show();
      let content = ``;
      for (let pair of tasks.entries()) {
        let task = pair[1];
        content += `<tr><th scope="row">` + task.id + `</th>`;
        content += `<td>` + task.name + `</td>`;
        content += `<td>` + task.description + `</td>`;
        content += `<td>` + task_status[task.status] + `</td>`;
        content += `<td>` + task.create_date + `</td>`;
        content += `<td>` + task.project.id + `-` + task.project.name + `</td>`;
        content += `<td>` + task.assignee.id + `-` + task.assignee.name + `</td>`;
      }
      $("#view_tasks_table tbody").empty();
      $("#view_tasks_table tbody").append(content);
    }
    else $("#view_tasks_div").hide();
  });
}

/*function show_tasks() {
  let project_id = $("#content-view-task #projectSelectToAddTask option:selected").text().split("-")[0];
  get_tasks(project_id, current_task_status);
  if (show_task_alert(tasks) == true) {
    $("#content-view-task #view_tasks_table").show();
    $("#view_tasks_div").show();
    let content = ``;
    for (let pair of tasks.entries()) {
      let task = pair[1];
      content += `<tr><th scope="row">` + task.id + `</th>`;
      content += `<td>` + task.name + `</td>`;
      content += `<td>` + task.description + `</td>`;
      content += `<td>` + task.status + `</td>`;
      content += `<td>` + task.create_date + `</td>`;
      content += `<td>` + task.project.id + `-` + task.project.name + `</td>`;
      content += `<td>` + task.assignee.id + `-` + task.assignee.name + `</td>`;
    }
    $("#view_tasks_table tbody").empty();
    $("#view_tasks_table tbody").append(content);
  }
  else $("#content-view-task #view_tasks_table").hide();
}*/