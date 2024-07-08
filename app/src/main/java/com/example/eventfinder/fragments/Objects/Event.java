package com.example.eventfinder.fragments.Objects;

import java.io.Serializable;

public class Event implements Serializable {
    private String id;
    private String date;
    private String time;
    private String icon;
    private String name;
    private String genre;
    private String venue;
    private String url;


    public void setId(String id){
        this.id = id;
    }

    public String getId(){
        return id;
    }

    public void setDate(String date){
        this.date = date;
    }

    public String getDate(){
        return date;
    }

    public void setTime(String time){
        this.time = time;
    }

    public String getTime(){
        return time;
    }

    public void setIcon(String icon){
        this.icon = icon;
    }

    public String getIcon(){
        return icon;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }

    public void setGenre(String genre){
        this.genre = genre;
    }

    public String getGenre(){
        return genre;
    }

    public void setVenue(String venue){
        this.venue = venue;
    }

    public String getVenue(){
        return venue;
    }

    public void setUrl(String url){
        this.url = url;
    }

    public String getUrl(){
        return url;
    }


    public Event(){
    }



    public Event(String id, String date, String time, String icon, String name, String genre, String venue, String url){
        this.id = id;
        this.date = date;
        this.time = time;
        this.icon = icon;
        this.name = name;
        this.genre = genre;
        this.venue = venue;
        this.url = url;
    }
}
