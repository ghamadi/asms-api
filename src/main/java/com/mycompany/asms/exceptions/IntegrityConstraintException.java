package com.mycompany.asms.exceptions;


public class IntegrityConstraintException extends ForbiddenActionException {

    /**
     *
     */
    private static final long serialVersionUID = 6676432575775229708L;

    public IntegrityConstraintException(Throwable cause) {
        super(cause);
    }

    public IntegrityConstraintException(String message, Throwable cause) {
        super(message, cause);
    }
}
