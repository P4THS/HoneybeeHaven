package com.example.honeybeehaven.classes;

import org.springframework.data.relational.core.sql.In;

public class EditMachinery {
    public String productHeading ;
    public Float productPrice;
    public String productDescription;
    public String dimension;

    public Float weight;
    public Integer warranty;
    public Integer quantityInStock;

    public EditMachinery(String productHeading, Float productPrice, String productDescription, String dimension, Float weight, Integer warranty, Integer quantityInStock) {
        this.productHeading = productHeading;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.dimension = dimension;
        this.weight = weight;
        this.warranty = warranty;
        this.quantityInStock = quantityInStock;
    }


    public String getProductHeading() {
        return productHeading;
    }

    public void setProductHeading(String productHeading) {
        this.productHeading = productHeading;
    }

    public Float getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Float productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getDimension() {
        return dimension;
    }

    public void setDimension(String dimension) {
        this.dimension = dimension;
    }

    public Float getWeight() {
        return weight;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
    }

    public Integer getWarranty() {
        return warranty;
    }

    public void setWarranty(Integer warranty) {
        this.warranty = warranty;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
