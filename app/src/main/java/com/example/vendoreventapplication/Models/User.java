package com.example.vendoreventapplication.Models;

public class User {
    private String id,Fname,Lname,Email,Address,Mobile,Password,gender,picture,isConfirm,currentDate;

    public User(String password) {
        Password = password;
    }

    public User() {
    }

    public User(String id, String fname, String lname, String email, String address, String mobile, String password, String gender, String picture, String isConfirm, String currentDate) {
        this.id = id;
        Fname = fname;
        Lname = lname;
        Email = email;
        Address = address;
        Mobile = mobile;
        Password = password;
        this.gender = gender;
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
        return Fname;
    }

    public void setFname(String fname) {
        Fname = fname;
    }

    public String getLname() {
        return Lname;
    }

    public void setLname(String lname) {
        Lname = lname;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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
