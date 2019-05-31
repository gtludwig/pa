package ie.gtludwig.pa.error;

public final class UserAlreadyExistsException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UserAlreadyExistsException() {
        super();
    }

    public UserAlreadyExistsException(final String message, final Throwable throwable) {
        super(message, throwable);
    }

    public UserAlreadyExistsException(final Throwable throwable) {
        super(throwable);
    }
}
