package com.example.honeybeehaven.classes;

import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public class Item {
    private String itemname;
    private Float itemPrice;
    private Float Itemrating;
    private String itemdescription;
    private Boolean issponsored;
    private Integer businessid;
    private String image;
    private Integer isdeleted = 0;
    private String date;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
    public Integer getIsdeleted() {
        return isdeleted;
    }

    public void setIsdeleted(Integer isdeleted) {
        this.isdeleted = isdeleted;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Float getItemrating() {
        return Itemrating;
    }

    public void setItemrating(Float itemrating) {
        Itemrating = itemrating;
    }

    public String getItemdescription() {
        return itemdescription;
    }

    public void setItemdescription(String itemdescription) {
        this.itemdescription = itemdescription;
    }

    public Boolean getIssponsored() {
        return issponsored;
    }

    public void setIssponsored(Boolean issponsored) {
        this.issponsored = issponsored;
    }

    public Integer getBusinessid() {
        return businessid;
    }

    public void setBusinessid(Integer businessid) {
        this.businessid = businessid;
    }
}
