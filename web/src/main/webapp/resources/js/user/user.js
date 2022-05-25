$(document).ready(function () {
  hide_preloader();
  hideAllContent();
  $("#view-user").click();
});

function hideAllContent() {
  $("#content-view-user").hide();
  $("#content-edit-user").hide();
  $("#content-activate-user").hide();
}