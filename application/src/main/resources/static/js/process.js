$( document ).ready( function() {
  $("#controls").find(".queue-action").click(function() {
    let command = $(this).attr("id")
    let url = "/process/" + command;
    sendProcessControlRequest(url);
  })
} );

$( document ).ready( function() {
  function getTime() {
    $.ajax({
      url: "/process/getRemainingTime"
    })
    .done(function(resp) {
      let time = resp["remaining"];
      let requestID = resp["requestID"]
      if (time === -1) {
        time = requestID = "No current item";
      } else {
        time = time + " Minutes";
      }
      $("#timeRemaining").text(time);
      $("#requestID").text(requestID);
    })
  }
  getTime();
  $(document).ready( function() {
    setInterval(getTime, 60000); //every minute
  });
} )

function sendProcessControlRequest(url) {
  $.ajax({
    url: url,
    type: 'GET'
  })
  .done(function() {location.reload()})
  .fail(function() {console.log("fail")});
}