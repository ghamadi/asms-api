package com.mycompany.asms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadParametersFormatException extends RuntimeException {


    /**
     *
     */
    private static final long serialVersionUID = 6565316831101307498L;

    public BadParametersFormatException(Throwable cause) {
        super(cause);
    }

    public BadParametersFormatException(String message) {
        super(message);
    }
}
