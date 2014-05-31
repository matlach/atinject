package org.atinject.core.exception;

// equivalent of the java EE @ApplicationException(inherited=true, rollback=true)
public abstract class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

    public ApplicationException() {
        super();
    }

    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }
    
    public abstract String getExceptionCode();
}
