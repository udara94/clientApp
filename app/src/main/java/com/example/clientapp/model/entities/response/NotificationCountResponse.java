package com.example.clientapp.model.entities.response;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class NotificationCountResponse extends BaseServerResponse {
    private String unRead_notification_count;

    public String getUnRead_notification_count() {
        return unRead_notification_count;
    }

    public void setUnRead_notification_count(String unRead_notification_count) {
        this.unRead_notification_count = unRead_notification_count;
    }
}
