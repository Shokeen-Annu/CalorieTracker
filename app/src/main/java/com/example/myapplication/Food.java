package com.example.myapplication;

import java.math.BigDecimal;

public class Food {

    private Integer id;
    private String name;
    private String category;
    private int calorieamount;
    private String servingunit;
    private BigDecimal servingamount;
    private int fat;

    public Food() {
    }

    public Food(Integer id) {
        this.id = id;
    }

    public Food(Integer id, String name, int calorieamount, String servingunit, BigDecimal servingamount, int fat) {
        this.id = id;
        this.name = name;
        this.calorieamount = calorieamount;
        this.servingunit = servingunit;
        this.servingamount = servingamount;
        this.fat = fat;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getCalorieamount() {
        return calorieamount;
    }

    public void setCalorieamount(int calorieamount) {
        this.calorieamount = calorieamount;
    }

    public String getServingunit() {
        return servingunit;
    }

    public void setServingunit(String servingunit) {
        this.servingunit = servingunit;
    }

    public BigDecimal getServingamount() {
        return servingamount;
    }

    public void setServingamount(BigDecimal servingamount) {
        this.servingamount = servingamount;
    }

    public int getFat() {
        return fat;
    }

    public void setFat(int fat) {
        this.fat = fat;
    }
}
