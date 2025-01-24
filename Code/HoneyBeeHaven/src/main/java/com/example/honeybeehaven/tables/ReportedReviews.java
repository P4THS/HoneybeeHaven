package com.example.honeybeehaven.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ReportedReviews {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer reportID;
    private Integer reviewID;
    private Integer complainerID;
    private String reviewContent;
    private Integer complaineeID;
    private String reportReason;
    private String reportType;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    public Integer getReportID() {
        return reportID;
    }

    public void setReportID(Integer reportID) {
        this.reportID = reportID;
    }

    public Integer getReviewID() {
        return reviewID;
    }

    public void setReviewID(Integer reviewID) {
        this.reviewID = reviewID;
    }

    public Integer getComplainerID() {
        return complainerID;
    }

    public void setComplainerID(Integer complainerID) {
        this.complainerID = complainerID;
    }

    public String getReviewContent() {
        return reviewContent;
    }

    public void setReviewContent(String reviewContent) {
        this.reviewContent = reviewContent;
    }

    public Integer getComplaineeID() {
        return complaineeID;
    }

    public void setComplaineeID(Integer complaineeID) {
        this.complaineeID = complaineeID;
    }

    public String getReportReason() {
        return reportReason;
    }

    public void setReportReason(String reportReason) {
        this.reportReason = reportReason;
    }

}
