package com.example.myapplication;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ScheduledIntentService extends IntentService {

    Bundle bundle;
    Users user;
    int calorieGoalVal;
    UserStepsDatabase db;
    Integer maxReportId;
    Integer totalCaloriesConsumed;
    double caloriesBurnedPerStep;
    double caloriesBurnedAtRest;
    int userId;
    static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss'+'hh:mm";
    String today;

    public ScheduledIntentService(){
        super("ScheduledIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("calorietracker", Context.MODE_PRIVATE);
            String userJson = sharedPreferences.getString("userObject", null);
            Gson gson=new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            user = gson.fromJson(userJson,Users.class);
            today = DateFormat.formatStringToLocalDate(LocalDate.now().toString()).toString();
            userId = user.getUserid();
            String calorieGoal = sharedPreferences.getString("caloriegoal"+userId+today, null);
            if(calorieGoal == null )
            {
                Log.i("Android Service","No calorie goal set");
                calorieGoal = "0";
            }
            calorieGoalVal = Integer.parseInt(calorieGoal);
            db = Room.databaseBuilder(getApplicationContext(), UserStepsDatabase.class, "UserStepsDatabase").fallbackToDestructiveMigration().build();

            new GetMaxReportId().execute();
        }
        catch (Exception ex)
        {
            Log.i("Android Service","Service failed in onHandleIntent method");
        }

    }

    @Override
    public int onStartCommand(Intent intent,int flags,int startId)
    {
        int result = super.onStartCommand(intent,flags,startId);
        return result;

    }

    private class GetMaxReportId extends AsyncTask<Void,Void,Integer>
    {
        @Override
        protected Integer doInBackground(Void... params)
        {
            return RestClient.getMaxId("getMaxReportId","report");
        }

        @Override
        protected void onPostExecute(Integer maxId)
        {
            try {
                maxReportId = maxId + 1;
                new GetUserSteps().execute();

            }catch (Exception ex)
            {
                Log.i("Android Service","Service failed in onPostExecute method of GetMaxReportId");
            }

        }
    }
    private class AddReport extends AsyncTask<Report,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Report... params)
        {
            try {
                RestClient.createReport(params[0]);
                return true;
            }catch (Exception ex)
            {
                ex.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess)
        {
            if(isSuccess)
                new DeleteStepsData().execute();
            else
                Log.i("Android Service","Service failed in doInBackground method of AddReport");
        }
    }

    private class GetUserSteps extends AsyncTask<Void,Void,List<UserSteps>>
    {
        @Override
        protected List<UserSteps> doInBackground(Void... params)
        {
            List<UserSteps> userSteps = null;
            try {
                totalCaloriesConsumed = RestClient.getTotalCaloriesConsumedOnDate(userId, today);
                caloriesBurnedPerStep = RestClient.getCaloriesBurnedPerStep(userId);
                caloriesBurnedAtRest = RestClient.getTotalCaloriesBurnedAtRest(userId);
                userSteps = db.userStepsDao().getAll(userId);
            }
            catch (Exception ex)
            {
                Log.i("Android Service","Service failed in doInBackground method of GetUserSteps");
            }
            return userSteps;
        }

        @Override
        protected void onPostExecute(List<UserSteps> userSteps)
        {
            try {
                int totalSteps = 0;
                if(userSteps != null) {
                    for (UserSteps userStep : userSteps) {
                        totalSteps += userStep.getSteps();
                    }
                }

                double totalCaloriesBurned = totalSteps * caloriesBurnedPerStep;
                totalCaloriesBurned += caloriesBurnedAtRest;
                Report report = new Report(maxReportId, new Date(), totalCaloriesConsumed, (int)totalCaloriesBurned, totalSteps, calorieGoalVal);
                report.setUserid(user);
                new AddReport().execute(report);
            }
            catch (Exception ex)
            {
                Log.i("Android Service","Service failed in onPostExecute method of GetUserSteps");
            }
        }
    }
    private class DeleteStepsData extends AsyncTask<Void,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Void... params)
        {
            try {
                db.userStepsDao().deleteAll(userId);
                return true;
            }catch(Exception ex)
            {
                ex.printStackTrace();
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean isSuccess)
        {
            if(isSuccess)
                Log.i("Android Service","Report data added successfully!");
            else
                Log.i("Android Service","Error occurred! Report data is not added to the table!");
        }
    }
}
