$(document).ready(function () {
  hide_preloader();
  hideAllContent();
  $("#view-position").click();
});

function hideAllContent() {
  $("#content-view-position").hide();
  $("#content-edit-position").hide();
  $("#content-add-position").hide();
}