package com.mycompany.asms.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST, reason = "Json object sent is either empty or has mismatching keys.", value = HttpStatus.BAD_REQUEST)
public class BadJsonFormatException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -2568378770180262127L;


}
