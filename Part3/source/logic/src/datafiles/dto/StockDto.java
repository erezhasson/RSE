package datafiles.dto;

import datafiles.stock.Stock;

public class StockDto {
    private final String companyName;
    private final String symbol;
    private final double price;
    private final double cycle;
    private final RecordBookDto records;

    public StockDto(Stock stock) {
        this.companyName = stock.getCompanyName();
        this.symbol = stock.getSymbol();
        this.price = stock.getPrice();
        this.records = new RecordBookDto(stock.getRecords());
        this.cycle = getCycle();
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
