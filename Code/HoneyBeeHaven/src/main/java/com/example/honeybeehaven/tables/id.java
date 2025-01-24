package com.example.honeybeehaven.tables;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class id {
    @Id
    private Integer userid;

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }
}
