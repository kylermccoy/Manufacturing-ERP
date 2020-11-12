$( function() {
    $("#request-list").find("button").click(function() {
        let attrId = $(this).attr("id");
        let orderId = attrId.split("-")[1];
        let url = "http://localhost:8080/manufacturing/api/requests/" + orderId;
        sendDeleteRequest(url);
    })
} )

$( function() {
    $("#products-list").find("button").click(function () {
        let attrId = $(this).attr("id");
        let productId = attrId.split("-")[1];
        let url = "http://localhost:8080/manufacturing/api/products/" + productId;
        sendDeleteRequest(url);
    })
})

$( function() {
    $("#controls").find("button").click(function() {
        let command = $(this).attr("id")
        let url = "http://localhost:8080/manufacturing/api/queue/" + command;
        sendProcessControlRequest(url);
    })
} )

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