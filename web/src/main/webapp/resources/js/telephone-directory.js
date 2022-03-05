$(document).ready(function () {
  $('#employeeTable').hide();
  $('#feedback').hide();
  $("#bth-search").click(function (event) {
    $('#feedback').hide();
    event.preventDefault();
    fire_ajax_submit();
  });

});

function fire_ajax_submit() {
  var search = {};
  let value = $("#search_select option:selected").val();

  if (value == "email") {
    Object.assign(search, { profile: { email: $("#searach_criteria").val() } });
  }
  else if (value == "phone") {
    Object.assign(search, { profile: { phone: $("#searach_criteria").val() } });
  }
  else {
    search[value] = $("#searach_criteria").val();
  }

  var token = $("meta[name='_csrf']").attr("content");
  var header = $("meta[name='_csrf_header']").attr("content");
  $(document).ajaxSend(function (e, xhr, options) {
    xhr.setRequestHeader(header, token);
  });

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/telephone-directory/search",
    data: JSON.stringify(search),
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {
      $('#tbody_search').remove();
      $('#employeeTable').show();

      $('#employeeTable').append(`
      <tbody id='tbody_search'>${data.map(n => `
        <tr>
          <td>${n.id}</td>
          <td>${n.name}</td>
          <td>${n.profile.phone}</td>
          <td>${n.profile.email}</td>
        </tr>`).join('')}
      </tbody>
    `);
    },
    error: function (e) {
      $('#employeeTable').hide();
      $('#feedback').show();
    }
  });

}