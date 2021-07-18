package ui.actionwindow;

import javafx.beans.property.*;

public class StockTableRow {
    private final StringProperty stockSymbol;
    private final IntegerProperty stockAmount;
    private final DoubleProperty stockPrice;

    public StockTableRow(String stockSymbol, int stockAmount, double stockPrice) {
        this.stockSymbol = new SimpleStringProperty(stockSymbol);
        this.stockAmount = new SimpleIntegerProperty(stockAmount);
        this.stockPrice = new SimpleDoubleProperty(stockPrice);
    }

    public String getStockSymbol() {
        return stockSymbol.get();
    }

    public StringProperty stockSymbolProperty() {
        return stockSymbol;
    }

    public int getStockAmount() {
        return stockAmount.get();
    }

    public IntegerProperty stockAmountProperty() {
        return stockAmount;
    }

    public double getStockPrice() {
        return stockPrice.get();
    }

    public DoubleProperty stockPriceProperty() {
        return stockPrice;
    }
}
