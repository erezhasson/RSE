package exceptions.command;

public class CommandTypeNotExistsException extends CommandException{
    private final String type;

    public CommandTypeNotExistsException(String type) {
        this.type = type;
    }

    @Override
    public String getMessage() {
        return "[" + type + "]" +  " is not a valid command type.";
    }

}
