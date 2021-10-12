package com.example.vendoreventapplication.Models;

public class FeedbackModel {
    private String userName,mobile,userEmail,feedback;

    public FeedbackModel(String userName, String mobile, String userEmail, String feedback) {
        this.userName = userName;
        this.mobile = mobile;
        this.userEmail = userEmail;
        this.feedback = feedback;
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

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
