package com.example.vendoreventapplication.Models;

public class EventModel {
    private String  rv_eventName,rv_decorationeName;
    private float money_start;
    private float money_end;
    private int decora_image;

    public EventModel() {
    }

    public EventModel(String rv_eventName, String rv_decorationeName, float money_start, float money_end, int decora_image) {
        this.rv_eventName = rv_eventName;
        this.rv_decorationeName = rv_decorationeName;
        this.money_start = money_start;
        this.money_end = money_end;
        this.decora_image = decora_image;
    }

    public String getRv_eventName() {
        return rv_eventName;
    }

    public void setRv_eventName(String rv_eventName) {
        this.rv_eventName = rv_eventName;
    }

    public String getRv_decorationeName() {
        return rv_decorationeName;
    }

    public void setRv_decorationeName(String rv_decorationeName) {
        this.rv_decorationeName = rv_decorationeName;
    }

    public float getMoney_start() {
        return money_start;
    }

    public void setMoney_start(float money_start) {
        this.money_start = money_start;
    }

    public float getMoney_end() {
        return money_end;
    }

    public void setMoney_end(float money_end) {
        this.money_end = money_end;
    }

    public int getDecora_image() {
        return decora_image;
    }

    public void setDecora_image(int decora_image) {
        this.decora_image = decora_image;
    }
}
