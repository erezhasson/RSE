package datafiles.dto;

import datafiles.commands.SellingCommand;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class SellingCommandDto extends RizpaCommandDto{
    private final StringProperty direction;

    public SellingCommandDto(SellingCommand command) {
        super(command);
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
