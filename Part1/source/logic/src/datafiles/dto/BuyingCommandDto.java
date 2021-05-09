package datafiles.dto;

public class BuyingCommandDto extends RizpaCommandDto{
    public BuyingCommandDto(String stockSymbol, String type, double price, int amount, String timestamp) {
        super(stockSymbol, type, price, amount, timestamp);
    }

    @Override
    public String toString() {
        return "Buying " + super.toString();
    }
}
