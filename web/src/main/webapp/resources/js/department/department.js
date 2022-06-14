$(document).ready(function () {
  hide_preloader();
  hideAllContent();
  $("#view-department").click();
});

function hideAllContent() {
  $("#content-add-department").hide();
  $("#content-edit-department").hide();
  $("#content-activate-department").hide();
  $("#content-view-department").hide();
  $("#content-assign-user").hide();
}