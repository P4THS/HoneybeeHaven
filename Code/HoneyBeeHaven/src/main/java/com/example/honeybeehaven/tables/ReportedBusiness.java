package com.example.honeybeehaven.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ReportedBusiness {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer report3ID;
    private Integer complainer3ID;
    private Integer complainee3ID;
    private String reportReason3;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    private String reportType;

    public Integer getReport3ID() {
        return report3ID;
    }

    public void setReport3ID(Integer reportID) {
        this.report3ID = reportID;
    }

    public Integer getComplainer3ID() {
        return complainer3ID;
    }

    public void setComplainer3ID(Integer complainerID) {
        this.complainer3ID = complainerID;
    }

    public Integer getComplainee3ID() {
        return complainee3ID;
    }

    public void setComplainee3ID(Integer complaineeID) {
        this.complainee3ID = complaineeID;
    }

    public String getReportReason3() {
        return reportReason3;
    }

    public void setReportReason3(String reportReason) {
        this.reportReason3 = reportReason;
    }
}
