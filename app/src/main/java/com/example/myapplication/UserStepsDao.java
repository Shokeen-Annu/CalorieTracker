package com.example.myapplication;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserStepsDao {

    @Query("SELECT * FROM UserSteps WHERE userid=:userId")
    List<UserSteps> getAll(Integer userId);

    @Insert
    long insert(UserSteps userSteps);

    @Update(onConflict = REPLACE)
    public void updateUserSteps(UserSteps... userSteps);

    @Query("DELETE FROM UserSteps WHERE userid=:userId")
    void deleteAll(Integer userId);
}
