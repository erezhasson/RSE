package datafiles.dto;
import datafiles.stock.RecordBook;

public class StockDto {
    private final String companyName;
    private final String symbol;
    private final double price;
    private final RecordBookDto records;

    public StockDto(String companyName, String symbol, double price, RecordBook records) {
        this.companyName = companyName;
        this.symbol = symbol;
        this.price = price;
        this.records = new RecordBookDto(records.getBuying(), records.getSelling(), records.getCompleted());
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

    @Override
    public String toString() {
        return "Stock{" +
                "Company='" + companyName + '\'' +
                ", Symbol='" + symbol + '\'' +
                ", Price=" + price +
                '}';
    }
}
