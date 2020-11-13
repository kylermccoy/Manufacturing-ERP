//TODO: Split this into separate js files

$( document ).ready( function() {
    $("#product-list").find("button").click(function() {
        let attrId = $(this).attr("id");
        let orderId = attrId.split("-")[1];
        let url = "http://localhost:8080/manufacturing/api/requests/" + orderId;
        sendDeleteRequest(url);
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