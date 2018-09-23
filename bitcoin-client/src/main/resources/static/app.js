var stompClient = null;

function connect() {
    var socket = new SockJS('/price-websocket');
    stompClient = Stomp.over(socket);

    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/price', function (price) {
                var priceBody = JSON.parse(price.body);
                var record = '<p>'+priceBody.value+'/'+priceBody.timestamp+'</p>';
                $('#priceList').prepend(record);
            });
        },
        function() {
            console.log('Unable to connect to Websocket!');
            $('#websocketSwitch').prop('checked', false);
        }
     );
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    console.log("Disconnected");
}

$(function () {
    $('#websocketSwitch').click(function() {
        if ($(this).prop('checked')) {
            connect();
        } else {
            disconnect();
        }
    });
});

connect();