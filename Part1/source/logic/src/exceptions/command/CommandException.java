package exceptions.command;

public class CommandException extends Exception{
    @Override
    public String getMessage() {
        return "Something went wrong with command";
    }
}
