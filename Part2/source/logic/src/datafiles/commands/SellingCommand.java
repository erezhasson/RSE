package datafiles.commands;

import datafiles.user.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SellingCommand extends RizpaCommand {
    public SellingCommand(String stockSymbol, CommandType type, double price, int amount, User seller) {
        super(stockSymbol, type, price, amount, null, seller);
    }

    @Override
    public int compareTo(RizpaCommand comparedCommand) {
        Double price = getPrice();
        int comparison = price.compareTo(comparedCommand.getPrice());

        if (comparison == 0) {
            comparison = super.compareTo(comparedCommand);
        }
        return comparison;
    }

    @Override
    public SellingCommand clone() {
        return new SellingCommand(this.stockSymbol, this.type, this.price, this.amount, this.seller);
    }
}

