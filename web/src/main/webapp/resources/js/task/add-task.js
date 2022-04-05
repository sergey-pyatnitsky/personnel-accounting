$(document).ready(function () {
  $("#add-task").click(function (event) {
    event.preventDefault();
    show_add_task_content();
  });

  $("body").on("click", "#add_task_btn", function (event) {
    event.stopImmediatePropagation();
    event.preventDefault();
    add_task();
  });
});

function show_add_task_content() {
  hideAllContent();
  $("#content-add-task").show();
  $("#content-add-task #projectSelectToAddTask").empty();
  $("#content-add-task #employeeSelectToAddTask").empty();
  clear_input();
  if ($("#content-add-task #departmentSelectToAddTask").length) {
    get_departments();
    if (show_department_alert(departments) == true) {
      let content = ``;
      for (let pair of departments.entries()) {
        let department = pair[1];
        content += `<option value="` + (pair[0] + 1) + `">` + department.id + `-` + department.name + `</option>`;
      }
      $("#content-add-task #departmentSelectToAddTask").empty();
      $("#content-add-task #departmentSelectToAddTask").append(content);
      //get_projects($("#content-add-task #departmentSelectToAddTask option:selected").text().split("-")[0]);
      show_project_part($("#content-add-task #departmentSelectToAddTask option:selected").text().split("-")[0]);
    }
  }
  else {
    $(".alert").replaceWith(`<div class="alert"></div>`);
    show_project_part(0);
  }

  /*if ($("#content-add-task #projectSelectToAddTask").length) {
    get_projects(0);
    show_project_select();
    if (projects != "") show_employee_select();
  }
  else $(".alert").replaceWith(`<div class="alert"></div>`);*/

  $('#content-add-task #departmentSelectToAddTask').on('change', function (e) {
    $(".alert").replaceWith(`<div class="alert"></div>`);
    //get_projects($("option:selected", this).text().split("-")[0]);
    show_project_part($("option:selected", this).text().split("-")[0]);
  });
}

function show_project_part(project_id) {
  if ($("#content-add-task #projectSelectToAddTask").length) {
    get_projects(project_id);
    //show_project_select();
    let content = ``;
    if (show_project_alert(projects) == true) {
      for (let pair of projects.entries()) {
        let project = pair[1];
        content += `<option value="` + (pair[0] + 1) + `">` + project.id + `-` + project.name + `</option>`;
      }
    }
    $("#content-add-task #projectSelectToAddTask").empty();
    $("#content-add-task #projectSelectToAddTask").append(content);
    if (projects != "") show_employee_select();
    //if (projects != "") show_employee_select();
  }
  else $(".alert").replaceWith(`<div class="alert"></div>`);
}

/*function show_project_select() {
  let content = ``;
  if (show_project_alert(projects) == true) {
    for (let pair of projects.entries()) {
      let project = pair[1];
      content += `<option value="` + (pair[0] + 1) + `">` + project.id + `-` + project.name + `</option>`;
    }
  }
  $("#content-add-task #projectSelectToAddTask").empty();
  $("#content-add-task #projectSelectToAddTask").append(content);
  if (projects != "") show_employee_select();
}*/

function show_employee_select() {
  get_employees_by_project($("#content-add-task #projectSelectToAddTask option:selected").text().split("-")[0]);
  let content = ``;
  if (show_employee_alert(projects) == true) {
    for (let pair of employees.entries()) {
      let employee = pair[1];
      content += `<option value="` + (pair[0] + 1) + `">` + employee.id + `-` + employee.name + `</option>`;
    }
  }
  $("#content-add-task #employeeSelectToAddTask").empty();
  $("#content-add-task #employeeSelectToAddTask").append(content);
}

function clear_input() {
  $('#taskNameInput').val('');
  $('#taskDescriptionInput').val('');
}

function show_project_alert(array) {
  $(".alert").replaceWith(`<div class="alert"></div>`);
  if (array == "") {
    $(".alert").replaceWith(`
        <div class="alert alert-warning" role="alert">Список проектов пуст!</div>`);
    return false;
  }
  else if (array == null) {
    $(".alert").replaceWith(`
        <div class="alert alert-danger"role="alert">Ошибка!</div>`);
    return false;
  }
  else return true;
}

function show_employee_alert(array) {
  $(".alert").replaceWith(`<div class="alert"></div>`);
  if (array == "") {
    $(".alert").text() != "" ? $(".alert").text($(".alert").text() + " Список сотрудников пуст!")
      : $(".alert").replaceWith(`
        <div class="alert alert-warning" role="alert">Список сотрудников пуст!</div>`);
    return false;
  }
  else if (array == null) {
    $(".alert").replaceWith(`
        <div class="alert alert-danger"role="alert">Ошибка!</div>`);
    return false;
  }
  else return true;
}

function add_task() {
  let task = {}, project = {}, assignee = {};
  Object.assign(task, { project });
  Object.assign(task, { assignee });
  task.name = $('#content-add-task #taskNameInput').val();
  task.description = $('#content-add-task #taskDescriptionInput').val();
  task.project.id = $("#content-add-task #projectSelectToAddTask option:selected").text().split('-')[0];
  task.assignee.id = $("#content-add-task #employeeSelectToAddTask option:selected").text().split('-')[0];

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/task/add",
    data: JSON.stringify(task),
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-success" role = "alert">Сохранено!</div>`);
      clear_input();
    },
    error: function () {
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">Ошибка!</div>`);
    }
  });
}