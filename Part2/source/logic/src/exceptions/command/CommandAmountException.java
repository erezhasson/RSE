package exceptions.command;

public class CommandAmountException extends CommandException{
    private final int amount;

    public CommandAmountException(int amount) {
        this.amount = amount;
    }

    @Override
    public String getMessage() {
        return "[" + amount + "]" + " is not a valid amount.";
    }
}
