var chatVersion = 0;
var refreshRate = 1000; //milli seconds
var timeoutTime = 2000;
var PROFILE_IMG_URL = "https://www.w3schools.com/howto/img_avatar.png";
var CHAT_LIST_URL = "/chat";
var MAX_LINE_CHARACTERS = 25;

function appendChatEntry(index, entry){
    var entryElement = createChatEntry(entry);

    $("#chatarea").append(entryElement)
}

function createChatEntry (entry){

    return '<div class="chat-message clearfix">' +
        '<img src="' + PROFILE_IMG_URL + '" alt="" width="32" height="32">' +
        '<div class="chat-message-content clearfix">' +
        '<span class="chat-time">' +  entry.time + '</span>' +
        '<h5>' + entry.user.name + '</h5>' +
        '<p>' + entry.messageString + '</p>' +
        '</div>' + '</div>' + '<hr>';
}


function appendToChatArea(entries) {
    $.each(entries || [], appendChatEntry);
}

function ajaxChatContent() {
    $.ajax({
        url: CHAT_LIST_URL,
        data: "chatversion=" + chatVersion,
        dataType: 'json',
        success: function(data) {
            /*
             data will arrive in the next form:
             {
                "entries": [
                    {
                        "chatString":"Hi",
                        "username":"bbb",
                        "time":1485548397514
                    },
                    {
                        "chatString":"Hello",
                        "username":"bbb",
                        "time":1485548397514
                    }
                ],
                "version":1
             }
             */
            if (data.version !== chatVersion) {
                chatVersion = data.version;
                appendToChatArea(data.entries);
            }
            $("#user-name-label").text("Live Chat: " + data.username);
            triggerAjaxChatContent();
        },
        error: function(error) {
            triggerAjaxChatContent();
        }
    });
}

function triggerAjaxChatContent() {
    setTimeout(ajaxChatContent, refreshRate);
}

function initChatForm() {
    $("#chatform").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: "/sendChat",
            timeout: 2000,
            error: function() {
                console.error("Failed to submit");
            },
            success: function(r) {
                //do not add the user string to the chat area
                //since it's going to be retrieved from the server
                //$("#result h1").text(r);
            }
        });

        $("#userstring").val("");
        // by default - we'll always return false so it doesn't redirect the user.
        return false;
    });
}

function initLiveChat() {


    $('#live-chat header').on('click', function() {
        $('.chat').slideToggle(300, 'swing');
    });

    $('.chat-close').on('click', function(e) {
        e.preventDefault();
        $('#live-chat').fadeOut(300);
    });
}

$(function() {
    initLiveChat();
    initChatForm();
    triggerAjaxChatContent();
});