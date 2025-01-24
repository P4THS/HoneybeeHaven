package com.example.honeybeehaven.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class pid {
    @Id
    private Integer productid;

    public Integer getProductid() {
        return productid;
    }

    public void setProductid(Integer productid) {
        this.productid = productid;
    }
}