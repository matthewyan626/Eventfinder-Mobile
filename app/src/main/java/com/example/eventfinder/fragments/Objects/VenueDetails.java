package com.example.eventfinder.fragments.Objects;

import java.io.Serializable;

public class VenueDetails implements Serializable {


    private String name;
    private String address;
    private String lineTwo;
    private String contact;
    private String openHours;
    private String generalRule;
    private String childRule;

    private double[] location;



    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLineTwo() {
        return lineTwo;
    }

    public void setLineTwo(String lineTwo) {
        this.lineTwo = lineTwo;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getGeneralRule() {
        return generalRule;
    }

    public void setGeneralRule(String generalRule) {
        this.generalRule = generalRule;
    }

    public String getChildRule() {
        return childRule;
    }

    public void setChildRule(String childRule) {
        this.childRule = childRule;
    }

    public VenueDetails() {
    }

    public VenueDetails(String name, String address, String lineTwo, String contact, String openHours, String generalRule, String childRule, double[] location) {
        this.name = name;
        this.address = address;
        this.lineTwo = lineTwo;
        this.contact = contact;
        this.openHours = openHours;
        this.generalRule = generalRule;
        this.childRule = childRule;
        this.location = location;
    }
}
