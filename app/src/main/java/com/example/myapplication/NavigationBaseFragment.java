package com.example.myapplication;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class NavigationBaseFragment extends Fragment  implements View.OnClickListener {

    Bundle receivedContent;
    private int userId;
    EditText calorieGoalEditor;
    TextView calorieGoalView;
    TextView calorieGoalUpdateView;
    View view;
    Button updateGoal;
    UserStepsDatabase db;
    Integer totalCaloriesConsumed;
    double caloriesBurnedPerStep;
    double caloriesBurnedAtRest;
    String today;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        view = inflater.inflate(R.layout.fragment_navigation_base,container,false);
        db = Room.databaseBuilder(view.getContext(), UserStepsDatabase.class, "UserStepsDatabase").fallbackToDestructiveMigration().build();
        today = DateFormat.formatStringToLocalDate(LocalDate.now().toString()).toString();
        // Setting on click of update button
        updateGoal = view.findViewById(R.id.updateGoal);
        updateGoal.setOnClickListener(this);
        //On click for calorieGoalUpdateView
        calorieGoalUpdateView = view.findViewById(R.id.calorieGoalUpdateView);
        calorieGoalUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieGoalEditor = view.findViewById(R.id.calorieGoalEditor);
                View update = view.findViewById(R.id.updateGoal);
                calorieGoalEditor.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
            }
        });

        // Calling intent service upon button clicked;
        Button startService = view.findViewById(R.id.startService);
        startService.setOnClickListener(new AdapterView.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(),ScheduledIntentService.class);
                getActivity().startService(intent);
            }
        });

        //Setting up welcome message

        receivedContent = getArguments();
        TextView welcomeText = view.findViewById(R.id.welcome);
        welcomeText.setText("Welcome "+receivedContent.getString("firstName")+"!");

        //Set date and time
        TextView time=view.findViewById(R.id.time);
        TextView date = view.findViewById(R.id.date);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH");
        DateTimeFormatter formatterMin = DateTimeFormatter.ofPattern("mm");
        time.setText(today.format(formatterHour)+" : "+today.format(formatterMin));
        date.setText(today.getDayOfMonth()+" "+today.getMonth()+", "+today.getYear());
        userId = receivedContent.getInt("userId");

        //Display calorie goal
        //ReportAsync showCalorie = new ReportAsync();
        //showCalorie.execute(userId);
        showCalorieGoal();
        new ShowReportData().execute();
        return view;
    }

    @Override
    public void onClick(View v)
    {
        calorieGoalEditor = view.findViewById(R.id.calorieGoalEditor);
        try {
            String calorieGoalString = calorieGoalEditor.getText().toString();

            int calorieGoal = Integer.parseInt(calorieGoalString);
            if(calorieGoal > 0) {
                SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                        "calorietracker", Context.MODE_PRIVATE);
                SharedPreferences.Editor spEditor = sharedPreferences.edit();

                spEditor.putString("caloriegoal"+userId+today, calorieGoalString);
                spEditor.apply();
            }
            else
                throw new Exception();
         //   userReport.setSetcaloriegoalforthatday(calorieGoal);
         //   ReportUpdateAsync update = new ReportUpdateAsync();
         //   update.execute(userReport);
            showCalorieGoal();
            calorieGoalEditor = view.findViewById(R.id.calorieGoalEditor);
            View update = view.findViewById(R.id.updateGoal);
            calorieGoalEditor.setVisibility(View.GONE);
            update.setVisibility(View.INVISIBLE);
        }
        catch (Exception ex)
        {
            Toast.makeText(view.getContext(),"Enter valid value greater than zero!",Toast.LENGTH_LONG).show();
        }

    }
//    private class ReportAsync extends AsyncTask<Integer,Void,Report>
//    {
//
//        @Override
//        protected Report doInBackground(Integer... params)
//        {
//            LocalDate todayDate = LocalDate.now();
//            return RestClient.findReport(params[0],todayDate.toString());
//        }
//
//        @Override
//        protected void onPostExecute(Report report)
//        {
//
//
//
//        }
//    }

//    private class ReportUpdateAsync extends AsyncTask<Report,Void,Boolean>
//    {
//        @Override
//        protected Boolean doInBackground(Report... params)
//        {
//
//            return RestClient.updateReport(params[0]);
//        }
//
//        @Override
//        protected void onPostExecute(Boolean result)
//        {
//            try {
//                if (result) {
//                    calorieGoalEditor = view.findViewById(R.id.calorieGoalEditor);
//                    calorieGoalUpdateView = view.findViewById(R.id.calorieGoalUpdateView);
//                    calorieGoalView = view.findViewById(R.id.calorieGoalView);
//
//                    int calorieGoalVal = Integer.parseInt(calorieGoalEditor.getText().toString());
//                    calorieGoalView.setText("Your calorie goal is " + calorieGoalVal);
//
//                    View update = view.findViewById(R.id.updateGoal);
//                    calorieGoalEditor.setVisibility(View.GONE);
//                    update.setVisibility(View.INVISIBLE);
//                }
//            }
//            catch(Exception ex)
//            {
//                Toast.makeText(view.getContext(),"Some error occurred!",Toast.LENGTH_LONG).show();
//            }
//        }
//    }
    public void showCalorieGoal()
    {
        try {

            calorieGoalView = view.findViewById(R.id.calorieGoalView);
            calorieGoalUpdateView = view.findViewById(R.id.calorieGoalUpdateView);
            calorieGoalUpdateView.setText("Click me to update your goal!");
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("calorietracker",
                    Context.MODE_PRIVATE);
            String calorieGoal = sharedPreferences.getString("caloriegoal"+userId+today, null);
            if (calorieGoal == null) {
                calorieGoalView.setText("Your calorie goal is not set for today!");
                return;
            }
            int calorieGoalVal = Integer.parseInt(calorieGoal);

            calorieGoalView.setText("Your calorie goal is " + calorieGoalVal+".");

        }
        catch(Exception ex)
        {
            Toast.makeText(view.getContext(),"Calorie goal is not found!",Toast.LENGTH_LONG).show();
        }
    }
    private class ShowReportData extends AsyncTask<Void,Void, List<UserSteps>>
    {
        @Override
        protected List<UserSteps> doInBackground(Void... params)
        {
            List<UserSteps> userSteps = null;
            try {

                String today = DateFormat.formatStringToLocalDate(LocalDate.now().toString()).toString();
                totalCaloriesConsumed = RestClient.getTotalCaloriesConsumedOnDate(userId, today);
                caloriesBurnedPerStep = RestClient.getCaloriesBurnedPerStep(userId);
                caloriesBurnedAtRest = RestClient.getTotalCaloriesBurnedAtRest(userId);
                userSteps = db.userStepsDao().getAll(userId);
            }
            catch (Exception ex)
            {
                Toast.makeText(view.getContext(),"Report could not be loaded due to some error!",Toast.LENGTH_LONG).show();
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

                TextView stepsText = view.findViewById(R.id.totalSteps);
                TextView calConsumed = view.findViewById(R.id.caloriesConsumed);
                TextView calBurned = view.findViewById(R.id.caloriesBurned);
                stepsText.setText("Total Steps : "+totalSteps);
                calConsumed.setText("Calories Consumed : "+totalCaloriesConsumed);
                calBurned.setText("Calories Burned : "+(int)totalCaloriesBurned);
            }
            catch (Exception ex)
            {
                Toast.makeText(view.getContext(),"Report could not be loaded due to some error!",Toast.LENGTH_LONG).show();
            }
        }
    }


}
