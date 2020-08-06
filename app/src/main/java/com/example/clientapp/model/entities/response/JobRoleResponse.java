package com.example.clientapp.model.entities.response;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class JobRoleResponse extends BaseServerResponse {
    List<String> jobRoleList;

    public List<String> getJobRoleList() {
        return jobRoleList;
    }

    public void setJobRoleList(List<String> jobRoleList) {
        this.jobRoleList = jobRoleList;
    }
}
