var stompClient = null;

function connect() {
    const socket = new SockJS('/price-websocket')
    stompClient = Stomp.over(socket)

    var prevPriceValue = null
    stompClient.connect({},
        function (frame) {
            console.log('Connected: ' + frame)

            stompClient.subscribe('/topic/prices', function (price) {
                const priceBody = JSON.parse(price.body)
                const priceValue = priceBody.value
                const priceTimestamp = priceBody.timestamp
                if (prevPriceValue == null) {
                    prevPriceValue = priceValue
                }
                const priceVar = priceValue - prevPriceValue
                prevPriceValue = priceValue

                $('#currentPrice').text(Number(priceValue).toFixed(2))
                $('#variation').text((priceVar > 0 ? "+" : "") + Number(priceVar).toFixed(2))

                var row = '<tr><td>'+Number(priceValue).toFixed(2)+'</td><td>'+moment(priceTimestamp).format('YYYY-MM-DD HH:mm:ss')+'</td></tr>'
                $('#priceList tbody').prepend(row)
            })

            stompClient.subscribe('/topic/comments', function (chatComment) {
                const chatCommentBody = JSON.parse(chatComment.body)
                const username = chatCommentBody.username
                const message = chatCommentBody.message
                const timestamp = chatCommentBody.timestamp

                const row = '<tr><td>['+moment(timestamp).format('YYYY-MM-DD HH:mm:ss')+'] '+username+': '+message+'</td></tr>'
                $('#chat tbody').prepend(row)
            })
        },
        function() {
            console.log('Unable to connect to Websocket!')
            $('#websocketSwitch').prop('checked', false)
        }
     )
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    console.log("Disconnected")
}

$(function () {
    $('#websocketSwitch').click(function() {
        if ($(this).prop('checked')) {
            connect()
        } else {
            disconnect()
        }
    })
    $('#send').click(function() {
        const usernameVal = $("#username").val()

        var message = $("#message")
        const messageVal = message.val()

        if (usernameVal.length !== 0 && messageVal.length !== 0) {
            const comment = JSON.stringify({'username': usernameVal, 'message': messageVal})
            stompClient.send("/app/chat", {}, comment)

            message.val('')
        }
    })
})

connect()