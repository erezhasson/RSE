package exceptions.user;

public class UserException extends Exception {
    @Override
    public String getMessage() {
        return "Something went wrong with user";
    }
}
