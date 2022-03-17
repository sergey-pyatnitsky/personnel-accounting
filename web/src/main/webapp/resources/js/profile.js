let employee = null;

$(document).ready(function () {
  get_profile_data();

  $('#edit_profile').click(function () {
    edit_profile_data();
  })

  $('#edit_pass').click(function () {
    $('.alert_pass').empty();
  })

  $("#modal_save").click(function () {
    if ($("#modal_input_new_pass").val() != $("#modal_input_repeat").val()) {
      $('.alert_pass').empty();
      $('.alert_pass').append(`<div class="alert alert-danger"role="alert">
      Пароли не совпадают!</div>`);
    }
    else {
      check_old_password();
    }
  })
});

function edit_profile_data() {
  employee.name = $('#name').val();
  employee.profile.phone = $('#phone').val();
  employee.profile.address = $('#address').val();
  employee.profile.email = $('#email').val();
  employee.profile.education = $('#education').val();
  employee.profile.skills = $('#experience').val();

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/employee/profile/edit",
    data: JSON.stringify(employee),
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-success"role="alert">
        Сохранено!</div>`);
    },
    error: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка изменения данных!</div>`);
    }
  });
}

function get_profile_data() {
  $.ajax({
    type: "GET",
    contentType: "application/json",
    url: "/api/employee/profile/get_profile_data",
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      employee = data;
      $('#name').val(data.name);
      $('#phone').val(data.profile.phone);
      $('#address').val(data.profile.address);
      $('#email').val(data.profile.email);
      $('#education').val(data.profile.education);
      $('#experience').val(data.profile.skills);
    },
    error: function () {
      $('.alert').empty();
      $('.alert').append(`<div class="alert alert-danger"role="alert">
        Ошибка!</div>`);
    }
  });
}

function check_old_password() {
  let entity = {};
  Object.assign(entity, { password: $("#modal_input_old_pass").val() });

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/employee/profile/check_old_password",
    data: JSON.stringify(entity),
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function () {
      save_password();
    },
    error: function () {
      $('.alert_pass').empty();
      $('.alert_pass').append(`<div class="alert alert-danger"role="alert">
      Неправильный пароль!</div>`);
    }
  });
}

function save_password() {
  let entity = {};
  Object.assign(entity, { password: $("#modal_input_new_pass").val() });

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/employee/profile/edit_password",
    data: JSON.stringify(entity),
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function () {
      $('#modal_input_old_pass').val('');
      $('#modal_input_new_pass').val('');
      $('#modal_input_repeat').val('');
      $('.alert_pass').empty();
      $('.alert_pass').append(`<div class="alert alert-success"role="alert">
        Пароль изменён</div>`);
    },
    error: function () {
      $('.alert_pass').empty();
      $('.alert_pass').append(`<div class="alert alert-danger"role="alert">
        Ошибка изменения пароля!</div>`);
    }
  });
}