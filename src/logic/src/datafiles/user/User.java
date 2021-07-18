package datafiles.user;

import datafiles.commands.RizpaCommand;
import datafiles.stock.Stock;
import datafiles.stock.UserStocks;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

public class User implements Serializable {
    private final String name;
    private final UserStocks stocks;
    private final Collection<RizpaCommand> appliedCommands;
    private final Collection<RizpaCommand> completedCommands;
    private final Collection<Transaction> transactions;
    private final UserAlerts alerts;
    private double balance;

    public User(String name) {
        this.name = name;
        alerts = new UserAlerts();
        stocks = new UserStocks();
        appliedCommands = new ArrayList<>();
        completedCommands = new ArrayList<>();
        this.balance = 0;
        this.transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public boolean hasStock(String stockSymbol) {
        return stocks.containsStock(stockSymbol);
    }

    public int getAmountOfStock(String stockSymbol) {
        return stocks.getAmountOfStock(stockSymbol);
    }

    public int getAmountOfSellingStock(String stockSymbol) { return stocks.getSellingAmountOfStock(stockSymbol);}

    public void addCommandToAppliedRecords(RizpaCommand command) {
        appliedCommands.add(command);
    }

    public void addCommandToCompletedRecords(RizpaCommand command) {
        completedCommands.add(command);
    }

    public Collection<RizpaCommand> getAppliedCommands() {
        return appliedCommands;
    }

    public Collection<RizpaCommand> getCompletedCommands() {
        return completedCommands;
    }

    public void addStockToUser(Stock stock, int quantity) {
        stocks.updateStockAmount(stock, quantity);
        stocks.updateSellingStock(stock, 0);
    }

    public Collection<Stock> getStocks() {
        return stocks.getAllStocks();
    }

    public UserStocks getStockStructure() { return stocks;}

    public Collection<Transaction> getTransactions() {
        return transactions;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getName() {
        return name;
    }

    public String getNextAlert() {
        return alerts.getNextAlert();
    }

    public void addAlert(String alert) {
        alerts.addAlert(alert);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;

        if (obj instanceof User) {
            User u = (User) obj;
            equal = this.name.equals(u.name);
        }

        return equal;
    }

    public void clearHoldings() {
        stocks.clearStocks();
    }

    public void decreaseStockAmount(Stock stock, int amount) {
        String symbol = stock.getSymbol();
        int userStockAmount = stocks.getAmountOfStock(symbol);
        int userStockSellingAmount = stocks.getSellingAmountOfStock(symbol);

        if (userStockAmount - amount > 0) {
            stocks.updateStockAmount(stocks.getStock(symbol), userStockAmount - amount);
            stocks.updateSellingStock(stock, userStockSellingAmount - amount);
        }
        else {
            stocks.removeStock(stocks.getStock(symbol));
        }
    }

    public void decreaseStockSellingAmount(Stock stock, int amount) {
        String symbol = stock.getSymbol();
        int userStockSellingAmount = stocks.getSellingAmountOfStock(symbol);

        stocks.updateSellingStock(stock, userStockSellingAmount - amount);
    }

    public void increaseStockAmount(Stock stock, int amount) {
        String symbol = stock.getSymbol();
        if (!stocks.containsStock(symbol)) {
            stocks.addStock(stock, 0);
        }
        stocks.updateStockAmount(stocks.getStock(symbol), stocks.getAmountOfStock(symbol) + amount);
    }

    public void increaseStockSellingAmount(Stock stock, int amount) {
        String symbol = stock.getSymbol();
        if (!stocks.containsStock(symbol)) {
            stocks.addStock(stock, 0);
        }
        stocks.updateSellingStock(stocks.getStock(symbol), stocks.getSellingAmountOfStock(symbol) + amount);
    }
}

