package com.example.clientapp.model.entities.response;

import com.example.clientapp.model.dto.NotificationData;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class NotificationResponse extends BaseServerResponse {
    private NotificationData data;

    public NotificationData getData() {
        return data;
    }

    public void setData(NotificationData data) {
        this.data = data;
    }
}
