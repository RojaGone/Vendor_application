package com.example.vendoreventapplication.Models;

public class GoogleUserModel {
    String id,fname,lname,email,picture,isConfirm,currentDate;

    public GoogleUserModel(String id, String fname, String lname, String email, String picture, String isConfirm, String currentDate) {
        this.id = id;
        this.fname = fname;
        this.lname = lname;
        this.email = email;
        this.picture = picture;
        this.isConfirm = isConfirm;
        this.currentDate = currentDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getIsConfirm() {
        return isConfirm;
    }

    public void setIsConfirm(String isConfirm) {
        this.isConfirm = isConfirm;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }
}
