package org.atinject.api.rolepermission.exception;

public class RolePermissionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RolePermissionException() {
        super();
    }

    public RolePermissionException(String message) {
        super(message);
    }

    public RolePermissionException(String message, Throwable cause) {
        super(message, cause);
    }

    public RolePermissionException(Throwable cause) {
        super(cause);
    }
}
