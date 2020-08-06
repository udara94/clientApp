package com.example.clientapp.model.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class NotificationListItem {
    private String _id;
    private String iconImage;
    private String image;
    private String date;
    private String creatorId;
    private String tourId;
    private String action;
    private String androidContent;
    private String mynotificationId;
    private boolean isread;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }

    public String getTourId() {
        return tourId;
    }

    public void setTourId(String tourId) {
        this.tourId = tourId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAndroidContent() {
        return androidContent;
    }

    public void setAndroidContent(String androidContent) {
        this.androidContent = androidContent;
    }

    public String getMynotificationId() {
        return mynotificationId;
    }

    public void setMynotificationId(String mynotificationId) {
        this.mynotificationId = mynotificationId;
    }

    public boolean isIsread() {
        return isread;
    }

    public void setIsread(boolean isread) {
        this.isread = isread;
    }
}
