package com.example.honeybeehaven.classes;

public class OrderCard {
    public Integer orderid;
    public String itemname;
    public Integer itemqty;
    public float itemprice;
    public String orderdate;
    public String dispatchdate;
    public String deliverdate;
    public String clientname;
    public String shippingaddress;
    public Integer itemid;
    public String href;
    public String clienthref;

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getShippingaddress() {
        return shippingaddress;
    }

    public void setShippingaddress(String shippingaddress) {
        this.shippingaddress = shippingaddress;
    }

    public String getClientname() {
        return clientname;
    }

    public void setClientname(String clientname) {
        this.clientname = clientname;
    }


    public Integer getOrderid() {
        return orderid;
    }

    public void setOrderid(Integer orderid) {
        this.orderid = orderid;
    }

    public String getItemname() {
        return itemname;
    }

    public void setItemname(String itemname) {
        this.itemname = itemname;
    }

    public Integer getItemqty() {
        return itemqty;
    }

    public void setItemqty(Integer itemqty) {
        this.itemqty = itemqty;
    }

    public float getItemprice() {
        return itemprice;
    }

    public void setItemprice(float itemprice) {
        this.itemprice = itemprice;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getDispatchdate() {
        return dispatchdate;
    }

    public void setDispatchdate(String dispatchdate) {
        this.dispatchdate = dispatchdate;
    }

    public String getDeliverdate() {
        return deliverdate;
    }

    public void setDeliverdate(String deliverdate) {
        this.deliverdate = deliverdate;
    }
}
