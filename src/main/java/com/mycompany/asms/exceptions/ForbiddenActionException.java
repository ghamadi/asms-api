package com.mycompany.asms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class ForbiddenActionException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -8554046358047083469L;

    public ForbiddenActionException(Throwable cause) {
        super(cause);
    }

    public ForbiddenActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
