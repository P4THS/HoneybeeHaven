package com.example.honeybeehaven.tables;

import com.example.honeybeehaven.classes.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Business extends User {

    @Id
    private Integer userid;
    private String businessname;
    private String businessdescription;
    private String contactinfo;
    private String targetkeywords;
    private float businessrating;
    private String bankaccountnumber;

    public String getBusinessname() {
        return businessname;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getBusinessdescription() {
        return businessdescription;
    }

    public void setBusinessdescription(String businessdescription) {
        this.businessdescription = businessdescription;
    }

    public String getContactinfo() {
        return contactinfo;
    }

    public void setContactinfo(String contactinfo) {
        this.contactinfo = contactinfo;
    }

    public String getTargetkeywords() {
        return targetkeywords;
    }

    public void setTargetkeywords(String targetkeywords) {
        this.targetkeywords = targetkeywords;
    }

    public float getBusinessrating() {
        return businessrating;
    }

    public void setBusinessrating(float businessrating) {
        this.businessrating = businessrating;
    }

    public String getBankaccountnumber() {
        return bankaccountnumber;
    }

    public void setBankaccountnumber(String bankaccountnumber) {
        this.bankaccountnumber = bankaccountnumber;
    }
}
