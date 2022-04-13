let activate_table = null;

$(document).ready(function () {
  $("#activate-project").click(function (event) {
    event.preventDefault();
    $('.alert').replaceWith(`<div class="alert"></div>`);
    hideAllContent();
    $("#content-activate-project").show();
    if (activate_table != null) activate_table.destroy();
    loadActivateTable("#content-activate-project #activate_project_table", "/api/project/get_all/open");

    $("body").on('click', "#activate_project_btn", function () {
      activate_project($(this).val(), $(this).text());
    });
  });
});

function loadActivateTable(table_id, req_url) {
  activate_table = $(table_id).DataTable({
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
      { "data": "id", "sClass": "project_id" },
      { "data": "name", "sClass": "project_name" },
      {
        "data": "department", render: function (data) {
          return data.id + "-" + data.name;
        }, "sClass": "selected_department"
      },
      { "data": "startDate" },
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
          let content = '<button type="button" class="btn btn-danger btn-rounded btn-sm my-0"'
            + 'id="activate_project_btn" value="' + data.id + '">';
          if (data.active != false)
            content += 'Активировать</button>';
          else content += 'Деактивировать</button>';
          return content;
        }
      }
    ],
    language: {
      url: language_url
    },
  });
  $(table_id).removeClass("no-footer");
}

function activate_project(project_id, action) {
  show_preloader();
  let project = {};
  project.id = project_id;
  if (action === "Активировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/project/activate/" + project.id,
      async: false,
      cache: false,
      timeout: 600000,
      success: function (data) {
        $('.alert').empty();
        if (data == "") {
          $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
          Проект с ID ` + project.id + ` активирован</div>`);
          activate_table.destroy();
          loadActivateTable("#content-activate-project #activate_project_table", "/api/project/get_all/open");
        } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
      }
    });
  }
  else if (action === "Деактивировать") {
    $.ajax({
      type: "PUT",
      contentType: "application/json",
      url: "/api/project/inactivate/" + project.id,
      data: JSON.stringify(project),
      async: false,
      cache: false,
      timeout: 600000,
      success: function (data) {
        $('.alert').empty();
        if (data == "") {
          $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
          Проект с ID ` + project.id + ` деактивирован</div>`);
          activate_table.destroy();
          loadActivateTable("#content-activate-project #activate_project_table", "/api/project/get_all/open");
        } else $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
      },
      error: function (error) {
        console.log(error);
        $('.alert').empty();
        $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
      }
    });
  }
  hide_preloader();
}