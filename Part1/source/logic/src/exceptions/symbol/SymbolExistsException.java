package exceptions.symbol;

public class SymbolExistsException extends SymbolException{
    public SymbolExistsException(String symbol) {
        super(symbol);
    }

    @Override
    public String getMessage() {
        return "[" + symbol + "]" + " already exists in system (Stock's symbol is unique).";
    }
}
