var refreshRate = 1000; //milli seconds
var timeoutTime = 2000;
var USER_LIST_URL = "/usersList";
var STOCK_LIST_URL = "/stocksList";
var COMMAND_LIST_URL = "/commandsList";
var DEAL_LIST_URL = "/dealsList";
var STOCK_SALES_URL = "/stockSales"
var STOCK_TABLE_URL = "/stockTable"
var ONLINE_USERS_URL = "/onlineUsers"

function ajaxList(argument) {
    $.ajax({
        url: argument.url,
        success: function(newAmount) {
            $(argument.htmlTag).text(newAmount);
            $(argument.htmlTag + " span").text(argument.spanText);
        }
    });
}

function ajaxStockSales() {
    $.ajax({
        url: STOCK_SALES_URL,
        success: function(newAmount) {
            $("#stockSales").text(newAmount + "%");
        }
    });
}

function refreshStockTable(tableEntries) {
    var stockTable = $("#stockTable").children('tbody');

    stockTable.empty();
    $.each(tableEntries || [], function(index, entry) {
        stockTable.append('<tr><td>' + entry.symbol + '</td><td>' + entry.companyName + '</td><td>' + entry.price + '$' +
            '</td><td>' + entry.cycle + '$' + '</td></tr>');
    });
}

function ajaxStockTable() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: STOCK_TABLE_URL,
        success: function(tableEntries) {
            refreshStockTable(tableEntries);
        }
    });
}

function refreshOnlineUsersList(userListEntries) {
    var usersList = $("#onlineUsers");

    usersList.empty();
    $.each(userListEntries || [], function(index, user) {
        usersList.append('<dt>' + user.name + ': ' + user.role + '</dt>');
    });
}

function ajaxOnlineUsers() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: ONLINE_USERS_URL,
        success: function(userListEntries) {
            refreshOnlineUsersList(userListEntries);
        }
    });
}

$(function() {
    setInterval(ajaxList, timeoutTime,{htmlTag:"#totalUsers", url:USER_LIST_URL, spanText:"Users"},  refreshRate);
    setInterval(ajaxList, timeoutTime,{htmlTag:"#totalStocks", url:STOCK_LIST_URL, spanText:"Stocks"},  refreshRate);
    setInterval(ajaxList, timeoutTime,{htmlTag:"#totalCommands", url:COMMAND_LIST_URL, spanText:"Applied Commands"},  refreshRate);
    setInterval(ajaxList, timeoutTime,{htmlTag:"#totalDeals", url:DEAL_LIST_URL, spanText:"Completed Deals"},  refreshRate);
    setInterval(ajaxStockTable, timeoutTime, refreshRate);
    setInterval(ajaxOnlineUsers, timeoutTime, refreshRate);
    setInterval(ajaxStockSales, timeoutTime, refreshRate);
});