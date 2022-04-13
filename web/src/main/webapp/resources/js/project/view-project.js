let view_table = null;

$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $("#view-project").click(function (event) {
    event.preventDefault();
    $('.alert').replaceWith(`<div class="alert"></div>`);
    hideAllContent();
    $("#content-view-project").show();
    if (view_table != null) view_table.destroy();
    loadViewTable("#content-view-project #view_projects_table", "/api/project/get_all/open");
    showColumn("#end_date_column", false);

    $("#content-view-project #getOpenProjectsBtn").click(function () {
      view_table.destroy();
      loadViewTable("#content-view-project #view_projects_table", "/api/project/get_all/open");
      showColumn("#end_date_column", false);
    });

    $("#content-view-project #getClosedProjectsBtn").click(function () {
      view_table.destroy();
      loadViewTable("#content-view-project #view_projects_table", "/api/project/get_all/closed");
      showColumn("#end_date_column", true);
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
      { "data": "name" },
      {
        "data": "department", render: function (data) {
          return data.id + "-" + data.name;
        }
      },
      {
        "data": "isActive", render: function (data) {
          return data
            ? '<p class="text-success">+</p>'
            : '<p class="text-danger">-</p>';
        }
      },
      { "data": "startDate" },
      { "data": "endDate" },
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function showColumn(column_id, mode) {
  if (mode == true) {
    $(column_id).show();
    view_table.column(5).visible(true);
  } else {
    $(column_id).hide();
    view_table.column(5).visible(false);
  }
}