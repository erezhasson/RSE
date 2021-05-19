package datafiles.dto;

public class SellingCommandDto extends RizpaCommandDto{
    public SellingCommandDto(String stockSymbol, String type, double price, int amount, String timestamp) {
        super(stockSymbol, type, price, amount, timestamp);
    }

    @Override
    public String toString() {
        return "Selling " + super.toString();
    }
}
