package com.example.vendoreventapplication.Models;

public class ViewOrder_view_model {
    private String fname, lname, event_name, decoration_name, decoration_pic_image, mobile, email, address, providePrice, meetingDate, meetingTime, meetingDescription;

    public ViewOrder_view_model(String fname, String lname, String event_name, String decoration_name, String decoration_pic_image, String mobile, String email, String address, String providePrice, String meetingDate, String meetingTime, String meetingDescription) {
        this.fname = fname;
        this.lname = lname;
        this.event_name = event_name;
        this.decoration_name = decoration_name;
        this.decoration_pic_image = decoration_pic_image;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.providePrice = providePrice;
        this.meetingDate = meetingDate;
        this.meetingTime = meetingTime;
        this.meetingDescription = meetingDescription;
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

    public String getEvent_name() {
        return event_name;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public String getDecoration_name() {
        return decoration_name;
    }

    public void setDecoration_name(String decoration_name) {
        this.decoration_name = decoration_name;
    }

    public String getDecoration_pic_image() {
        return decoration_pic_image;
    }

    public void setDecoration_pic_image(String decoration_pic_image) {
        this.decoration_pic_image = decoration_pic_image;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getProvidePrice() {
        return providePrice;
    }

    public void setProvidePrice(String providePrice) {
        this.providePrice = providePrice;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public void setMeetingDescription(String meetingDescription) {
        this.meetingDescription = meetingDescription;
    }
}
