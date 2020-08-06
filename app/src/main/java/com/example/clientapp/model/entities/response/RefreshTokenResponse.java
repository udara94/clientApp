package com.example.clientapp.model.entities.response;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class RefreshTokenResponse extends BaseServerResponse {
    private String token;
    private String expire;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
