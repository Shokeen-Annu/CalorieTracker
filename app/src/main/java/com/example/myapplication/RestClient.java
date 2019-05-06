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
            setConnectionParameters(conn,"POST",stringUsersJson);

            //sending the post
            sendPost(conn,stringUsersJson);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> createUser",ex.getMessage());
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
            setConnectionParameters(conn,"POST",stringCredentialJson);

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

    public static String findCredential(String username){

        final String path = "calorietracker.credential/"+username;
        URL url=null;
        HttpURLConnection conn = null;
        String result="";

        try
        {
            url =new URL(BASE_URL+path);
            conn = (HttpURLConnection)url.openConnection();

            setConnectionParameters(conn,"GET","");

            result = readResponse(conn,result);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> findCredential",ex.getMessage());
        }
        finally {
            conn.disconnect();
        }

        return result;
    }

    public static void setConnectionParameters(HttpURLConnection conn,String methodType,String data) throws java.net.ProtocolException
    {

          conn.setReadTimeout(10000);
          conn.setConnectTimeout(15000);
          conn.setRequestProperty("Content-Type", "application/json");

          if(methodType.equals("POST")) {
              conn.setRequestMethod("POST");
              conn.setDoOutput(true);
              conn.setFixedLengthStreamingMode(data.getBytes().length);
          }
          else if(methodType.equals("GET"))
          {
              conn.setRequestMethod("GET");
              conn.setRequestProperty("Accept","application/json");
          }

    }

    public static void sendPost(HttpURLConnection conn,String data) throws IOException
    {
        PrintWriter writer=new PrintWriter(conn.getOutputStream());
        writer.print(data);
        writer.close();
    }

    public static String readResponse(HttpURLConnection conn,String data) throws IOException
    {
        Scanner scanner = new Scanner(conn.getInputStream());

        while(scanner.hasNextLine())
        {
            data += scanner.nextLine();
        }

        return data;
    }
}
