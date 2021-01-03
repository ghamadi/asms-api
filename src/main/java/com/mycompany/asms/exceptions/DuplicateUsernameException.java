package com.mycompany.asms.exceptions;


public class DuplicateUsernameException extends ForbiddenActionException {

    /**
     *
     */
    private static final long serialVersionUID = 4549065781564047914L;

    public DuplicateUsernameException(Throwable cause) {
        super("This username already exists", cause);
    }

    public DuplicateUsernameException(String message, Throwable cause) {
        super(message, cause);
    }
}
