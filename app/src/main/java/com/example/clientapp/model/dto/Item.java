package com.example.clientapp.model.dto;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class Item {
    private String paid;
    private String tableNo;
    private String id;
    private String itemName;
    private String itemCode;
    private String itemDescription;
    private String itemPrice;
    private String itemImg;
    private String itemDiscount;
    private String[] itemTypes;
    private int itemQty;
    private String itemStatus;
}
