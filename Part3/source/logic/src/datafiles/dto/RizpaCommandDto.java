package datafiles.dto;

import datafiles.commands.RizpaCommand;
import datafiles.user.User;
import javafx.beans.property.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class RizpaCommandDto implements Comparable<RizpaCommandDto>{
    protected final StringProperty stockSymbol;
    protected final StringProperty type;
    protected final DoubleProperty price;
    protected final IntegerProperty amount;
    protected final StringProperty timestamp;
    protected final StringProperty buyer;
    protected final StringProperty seller;

    public RizpaCommandDto(RizpaCommand command) {
        User buyer = command.getBuyer(), seller = command.getSeller();

        this.stockSymbol = new SimpleStringProperty(command.getStockSymbol());
        this.type = new SimpleStringProperty(command.getType());
        this.price = new SimpleDoubleProperty(command.getPrice());
        this.amount = new SimpleIntegerProperty(command.getAmount());
        this.timestamp = new SimpleStringProperty(command.getTimestamp());
        this.buyer = buyer != null ? new SimpleStringProperty(buyer.getName()) :
                new SimpleStringProperty("None");
        this.seller = seller != null ? new SimpleStringProperty(seller.getName()) :
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


    @Override
    public int compareTo(RizpaCommandDto comparedCommand) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");

        try {
            Date d1 = sdf.parse(this.timestamp.get());
            Date d2 = sdf.parse(comparedCommand.timestamp.get());

            return d1.compareTo(d2);
        } catch (ParseException ignored) {
        }
        return -1;
    }
}
