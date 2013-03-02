package org.atinject.core.exception;

public class CacheStoreException extends RuntimeException
{
    private static final long serialVersionUID = 1L;

    public CacheStoreException() {
        super();
    }

    public CacheStoreException(String message) {
        super(message);
    }

    public CacheStoreException(String message, Throwable cause) {
        super(message, cause);
    }

    public CacheStoreException(Throwable cause) {
        super(cause);
    }

}
