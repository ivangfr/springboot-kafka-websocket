let stompClient = null;

function connect() {
    const socket = new SockJS('/websocket')
    stompClient = Stomp.over(socket)

    let prevPriceValue = null
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

                const row = '<tr><td>'+Number(priceValue).toFixed(2)+'</td><td>'+moment(priceTimestamp).format('YYYY-MM-DD HH:mm:ss')+'</td></tr>'
                if ($('#priceList tr').length > 20) {
                    $('#priceList tr:last').remove()
                }
                $('#priceList').find('tbody').prepend(row)
            })

            stompClient.subscribe('/topic/comments', function (chatComment) {
                const chatCommentBody = JSON.parse(chatComment.body)
                const fromUser = chatCommentBody.fromUser
                const message = chatCommentBody.message
                const timestamp = chatCommentBody.timestamp

                const row = '<tr><td>['+moment(timestamp).format('YYYY-MM-DD HH:mm:ss')+'] '+fromUser+' to all: '+message+'</td></tr>'
                $('#chat').find('tbody').prepend(row)
            })

            stompClient.subscribe('/user/topic/comments', function (chatComment) {
                const chatCommentBody = JSON.parse(chatComment.body)
                const fromUser = chatCommentBody.fromUser
                const message = chatCommentBody.message
                const timestamp = chatCommentBody.timestamp

                const row = '<tr><td>['+moment(timestamp).format('YYYY-MM-DD HH:mm:ss')+'] '+fromUser+' to you: '+message+'</td></tr>'
                $('#chat').find('tbody').prepend(row)
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

    $('#chatForm').submit(function(e) {
        e.preventDefault();

        const fromUserVal = $("#fromUser").val()
        const toUserVal = $("#toUser").val()
        const message = $("#message")
        const messageVal = message.val()

        if (fromUserVal.length !== 0 && messageVal.length !== 0) {
            const comment = JSON.stringify({'fromUser': fromUserVal, 'toUser': toUserVal, 'message': messageVal})
            stompClient.send("/app/chat", {}, comment)
            message.val('')
        }
    })

    let height = window.innerHeight - 245
    $('#priceList').parent().css({"height": height, "max-height": height, "overflow-y": "auto"})

    height = window.innerHeight - 500
    $('#chat').parent().css({"height": height, "max-height": height, "overflow-y": "auto"})
})

connect()