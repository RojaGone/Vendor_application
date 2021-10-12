package com.example.vendoreventapplication.Models;

public class RatingModel {
    private String userName,mobile,email,rating;

    public RatingModel(String userName, String mobile, String email, String rating) {
        this.userName = userName;
        this.mobile = mobile;
        this.email = email;
        this.rating = rating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
