$(document).ready(function () {
  hide_preloader();
  hideAllContent();

  $(function () {
    $('#departmentDivSelectEditModal').trigger('onload');
  });
});

function hideAllContent() {
  $("#content-add-project").hide();
  $("#content-edit-project").hide();
  $("#content-activate-project").hide();
  $("#content-view-project").hide();
  $("#content-assign-user").hide();
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