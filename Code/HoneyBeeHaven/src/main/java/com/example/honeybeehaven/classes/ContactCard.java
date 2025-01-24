package com.example.honeybeehaven.classes;

public class ContactCard {
    public  String contactName;
    public  String contactEmail;
    public  String contactSubject;
    public  String contactMessage;
    public ContactCard(){
        this.contactName = "";
        this.contactEmail =  "";
        this.contactSubject =  "";
        this.contactMessage =  "";
    }
    public ContactCard(String contactName, String contactEmail, String contactSubject, String contactMessage) {
        this.contactName = contactName;
        this.contactEmail = contactEmail;
        this.contactSubject = contactSubject;
        this.contactMessage = contactMessage;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactSubject() {
        return contactSubject;
    }

    public void setContactSubject(String contactSubject) {
        this.contactSubject = contactSubject;
    }

    public String getContactMessage() {
        return contactMessage;
    }

    public void setContactMessage(String contactMessage) {
        this.contactMessage = contactMessage;
    }
}
