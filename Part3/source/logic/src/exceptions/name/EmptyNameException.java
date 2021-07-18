package exceptions.name;

public class EmptyNameException extends NameException {
    public EmptyNameException(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "Empty name entered.";
    }
}
