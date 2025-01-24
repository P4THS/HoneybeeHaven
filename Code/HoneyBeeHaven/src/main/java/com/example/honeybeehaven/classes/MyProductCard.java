package com.example.honeybeehaven.classes;

public class MyProductCard {
    public String pType;
    public String pTitle;
    public String price;
    public String pID;
    public String pStock;
    public String pRating;

    public String pImagePath;

    public String editHref;
    public String deleteHref;
    public String viewHref;



    public MyProductCard(String pType, String pTitle, String price, String pID, String pStock, String pRating, String pImagePath,String editHref, String deleteHref, String viewHref) {
        this.pType = pType;
        this.pTitle = pTitle;
        this.price = price;
        this.pID = pID;
        this.pStock = pStock;
        this.pRating = pRating;
        this.pImagePath = pImagePath;
        this.editHref = editHref;
        this.deleteHref = deleteHref;
        this.viewHref = viewHref;
    }


}
