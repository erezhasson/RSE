package exceptions.name;

public class NameExistsException extends NameException {
    public NameExistsException(String name) {
        super(name);
    }

    @Override
    public String getMessage() {
        return "[" + name + "]" + " already exists in system.";
    }
}
