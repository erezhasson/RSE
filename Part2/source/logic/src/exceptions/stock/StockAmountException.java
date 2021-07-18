package exceptions.stock;

public class StockAmountException extends StockException{
    private final int amount;

    public StockAmountException(int amount) {
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        return "[" + amount + "]" + " not a valid stock amount (must be positive number).";
    }
}
