let language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json";

function show_preloader() {
  $("#preloader_malc").show();
}

function hide_preloader() {
  $("#preloader_malc").hide();
}

$(document).ready(function () {
  if (localStorage.getItem("lang") != null && window.location.href.indexOf("lang") <= -1)
    window.location.replace('?lang=' + localStorage.getItem("lang"));
  else if(localStorage.getItem("lang") == null) localStorage.setItem("lang", "en");

  $("#en_lang").click(function () {
    window.location.replace('?lang=' + $('#en_lang').val());
    localStorage.setItem("lang", "en");
    language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json";
  })

  $("#ru_lang").click(function () {
    window.location.replace('?lang=' + $('#ru_lang').val());
    localStorage.setItem("lang", "ru");
    language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json";
  })
});

function get_message(lang, property) {
  let message = null;
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/messages/" + lang + "/" + property,
    async: false,
    cache: false,
    timeout: 600000,
    success: function (data) {
      message = data;
    },
    error: function (error) {
      console.log(error);
      $('.alert').empty();
      $('.alert').replaceWith(`<div class="alert alert-danger" role = "alert">500 Error</div>`);
    }
  });
  return message;
}