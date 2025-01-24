package com.example.honeybeehaven.classes;

public class Admin {
    private static Admin instance = null;
    private String name;
    private String email;
    private String password;
    private Integer adminID;

    public Integer getAdminID() {
        return adminID;
    }

    public void setAdminID(Integer adminID) {
        this.adminID = adminID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static synchronized Admin getInstance(){

        if(instance == null)
            instance = new Admin();
        return instance;
    }

    protected Admin(){
        email = "admin@honeybeehaven.com";
        password = "Fastnuces123";
        name = "Team 1";
        adminID = 1;
    }
}
