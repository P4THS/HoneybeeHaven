package com.example.honeybeehaven.tables;

import com.example.honeybeehaven.classes.Item;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Machinery extends Item {
    @Id
    private Integer itemid;
    private String machinedimension;
    private Float machineweight;
    private String powersource;
    private Integer warranty;
    private Integer quantityinstock;
    private String machinetype;

    public String getMachinetype() {
        return machinetype;
    }

    public void setMachinetype(String machinetype) {
        this.machinetype = machinetype;
    }

    public Integer getItemid() {
        return itemid;
    }

    public void setItemid(Integer itemid) {
        this.itemid = itemid;
    }

    public String getMachinedimension() {
        return machinedimension;
    }

    public void setMachinedimension(String machinedimension) {
        this.machinedimension = machinedimension;
    }

    public Float getMachineweight() {
        return machineweight;
    }

    public void setMachineweight(Float machineweight) {
        this.machineweight = machineweight;
    }

    public String getPowersource() {
        return powersource;
    }

    public void setPowersource(String powersource) {
        this.powersource = powersource;
    }

    public Integer getWarranty() {
        return warranty;
    }

    public void setWarranty(Integer warranty) {
        this.warranty = warranty;
    }

    public Integer getQuantityinstock() {
        return quantityinstock;
    }

    public void setQuantityinstock(Integer quantityinstock) {
        this.quantityinstock = quantityinstock;
    }
}
