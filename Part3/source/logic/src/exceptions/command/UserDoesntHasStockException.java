package exceptions.command;

public class UserDoesntHasStockException extends CommandException{
    private final String userName;

    public UserDoesntHasStockException(String userName) {
        this.userName = userName;
    }

    @Override
    public String getMessage() {
        return "Cannot excute command, " + userName + " doesn't have this stock.";
    }
}
