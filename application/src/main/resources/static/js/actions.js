//TODO: Split this into separate js files

$( document ).ready( function() {
    $("#order-list").find("button").click(function() {
        let attrId = $(this).attr("id");
        let orderId = attrId.split("-")[1];
        let url = "/orders/" + orderId;
        sendDeleteRequest(url);
    })
} );

$( document ).ready( function() {
    $("#recall-list").find("button").click(function() {
        let attrId = $(this).attr("id");
        let orderId = attrId.split("-")[1];
        let url = "/recalls/" + orderId;
        sendDeleteRequest(url);
    })
} );

$(document).ready( function() {
    $("#products-list").find("button").click(function () {
        let attrId = $(this).attr("id");
        let productId = attrId.split("-")[1];
        let url = "/products/" + productId;
        sendDeleteRequest(url);
    })
});

function sendDeleteRequest(url) {
    $.ajax({
        url: url,
        type: 'DELETE'
    })
        .done(function() {location.reload()})
        .fail(function() {console.log("fail")});
}