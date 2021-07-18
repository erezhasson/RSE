package datafiles.dto;

import datafiles.commands.RizpaCommand;
import datafiles.stock.Stock;
import datafiles.stock.UserStocks;
import datafiles.user.Transaction;
import datafiles.user.User;
import exceptions.stock.StockNotFoundException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class UserDto {
    private final String name;
    private final UserStocksDto stocks;
    private final ObservableList<RizpaCommandDto> appliedCommands;
    private final ObservableList<RizpaCommandDto> completedCommands;
    private final Collection<Transaction> transactions;
    private final double balance;
    private final String alert;

    public UserDto(User user) {
        UserStocks userStocks = user.getStockStructure();
        Collection<RizpaCommand> appliedCommands = user.getAppliedCommands(), completedCommands = user.getCompletedCommands();

        this.name = user.getName();
        this.alert = user.getNextAlert();
        stocks = new UserStocksDto();
        for(Stock stock : userStocks.getAllStocks()) {
            stocks.addStock(new StockDto(stock), userStocks.getAmountOfStock(stock.getSymbol()));
        }
        this.appliedCommands = appliedCommands.size() != 0 ? RecordBookDto.buildDtoList(appliedCommands) :
             FXCollections.observableArrayList();
        this.completedCommands = completedCommands.size() != 0 ? RecordBookDto.buildDtoList(completedCommands) :
                FXCollections.observableArrayList();
        this.transactions = user.getTransactions();
        this.balance = user.getBalance();
    }

    public String getAlert() {
        return alert;
    }

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public ObservableList<RizpaCommandDto> getAppliedCommands() {
        return appliedCommands;
    }

    public ObservableList<RizpaCommandDto> getCompletedCommands() {
        return completedCommands;
    }

    public Collection<StockDto> getStocks() {
        return stocks.getAllStocks();
    }

    public String getName() {
        return name;
    }

    public int getStockAmount(String symbol) {
        return stocks.getStockAmount(symbol);
    }

    public double getBalance() {
        return balance;
    }
}
