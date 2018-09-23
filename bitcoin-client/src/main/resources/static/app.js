var stompClient = null;

function connect() {
    var socket = new SockJS('/price-websocket');
    stompClient = Stomp.over(socket);

    var prevPriceValue = null;
    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame);
            stompClient.subscribe('/topic/price', function (price) {
                var priceBody = JSON.parse(price.body);
                var priceValue = priceBody.value;
                var priceTimestamp = priceBody.timestamp;
                if (prevPriceValue == null) {
                    prevPriceValue = priceValue;
                }
                var priceVar = priceValue - prevPriceValue;
                prevPriceValue = priceValue;

                $('#currentPrice').text(Number(priceValue).toFixed(2));
                $('#variation').text((priceVar > 0 ? "+" : "") + Number(priceVar).toFixed(2));

                var row = '<tr><td>'+priceValue+'</td><td>'+priceTimestamp+'</td></tr>';
                $('#priceList tbody').prepend(row);
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