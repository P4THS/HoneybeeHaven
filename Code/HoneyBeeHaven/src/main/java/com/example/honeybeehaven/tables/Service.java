package com.example.honeybeehaven.tables;


import com.example.honeybeehaven.classes.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Service extends Item {
    @Id
    private Integer itemid;
    private String servicetype;
    private String basecharges;
    private Boolean isavailable;

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getServicetype() {
        return servicetype;
    }

    public void setServicetype(String servicetype) {
        this.servicetype = servicetype;
    }

    public String getBasecharges() {
        return basecharges;
    }

    public void setBasecharges(String basecharges) {
        this.basecharges = basecharges;
    }

    public Boolean getIsavailable() {
        return isavailable;
    }

    public void setIsavailable(Boolean isavailable) {
        this.isavailable = isavailable;
    }
}