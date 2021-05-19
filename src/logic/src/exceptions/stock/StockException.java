package exceptions.stock;

public class StockException extends Exception{
    @Override
    public String getMessage() {
        return "Something went wrong with stock";
    }
}
