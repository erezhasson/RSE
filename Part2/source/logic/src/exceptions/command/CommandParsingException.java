package exceptions.command;

public class CommandParsingException extends CommandException{
    String argument, expectedType;

    public CommandParsingException(String argument, String type) {
        this.argument = argument;
        this.expectedType = type;
    }

    @Override
    public String getMessage() {
        return "[" + argument + "]"  + " is not a valid " + expectedType + ".";
    }
}
