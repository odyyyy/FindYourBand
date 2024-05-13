package com.example.findyourband.services;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class MemberDataClass implements Parcelable {

    private String nickname;
    private String image;
    private boolean isSelected;
    public MemberDataClass(String nickname) {
        this.nickname = nickname;
        this.isSelected = false;
    }

    public MemberDataClass(String nickname, String image) {
        this.nickname = nickname;
        this.image = image;
        this.isSelected = false;
    }

    public MemberDataClass(Parcel in) {
        nickname = in.readString();
        image = in.readString();
        isSelected = in.readByte() != 0;
    }

    public static final Creator<MemberDataClass> CREATOR = new Creator<MemberDataClass>() {
        @Override
        public MemberDataClass createFromParcel(Parcel in) {
            return new MemberDataClass(in);
        }

        @Override
        public MemberDataClass[] newArray(int size) {
            return new MemberDataClass[size];
        }
    };

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeString(this.image);
        dest.writeByte(this.isSelected ? (byte) 1 : (byte) 0);
    }
}
