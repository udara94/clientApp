package com.example.clientapp.model.dto;

import org.parceler.Parcel;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class NotificationBody implements Serializable {
    private String _id;
    private String creatorId;
    private String tourId;
    private String date;
    private String action;
    private String content;
    private String andoridContent;
    private String image;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAndoridContent() {
        return andoridContent;
    }

    public void setAndoridContent(String andoridContent) {
        this.andoridContent = andoridContent;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
