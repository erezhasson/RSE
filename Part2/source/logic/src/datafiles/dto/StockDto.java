package datafiles.dto;
import datafiles.stock.RecordBook;

import java.util.List;

public class StockDto {
    private final String companyName;
    private final String symbol;
    private final double price;
    private final RecordBookDto records;

    public StockDto(String companyName, String symbol, double price, RecordBook stockRecords) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.price = price;
        this.records = new RecordBookDto(stockRecords.getBuying(), stockRecords.getSelling(), stockRecords.getCompleted());
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getSymbol() {
        return symbol;
    }

    public double getPrice() {
        return price;
    }

    public RecordBookDto getRecords() {
        return records;
    }

    public double getCycle() {
        double cycle = 0;
        int totalRecords = records.getCompleted().size();

        if (totalRecords != 0) {
            for (RizpaCommandDto command: records.getCompleted()) {
                cycle += command.getCycle();
            }
        }

        return cycle;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "Company='" + companyName + '\'' +
                ", Symbol='" + symbol + '\'' +
                ", Price=" + price +
                '}';
    }
}
