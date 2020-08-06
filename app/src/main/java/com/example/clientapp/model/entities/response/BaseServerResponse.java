package com.example.clientapp.model.entities.response;


import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class BaseServerResponse {
    private String status_code;
    private boolean success;
    private boolean isAPIError;
    private String Message;
    private boolean isTokenExpired;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean isAPIError() {
        return isAPIError;
    }

    public void setAPIError(boolean APIError) {
        isAPIError = APIError;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public boolean isTokenExpired() {
        return isTokenExpired;
    }

    public void setTokenExpired(boolean tokenExpired) {
        isTokenExpired = tokenExpired;
    }
}
