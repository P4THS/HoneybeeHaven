package com.example.honeybeehaven.tables;

import com.example.honeybeehaven.classes.User;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Client extends User {

    @Id
    private Integer userid;
    private String gender;
    private String age;
    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}