package grp.javatemplate.exception;

public class UserException extends BusinessException {
    public static final String USER_DOES_NOT_EXIST = "Given user does not exist";
    public static final String USER_DUPLICATE_NAME = "User with this name already exists";
    public static final String INVALID_USER_EMAIL = "Invalid user email";

    public UserException( String message ) {
        super(message);
    }
}
