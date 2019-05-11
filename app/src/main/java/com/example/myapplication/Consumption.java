package com.example.myapplication;

import java.util.Date;

public class Consumption {

    private Date date;
    private int quantity;
    private Integer consumptionid;
    private Food foodid;
    private Users userid;

    public Consumption() {
    }

    public Consumption(Integer consumptionid) {
        this.consumptionid = consumptionid;
    }

    public Consumption(Integer consumptionid, Date date, int quantity,Users user,Food food) {
        this.consumptionid = consumptionid;
        this.date = date;
        this.quantity = quantity;
        this.foodid = food;
        this.userid = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Integer getConsumptionid() {
        return consumptionid;
    }

    public void setConsumptionid(Integer consumptionid) {
        this.consumptionid = consumptionid;
    }

    public Food getFoodid() {
        return foodid;
    }

    public void setFoodid(Food foodid) {
        this.foodid = foodid;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }
}
