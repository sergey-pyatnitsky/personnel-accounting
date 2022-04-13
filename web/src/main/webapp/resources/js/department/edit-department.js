let edit_table = null;
let current_url_for_edit_table = "/api/department/get_all/open";

$(document).ready(function () {
  $("#edit-department").click(function (event) {
    event.preventDefault();
    $('.alert').replaceWith(`<div class="alert"></div>`);
    hideAllContent();
    $("#content-edit-department").show();
    if (edit_table != null) edit_table.destroy();
    current_url_for_edit_table = "/api/department/get_all/open";
    loadEditTable("#content-edit-department #edit_departments_table", current_url_for_edit_table);

    $("#content-edit-department #getOpenDepartmentsBtn").click(function () {
      edit_table.destroy();
      current_url_for_edit_table = "/api/department/get_all/open";
      loadEditTable("#content-edit-department #edit_departments_table", current_url_for_edit_table);
    });

    $("#content-edit-department #getClosedDepartmentsBtn").click(function () {
      edit_table.destroy();
      current_url_for_edit_table = "/api/department/get_all/closed";
      loadEditTable("#content-edit-department #edit_departments_table", current_url_for_edit_table);
    });

    let current_row = null;
    $("body").on('show.bs.modal', "#departmentEditModal", function (event) {
      current_row = $(event.relatedTarget).closest('tr');
      let modal = $(this);

      modal.find('#department_modal_name').val(current_row.find('.department_name').text());

      $("#save_department_modal_btn").click(function () {
        edit_department(current_row.find('.department_id').text(),
          modal.find('#department_modal_name').val());
      })
    });

    $("body").on("click", "#content-edit-department #close_department_btn", function () {
      close_department($(this).val());
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
      { "data": "id", "sClass": "department_id" },
      { "data": "name", "sClass": "department_name" },
      {
        "data": "startDate", render: function (data) {
          return data != null
            ? '<p class="text-success">' + data + '</p>'
            : '<p class="text-danger">-</p>';
        }
      },
      {
        "data": "isActive", render: function (data) {
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
            + ' data-target="#departmentEditModal">Изменить</button>'
            + '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0" id="close_department_btn"'
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

function edit_department(department_id, department_name) {
  let department = {};
  department.name = department_name;
  $.ajax({
    type: "PUT",
    contentType: "application/json",
    url: "/api/department/edit/" + department_id,
    data: JSON.stringify(department),
    async: false,
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      if (data == "") {
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Отдел с ID ` + department.id + ` изменён</div>`);
        edit_table.destroy();
        loadEditTable("#content-edit-department #edit_departments_table", current_url_for_edit_table);
      } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
  hide_preloader();
}

function close_department(departmentId) {
  let department = {};
  department.id = departmentId;
  $.ajax({
    type: "DELETE",
    contentType: "application/json",
    url: "/api/department/close/" + departmentId,
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data == "") {
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Отдел с ID ` + department.id + ` удалён</div>`);
        edit_table.destroy();
        loadEditTable("#content-edit-department #edit_departments_table", current_url_for_edit_table);
      } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
}