var PENDING_COMMANDS_URL =  buildUrlWithContextPath("PendingCommands");
var chart;

function clearStockInfo() {
    $("h1").empty();
    $("#company-name").empty();
    $("#price").empty();
    $("#commandsTable").empty();
    $("#pendingCommandsTable").empty();
    $("#cycle").empty();
    $("#stock-holding").empty();
    $("h2").text("");
}

function buildCommandsTable(stock) {
    var commandsTable = $("#commandsTable");
    var commands = stock.records.completed;

    commandsTable.append('<thead><tr><th>Time</th><th>Amount</th><th>Price</th></tr></thead><tbody></tbody>')
    $.each(commands || [], function(index, command) {

        $('<tr><td>' + command.timestamp.value + '</td>' + '<td>' + command.amount.value + '</td>' +
            '<td>' + command.price.value + '$</td></tr>')
            .appendTo(commandsTable.children('tbody'));
    });
}

function buildData(commands) {
    var data = {labels: [], prices: []};

    $.each(commands || [], function(index, command) {
        data.labels.push(command.timestamp.value);
        data.prices.push(command.price.value)
    });

    return data;
}

function buildStockGraph(stock) {
    var commands = stock.records.completed;
    var data = buildData(commands);
    var ctx = $('#myChart');
    if (chart) {
        chart.destroy();
    }
    chart = new Chart(ctx, {
        type: 'line',
        data: {
            labels: data.labels,
            datasets: [{
                label: 'Deal Price',
                data: data.prices,
                fill: 'origin',
                backgroundColor: [
                    'rgba(255, 99, 132, 0.2)',
                    'rgba(54, 162, 235, 0.2)',
                    'rgba(255, 206, 86, 0.2)',
                    'rgba(75, 192, 192, 0.2)',
                    'rgba(153, 102, 255, 0.2)',
                    'rgba(255, 159, 64, 0.2)'
                ],
                borderColor: [
                    'rgba(255, 99, 132, 1)',
                    'rgba(54, 162, 235, 1)',
                    'rgba(255, 206, 86, 1)',
                    'rgba(75, 192, 192, 1)',
                    'rgba(153, 102, 255, 1)',
                    'rgba(255, 159, 64, 1)'
                ],
                borderWidth: 2

            }]
        },
        options: {
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });
}

function displayStockInfo(stockInfo) {
    var stock = stockInfo.stock;

    clearStockInfo();
    $(".stock-container").show();
    $("#stock-symbol").text(stock.symbol)
    $("#company-name").append('<span>Company Name:</span>' + '<val style="margin-left: 5px">' + stock.companyName + '</val>');
    $("#price").append('<span>Price:</span>' + '<val style="margin-left: 5px">' + stock.price + '$</val>');
    $("#cycle").append('<span>Cycle:</span>' + '<val style="margin-left: 5px">' + stock.cycle + '$</val>');
    $("#stock-holding").append('<span>You hold ' + stockInfo.amount + ' ' + stock.symbol + ' stocks.</span>');
    $("#pendingTitle").text("Pending Commands");
    $("#completedTitle").text("Completed Commands");
    $("#chartTitle").text("Time vs. Price Graph");
    buildCommandsTable(stock);
    buildStockGraph(stock);
}

function displayPendingCommands(commands) {
    var commandsTable = $("#pendingCommandsTable");

    commandsTable.append('<thead><tr><th>Time</th><th>Type</th><th>Direction</th><th>Amount</th><th>Price</th></tr></thead><tbody></tbody>')
    $.each(commands || [], function(index, command) {

        $('<tr><td>' + command.timestamp.value + '</td>' + '<td>' + command.type.value + '</td>' +
            '<td>' + command.direction.value + '</td>' + '<td>' + command.amount.value + '</td>' + '<td>' +
            command.price.value + '$</td></tr>')
            .appendTo(commandsTable.children('tbody'));
    });
}

function ajaxPendingCommands(symbol) {
    $.ajax({
        type: 'GET',
        dataType: "json",
        data: {"symbol": symbol},
        url: PENDING_COMMANDS_URL,
        timeout: 2000,
        error: function(errorObject) {
            var errorJSON = errorObject.responseJSON;

            alert("Error status " + errorJSON.status + ": " + errorJSON.message);
        },
        success: function(commands) {
            displayPendingCommands(commands);
        }
    });
    return false;
}

function initSearchForm() {
    $("#searchForm").submit(function() {
        $.ajax({
            type: 'GET',
            dataType: "json",
            data: $(this).serialize(),
            url: this.action,
            timeout: 2000,
            error: function(errorObject) {
                var errorJSON = errorObject.responseJSON;

                alert("Error status " + errorJSON.status + ": " + errorJSON.message);
            },
            success: function(stockInfo) {
                displayStockInfo(stockInfo);
                ajaxPendingCommands(stockInfo.stock.symbol);
            }
        });
        return false;
    });
}


$(function() {
    initSearchForm();
});