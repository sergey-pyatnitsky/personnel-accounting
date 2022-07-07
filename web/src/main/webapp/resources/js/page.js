document.addEventListener('DOMContentLoaded', function () {
  let token = document.getElementById("span_token_id").innerHTML;
  if (token != ""){
    saveToken(token);
    window.location.href = window.location.href + "/main"
  }

  let arr = window.location.href.split("/");
  //http://localhost:8080/page/main
  arr.shift();
  arr.shift();
  arr.shift();
  arr.shift();
  getHTML("/" + arr.join("/"));
});

function getHTML(link) {
  let data = fetch(link, {
    method: 'GET',
    headers: {
      'Authorization': sessionStorage.getItem('tokenData'),
    }
  }).then(function (response) {
    return response.text()
  })
    .then(function (html) {
      document.body.innerHTML = document.body.innerHTML + html;

      var linkTag = $('body link');
      for (const link of linkTag) {
        document.head.innerHTML += link.outerHTML;
        $.ajax({
          url: link.getAttribute('href'),
          dataType: "text/css"
        });
      }

      for (const link of linkTag) {
        link.parentNode.removeChild(link);
      }

      var scriptTag = $('body script'), countAllSrc = scriptTag.length, currCountSrc = 0;
      for (const tag of scriptTag) {
        $.ajax({
          url: tag.getAttribute('src'),
          async: false,
          dataType: "script",
          success: function () {
            currCountSrc++;
            if (currCountSrc == countAllSrc && $(".first_button").length != 0)
              document.getElementById($(".first_button").attr('id')).click();
          },
        });
      }
    });
}

function saveToken(token) {
  sessionStorage.setItem('tokenData', 'Bearer ' + JSON.stringify(token).replaceAll("\"", ""));
}