$(document).ready(function () {
  $('#reg_button').click(function () {
    if ($("#password").val() != $("#repeat_password").val()) {
      $('.alert_reg').empty();
      $('.alert_reg').append(`<div class="alert alert-danger" role="alert">
      Пароли не совпадают!</div>`);
    }
    else {
      reg_user();
    }
  })
});

function reg_user() {
  let employee = {}, profile = {};
  employee.name = $('#name').val();
  Object.assign(employee, { profile });
  employee.profile.email = $('#email').val();
  Object.assign(employee, { user: { username: $('#username').val(), password: $("#password").val() } });

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/registration",
    data: JSON.stringify(employee),
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('.alert_reg').empty();
      data != ""
        ? $('.alert_reg').append(`<div class="alert alert-danger" role="alert">` + data.error + `</div>`)
        : $('.alert_reg').append(`<div class="alert alert-success" role="alert">Зарегестрировано!</div>`);
    },
    error: function (error) {
      console.log(error);
      $('.alert_reg').empty();
      $('.alert_reg').append(`<div class="alert alert-danger" role="alert">
          Данный пользователь уже существует!</div>`);
    }
  });
}