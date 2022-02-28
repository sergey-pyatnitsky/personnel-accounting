/*
$(document).ready(function () {
  $(".search").keyup(function () {
    var searchTerm = $(".search").val();
    var listItem = $('.results tbody').children('tr');
    var searchSplit = searchTerm.replace(/ /g, "'):containsi('")

    $.extend($.expr[':'], {
      'containsi': function (elem, i, match, array) {
        return (elem.textContent || elem.innerText || '').toLowerCase().indexOf((match[3] || "").toLowerCase()) >= 0;
      }
    });

    $(".results tbody tr").not(":containsi('" + searchSplit + "')").each(function (e) {
      $(this).attr('visible', 'false');
    });

    $(".results tbody tr:containsi('" + searchSplit + "')").each(function (e) {
      $(this).attr('visible', 'true');
    });

    var jobCount = $('.results tbody tr[visible="true"]').length;
    $('.counter').text(jobCount + ' item');

    if (jobCount == '0') {
      $('.no-result').show();
    } else {
      $('.no-result').hide();
    }
  });
});*/

$(document).ready(function () {

  $("#search-form").submit(function (event) {

    //stop submit the form, we will post it manually.
    event.preventDefault();

    fire_ajax_submit();

  });

});

function fire_ajax_submit() {

  var search = {}
  search["username"] = $("#username").val();
  //search["email"] = $("#email").val();

  $("#btn-search").prop("disabled", true);

  $.ajax({
    type: "POST",
    contentType: "application/json",
    url: "/api/telephone-directory/search",
    data: JSON.stringify(search),
    dataType: 'json',
    cache: false,
    timeout: 600000,
    success: function (data) {

      var json = "<h4>Ajax Response</h4><pre>"
        + JSON.stringify(data, null, 4) + "</pre>";
      $('#feedback').html(json);

      console.log("SUCCESS : ", data);
      $("#btn-search").prop("disabled", false);

    },
    error: function (e) {

      var json = "<h4>Ajax Response</h4><pre>"
        + e.responseText + "</pre>";
      $('#feedback').html(json);

      console.log("ERROR : ", e);
      $("#btn-search").prop("disabled", false);

    }
  });

}
