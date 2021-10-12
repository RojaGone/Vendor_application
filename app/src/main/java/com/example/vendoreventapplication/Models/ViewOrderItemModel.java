package com.example.vendoreventapplication.Models;

public class ViewOrderItemModel {
    private String addDecorationID,currentDate,customerID,eventDetailsID,isOrderConfirm,meetingDate,meetingDescription,meetingTime,providePrice,vendorEmail;

    public ViewOrderItemModel() {
    }

    public ViewOrderItemModel(String addDecorationID, String currentDate, String customerID, String eventDetailsID, String isOrderConfirm, String meetingDate, String meetingDescription, String meetingTime, String providePrice, String vendorEmail) {
        this.addDecorationID = addDecorationID;
        this.currentDate = currentDate;
        this.customerID = customerID;
        this.eventDetailsID = eventDetailsID;
        this.isOrderConfirm = isOrderConfirm;
        this.meetingDate = meetingDate;
        this.meetingDescription = meetingDescription;
        this.meetingTime = meetingTime;
        this.providePrice = providePrice;
        this.vendorEmail = vendorEmail;
    }

    public String getAddDecorationID() {
        return addDecorationID;
    }

    public void setAddDecorationID(String addDecorationID) {
        this.addDecorationID = addDecorationID;
    }

    public String getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(String currentDate) {
        this.currentDate = currentDate;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getEventDetailsID() {
        return eventDetailsID;
    }

    public void setEventDetailsID(String eventDetailsID) {
        this.eventDetailsID = eventDetailsID;
    }

    public String getIsOrderConfirm() {
        return isOrderConfirm;
    }

    public void setIsOrderConfirm(String isOrderConfirm) {
        this.isOrderConfirm = isOrderConfirm;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public void setMeetingDescription(String meetingDescription) {
        this.meetingDescription = meetingDescription;
    }

    public String getMeetingTime() {
        return meetingTime;
    }

    public void setMeetingTime(String meetingTime) {
        this.meetingTime = meetingTime;
    }

    public String getProvidePrice() {
        return providePrice;
    }

    public void setProvidePrice(String providePrice) {
        this.providePrice = providePrice;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }
}

