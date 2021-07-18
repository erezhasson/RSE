package exceptions.symbol;

public class SymbolMismatchException extends SymbolException{
    public SymbolMismatchException(String symbol) {
        super(symbol);
    }

    @Override
    public String getMessage() {
        return "[" + symbol + "]" + " must contain only letters.";
    }
}
