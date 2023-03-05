package grp.javatemplate.exception;

public class UserException extends BusinessException {
    public static final String USER_EXISTS = "Given user does not exist";

    public UserException( String message ) {
        super(message);
    }
}
