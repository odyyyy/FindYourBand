package com.example.findyourband.services;

import com.google.firebase.database.PropertyName;

import java.util.List;

public class BandVacancyDataClass {
    private String id;
    private String instrument;
    private String description;


    private List<String> tracks;
    private List<String> contacts;

    public BandVacancyDataClass() {
    }

    public BandVacancyDataClass(String id, String instrument, String description, List<String> tracks, List<String> contacts) {
        this.id = id;
        this.instrument = instrument;
        this.description = description;
        this.tracks = tracks;
        this.contacts = contacts;
    }

    public BandVacancyDataClass(String instrument, String description, List<String> tracks, List<String> contacts) {
        this.instrument = instrument;
        this.description = description;
        this.tracks = tracks;
        this.contacts = contacts;
    }


    @PropertyName("id")
    public String getId() {
        return id;
    }

    @PropertyName("id")
    public void setId(String id) {
        this.id = id;
    }

    @PropertyName("instrument")
    public String getInstrument() {
        return instrument;
    }

    @PropertyName("instrument")
    public void setInstrument(String instrument) {
        this.instrument = instrument;
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
