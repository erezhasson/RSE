var refreshRate = 1000; //milli seconds
var timeoutTime = 2000;
var ALERT_USER_URL = "/alertUser"

function ajaxAlertUser() {
    $.ajax({
        type: "GET",
        url: ALERT_USER_URL,
        timeout: 2000,
        success: function(message) {
            console.log(message)
            if (message) alert(message);
        }
    });
    return false;
}

$(function() {
    setInterval(ajaxAlertUser, timeoutTime, refreshRate);
});