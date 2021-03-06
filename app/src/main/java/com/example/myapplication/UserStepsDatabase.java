package com.example.myapplication;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {UserSteps.class},version = 6,exportSchema = false)
public abstract class UserStepsDatabase extends RoomDatabase {

    public abstract UserStepsDao userStepsDao();

    private static volatile UserStepsDatabase INSTANCE;

    static UserStepsDatabase getDatabase(final Context context)
    {
        if(INSTANCE == null)
        {
            synchronized (UserStepsDatabase.class){
                if(INSTANCE == null)
                {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),UserStepsDatabase.class,"user_stepsDB").build();
                }
            }
        }
        return INSTANCE;
    }
}
