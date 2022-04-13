let edit_table = null;
let current_url_for_edit_table = "/api/employee/get_all";

$(document).ready(function () {
  $("#edit-user").click(function (event) {
    event.preventDefault();
    $('.alert').replaceWith(`<div class="alert"></div>`);
    hideAllContent();
    $("#content-edit-user").show();
    if (edit_table != null) edit_table.destroy();
    current_url_for_edit_table = "/api/employee/get_all";
    loadEditTable("#content-edit-user #edit_users_table", current_url_for_edit_table);

    $("#content-edit-user #employee_btn").click(function () {
      edit_table.destroy();
      current_url_for_edit_table = "/api/employee/get_all";
      loadEditTable("#content-edit-user #edit_users_table", current_url_for_edit_table);
    });

    $("#content-edit-user #admin_btn").click(function () {
      edit_table.destroy();
      current_url_for_edit_table = "/api/employee/get_all/admins";
      loadEditTable("#content-edit-user #edit_users_table", current_url_for_edit_table);
    });

    let current_row = null;
    $("body").on('show.bs.modal', "#editUserModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);

      modal.find('#inputNameEditModal').val(current_row.find('.employee_name').text());
      modal.find('#roleSelectEditModal').val(current_row.find('.employee_role').text());

      $("#modal_save").click(function () {
        edit_user(current_row.find('.employee_id').text(),
          modal.find('#inputNameEditModal').val(), modal.find('#roleSelectEditModal').val());
      })
    });

    $("body").on("click", "#content-edit-user #removeUserBtn", function () {
      delete_employee($(this).val());
    });
  });
});

function loadEditTable(table_id, req_url) {
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
      { "data": "id", "sClass": "employee_id" },
      { "data": "user.username" },
      { "data": "name", "sClass": "employee_name" },
      { "data": "user.authority.role", "sClass": "employee_role" },
      {
        "data": "active", render: function (data) {
          return data
            ? '<p class="text-success">+</p>'
            : '<p class="text-danger">-</p>';
        }
      },
      {
        "mData": null,
        "bSortable": false,
        "mRender": function (data) {
          return '<button type="button" class="btn btn-warning btn-rounded btn-sm my-0 mr-2" data-toggle="modal"'
            + ' data-target="#editUserModal" value="' + data.user.username + '">Изменить</button>'
            + '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" id="removeUserBtn"'
            + ' value="' + data.id + '">Удалить</button>';
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function edit_user(id, name, emp_role) {
  show_preloader();
  let employee = {}, user = {}, authority = {};
  Object.assign(employee, { user });
  Object.assign(user, { authority });
  employee.name = name;
  employee.user.authority.role = emp_role;
  employee.id = id;
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/employee/edit",
    data: JSON.stringify(employee),
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data == "") {
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Пользователь с ID ` + employee.id + ` изменён</div>`);
        edit_table.destroy();
        loadEditTable("#content-edit-user #edit_users_table", current_url_for_edit_table);
      } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
  hide_preloader();
}

function delete_employee(employeeId) {
  show_preloader();
  let employee = {};
  employee.id = employeeId;
  $.ajax({
    type: "DELETE",
    contentType: "application/json",
    url: "/api/employee/remove/" + employeeId,
    data: JSON.stringify(employee),
    async: false,
    cache: false,
    timeout: 600000,
    success: function () {
      edit_table.destroy();
      loadActivateTable("#content-activate-user #edit_users_table", current_url_for_edit_table);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Выполнено</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
  hide_preloader();
}