package com.example.clientapp.model.entities.response;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Parcel
public class JobItemResponse extends BaseServerResponse {

    private String _id;
    private String jobId;
    private String jobName;
    private String jobType;
    private String jobTypeName;
    private String[] jobDescription;
    private String[] jobSkill;
    private String[] jobQualification;
    private String[] experience;
    private String jobRole;
    private String employer;
    private String employerEmail;
    private String imgUrl;
    private String postedDate;
    private String closingDate;
    private boolean isExpired;

    public String getJobTypeName() {
        return jobTypeName;
    }

    public void setJobTypeName(String jobTypeName) {
        this.jobTypeName = jobTypeName;
    }

    public String[] getJobSkill() {
        return jobSkill;
    }

    public void setJobSkill(String[] jobSkill) {
        this.jobSkill = jobSkill;
    }

    public String[] getJobQualification() {
        return jobQualification;
    }

    public void setJobQualification(String[] jobQualification) {
        this.jobQualification = jobQualification;
    }

    public String[] getExperience() {
        return experience;
    }

    public void setExperience(String[] experience) {
        this.experience = experience;
    }

    public String get_id() {
        return _id;
    }

    public String[] getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String[] jobDescription) {
        this.jobDescription = jobDescription;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }


    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }

    public void setEmployerEmail(String employerEmail) {
        this.employerEmail = employerEmail;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(String postedDate) {
        this.postedDate = postedDate;
    }

    public String getClosingDate() {
        return closingDate;
    }

    public void setClosingDate(String closingDate) {
        this.closingDate = closingDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }
}
