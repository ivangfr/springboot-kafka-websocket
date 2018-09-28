var stompClient = null;

function connect() {
    var socket = new SockJS('/price-websocket');
    stompClient = Stomp.over(socket);

    var prevPriceValue = null;
    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame);

            stompClient.subscribe('/topic/prices', function (price) {
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

                var row = '<tr><td>'+priceValue+'</td><td>'+moment(priceTimestamp).format('YYYY-MM-DD HH:mm:ss')+'</td></tr>';
                $('#priceList tbody').prepend(row);
            });

            stompClient.subscribe('/topic/comments', function (chatComment) {
                var chatCommentBody = JSON.parse(chatComment.body);
                var username = chatCommentBody.username;
                var message = chatCommentBody.message;
                var timestamp = chatCommentBody.timestamp;

                var row = '<tr><td>['+moment(timestamp).format('YYYY-MM-DD HH:mm:ss')+'] '+username+': '+message+'<td></tr>';
                $('#chat tbody').prepend(row);
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
    $('#send').click(function() {
        var usernameVal = $("#username").val();

        var message = $("#message");
        var messageVal = message.val();

        if (usernameVal.length !== 0 && messageVal.length !== 0) {
            var comment = JSON.stringify({'username': usernameVal, 'message': messageVal})
            stompClient.send("/app/chat", {}, comment);

            message.val('');
        }
    });
});

connect();