package datafiles.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SellingCommandDto extends RizpaCommandDto{
    private final StringProperty direction;

    public SellingCommandDto(String stockSymbol, String type, double price, int amount, String timestamp, String seller, String buyer) {
        super(stockSymbol, type, price, amount, timestamp, buyer, seller);
        direction = new SimpleStringProperty("Selling");
    }

    @Override
    public StringProperty directionProperty() {
        return direction;
    }

    @Override
    public String toString() {
        return "Selling " + super.toString();
    }
}
