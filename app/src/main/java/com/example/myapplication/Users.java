package com.example.myapplication;

import java.math.BigDecimal;
import java.util.Date;

public class Users {
    private static final long serialVersionUID = 1L;

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

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userid != null ? userid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.userid == null && other.userid != null) || (this.userid != null && !this.userid.equals(other.userid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "calorietracker.Users[ userid=" + userid + " ]";
    }

}
