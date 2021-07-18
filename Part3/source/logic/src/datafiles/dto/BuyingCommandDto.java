package datafiles.dto;

import datafiles.commands.BuyingCommand;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BuyingCommandDto extends RizpaCommandDto{
    private final StringProperty direction;

    public BuyingCommandDto(BuyingCommand command) {
        super(command);
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
