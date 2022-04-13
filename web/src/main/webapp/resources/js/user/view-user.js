let view_table = null;

$(document).ready(function () {
  $("#view-user").click(function (event) {
    event.preventDefault();
    $('.alert').replaceWith(`<div class="alert"></div>`);
    hideAllContent();
    $("#content-view-user").show();
    if (view_table != null) view_table.destroy();
    loadViewTable("#content-view-user #view_data_table", "/api/employee/get_all");

    $("#content-view-user #employee_btn").click(function () {
      view_table.destroy();
      loadViewTable("#content-view-user #view_data_table", "/api/employee/get_all");
    });

    $("#content-view-user #admin_btn").click(function () {
      view_table.destroy();
      loadViewTable("#content-view-user #view_data_table", "/api/employee/get_all/admins");
    });
  });
});

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
      { "data": "user.username" },
      { "data": "name" },
      { "data": "user.authority.role" },
      {
        "data": "active", render: function (data) {
          return data
            ? '<p class="text-success">+</p>'
            : '<p class="text-danger">-</p>';
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}