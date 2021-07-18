package datafiles.dto;

import javafx.beans.property.*;

public abstract class RizpaCommandDto {
    protected final StringProperty stockSymbol;
    protected final StringProperty type;
    protected final DoubleProperty price;
    protected final IntegerProperty amount;
    protected final StringProperty timestamp;
    protected final StringProperty buyer;
    protected final StringProperty seller;

    public RizpaCommandDto(String stockSymbol, String type, double price, int amount, String timestamp, String buyer, String seller) {
        this.stockSymbol = new SimpleStringProperty(stockSymbol);
        this.type = new SimpleStringProperty(type);
        this.price = new SimpleDoubleProperty(price);
        this.amount = new SimpleIntegerProperty(amount);
        this.timestamp = new SimpleStringProperty(timestamp);
        this.buyer = buyer != null ? new SimpleStringProperty(buyer) :
                new SimpleStringProperty("None");
        this.seller = seller != null ? new SimpleStringProperty(seller) :
                new SimpleStringProperty("None");
    }

    @Override
    public String toString() {
        String type = getType() + " Command\n";
        String symbol = "Symbol: " + getStockSymbol() + "\n";
        String price = ((this.price.get() > 0) ? "Price: " + getPrice() + "\nCycle: " + getPrice() * getAmount() + "\n" : "");
        String timestamp = "Time: " + getTimestamp() + "\n";
        String seller = "Seller: " + getSeller() + "\n";
        String buyer = "Buyer: " + getBuyer() + "\n";

        return type + symbol + price + timestamp + seller + buyer;
    }

    public abstract StringProperty directionProperty();

    public double getCycle() {
        return price.get() * amount.get();
    }

    public String getType() {
        return type.get();
    }

    public double getPrice() {
        return price.get();
    }

    public int getAmount() {
        return amount.get();
    }

    public String getStockSymbol() {
        return stockSymbol.get();
    }

    public String getTimestamp() {
        return timestamp.get();
    }

    public StringProperty stockSymbolProperty() {
        return stockSymbol;
    }

    public StringProperty typeProperty() {
        return type;
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public IntegerProperty amountProperty() {
        return amount;
    }

    public StringProperty timestampProperty() {
        return timestamp;
    }

    public String getBuyer() {
        return buyer.get();
    }

    public StringProperty buyerProperty() {
        return buyer;
    }

    public String getSeller() {
        return seller.get();
    }

    public StringProperty sellerProperty() {
        return seller;
    }
}
