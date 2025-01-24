package com.example.honeybeehaven.classes;

public class EditService {
    public String productHeading ;
    public Float productPrice;
    public String productDescription;
    public String  baseCharges;
    public Boolean isavailable;

    public Boolean getIsavailable() {
        return isavailable;
    }

    public void setIsavailable(Boolean isavailable) {
        this.isavailable = isavailable;
    }

    public EditService(String productHeading, Float productPrice, String productDescription, String  baseCharges, Boolean isavailable) {
        this.productHeading = productHeading;
        this.productPrice = productPrice;
        this.productDescription = productDescription;
        this.baseCharges = baseCharges;
        this.isavailable = isavailable;
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

    public String getBaseCharges() {
        return baseCharges;
    }

    public void setBaseCharges(String baseCharges) {
        this.baseCharges = baseCharges;
    }
}
