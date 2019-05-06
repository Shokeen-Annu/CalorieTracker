package com.example.myapplication;

import android.app.Application;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MainActivity extends AppCompatActivity {

    private String hashInputPswd = "";
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

    private class LoginAsyncTask extends AsyncTask<String,Void,String>
    {
        @Override
        protected String doInBackground(String... params)
        {
            return RestClient.findCredential(params[0]);
        }

        @Override
        protected  void onPostExecute(String credentialJson)
        {
            Credential userCredential = new Credential();
            Gson gson=new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
            userCredential = gson.fromJson(credentialJson,Credential.class);
            String passwordDB = userCredential.getPasswordhash();
            if(passwordDB.toLowerCase().equals(hashInputPswd.toLowerCase())) {
                Intent intent = new Intent(MainActivity.this,Home.class);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(getApplicationContext(),"Either Username or Password is incorrect!",Toast.LENGTH_LONG).show();
            }
        }

    }
}
