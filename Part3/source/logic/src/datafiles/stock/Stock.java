package datafiles.stock;

import datafiles.commands.BuyingCommand;
import datafiles.commands.RizpaCommand;
import datafiles.user.User;
import exceptions.command.CommandParsingException;

import java.io.Serializable;

public class Stock implements Serializable {
    private final String companyName;
    private final String symbol;
    private double price;
    private RecordBook records;

    public void setRecords(RecordBook newRecordBook) {
        this.records = newRecordBook;
    }

    public RecordBook getRecords() {
        return records;
    }

    public Stock(String companyName, String symbol, double price) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.price = price;
        records = new RecordBook();
    }

    public String getSymbol() {
        return symbol;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean equal = false;

        if (obj instanceof Stock) {
            Stock s = (Stock) obj;
            equal = this.companyName.equals(s.companyName) && this.symbol.equals(s.symbol);
        }

        return equal;
    }
}


