package exceptions.command;

public class CommandArgEmptyException extends CommandException{
    String argument;

    public CommandArgEmptyException(String argument) {
        this.argument = argument;
    }

    @Override
    public String getMessage() {
        return argument + " is empty, please enter value.";
    }
}
