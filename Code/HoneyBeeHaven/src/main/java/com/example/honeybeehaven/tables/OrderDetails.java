package com.example.honeybeehaven.tables;

import com.example.honeybeehaven.classes.OrderDetailsKey;
import jakarta.persistence.*;


@Entity
public class OrderDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    private Integer orderdetailkey;
    private Integer orderid;
    private Integer itemid;
    private String itemtype;
    private String itemDeliveryDate;
    private String itemDispatchDate;
    private Integer itemqty;

    public Integer getItemqty() {
        return itemqty;
    }

    public void setItemqty(Integer itemqty) {
        this.itemqty = itemqty;
    }

    public Integer getOrderdetailkey() {
        return orderdetailkey;
    }

    public void setOrderdetailkey(Integer orderdetailkey) {
        this.orderdetailkey = orderdetailkey;
    }

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

    public String getItemtype() {
        return itemtype;
    }

    public void setItemtype(String itemtype) {
        this.itemtype = itemtype;
    }

    public String getItemDeliveryDate() {
        return itemDeliveryDate;
    }

    public void setItemDeliveryDate(String itemDeliveryDate) {
        this.itemDeliveryDate = itemDeliveryDate;
    }

    public String getItemDispatchDate() {
        return itemDispatchDate;
    }

    public void setItemDispatchDate(String itemDispatchDate) {
        this.itemDispatchDate = itemDispatchDate;
    }
}
