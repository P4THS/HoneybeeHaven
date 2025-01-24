package com.example.honeybeehaven.classes;

public class EditChemical {
    public String productHeading ;
    public Float productPrice;
    public String productDescription;
    public String metricSystem;
    public String expiryDate;
    public Float quantity;
    public Integer quantityInStock;


    public EditChemical(String productHeading, Float productPrice, String productDescription, String metricSystem, String expiryDate, Float quantity, Integer quantityInStock) {
        this.productHeading = productHeading;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.metricSystem = metricSystem;
        this.expiryDate = expiryDate;
        this.quantity = quantity;
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

    public String getMetricSystem() {
        return metricSystem;
    }

    public void setMetricSystem(String metricSystem) {
        this.metricSystem = metricSystem;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public Float getQuantity() {
        return quantity;
    }

    public void setQuantity(Float quantity) {
        this.quantity = quantity;
    }

    public Integer getQuantityInStock() {
        return quantityInStock;
    }

    public void setQuantityInStock(Integer quantityInStock) {
        this.quantityInStock = quantityInStock;
    }
}
