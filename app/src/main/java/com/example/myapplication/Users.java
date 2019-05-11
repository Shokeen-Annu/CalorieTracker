package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Users implements Parcelable {


    private Integer userid;

    private String name;

    private String surname;

    private String email;

    private Date dob;

    private BigDecimal height;

    private BigDecimal weight;

    private Character gender;

    private String address;

    private String postcode;

    private short levelofactivity;

    private int stepspermile;

    public Users() {
    }

    public Users(Integer userid) {
        this.userid = userid;
    }

    public Users(Integer userid,String name,String surname, String email, Date dob, BigDecimal height, BigDecimal weight, Character gender, String postcode, String address, short levelofactivity, int stepspermile) {
        this.userid = userid;
        this.name = name;
        this.surname=surname;
        this.email = email;
        this.dob = dob;
        this.height = height;
        this.weight = weight;
        this.gender = gender;
        this.postcode = postcode;
        this.address = address;
        this.levelofactivity = levelofactivity;
        this.stepspermile = stepspermile;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public short getLevelofactivity() {
        return levelofactivity;
    }

    public void setLevelofactivity(short levelofactivity) {
        this.levelofactivity = levelofactivity;
    }

    public int getStepspermile() {
        return stepspermile;
    }

    public void setStepspermile(int stepspermile) {
        this.stepspermile = stepspermile;
    }


    public Users(Parcel in)
    {
        try {
            this.userid = in.readInt();
            this.name = in.readString();
            this.surname = in.readString();
            this.email = in.readString();

            this.dob = (java.util.Date)in.readSerializable();
            this.height = new BigDecimal(in.readDouble());
            this.weight = new BigDecimal(in.readDouble());
            this.gender =in.readString().toCharArray()[0];
            this.postcode = in.readString();
            this.address = in.readString();
            this.levelofactivity = (short)in.readInt();
            this.stepspermile = in.readInt();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public void writeToParcel(Parcel parcel, int flags) {
        try {
            parcel.writeInt(userid);
            parcel.writeString(name);
            parcel.writeString(surname);
            parcel.writeString(email);
            parcel.writeString(dob.toString());
            parcel.writeDouble(height.doubleValue());
            parcel.writeDouble(weight.doubleValue());
            parcel.writeString(Character.toString(gender));
            parcel.writeString(postcode);
            parcel.writeString(address);
            parcel.writeInt(levelofactivity);
            parcel.writeInt(stepspermile);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
    }
    public int describeContents() {
        return 0;
    }
    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }
        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };


}
