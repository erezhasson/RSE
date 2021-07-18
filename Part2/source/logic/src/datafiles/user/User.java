package datafiles.user;

import datafiles.stock.Stock;
import datafiles.stock.UserStocks;

import java.io.Serializable;
import java.util.Collection;

public class User implements Serializable {
    private final String name;
    private final UserStocks stocks;

    public User(String name) {
        this.name = name;
        stocks = new UserStocks();
    }

    public boolean hasStock(String stockSymbol) {
        return stocks.containsStock(stockSymbol);
    }

    public int getAmountOfStock(String stockSymbol) {
        return stocks.getAmountOfStock(stockSymbol);
    }

    public int getAmountOfSellingStock(String stockSymbol) { return stocks.getSellingAmountOfStock(stockSymbol);}

    public void addStockToUser(Stock stock, int quantity) {
        stocks.updateStockAmount(stock, quantity);
        stocks.updateSellingStock(stock, 0);
    }

    public Collection<Stock> getStocks() {
        return stocks.getAllStocks();
    }

    public UserStocks getStockStructure() { return stocks;}

    public String getName() {
        return name;
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

    public void decreaseStockAmount(Stock stock, int amount) {
        String symbol = stock.getSymbol();
        int userStockAmount = stocks.getAmountOfStock(symbol);

        if (userStockAmount - amount > 0) {
            stocks.updateStockAmount(stocks.getStock(symbol), userStockAmount - amount);
        }
        else {
            stocks.removeStock(stocks.getStock(symbol));
        }
    }

    public void increaseStockAmount(Stock stock, int amount) {
        String symbol = stock.getSymbol();
        if (!stocks.containsStock(symbol)) {
            stocks.addStock(stock, 0);
        }
        stocks.updateStockAmount(stocks.getStock(symbol), stocks.getAmountOfStock(symbol) + amount);
    }

    public void decreaseStockSellingAmount(Stock stock, int amount) {
        String symbol = stock.getSymbol();
        int userStockSellingAmount = stocks.getSellingAmountOfStock(symbol);

        stocks.updateSellingStock(stocks.getStock(symbol), userStockSellingAmount - amount);
    }

    public void increaseStockSellingAmount(Stock stock, int amount) {
        String symbol = stock.getSymbol();
        if (!stocks.containsStock(symbol)) {
            stocks.addStock(stock, 0);
        }
        stocks.updateSellingStock(stocks.getStock(symbol), stocks.getSellingAmountOfStock(symbol) + amount);
    }
}

