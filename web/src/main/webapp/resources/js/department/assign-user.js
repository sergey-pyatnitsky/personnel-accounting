let assign_table = null, open_departments = null;
let current_url_for_assign_table = "/api/employee/get_all/free";

$(document).ready(function () {
  $("#assign-user").click(function (event) {
    event.preventDefault();
    $('.alert').replaceWith(`<div class="alert"></div>`);
    hideAllContent();
    $("#content-assign-user").show();
    if (assign_table != null) assign_table.destroy();
    current_url_for_assign_table = "/api/employee/get_all/free";
    loadAssignTable("#content-assign-user #assign_users_table", current_url_for_assign_table);

    $("#content-assign-user #assignEmployeeBtn").click(function () {
      assign_table.destroy();
      current_url_for_assign_table = "/api/employee/get_all/free";
      loadAssignTable("#content-assign-user #assign_users_table", current_url_for_assign_table);
    });

    $("#content-assign-user #transferEmployeeBtn").click(function () {
      assign_table.destroy();
      current_url_for_assign_table = "/api/employee/get_all/assigned";
      loadAssignTable("#content-assign-user #assign_users_table", current_url_for_assign_table);
    });

    let current_row = null;
    $("body").on('show.bs.modal', "#content-assign-user #assignUserModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);
      get_open_department();
      let content = ``;
      for (let pair of open_departments.entries()) {
        let department = pair[1];
        content += `<option option value = "` + (pair[0] + 1) + `" > ` + department.id + ` - ` + department.name + `</option > `;
      }
      $("#departmentSelect").empty();
      $("#departmentSelect").append(content);

      if (current_row.find('.department_column').text() != "-")
        modal.find('#departmentSelect select').val(current_row.find('.department_column').text());

      $("#assign_modal_save_btn").click(function () {
        assign_user($(this).val(), $("#departmentSelect option:selected").text().split("-")[0]);
      })
    });
  });
});

function loadAssignTable(table_id, req_url) {
  assign_table = $(table_id).DataTable({
    "processing": true,
    "serverSide": true,
    "pagingType": "full_numbers",
    "ajax": {
      "url": req_url,
      "type": "POST",
      "dataType": "json",
      "contentType": "application/json",
      "data": function (d) {
        console.log(JSON.stringify(d));
        return JSON.stringify(d);
      }
    },
    "columns": [
      { "data": "id" },
      { "data": "user.username" },
      { "data": "name" },
      { "data": "user.authority.role" },
      {
        "data": "department", render: function (data) {
          return data != null
            ? data.id + "-" + data.name
            : '<p class="text-danger">-</p>';
        }, "sClass": "department_column"
      },
      {
        "mData": null,
        "bSortable": false,
        "mRender": function (data) {
          return data.department != null
            ? '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal"' +
            'data-target="#assignUserModal" id="transfer_user_btn" value="' + data.department.id + '">Перевод</button>'
            : '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" data-toggle="modal"' +
            'data-target="#assignUserModal" id="transfer_user_btn" value="' + data.department.id + '">Назначить</button>';
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function get_open_department() {
  show_preloader();
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/department/get_all/open",
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      open_departments = data;
    },
    error: function (error) {
      console.log(error);
    }
  });
  hide_preloader();
}

function assign_user(employee_id, department_id) {
  let employee = {}, department = {};
  Object.assign(employee, { department });
  employee.id = employee_id;
  employee.department.id = department_id;
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/employee/assign/department",
    data: JSON.stringify(employee),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data == "") {
        $('.alert').replaceWith(`<div div class="alert alert-success" role = "alert" >
            Пользователь с ID `+ employee.id + ` назначен на отдел с ID ` + employee.department.id + `</div > `);
        assign_table.destroy();
        loadAssignTable("#content-assign-user #edit_departments_table", current_url_for_assign_table);
      } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
}