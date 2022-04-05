let projects = "", departments = "", employees = "", tasks = "";
let current_task_status = Object.keys(task_status).find(item => task_status[item] == "Открыта");

function hideAllContent() {
  $("#content-add-task").hide();
  $("#content-edit-task").hide();
  $("#content-activate-task").hide();
  $("#content-view-task").hide();
}

function get_projects(department_id) {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/project/by_department/open/" + department_id,
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      data == "" ? projects = "" : projects = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      if (error.status == 409)
        $('#alert').append(`<div class="alert alert-danger" role = "alert">
      Список проектов в отделе пуст!</div>`)
      else $('#alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
      projects = "";
    }
  });
  hide_preloader();
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
      data == "" ? departments = "" : departments = data;
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

function get_employees_by_project(project_id) {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/get_all/by_project/" + project_id,
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      data == "" ? employees = "" : employees = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      if (error.status == 409) {
        $('#alert').append(`<div class="alert alert-danger" role = "alert">
      Список сотрудников на проекте пуст!</div>`)
        employees = "";
      } else $('#alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
    }
  });
  hide_preloader();
}

function get_tasks(project_id, status) {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/task/get_all/project/" + project_id + "/by_status/" + status,
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
        $('#alert').append(`<div class="alert alert-danger" role = "alert">
      Список задач на проекте пуст!</div>`)
        tasks = "";
      } else $('#alert').append(`<div class="alert alert-danger" role = "alert">
      Ошибка!</div>`);
    }
  });
  hide_preloader();
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

function show_task_alert(array) {
  $(".alert").replaceWith(`<div class="alert"></div>`);
  if (array == "") {
    $(".alert").text() != "" ? $(".alert").text($(".alert").text() + " Список задач пуст!!")
      : $(".alert").replaceWith(`
        <div class="alert alert-warning" role="alert">Список задач пуст!</div>`);
    return false;
  }
  else if (array == null) {
    $(".alert").replaceWith(`
        <div class="alert alert-danger"role="alert">Ошибка!</div>`);
    return false;
  }
  else return true;
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

function show_project_select(select_id) {
  let content = ``;
  if (show_project_alert(projects) == true) {
    for (let pair of projects.entries()) {
      let project = pair[1];
      content += `<option value="` + (pair[0] + 1) + `">` + project.id + `-` + project.name + `</option>`;
    }
  }
  $(select_id).empty();
  $(select_id).append(content);
}

function show_tasks(mode, content_id, select_id, table_id) {
  let project_id = $(content_id + " " + select_id + " option:selected").text().split("-")[0];
  get_tasks(project_id, current_task_status);
  if (show_task_alert(tasks) == true) {
    $(content_id + " " + table_id).show();
    $(table_id).show();
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
      if (mode == "2") content +=
        `<td>
          <button type="button" class="btn btn-danger btn-rounded btn-sm my-0"
            data-toggle="modal" data-target="#task_edit_modal">
            Изменить
          </button>
        </td></tr>`;
    }
    $(content_id + " " + table_id + " tbody").empty();
    $(content_id + " " + table_id + " tbody").append(content);
  }
  else $(content_id + " " + table_id).hide();
}