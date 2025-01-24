package com.example.honeybeehaven.tables;

import com.example.honeybeehaven.classes.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Chemical extends Item {
    @Id
    private Integer itemid;
    private String metricsystem;
    private Integer quantityinstock;
    private Float quantity;
    private String expirydate;
    private Float hazardlevel;
    private String chemicalType;

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getMetricsystem() {
        return metricsystem;
    }

    public void setMetricsystem(String metricsystem) {
        this.metricsystem = metricsystem;
    }

    public Integer getQuantityinstock() {
        return quantityinstock;
    }

    public void setQuantityinstock(Integer quantityinstock) {
        this.quantityinstock = quantityinstock;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public String getExpirydate() {
        return expirydate;
    }

    public void setExpirydate(String expirydate) {
        this.expirydate = expirydate;
    }

    public Float getHazardlevel() {
        return hazardlevel;
    }

    public void setHazardlevel(Float hazardlevel) {
        this.hazardlevel = hazardlevel;
    }

    public String getChemicalType() {
        return chemicalType;
    }

    public void setChemicalType(String chemicalType) {
        this.chemicalType = chemicalType;
    }
}