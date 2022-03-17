$(document).ready(function () {
  $('#reg_button').click(function () {
    if ($("#password").val() != $("#repeat_password").val()) {
      $('.alert_reg').empty();
      $('.alert_reg').append(`<div class="alert alert-danger"role="alert">
      Пароли не совпадают!</div>`);
    }
    else {
      reg_user();
    }
  })
});

function reg_user() {
  let employee = {};
  //Object.assign(employee, { password: $("#name").val() });
  employee.name = $('#name').val();
  Object.assign(employee, { user: { username: $('#username').val(), password: $("#password").val() } });

  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/registration",
    data: JSON.stringify(employee),
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert_reg').empty();
      $('.alert_reg').append(`<div class="alert alert-success"role="alert">
          Зарегестрировано!</div>`);
    },
    error: function () {
      $('.alert_reg').empty();
      $('.alert_reg').append(`<div class="alert alert-danger"role="alert">
          Данный пользователь уже существует!</div>`);
    }
  });
}