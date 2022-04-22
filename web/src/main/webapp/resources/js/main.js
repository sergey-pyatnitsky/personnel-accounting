let language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json";

function show_preloader() {
  $("#preloader_malc").show();
}

function hide_preloader() {
  $("#preloader_malc").hide();
}

$(document).ready(function () {
  if (localStorage.getItem("lang") != null && window.location.href.indexOf("logout") > -1
    && window.location.href.indexOf("lang") <= -1) {
    window.location.replace('?logout' + '&lang=' + localStorage.getItem("lang"));
    switchTableLang(localStorage.getItem("lang"));
  }
  else if (localStorage.getItem("lang") != null && window.location.href.indexOf("error") > -1
    && window.location.href.indexOf("lang") <= -1) {
    window.location.replace('?error' + '&lang=' + localStorage.getItem("lang"));
    switchTableLang(localStorage.getItem("lang"));
  }
  else if (localStorage.getItem("lang") != null && window.location.href.indexOf("lang") <= -1) {
    window.location.replace('?lang=' + localStorage.getItem("lang"));
    switchTableLang(localStorage.getItem("lang"));
  }
  else switchTableLang(localStorage.getItem("lang"));

  $("#en_lang").click(function () {
    window.location.replace('?lang=' + $('#en_lang').val());
    localStorage.setItem("lang", "en");
    switchTableLang(localStorage.getItem("lang"));
  })

  $("#ru_lang").click(function () {
    window.location.replace('?lang=' + $('#ru_lang').val());
    localStorage.setItem("lang", "ru");
    switchTableLang(localStorage.getItem("lang"));
  })
});

function switchTableLang(lang) {
  lang == "ru"
    ? language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/Russian.json"
    : language_url = "http://cdn.datatables.net/plug-ins/9dcbecd42ad/i18n/English.json";
}

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