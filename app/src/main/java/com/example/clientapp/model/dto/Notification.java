package com.example.clientapp.model.dto;


import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class Notification {
    private String cartId;
    private String message;
    private String tableNo;
    private String orderNo;
    private int userType;
}
