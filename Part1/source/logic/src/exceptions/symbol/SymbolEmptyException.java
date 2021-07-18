package exceptions.symbol;

public class SymbolEmptyException extends SymbolException{
    public SymbolEmptyException() {
        super("");
    }

    @Override
    public String getMessage() {
        return "Symbol must contain letters.";
    }
}
