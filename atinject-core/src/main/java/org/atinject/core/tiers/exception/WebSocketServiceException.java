package org.atinject.core.tiers.exception;

public class WebSocketServiceException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public WebSocketServiceException() {
        super();
    }

    public WebSocketServiceException(String message) {
        super(message);
    }

    public WebSocketServiceException(String message, Throwable cause) {
        super(message, cause);
    }

    public WebSocketServiceException(Throwable cause) {
        super(cause);
    }
    
}
