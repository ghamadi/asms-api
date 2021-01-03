package com.mycompany.asms.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RefreshTokenRequest {
    private final String jwt;

    @JsonProperty("refresh_token")
    private final String refreshToken;


    public RefreshTokenRequest(String jwt, String refreshToken) {
        this.jwt = jwt;
        this.refreshToken = refreshToken;
    }

    public String getJwt() {
        return jwt;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
