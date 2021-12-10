package ee.tlu.forum.exception;

public class NoPermissionException extends RuntimeException {

    public NoPermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoPermissionException(String message) {
        super(message);
    }
}
