package datafiles.stock;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;

public class UserStocks implements Serializable {
    private final HashMap<Stock, Integer> Stocks2Amount;
    private final HashMap<Stock, Integer> Stocks2SellingAmount;

    public UserStocks() {
        this.Stocks2Amount = new HashMap<>();
        this.Stocks2SellingAmount = new HashMap<>();
    }

    public boolean containsStock(String stockSymbol) {
        for (Stock stock : Stocks2Amount.keySet()) {
            if (stock.getSymbol().equals(stockSymbol)) {
                return true;
            }
        }

        return false;
    }

    public void addStock(Stock stock, int quantity) {
        Stocks2Amount.put(stock, quantity);
        Stocks2SellingAmount.put(stock, 0);
    }

    public void updateStockAmount(Stock stock, int quantity) {
        Stocks2Amount.put(stock, quantity);
    }

    public void updateSellingStock(Stock stock, int quantity) {
        Stocks2SellingAmount.put(stock, quantity);
    }

    public Collection<Stock> getAllStocks() {
        return Stocks2Amount.keySet();
    }

    public Stock getStock(String stockSymbol) {
        for (Stock stock : Stocks2Amount.keySet()) {
            if (stock.getSymbol().equals(stockSymbol)) {
                return stock;
            }
        }

        return null;
    }

    public Stock getSellingStock(String stockSymbol) {
        for (Stock stock : Stocks2SellingAmount.keySet()) {
            if (stock.getSymbol().equals(stockSymbol)) {
                return stock;
            }
        }

        return null;
    }

    public int getSellingAmountOfStock(String stockSymbol) {
        return Stocks2SellingAmount.get(getSellingStock(stockSymbol));
    }

    public int getAmountOfStock(String stockSymbol) {
        return Stocks2Amount.get(getStock(stockSymbol));
    }

    public void removeStock(Stock stock) {
        Stocks2Amount.remove(stock);
        Stocks2SellingAmount.remove(stock);
    }
}
