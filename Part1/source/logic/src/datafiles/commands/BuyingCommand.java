package datafiles.commands;

import exceptions.command.CommandParsingException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BuyingCommand extends RizpaCommand {
    public BuyingCommand(String stockSymbol, CommandType type, double price, int amount) {
        super(stockSymbol, type, price, amount);
    }

    @Override
    public int compareTo(RizpaCommand comparedCommand) {
        Double price = comparedCommand.getPrice();
        int comparison = price.compareTo(getPrice());

        if (comparison == 0) {
            comparison = super.compareTo(comparedCommand);
        }
        return comparison;
    }

    @Override
    public BuyingCommand clone() {
        CommandType commandType = null;

        try {
            commandType = convertStringToType(this.displayType);
        } catch (CommandParsingException ignored){
        }
        return new BuyingCommand(this.stockSymbol, commandType, this.price, this.amount);
    }
}
