package exceptions.stock;

public class StockNotFoundException extends StockException{
    private final String stockSymbol;

    public StockNotFoundException(String symbol) {
        this.stockSymbol = symbol;
    }

    @Override
    public String getMessage() {
        return "[" + stockSymbol.toUpperCase() + "]" + " does not exists in system.";
    }
}
