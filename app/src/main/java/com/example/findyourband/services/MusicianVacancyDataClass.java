package com.example.findyourband.services;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class MusicianVacancyDataClass {
    String city;
    String experience;

    List<String> genres;
    List<String> instruments;
    String description;



    List<String> tracks;
    List<String> contacts;

    public MusicianVacancyDataClass() {
    }





    public MusicianVacancyDataClass(String city, String experience, List<String> genres, List<String> instruments, String description, List<String> tracks, List<String> contacts) {
        this.city = city;
        this.experience = experience;
        this.genres = genres;
        this.instruments = instruments;
        this.description = description;
        this.tracks = tracks;
        this.contacts = contacts;
    }


    @PropertyName("city")
    public String getCity() {
        return city;
    }
    @PropertyName("city")
    public void setCity(String city) {
        this.city = city;
    }
    @PropertyName("experience")
    public String getExperience() {
        return experience;
    }
    @PropertyName("experience")
    public void setExperience(String experience) {
        this.experience = experience;
    }

    @PropertyName("genres")
    public List<String> getGenres() {
        return genres;
    }

    @PropertyName("genres")
    public void setGenres(List<String> genres) {
        this.genres = genres;
    }
    @PropertyName("instruments")
    public List<String> getInstruments() {
        return instruments;
    }
    @PropertyName("instruments")
    public void setInstruments(List<String> instruments) {
        this.instruments = instruments;
    }
    @PropertyName("description")
    public String getDescription() {
        return description;
    }
    @PropertyName("description")
    public void setDescription(String description) {
        this.description = description;
    }
    @PropertyName("tracks")
    public List<String> getTracks() {
        return tracks;
    }
    @PropertyName("tracks")
    public void setTracks(List<String> tracks) {
        this.tracks = tracks;
    }
    @PropertyName("contacts")
    public List<String> getContacts() {
        return contacts;
    }
    @PropertyName("contacts")
    public void setContacts(List<String> contacts) {
        this.contacts = contacts;
    }

}
