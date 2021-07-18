package exceptions.user;

public class UserNotFoundException extends UserException {
    private final String userName;

    public UserNotFoundException(String symbol) {
        this.userName = symbol;
    }

    @Override
    public String getMessage() {
        return "[" + userName + "]" + " does not exists in system.";
    }
}
