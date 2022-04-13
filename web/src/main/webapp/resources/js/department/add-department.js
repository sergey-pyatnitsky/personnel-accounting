$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $("#add-department").click(function (event) {
    event.preventDefault();
    show_add_department_content();

    $("body").on("click", "#department_save_btn", function (event) {
      event.stopImmediatePropagation();
      event.preventDefault();
      add_department();
    });
  });
});

function show_add_department_content() {
  hideAllContent();
  $("#content-add-department").show();
  $('#departmentNameInput').val('');
  $(".alert").replaceWith(`<div class="alert"></div>`);
}

function add_department() {
  show_preloader();
  let department = {};
  department.name = $('#departmentNameInput').val();
  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/department/add",
    data: JSON.stringify(department),
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert').empty();
      if (data.id == null)
        $('.alert').replaceWith(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`);
      else {
        $('.alert').replaceWith(`<div class="alert alert-success" role="alert">
        Отдел "` + data.name + `" с ID ` + data.id + ` создан</div>`);
        $('#departmentNameInput').val('');
      }
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
  hide_preloader();
}