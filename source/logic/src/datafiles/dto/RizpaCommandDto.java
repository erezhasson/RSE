package datafiles.dto;

public abstract class RizpaCommandDto {
    protected final String stockSymbol;
    protected final String type;
    protected final double price;
    protected final int amount;
    protected final String timestamp;

    public RizpaCommandDto(String stockSymbol, String type, double price, int amount, String timestamp) {
        this.stockSymbol = stockSymbol;
        this.type = type;
        this.price = price;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return type + " Command{" +
                "Stock Symbol='" + stockSymbol + '\'' +
                ", Stocks amount='" + amount + '\'' +
                ((this.price > 0) ? (", Price=" + price + '\'' + ", Cycle=" + price * amount + '\'') : "") +
                ", Time: " + timestamp +
                '}';
    }

    public double getCycle() {
        return price * amount;
    }

    public String getType() {
        return type;
    }

    public double getPrice() {
        return price;
    }

    public int getAmount() {
        return amount;
    }

}
