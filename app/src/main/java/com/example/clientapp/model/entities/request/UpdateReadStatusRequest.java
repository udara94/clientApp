package com.example.clientapp.model.entities.request;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class UpdateReadStatusRequest {
    private List<String> mynotificationId;
    private boolean isread;

    public List<String> getMynotificationId() {
        return mynotificationId;
    }

    public void setMynotificationId(List<String> mynotificationId) {
        this.mynotificationId = mynotificationId;
    }

    public boolean isIsread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }
}
