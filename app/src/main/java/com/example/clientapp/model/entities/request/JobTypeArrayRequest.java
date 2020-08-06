package com.example.clientapp.model.entities.request;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class JobTypeArrayRequest {

    private String[] jobTypeArry;

    public String[] getJobTypeArry() {
        return jobTypeArry;
    }

    public void setJobTypeArry(String[] jobTypeArry) {
        this.jobTypeArry = jobTypeArry;
    }
}
