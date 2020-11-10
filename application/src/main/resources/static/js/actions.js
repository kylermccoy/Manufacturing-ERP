$( document ).ready( function() {
    $("#product-list").find("button").click(function() {
        let attrId = $(this).attr("id");
        let orderId = attrId.split("-")[1];
        let url = "http://localhost:8080/manufacturing/api/requests/" + orderId;
        sendDeleteRequest(url);
    })
} )

$( document ).ready( function() {

    $("#controls").find("button").click(function() {
        let command = $(this).attr("id")
        let url = "/process/" + command;
        sendProcessControlRequest(url);
    })
} )

$(document).ready( function() {
    setInterval(function() {
        $.ajax({
            url: "/process/getRemainingTime"
        })
        .done(function(resp) {
            console.log(resp);
//        $("timeRemaining").text(resp)
        })
    }, 1000);
});

function sendDeleteRequest(url) {
    $.ajax({
        url: url,
        type: 'DELETE'
    })
        .done(function() {location.reload()})
        .fail(function() {console.log("fail")});
}

function sendProcessControlRequest(url) {
    $.ajax({
        url: url,
        type: 'GET'
    })
        .done(function() {location.reload()})
        .fail(function() {console.log("fail")});
}