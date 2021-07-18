package datafiles.dto;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BuyingCommandDto extends RizpaCommandDto{
    private final StringProperty direction;

    public BuyingCommandDto(String stockSymbol, String type, double price, int amount, String timestamp, String buyer, String seller) {
        super(stockSymbol, type, price, amount, timestamp, buyer, seller);
        direction = new SimpleStringProperty("Buying");
    }

    @Override
    public StringProperty directionProperty() {
        return direction;
    }

    @Override
    public String toString() {
        return "Buying " + super.toString();
    }
}
