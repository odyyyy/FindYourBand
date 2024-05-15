package com.example.findyourband.services;

import java.util.List;

public class BandDataClass {
    private String name;

    private String city;
    private String bandImage;
    private List<String> genres;
    private List<String> memberUserLogins;

    public BandDataClass() {
    }



    public BandDataClass(String name, String city, List<String> genres, String bandImage, List<String> memberUserIds) {
        this.name = name;
        this.city = city;
        this.genres = genres;
        this.bandImage = bandImage;
        this.memberUserLogins = memberUserIds;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getName() {
        return name;
    }

    public List<String> getGenres() {
        return genres;
    }

    public void setGenres(List<String> genres) {
        this.genres = genres;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List<String> getMemberUserLogins() {
        return memberUserLogins;
    }

    public void setMemberUserLogins(List<String> memberUserLogins) {
        this.memberUserLogins = memberUserLogins;
    }

    public String getBandImage() {
        return bandImage;
    }

    public void setBandImage(String bandImage) {
        this.bandImage = bandImage;
    }


}