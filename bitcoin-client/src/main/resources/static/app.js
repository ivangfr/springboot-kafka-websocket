let stompClient = null;

function setWsStatus(connected) {
    const dot = $('#wsStatusDot')
    const text = $('#wsStatusText')
    const controlText = $('#wsControlText')

    if (connected) {
        dot.html(
            '<span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>' +
            '<span class="relative inline-flex rounded-full h-3 w-3 bg-emerald-500"></span>'
        )
        text.text('Connected').removeClass('text-red-400').addClass('text-emerald-400')
        controlText.text('Connected')
    } else {
        dot.html(
            '<span class="animate-ping absolute inline-flex h-full w-full rounded-full bg-red-400 opacity-75"></span>' +
            '<span class="relative inline-flex rounded-full h-3 w-3 bg-red-500"></span>'
        )
        text.text('Disconnected').removeClass('text-emerald-400').addClass('text-red-400')
        controlText.text('Disconnected')
    }
}

function setVariationStyle(priceVar) {
    const badge = $('#variationBadge')
    const arrow = $('#variationArrow')
    const variation = $('#variation')

    badge.removeClass(
        'bg-emerald-950 border-emerald-800 text-emerald-400 ' +
        'bg-red-950 border-red-800 text-red-400 ' +
        'bg-gray-800 border-gray-700 text-gray-500'
    )
    arrow.text('')
    variation.removeClass('text-emerald-400 text-red-400 text-gray-500')

    const formatted = (priceVar > 0 ? '+' : '') + Number(priceVar).toFixed(2)

    if (priceVar > 0) {
        badge.addClass('bg-emerald-950 border border-emerald-800 text-emerald-400')
        arrow.text('▲')
        variation.addClass('text-emerald-400')
    } else if (priceVar < 0) {
        badge.addClass('bg-red-950 border border-red-800 text-red-400')
        arrow.text('▼')
        variation.addClass('text-red-400')
    } else {
        badge.addClass('bg-gray-800 border border-gray-700 text-gray-500')
        arrow.text('—')
    }

    variation.text(formatted)
}

function connect() {
    stompClient = new StompJs.Client({
        webSocketFactory: () => new SockJS('/websocket'),
        debug: () => {}
    })

    let prevPriceValue = null

    stompClient.onConnect = function (frame) {
        console.log('Connected: ' + frame)
        setWsStatus(true)

        stompClient.subscribe('/topic/prices', function (price) {
            const priceBody = JSON.parse(price.body)
            const priceValue = priceBody.value
            const priceTimestamp = priceBody.timestamp

            if (prevPriceValue == null) {
                prevPriceValue = priceValue
            }
            const priceVar = priceValue - prevPriceValue
            prevPriceValue = priceValue

            $('#currentPrice').text(Number(priceValue).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 }))

            setVariationStyle(priceVar)

            const formattedPrice = Number(priceValue).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
            const formattedTime = dayjs(priceTimestamp).format('YYYY-MM-DD HH:mm:ss')
            const varSign = priceVar > 0 ? '+' : ''
            const varClass = priceVar > 0 ? 'text-emerald-400' : priceVar < 0 ? 'text-red-400' : 'text-gray-500'
            const row = `<tr class="border-b border-gray-800/60 hover:bg-gray-800/40 transition-colors">
                <td class="px-5 py-2.5 text-white font-semibold tabular-nums">
                    $${formattedPrice}
                    <span class="ml-2 text-xs ${varClass} tabular-nums">${varSign}${Number(priceVar).toFixed(2)}</span>
                </td>
                <td class="px-5 py-2.5 text-gray-500 text-xs text-right tabular-nums">${formattedTime}</td>
            </tr>`

            if ($('#priceList tr').length > 20) {
                $('#priceList tbody tr:last').remove()
            }
            $('#priceList').find('tbody').prepend(row)
            $('#priceEmpty').hide()
        })

        stompClient.subscribe('/topic/chat-messages', function (chatMessage) {
            appendChatMessage(chatMessage, false)
        })

        stompClient.subscribe('/user/topic/chat-messages', function (chatMessage) {
            appendChatMessage(chatMessage, true)
        })
    }

    stompClient.onStompError = function (frame) {
        console.log('STOMP error: ' + frame)
        setWsStatus(false)
        $('#websocketSwitch').prop('checked', false)
    }

    stompClient.onWebSocketClose = function () {
        console.log('WebSocket closed')
        setWsStatus(false)
        $('#websocketSwitch').prop('checked', false)
    }

    stompClient.activate()
}

function appendChatMessage(chatMessage, isPrivate) {
    const body = JSON.parse(chatMessage.body)
    const fromUser = body.fromUser
    const comment = body.comment
    const timestamp = body.timestamp

    const formattedTime = dayjs(timestamp).format('HH:mm:ss')
    const initials = fromUser.substring(0, 2).toUpperCase()
    const colors = ['bg-violet-800', 'bg-blue-800', 'bg-emerald-800', 'bg-amber-800', 'bg-rose-800']
    const colorIndex = fromUser.charCodeAt(0) % colors.length
    const avatarColor = colors[colorIndex]

    const privateTag = isPrivate
        ? '<span class="ml-2 px-1.5 py-0.5 rounded text-xs font-semibold bg-violet-900/60 text-violet-300 border border-violet-700">private</span>'
        : '<span class="ml-2 px-1.5 py-0.5 rounded text-xs font-semibold bg-gray-800 text-gray-500 border border-gray-700">broadcast</span>'

    const row = `<tr class="border-b border-gray-800/60">
        <td class="px-4 py-3">
            <div class="flex items-start gap-3">
                <div class="flex-shrink-0 w-7 h-7 rounded-full ${avatarColor} flex items-center justify-center text-xs font-bold text-white">${initials}</div>
                <div class="flex-1 min-w-0">
                    <div class="flex items-center gap-1 flex-wrap">
                        <span class="text-white text-xs font-semibold">${fromUser}</span>
                        ${privateTag}
                        <span class="text-gray-600 text-xs ml-auto tabular-nums">${formattedTime}</span>
                    </div>
                    <p class="text-gray-300 text-sm mt-0.5 break-words">${comment}</p>
                </div>
            </div>
        </td>
    </tr>`

    $('#chatList').find('tbody').prepend(row)
    $('#chatEmpty').hide()
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.deactivate()
    }
    setWsStatus(false)
    console.log('Disconnected')
}

$(function () {
    $('#websocketSwitch').click(function () {
        if ($(this).prop('checked')) {
            connect()
        } else {
            disconnect()
        }
    })

    $('#comment').keydown(function (e) {
        if (e.key === 'Enter' && !e.shiftKey) {
            e.preventDefault()
            $('#chatForm').submit()
        }
    })

    $('#comment').on('input', function () {
        $(this).removeClass('border-red-500')
    })

    $('#chatForm').submit(function (e) {
        e.preventDefault()

        const fromUser = $('#fromUser').val()
        const toUser = $('#toUser').val()
        const $comment = $('#comment')
        const comment = $comment.val()
        const timestamp = new Date()

        if (fromUser.length !== 0 && comment.length !== 0) {
            const chatMessage = JSON.stringify({ fromUser, toUser, comment, timestamp })
            stompClient.publish({ destination: '/app/chat', body: chatMessage })
            $comment.val('')
            $comment.removeClass('border-red-500')
        } else if (comment.length === 0) {
            $comment.addClass('border-red-500').focus()
        }
    })

    connect()
})
