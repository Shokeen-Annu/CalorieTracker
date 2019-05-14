package com.example.myapplication;

import android.app.IntentService;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.util.Date;
import java.util.List;

public class ScheduledIntentService extends IntentService {

    Bundle bundle;
    Users user;
    int calorieGoalVal;
    UserStepsDatabase db;
    List<UserSteps> userStepsList;
    Integer maxReportId;
    public ScheduledIntentService(){
        super("ScheduledIntentService");
    }
    @Override
    protected void onHandleIntent(Intent intent)
    {
        try {
            SharedPreferences sharedPreferences = getSharedPreferences("calorietracker", Context.MODE_PRIVATE);
            String calorieGoal = sharedPreferences.getString("caloriegoal", null);
            calorieGoalVal = Integer.parseInt(calorieGoal);
            bundle = intent.getExtras();
            user = bundle.getParcelable("userObject");
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
        return super.onStartCommand(intent,flags,startId);
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
            RestClient.createReport(params[0]);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean isSuccess)
        {

            if(isSuccess)
            {
                Toast.makeText(getApplicationContext(),"Report added successfully",Toast.LENGTH_LONG).show();
            }
        }
    }

    private class GetUserSteps extends AsyncTask<Void,Void,List<UserSteps>>
    {
        @Override
        protected List<UserSteps> doInBackground(Void... params)
        {
            return db.userStepsDao().getAll();
        }

        @Override
        protected void onPostExecute(List<UserSteps> userSteps)
        {
            int totalSteps = 0;
            userStepsList = userSteps;
            for (UserSteps userStep : userStepsList) {
                totalSteps += userStep.getSteps();
            }
            Report report = new Report(maxReportId, new Date(), 0, 0, totalSteps, calorieGoalVal);
            report.setUserid(user);
            new AddReport().execute(report);
        }
    }
}
