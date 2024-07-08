package com.example.eventfinder.fragments.Objects;

import java.io.Serializable;
import java.util.ArrayList;

public class ArtistDetails implements Serializable {

    private String name;
    private String image;
    private int popularity;
    private int followers;
    private String spotify;
    private ArrayList<String> albumCovers;

    public ArtistDetails(String name, String image, int popularity, int followers, String spotify, ArrayList<String> albumCovers) {
        this.name = name;
        this.image = image;
        this.popularity = popularity;
        this.followers = followers;
        this.spotify = spotify;
        this.albumCovers = albumCovers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getFollowers() {
        return followers;
    }

    public void setFollowers(int followers) {
        this.followers = followers;
    }

    public String getSpotify() {
        return spotify;
    }

    public void setSpotify(String spotify) {
        this.spotify = spotify;
    }

    public ArrayList<String> getAlbumCovers() {
        return albumCovers;
    }

    public void setAlbumCovers(ArrayList<String>  albumCovers) {
        this.albumCovers = albumCovers;
    }



    public ArtistDetails(){

    }


}
