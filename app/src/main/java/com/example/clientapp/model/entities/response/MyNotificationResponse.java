package com.example.clientapp.model.entities.response;

import com.example.clientapp.model.dto.NotificationList;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class MyNotificationResponse extends BaseServerResponse {

    private List<NotificationList> notificationList;

    public List<NotificationList> getNotificationList() {
        return notificationList;
    }

    public void setNotificationList(List<NotificationList> notificationList) {
        this.notificationList = notificationList;
    }
}
