package com.example.eventfinder.fragments.Objects;

import java.io.Serializable;

public class EventDetails implements Serializable {

    private String name;
    private String artists;
    private String venue;
    private String date;
    private String time;
    private String genres;
    private String priceRange;
    private String status;
    private String buyLink;
    private String seatMap;

    public EventDetails(){
        //empty constructor
    }

    public EventDetails(String name, String artists, String venue, String date, String time, String genres, String priceRange, String status, String buyLink, String seatMap) {
        this.name = name;
        this.artists = artists;
        this.venue = venue;
        this.date = date;
        this.time = time;
        this.genres = genres;
        this.priceRange = priceRange;
        this.status = status;
        this.buyLink = buyLink;
        this.seatMap = seatMap;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getArtists() {
        return artists;
    }

    public void setArtists(String artists) {
        this.artists = artists;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBuyLink() {
        return buyLink;
    }

    public void setBuyLink(String buyLink) {
        this.buyLink = buyLink;
    }

    public String getSeatMap() {
        return seatMap;
    }

    public void setSeatMap(String seatMap) {
        this.seatMap = seatMap;
    }

}
