package com.example.myapplication;

import android.content.Intent;
import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private int userId;
    EditText calorieGoalEditor;
    TextView calorieGoalView;
    TextView calorieGoalUpdateView;
    Report userReport;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        //On click for calorieGoalUpdateView
        calorieGoalUpdateView = findViewById(R.id.calorieGoalUpdateView);
        calorieGoalUpdateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calorieGoalEditor = findViewById(R.id.calorieGoalEditor);
                View update = findViewById(R.id.updateGoal);
                calorieGoalEditor.setVisibility(View.VISIBLE);
                update.setVisibility(View.VISIBLE);
            }
        });
        //Setting up welcome message
        Intent intent=getIntent();
        Bundle bundle = intent.getExtras();
        TextView welcomeText = findViewById(R.id.welcome);
        welcomeText.setText("Welcome "+bundle.getString("firstName")+"!");

        //Set date and time
        TextView time=findViewById(R.id.time);
        TextView date = findViewById(R.id.date);
        LocalDateTime today = LocalDateTime.now();
        DateTimeFormatter formatterHour = DateTimeFormatter.ofPattern("HH");
        DateTimeFormatter formatterMin = DateTimeFormatter.ofPattern("mm");
        time.setText(today.format(formatterHour)+" : "+today.format(formatterMin));
        date.setText(today.getDayOfMonth()+" "+today.getMonth()+", "+today.getYear());
        userId = bundle.getInt("userId");

        //Display calorie goal
        ReportAsync showCalorie = new ReportAsync();
        showCalorie.execute(userId);

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_tools) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void updateCalorieGoal(View v)
    {
        calorieGoalEditor = findViewById(R.id.calorieGoalEditor);
        try {
            int calorieGoal = Integer.parseInt(calorieGoalEditor.getText().toString());

            userReport.setSetcaloriegoalforthatday(calorieGoal);
            ReportUpdateAsync update = new ReportUpdateAsync();
            update.execute(userReport);
        }
        catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(),"Enter valid value greater than zero!",Toast.LENGTH_LONG).show();
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

                calorieGoalView = findViewById(R.id.calorieGoalView);

                if (report == null)
                    calorieGoalView.setText("No calorie data found!");
                int calorieGoalVal = report.getSetcaloriegoalforthatday();
                userReport = report;

                calorieGoalUpdateView = findViewById(R.id.calorieGoalUpdateView);
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
                    calorieGoalEditor = findViewById(R.id.calorieGoalEditor);
                    calorieGoalUpdateView = findViewById(R.id.calorieGoalUpdateView);
                    calorieGoalView = findViewById(R.id.calorieGoalView);

                    int calorieGoalVal = Integer.parseInt(calorieGoalEditor.getText().toString());
                    calorieGoalView.setText("Your calorie goal is " + calorieGoalVal);

                    View update = findViewById(R.id.updateGoal);
                    calorieGoalEditor.setVisibility(View.GONE);
                    update.setVisibility(View.INVISIBLE);
                }
            }
            catch(Exception ex)
            {
                Toast.makeText(getApplicationContext(),"Some error occurred!",Toast.LENGTH_LONG).show();
            }
        }
    }
}
