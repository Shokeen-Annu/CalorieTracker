package com.example.myapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NavigationBaseFragment extends Fragment  implements View.OnClickListener {

    Bundle receivedContent;
    private int userId;
    EditText calorieGoalEditor;
    TextView calorieGoalView;
    TextView calorieGoalUpdateView;
    Report userReport;
    View view;
    Button updateGoal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance)
    {
        view = inflater.inflate(R.layout.fragment_navigation_base,container,false);

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
        ReportAsync showCalorie = new ReportAsync();
        showCalorie.execute(userId);

        return view;
    }

    @Override
    public void onClick(View v)
    {
        calorieGoalEditor = view.findViewById(R.id.calorieGoalEditor);
        try {
            int calorieGoal = Integer.parseInt(calorieGoalEditor.getText().toString());

            userReport.setSetcaloriegoalforthatday(calorieGoal);
            ReportUpdateAsync update = new ReportUpdateAsync();
            update.execute(userReport);
        }
        catch (Exception ex)
        {
            Toast.makeText(view.getContext(),"Enter valid value greater than zero!",Toast.LENGTH_LONG).show();
        }

    }
    private class ReportAsync extends AsyncTask<Integer,Void,Report>
    {

        @Override
        protected Report doInBackground(Integer... params)
        {
            LocalDate todayDate = LocalDate.now();
            return RestClient.findReport(params[0],todayDate.toString());
        }

        @Override
        protected void onPostExecute(Report report)
        {

            try {

                calorieGoalView = view.findViewById(R.id.calorieGoalView);

                if (report == null) {
                    calorieGoalView.setText("No calorie data found!");
                    return;
                }
                int calorieGoalVal = report.getSetcaloriegoalforthatday();
                userReport = report;

                calorieGoalUpdateView = view.findViewById(R.id.calorieGoalUpdateView);
                calorieGoalUpdateView.setText("Click me to update your goal");
                if (calorieGoalVal > 0) {
                    calorieGoalView.setText("Your calorie goal is " + calorieGoalVal);

                } else {
                    calorieGoalView.setText("Your calorie goal is not set for today");
                }
            }
            catch(Exception ex)
            {
                ex.printStackTrace();
            }

        }
    }

    private class ReportUpdateAsync extends AsyncTask<Report,Void,Boolean>
    {
        @Override
        protected Boolean doInBackground(Report... params)
        {

            return RestClient.updateReport(params[0]);
        }

        @Override
        protected void onPostExecute(Boolean result)
        {
            try {
                if (result) {
                    calorieGoalEditor = view.findViewById(R.id.calorieGoalEditor);
                    calorieGoalUpdateView = view.findViewById(R.id.calorieGoalUpdateView);
                    calorieGoalView = view.findViewById(R.id.calorieGoalView);

                    int calorieGoalVal = Integer.parseInt(calorieGoalEditor.getText().toString());
                    calorieGoalView.setText("Your calorie goal is " + calorieGoalVal);

                    View update = view.findViewById(R.id.updateGoal);
                    calorieGoalEditor.setVisibility(View.GONE);
                    update.setVisibility(View.INVISIBLE);
                }
            }
            catch(Exception ex)
            {
                Toast.makeText(view.getContext(),"Some error occurred!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
