package exceptions.name;

public class NameException extends Exception {
    protected final String name;

    public NameException(String name) {
        this.name = name;
    }

    @Override
    public String getMessage() {
        return "Something went wrong with " + name;
    }
}
