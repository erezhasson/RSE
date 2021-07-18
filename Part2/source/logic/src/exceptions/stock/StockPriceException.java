package exceptions.stock;

public class StockPriceException extends StockException {
    private final double price;

    public StockPriceException(double price) {
        this.price = price;
    }

    @Override
    public String getMessage() {
        return "[" + price + "]" + " not a valid stock price (must be positive number).";
    }
}
