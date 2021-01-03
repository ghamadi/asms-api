package com.mycompany.asms.exceptions;

public class InvalidLoginCredentialsException extends ForbiddenActionException {

    /**
     *
     */
    private static final long serialVersionUID = -3637710426484367011L;

    public InvalidLoginCredentialsException(Throwable cause) {
        super("Invalid login credentials.", cause);
    }

    public InvalidLoginCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }
}
