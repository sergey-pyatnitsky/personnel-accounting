$(document).ready(function () {
  $("#my-task").click(function (event) {
    event.preventDefault();
    show_view_my_task();

    $("#content-my-task #open_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Открыта");
      show_task_div()
    });

    $("#content-my-task #in_progress_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Выполняется");
      show_task_div()
    });

    $("#content-my-task #done_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Выполнена");
      show_task_div()
    });

    $("#content-my-task #closed_projects_btn").click(function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      current_task_status = Object.keys(task_status).find(item => task_status[item] == "Завершена");
      show_task_div()
    });
  });

  $("body").on("click", "#content-my-task #edit_task_status_btn", function () {
    edit_task_status($(this).attr("value"));
  });
});

function show_view_my_task() {
  hideAllContent();
  $("#content-my-task").show();
  $("#view_my_tasks_div").hide();
  if ($("#content-my-task #departmentSelectToGetMyTask").length) {
    get_departments();
    if (show_department_alert(departments) == true) {
      let content = ``;
      for (let pair of departments.entries()) {
        let department = pair[1];
        content += `<option value="` + (pair[0] + 1) + `">` + department.id + `-` + department.name + `</option>`;
      }
      $("#content-my-task #departmentSelectToGetMyTask").empty();
      $("#content-my-task #departmentSelectToGetMyTask").append(content);
      get_projects($("#content-my-task #departmentSelectToGetMyTask option:selected").text().split("-")[0]);
    }
  }
  else {
    $(".alert").replaceWith(`<div class="alert"></div>`);
    get_projects(0);
  }
  show_project_select("#content-my-task #projectSelectToGetMyTask");

  if (projects != "") show_task_div();
  else $("#view_my_tasks_div").hide();

  $('#content-my-task #departmentSelectToGetMyTask').on('change', function (e) {
    $('.alert').empty();
    get_projects($("option:selected", this).text().split("-")[0]);
    show_project_select("#content-my-task #projectSelectToGetMyTask");
    if (projects != "") show_task_div();
    else $("#view_my_tasks_div").hide();
  });

  $('#content-my-task #projectSelectToGetMyTask').on('change', function (e) {
    if (projects != "") show_task_div();
    else $("#view_my_tasks_div").hide();
  });
}

function show_task_div() {
  let project_id = $("#content-my-task #projectSelectToGetMyTask option:selected").text().split("-")[0];
  get_tasks_by_employee(project_id, current_task_status);
  $("#content-my-task #view_my_tasks_div").show();
  if (show_task_alert(tasks) == true) {
    $("#content-my-task #view_my_tasks_div #task_blocks_div").show();
    $("#view_my_tasks_div").show();
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
      if (task.status != "CLOSED")
        content += `<div class="col-2">
        <button type="button" class="btn btn-secondary" id="edit_task_status_btn" 
        style="float: right" value="` + task.id + `">Изменить статус</button></div>`;
      content += `</div></a>`;
    }
    $("#content-my-task #view_my_tasks_div #task_blocks_div").empty();
    $("#content-my-task #view_my_tasks_div #task_blocks_div").append(content);
  }
  else $("#content-my-task #view_my_tasks_div #task_blocks_div").hide();
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
      data == "" ? tasks = "" : tasks = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      if (error.status == 409) {
        $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Список ваших задач с данным статусом пуст!</div>`)
        tasks = "";
      } else $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
    }
  });
  hide_preloader();
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
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success" role="alert">
      Статус изменён!</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
    }
  });
}