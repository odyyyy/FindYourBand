package com.example.findyourband.services;

import java.util.List;

public class BandDataClass {
    private String name;
    private String leaderUserId;
    private String bandImage;
    private List<String> memberUserLogins;

    public BandDataClass() {
    }

    public BandDataClass(String name, String leaderUserId, List<String> memberUserIds) {
        this.name = name;
        this.leaderUserId = leaderUserId;
        this.bandImage = "";
        this.memberUserLogins = memberUserIds;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeaderUserId() {
        return leaderUserId;
    }

    public void setLeaderUserId(String leaderUserId) {
        this.leaderUserId = leaderUserId;
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