package com.mycompany.asms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "No records with matching criteria found")
public class RecordNotFoundException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 4746862507231153604L;

}
