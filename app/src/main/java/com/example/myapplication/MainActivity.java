package com.example.myapplication;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    private String hashInputPswd = "";
    Bundle bundle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void getNewUserScreen(View v)
    {
        Intent intent = new Intent(MainActivity.this,NewUser.class);
        startActivity(intent);
    }

    public void loginUser(View v)
    {
        try {
            EditText username = findViewById(R.id.username);
            EditText password = findViewById(R.id.password);

            String usernameVal = username.getText().toString().trim();
            String passwordVal = password.getText().toString();

            if(usernameVal.isEmpty() || passwordVal.isEmpty())
            {
                Toast.makeText(getApplicationContext(),"Username or Password cannot be empty!",Toast.LENGTH_LONG).show();
                return;
            }

            hashInputPswd = HashGenerator.hashCodeGenerator(passwordVal);

            LoginAsyncTask login = new LoginAsyncTask();
            login.execute(usernameVal);
        }
        catch(NullPointerException ex)
        {
            Toast.makeText(getApplicationContext(),"Username or Password cannot be empty!",Toast.LENGTH_LONG).show();
        }
        catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(),"Error occurred : Please enter valid input",Toast.LENGTH_LONG).show();
        }


    }

    private class LoginAsyncTask extends AsyncTask<String,Void,Credential>
    {
        @Override
        protected Credential doInBackground(String... params)
        {
            return RestClient.findCredential(params[0]);
        }

        @Override
        protected  void onPostExecute(Credential userCredential)
        {


            if(userCredential == null) {
                Toast.makeText(getApplicationContext(),"Username is incorrect!",Toast.LENGTH_LONG).show();
                return;
            }
            String passwordDB = userCredential.getPasswordhash();
            if(passwordDB.toLowerCase().equals(hashInputPswd.toLowerCase())) {
                Intent intent = new Intent(MainActivity.this,Home.class);
                bundle = new Bundle();
                bundle.putInt("userId",userCredential.getUserid().getUserid());
                bundle.putString("firstName",userCredential.getUserid().getName());
                bundle.putParcelable("userObject",userCredential.getUserid());
                intent.putExtras(bundle);

                // Calling Android service after login is successful and before calling Home navigation drawer.
                runScheduledIntentService();
                startActivity(intent);

            }
            else
            {
                Toast.makeText(getApplicationContext(),"Either Username or Password is incorrect!",Toast.LENGTH_LONG).show();
            }
        }

    }

    public void runScheduledIntentService()
    {
        Log.i("Android service","Setting alarm");
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent(this,ScheduledIntentService.class);
        alarmIntent.putExtras(bundle);
        PendingIntent pendingIntent = PendingIntent.getService(this,0,alarmIntent,0);
        Calendar calendar=Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY,23);
        calendar.set(Calendar.MINUTE,59);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),alarmManager.INTERVAL_DAY,pendingIntent);
    }
}
