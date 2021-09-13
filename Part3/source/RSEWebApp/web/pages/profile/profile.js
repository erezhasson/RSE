var refreshRate = 1000; //milli seconds
var timeoutTime = 2000;
var USER_DATA_URL = buildUrlWithContextPath("userData");
var BALANCE_URL =  buildUrlWithContextPath("balance");
var TRANSACTION_TABLE_URL =  buildUrlWithContextPath("transactionTable");
var PROFILE_IMG_URL = "https://www.w3schools.com/howto/img_avatar.png";

function displayUserInfo(userInfo) {
    var profile = $("#profile-container");

    profile.prepend('<img src="' + PROFILE_IMG_URL + '" alt="Avatar">')
    $("#name").text(userInfo.name);
    $("#balance").text("CURRENT BALANCE: " + userInfo.balance)
        .append('<span style="color: #3ba55d;background: transparent">$</span>');
}

function transactionSign(transactionAmount) {
    if (transactionAmount > 0) {
        return '<span style="color: #3ba55d;background:transparent;font-weight: bold">&#8593;</span>'
    }
    else if (transactionAmount === 0) {
        return '<span style="color: #007eff;background:transparent;font-weight: bold">-</span>'
    }
    else {
        return '<span style="color: #ff0000;background:transparent;font-weight: bold">&#8595;</span>'
    }
}

function refreshTransactionTable(tableEntries) {
    var transactionTable = $("#transaction-table").children('tbody');

    transactionTable.empty();
    $.each(tableEntries || [], function(index, entry) {
        transactionTable.append('<tr><td>' + entry.timestamp + '</td><td>' + entry.type + '</td><td>' +
            entry.transactionAmount + '</td><td>' +  transactionSign(entry.transactionAmount) +
            entry.balanceAfterTransaction  + '</td></tr>');
    });
}

function ajaxUserInfo() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: USER_DATA_URL,
        timeout: 2000,
        success: function(transactionTable) {
            displayUserInfo(transactionTable)
        }
    });
    return false;
}

function ajaxTransactionTable() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: TRANSACTION_TABLE_URL,
        timeout: 2000,
        success: function(userInfo) {
            refreshTransactionTable(userInfo)
        }
    });
    return false;
}

function chargeBalanceForm() {
    $("#balance-charge-form").submit(function() {
        $.ajax({
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            success: function() {
                alert("Balance charged successfully.")
                $("#charge-amount").val('')
            }
        });
        return false;
    });
}

function ajaxBalance() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: BALANCE_URL,
        timeout: 2000,
        success: function(balanceObject) {
            $("#balance").text("CURRENT BALANCE: " + balanceObject.balance)
                .append('<span style="color: #3ba55d;background: transparent">$</span>');
        }
    });
    return false;
}

function ajaxFileUpload() {
    $("#file-upload-form").submit(function() {
        var formData = new FormData($(this)[0]);

        console.log(formData)
        $.ajax({
            type: 'POST',
            url: this.action,
            data: formData,
            processData: false,
            contentType: false,
            timeout: timeoutTime,
            error: function(errorObject) {
                var errorJSON = errorObject.responseJSON;

                alert("Error status " + errorJSON.status + ": " + errorJSON.message);
            },
            success: function(message) {
                alert(message);
            }
        });
        return false;
    });
}

$(function() {
    $("#balance-charger").hide();
    ajaxUserInfo();
    setInterval(ajaxTransactionTable, timeoutTime, refreshRate);
    setInterval(ajaxBalance, timeoutTime, refreshRate);
    chargeBalanceForm();
    ajaxFileUpload();
});