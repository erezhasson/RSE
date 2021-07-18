package exceptions.symbol;

public class SymbolException extends Exception {
    protected final String symbol;

    public SymbolException(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String getMessage() {
        return "Something went wrong with " + symbol;
    }
}
