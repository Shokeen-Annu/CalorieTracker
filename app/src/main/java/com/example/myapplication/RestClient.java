package com.example.myapplication;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class RestClient {

    static final String BASE_URL="http://10.0.2.2:8080/CalorieTrackerAssignment/webresources/";

    public static void createUser(Users user){
        URL url = null;
        HttpURLConnection conn =null;
        final String path = "calorietracker.users/";

        try
        {
            Gson gson=new Gson();
            String stringUsersJson = gson.toJson(user);
            url=new URL(BASE_URL+path);

            //opening connection
            conn=(HttpURLConnection)url.openConnection();

            //setting connection parameters
            setPostConnectionParameters(conn,stringUsersJson);

            //sending the post
            sendPost(conn,stringUsersJson);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
             conn.disconnect();
        }

    }

    public static void createCredential(Credential cred)
    {
        URL url = null;
        HttpURLConnection conn =null;
        final String path = "calorietracker.credential/";

        try
        {
            Gson gson=new Gson();
            String stringCredentialJson = gson.toJson(cred);
            url=new URL(BASE_URL+path);

            //opening connection
            conn=(HttpURLConnection)url.openConnection();

            //setting connection parameters
            setPostConnectionParameters(conn,stringCredentialJson);

            //sending the post
            sendPost(conn,stringCredentialJson);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

    public static void setPostConnectionParameters(HttpURLConnection conn,String data) throws java.net.ProtocolException
    {

          conn.setReadTimeout(10000);
          conn.setConnectTimeout(15000);
          conn.setRequestMethod("POST");
          conn.setDoOutput(true);
          conn.setFixedLengthStreamingMode(data.getBytes().length);
          conn.setRequestProperty("Content-Type", "application/json");

    }

    public static void sendPost(HttpURLConnection conn,String data) throws IOException
    {
        PrintWriter writer=new PrintWriter(conn.getOutputStream());
        writer.print(data);
        writer.close();
    }
}
