package com.example.honeybeehaven.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class ReportedClients {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer report2ID;
    private Integer complainer2ID;
    private Integer complainee2ID;
    private String reportReason2;

    public String getReportType() {
        return reportType;
    }

    public void setReportType(String reportType) {
        this.reportType = reportType;
    }

    private String reportType;

    public Integer getReport2ID() {
        return report2ID;
    }

    public void setReport2ID(Integer reportID) {
        this.report2ID = reportID;
    }

    public Integer getComplainer2ID() {
        return complainer2ID;
    }

    public void setComplainer2ID(Integer complainerID) {
        this.complainer2ID = complainerID;
    }

    public Integer getComplainee2ID() {
        return complainee2ID;
    }

    public void setComplainee2ID(Integer complaineeID) {
        this.complainee2ID = complaineeID;
    }

    public String getReportReason2() {
        return reportReason2;
    }

    public void setReportReason2(String reportReason) {
        this.reportReason2 = reportReason;
    }

}
