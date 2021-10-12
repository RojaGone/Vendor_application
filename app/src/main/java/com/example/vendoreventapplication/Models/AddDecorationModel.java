package com.example.vendoreventapplication.Models;

public class AddDecorationModel {
    private String addDecorationID,select_event,decoration_name,decoration_pic_image,station_service,designers,stay_travel,event_start_price,event_upto_price,vendorEmail;

    public AddDecorationModel() {
    }

    public AddDecorationModel(String addDecorationID,String vendorEmail,String select_event, String decoration_name, String decoration_pic_image, String station_service, String designers, String stay_travel, String event_start_price, String event_upto_price) {
        this.addDecorationID = addDecorationID;
        this.vendorEmail = vendorEmail;
        this.select_event = select_event;
        this.decoration_name = decoration_name;
        this.decoration_pic_image = decoration_pic_image;
        this.station_service = station_service;
        this.designers = designers;
        this.stay_travel = stay_travel;
        this.event_start_price = event_start_price;
        this.event_upto_price = event_upto_price;
    }

    public String getAddDecorationID() {
        return addDecorationID;
    }

    public void setAddDecorationID(String addDecorationID) {
        this.addDecorationID = addDecorationID;
    }

    public String getVendorEmail() {
        return vendorEmail;
    }

    public void setVendorEmail(String vendorEmail) {
        this.vendorEmail = vendorEmail;
    }

    public String getSelect_event() {
        return select_event;
    }

    public void setSelect_event(String select_event) {
        this.select_event = select_event;
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

    public String getStation_service() {
        return station_service;
    }

    public void setStation_service(String station_service) {
        this.station_service = station_service;
    }

    public String getDesigners() {
        return designers;
    }

    public void setDesigners(String designers) {
        this.designers = designers;
    }

    public String getStay_travel() {
        return stay_travel;
    }

    public void setStay_travel(String stay_travel) {
        this.stay_travel = stay_travel;
    }

    public String getEvent_start_price() {
        return event_start_price;
    }

    public void setEvent_start_price(String event_start_price) {
        this.event_start_price = event_start_price;
    }

    public String getEvent_upto_price() {
        return event_upto_price;
    }

    public void setEvent_upto_price(String event_upto_price) {
        this.event_upto_price = event_upto_price;
    }
}
