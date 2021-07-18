package exceptions.stock;

public class CompanyNameException extends StockException {
    private final String name;

    public CompanyNameException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "[" + name.toUpperCase() + "]" + " already exists in system (Company must possess at the most one stock).";
    }
}
