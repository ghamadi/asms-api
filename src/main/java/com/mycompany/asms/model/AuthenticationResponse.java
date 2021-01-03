package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class AuthenticationResponse {

    private final String jwt;

    @JsonProperty("expires_in")
    private final long expiresIn;

    public AuthenticationResponse(String jwt, long expiresIn) {
        this.jwt = jwt;
        this.expiresIn = expiresIn;
    }

    public String getJwt() {
        return jwt;
    }

    public long getexpiresIn() {
        return expiresIn;
    }
}
