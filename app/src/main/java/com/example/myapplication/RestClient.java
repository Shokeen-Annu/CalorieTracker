package com.example.myapplication;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.Scanner;

public class RestClient {

    static final String BASE_URL="http://10.0.2.2:8080/CalorieTrackerAssignment/webresources/";
    static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss'+'hh:mm";
    public static void createUser(Users user){
        URL url = null;
        HttpURLConnection conn =null;
        final String path = "calorietracker.users/";

        try
        {
            Gson gson=new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            String stringUsersJson = gson.toJson(user);
            url=new URL(BASE_URL+path);

            //opening connection
            conn=(HttpURLConnection)url.openConnection();

            //setting connection parameters
            setConnectionParameters(conn,"POST",stringUsersJson);

            //sending the post
            sendPost(conn,stringUsersJson);
            int responseCode = conn.getResponseCode();
            if(responseCode!=204)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Create User Post: ",errorResult);
            }
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
            Gson gson=new GsonBuilder().setDateFormat(DATE_FORMAT).create();
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

            result = readResponse(conn);

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

    public static Report findReport(Integer userid, String date)
    {
        URL url=null;
        final String path = "calorietracker.report/findByUseridAndDate/"+userid+"/"+date;
        HttpURLConnection conn = null;
        String result = "";
        Report report = null;
        try
        {
           url = new URL(BASE_URL+path);

           conn = (HttpURLConnection) url.openConnection();

           setConnectionParameters(conn,"GET","");

            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Find Report: ",errorResult);
                return null;
            }
           result = readResponse(conn);

           Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
           report = gson.fromJson(result,Report.class);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> findReport",ex.getMessage());
            report = null;
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> findReport",ex.getMessage());
        }
        return report;
    }

    public static Boolean updateReport(Report report)
    {
        URL url=null;
        final String path = "calorietracker.report/"+report.getReportid();
        HttpURLConnection conn = null;
        String reportJson = "";
        try
        {
            Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            reportJson = gson.toJson(report);
            url = new URL(BASE_URL+path);

            conn = (HttpURLConnection) url.openConnection();

            setConnectionParameters(conn,"PUT","");

            sendPost(conn,reportJson);

            int responseCode = conn.getResponseCode();
            if(responseCode!=204)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Create User Post: ",errorResult);
                return false;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> findReport",ex.getMessage());
        }
       return true;
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
          else if(methodType.equals("PUT"))
          {
              conn.setRequestMethod("PUT");
          }

    }

    public static void sendPost(HttpURLConnection conn,String data) throws IOException
    {
        PrintWriter writer=new PrintWriter(conn.getOutputStream());
        writer.print(data);
        writer.close();
    }

    public static String readResponse(HttpURLConnection conn) throws IOException
    {
        String data="";
        Scanner scanner = new Scanner(conn.getInputStream());

        while(scanner.hasNextLine())
        {
            data += scanner.nextLine();
        }

        return data;
    }

    public static String errorResponse(HttpURLConnection conn) throws IOException
    {
        String data="";
        Scanner scanner = new Scanner(conn.getErrorStream());

        while(scanner.hasNextLine())
        {
            data += scanner.nextLine();
        }

        return data;
    }
}
