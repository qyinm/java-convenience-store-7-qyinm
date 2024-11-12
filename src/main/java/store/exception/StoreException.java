package store.exception;

public class StoreException extends IllegalArgumentException {
    private static final String ERROR_MESSAGE_PREFIX = "[ERROR] ";

    public StoreException(ErrorCode errorCode) {
        super(ERROR_MESSAGE_PREFIX + errorCode.getMessage());
    }
}
