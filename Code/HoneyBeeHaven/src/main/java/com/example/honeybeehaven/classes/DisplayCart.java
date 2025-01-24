package com.example.honeybeehaven.classes;

public class DisplayCart {
    public Integer userid;
    public Integer itemid;
    public String type;
    public Integer itemqty;
    public Integer qty;
    public String itemName;
    public Float itemPrice;
    public Float subTotal;

    public String deleteURL;
    public String viewHref;

    public String getDeleteURL() {
        return deleteURL;
    }

    public void setDeleteURL(String deleteURL) {
        this.deleteURL = deleteURL;
    }

    public String getViewHref() {
        return viewHref;
    }

    public void setViewHref(String viewHref) {
        this.viewHref = viewHref;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getItemqty() {
        return itemqty;
    }

    public void setItemqty(Integer itemqty) {
        this.itemqty = itemqty;
    }

    public Integer getQty() {
        return qty;
    }

    public void setQty(Integer qty) {
        this.qty = qty;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Float getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(Float itemPrice) {
        this.itemPrice = itemPrice;
    }

    public Float getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(Float subTotal) {
        this.subTotal = subTotal;
    }
}
