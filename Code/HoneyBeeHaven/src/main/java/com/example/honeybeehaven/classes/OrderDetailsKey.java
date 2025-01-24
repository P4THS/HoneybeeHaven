package com.example.honeybeehaven.classes;

public class OrderDetailsKey implements java.io.Serializable{
    private Integer orderid;
    private Integer itemid;

    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }
}
