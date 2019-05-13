package com.example.myapplication;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class UserSteps {

    @PrimaryKey(autoGenerate = true)
    private Integer id;


    @ColumnInfo(name = "date_time")
    private String dateTime;

    @ColumnInfo(name = "steps")
    private Integer steps;

    @ColumnInfo(name = "userid")
    private Integer userId;

    public UserSteps()
    {

    }

    public UserSteps(Integer user,Integer steps,String dateTime)
    {
        this.dateTime = dateTime;
        this.steps = steps;
        this.userId = user;
    }

    public Integer getId()
    {
        return id;
    }

    public void setId(Integer id)
    {
        this.id = id;
    }

    public String getDateTime()
    {
        return dateTime;
    }

    public Integer getSteps()
    {
        return steps;
    }

    public Integer getUserId()
    {
        return userId;
    }

    public void setDateTime(String dateTime)
    {
        this.dateTime = dateTime;
    }

    public void setSteps(Integer steps)
    {
        this.steps = steps;
    }

    public void setUserId(Integer user)
    {
        this.userId = user;
    }


}
