package com.example.findyourband.services;

public class MemberDataClass {

    private String nickname;
    private String image;

    public MemberDataClass(String nickname) {
        this.nickname = nickname;
    }

    public MemberDataClass(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
    }

    public String getNickname() {
        return nickname;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


}
