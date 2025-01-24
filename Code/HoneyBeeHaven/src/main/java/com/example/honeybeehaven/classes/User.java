package com.example.honeybeehaven.classes;

import com.example.honeybeehaven.services.IdService;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@MappedSuperclass
public class User {

    private String name;
    private String password;
    private String email;
    private String address;
    private String datejoined;
    private String primarylocation;

    public Boolean getBanned() {
        return isBanned;
    }

    public void setBanned(Boolean banned) {
        isBanned = banned;
    }

    private Boolean isBanned;

    public String getDateBanned() {
        return dateBanned;
    }

    public void setDateBanned(String dateBanned) {
        this.dateBanned = dateBanned;
    }

    private String dateBanned;

    private String image;

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPrimarylocation() {
        return primarylocation;
    }

    public void setPrimarylocation(String primarylocation) {
        this.primarylocation = primarylocation;
    }

    public String getDatejoined() {
        return datejoined;
    }

    public void setDatejoined(String datejoined) {
        this.datejoined = datejoined;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
