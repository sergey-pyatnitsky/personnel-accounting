function hideAllContent() {
  $("#content-add-task").hide();
  $("#content-activate-task").hide();
  $("#content-view-task").hide();
  $("#content-my-task").hide();
}

$(document).ready(function () {
  hide_preloader();
  hideAllContent();
  //$("#view-task").click();
  //$("#my-task").click();
});