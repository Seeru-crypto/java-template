package grp.javatemplate.exception;

public abstract class BusinessException extends RuntimeException {
    public static final String ID_MUST_NOT_BE_NULL = "The given id must not be null";

    protected BusinessException(String message) {
        super(message);
    }
}
