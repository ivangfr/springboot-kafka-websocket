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

            stompClient.subscribe('/topic/chat-messages', function (chatMessage) {
                const chatMessageBody = JSON.parse(chatMessage.body)
                const fromUser = chatMessageBody.fromUser
                const comment = chatMessageBody.comment
                const timestamp = chatMessageBody.timestamp

                const row = '<tr><td>['+moment(timestamp).format('YYYY-MM-DD HH:mm:ss')+'] '+fromUser+' to all: '+comment+'</td></tr>'
                $('#chat').find('tbody').prepend(row)
            })

            stompClient.subscribe('/user/topic/chat-messages', function (chatMessage) {
                const chatMessageBody = JSON.parse(chatMessage.body)
                const fromUser = chatMessageBody.fromUser
                const comment = chatMessageBody.comment
                const timestamp = chatMessageBody.timestamp

                const row = '<tr><td>['+moment(timestamp).format('YYYY-MM-DD HH:mm:ss')+'] '+fromUser+' to you: '+comment+'</td></tr>'
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

        const fromUser = $("#fromUser").val()
        const toUser = $("#toUser").val()
        const $comment = $("#comment")
        const comment = $comment.val()
        const timestamp = new Date()

        if (fromUser.length !== 0 && comment.length !== 0) {
            const chatMessage = JSON.stringify({fromUser, toUser, comment, timestamp})
            stompClient.send("/app/chat", {}, chatMessage)
            $comment.val('')
        }
    })

    let height = window.innerHeight - 245
    $('#priceList').parent().css({"height": height, "max-height": height, "overflow-y": "auto"})

    height = window.innerHeight - 500
    $('#chat').parent().css({"height": height, "max-height": height, "overflow-y": "auto"})

    connect()
})