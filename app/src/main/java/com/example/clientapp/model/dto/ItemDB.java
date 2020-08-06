package com.example.clientapp.model.dto;

import com.j256.ormlite.field.DatabaseField;

import lombok.Getter;
import lombok.Setter;

public class ItemDB {
    @Getter
    @Setter
    @DatabaseField(generatedId = true)
    private long id;

    @Getter
    @Setter
    @DatabaseField
    private int score;
}
