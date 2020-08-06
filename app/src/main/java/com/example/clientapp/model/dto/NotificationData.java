package com.example.clientapp.model.dto;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class NotificationData {
    private String count;
    private List<NotificationListItem> notifications;
}
