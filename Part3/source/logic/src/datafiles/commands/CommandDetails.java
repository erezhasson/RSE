package datafiles.commands;

public class CommandDetails {
    private final String stockSymbol;
    private final String type;
    private final String price;
    private final String amount;
    private final String direction;

    public CommandDetails(String stockSymbol, String type, String price, String amount, String direction) {
        this.stockSymbol = stockSymbol;
        this.type = type;
        this.price = price;
        this.amount = amount;
        this.direction = direction;
    }

    public String getStockSymbol() {
        return stockSymbol;
    }

    public String getType() {
        return type;
    }

    public String getPrice() {
        return price;
    }

    public String getAmount() {
        return amount;
    }

    public String getDirection() {
        return direction;
    }
}
