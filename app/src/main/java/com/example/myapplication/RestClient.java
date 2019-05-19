package com.example.myapplication;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
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
            int responseCode = conn.getResponseCode();
            if(responseCode!=204)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Create Credential Post: ",errorResult);
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }

    public static Credential findCredential(String username){

        final String path = "calorietracker.credential/"+username;
        URL url=null;
        HttpURLConnection conn = null;
        String result="";
        Credential userCredential=null;
        try
        {
            url =new URL(BASE_URL+path);
            conn = (HttpURLConnection)url.openConnection();

            setConnectionParameters(conn,"GET","");

            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Find Credential: ",errorResult);
                return null;
            }
            result = readResponse(conn);

            Gson gson=new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            userCredential = gson.fromJson(result,Credential.class);

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> findCredential",ex.getMessage());
            return null;
        }
        finally {
            conn.disconnect();
            return userCredential;
        }


    }


//    public static Report findReport(Integer userid, String date)
//    {
//        URL url=null;
//        final String path = "calorietracker.report/findByUseridAndDate/"+userid+"/"+date;
//        HttpURLConnection conn = null;
//        String result = "";
//        Report report = null;
//        try
//        {
//           url = new URL(BASE_URL+path);
//
//           conn = (HttpURLConnection) url.openConnection();
//
//           setConnectionParameters(conn,"GET","");
//
//            int responseCode = conn.getResponseCode();
//            if(responseCode!=200)
//            {
//                String errorResult=errorResponse(conn);
//
//                Log.e("Error Find Report: ",errorResult);
//                return null;
//            }
//           result = readResponse(conn);
//
//           Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
//           report = gson.fromJson(result,Report.class);
//        }
//        catch(IOException ex)
//        {
//            ex.printStackTrace();
//            Log.i("error in RestClient -> findReport",ex.getMessage());
//            report = null;
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//            Log.i("error in RestClient -> findReport",ex.getMessage());
//        }
//        return report;
//    }

//    public static Boolean updateReport(Report report)
//    {
//        URL url=null;
//        final String path = "calorietracker.report/"+report.getReportid();
//        HttpURLConnection conn = null;
//        String reportJson = "";
//        try
//        {
//            Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
//            reportJson = gson.toJson(report);
//            url = new URL(BASE_URL+path);
//
//            conn = (HttpURLConnection) url.openConnection();
//
//            setConnectionParameters(conn,"PUT","");
//
//            sendPost(conn,reportJson);
//
//            int responseCode = conn.getResponseCode();
//            if(responseCode!=204)
//            {
//                String errorResult=errorResponse(conn);
//
//                Log.e("Error Create User Post: ",errorResult);
//                return false;
//            }
//        }
//        catch(Exception ex)
//        {
//            ex.printStackTrace();
//            Log.i("error in RestClient -> findReport",ex.getMessage());
//        }
//       return true;
//    }

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

    public static List<String> getFoodCategories() {

        String path = "calorietracker.food/getCategories";
        URL url = null;
        List<String> categoryList = new ArrayList<String>();
        HttpURLConnection conn=null;
        String result ="";
        try
        {
            url =new URL(BASE_URL+path);
            conn = (HttpURLConnection) url.openConnection();
            setConnectionParameters(conn,"GET","");

            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Get Food Categories: ",errorResult);
                return null;
            }
            result = readResponse(conn);
            String list = result.split(":")[1];
            String[] categories = list.substring(1,list.length()-2).split(",");
            categoryList.add("Select Category");
            for(String item : categories)
            {
                categoryList.add(item);
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> getFoodCategories",ex.getMessage());
        }
        finally {

            conn.disconnect();
        }
        return categoryList;
    }

    public static Food[] getFoodItems(String param) {
        String path = "calorietracker.food/findByCategory/"+param;
        URL url = null;

        HttpURLConnection conn=null;
        String result ="";
        Food[] foodItems=null;
        try
        {
            url =new URL(BASE_URL+path);
            conn = (HttpURLConnection) url.openConnection();
            setConnectionParameters(conn,"GET","");

            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Get Food Items: ",errorResult);
                return null;
            }
            result = readResponse(conn);
            Gson gson=new Gson();
            foodItems = gson.fromJson(result,Food[].class);


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> getFoodItems",ex.getMessage());
        }
        finally {

            conn.disconnect();
        }
        return foodItems;
    }
    //Test on empty table
    public static int getMaxId(String methodName,String className)
    {
        String path = "calorietracker."+className+"/"+methodName;
        URL url = null;
        int maxId=0;
        HttpURLConnection conn = null;
        try
        {
            url = new URL(BASE_URL+path);
            conn=(HttpURLConnection)url.openConnection();
            setConnectionParameters(conn,"GET","");
            conn.setRequestProperty("Accept", "text/plain; charset=utf-8");
            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error in "+methodName+": ",errorResult);
                return maxId;
            }
            maxId = Integer.parseInt(readResponse(conn));
        }
        catch(Exception ex)
        {
            maxId = 0;
            ex.printStackTrace();
            Log.i("error in RestClient -> "+methodName+"",ex.getMessage());

        }
        finally
        {
            conn.disconnect();
        }

        return  maxId;
    }
    public static void createConsumption(Consumption consumption){
        URL url = null;
        HttpURLConnection conn =null;
        final String path = "calorietracker.consumption/";

        try
        {
            Gson gson=new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            String consumptionJson = gson.toJson(consumption);
            url=new URL(BASE_URL+path);

            //opening connection
            conn=(HttpURLConnection)url.openConnection();

            //setting connection parameters
            setConnectionParameters(conn,"POST",consumptionJson);

            //sending the post
            sendPost(conn,consumptionJson);
            int responseCode = conn.getResponseCode();
            if(responseCode!=204)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Create Consumption Post: ",errorResult);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> createConsumption",ex.getMessage());
        }
        finally {
            conn.disconnect();
        }

    }
    public static void createFoodItem(Food food){
        URL url;
        HttpURLConnection conn =null;
        final String path = "calorietracker.food/";

        try
        {
            Gson gson=new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            String json = gson.toJson(food);
            url=new URL(BASE_URL+path);

            //opening connection
            conn=(HttpURLConnection)url.openConnection();

            //setting connection parameters
            setConnectionParameters(conn,"POST",json);

            //sending the post
            sendPost(conn,json);
            int responseCode = conn.getResponseCode();
            if(responseCode!=204)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Create Food Post: ",errorResult);
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> createFood",ex.getMessage());
        }
        finally {
            conn.disconnect();
        }

    }
    public static Consumption isFoodItemAlreadyAddedByUser(Integer userid,String date,Integer foodId) {

        String path = "calorietracker.consumption/isFoodItemAlreadyAddedByUser/"+userid+"/"+date+"/"+foodId;
        URL url = null;
        Consumption consumption = null;
        HttpURLConnection conn=null;
        try
        {
            url =new URL(BASE_URL+path);
            conn = (HttpURLConnection) url.openConnection();
            setConnectionParameters(conn,"GET","");

            int responseCode = conn.getResponseCode();
            if(responseCode==204)
            {
                return null;
            }
             String result = readResponse(conn);
            Gson gson= new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            consumption = gson.fromJson(result,Consumption.class);

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> isFoodItemAlreadyAddedByUser",ex.getMessage());
        }
        finally {

            conn.disconnect();
        }
        return consumption;
    }
    public static Boolean updateConsumption(Consumption consumption)
    {
        URL url=null;
        final String path = "calorietracker.consumption/"+consumption.getConsumptionid();
        HttpURLConnection conn = null;
        String consumptionJson = "";
        try
        {
            Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            consumptionJson = gson.toJson(consumption);
            url = new URL(BASE_URL+path);

            conn = (HttpURLConnection) url.openConnection();

            setConnectionParameters(conn,"PUT","");

            sendPost(conn,consumptionJson);

            int responseCode = conn.getResponseCode();
            if(responseCode!=204)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error update consumption Put: ",errorResult);
                return false;
            }
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> updateConsumption",ex.getMessage());
        }
        return true;
    }

    public static void createReport(Report report)
    {
        URL url = null;
        HttpURLConnection conn =null;
        final String path = "calorietracker.report/";

        try
        {
            Gson gson=new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            String stringReportJson = gson.toJson(report);
            url=new URL(BASE_URL+path);

            //opening connection
            conn=(HttpURLConnection)url.openConnection();

            //setting connection parameters
            setConnectionParameters(conn,"POST",stringReportJson);

            //sending the post
            sendPost(conn,stringReportJson);
            int responseCode = conn.getResponseCode();
            if(responseCode!=204)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error Create Report Post: ",errorResult);
            }

        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally {
            conn.disconnect();
        }
    }
    public static int getTotalCaloriesConsumedOnDate(Integer userId,String date)
    {
        String path = "calorietracker.consumption/getTotalCaloriesConsumedOnDate/"+userId+"/"+date;
        URL url;
        int calories = 0;
        HttpURLConnection conn = null;
        try
        {
            url = new URL(BASE_URL+path);
            conn=(HttpURLConnection)url.openConnection();
            setConnectionParameters(conn,"GET","");
            conn.setRequestProperty("Accept", "text/plain; charset=utf-8");
            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error in getTotalCaloriesConsumedOnDate",errorResult);
                return calories;
            }
            calories = Integer.parseInt(readResponse(conn));
        }
        catch(Exception ex)
        {
            calories = 0;
            ex.printStackTrace();
            Log.i("error in RestClient -> getTotalCaloriesConsumedOnDate",ex.getMessage());

        }
        finally
        {
            conn.disconnect();
        }

        return  calories;
    }

    public static double getCaloriesBurnedPerStep(Integer userId)
    {
        String path = "calorietracker.users/getCaloriesBurnedPerStep/"+userId;
        URL url;
        double calories = 0.0;
        HttpURLConnection conn = null;
        try
        {
            url = new URL(BASE_URL+path);
            conn=(HttpURLConnection)url.openConnection();
            setConnectionParameters(conn,"GET","");
            conn.setRequestProperty("Accept", "text/plain; charset=utf-8");
            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error in getCaloriesBurnedPerStep",errorResult);
                return calories;
            }
            calories = Double.parseDouble(readResponse(conn));
        }
        catch(Exception ex)
        {
            calories = 0.0;
            ex.printStackTrace();
            Log.i("error in RestClient -> getCaloriesBurnedPerStep",ex.getMessage());

        }
        finally
        {
            conn.disconnect();
        }

        return  calories;
    }
    public static double getTotalCaloriesBurnedAtRest(Integer userId)
    {
        String path = "calorietracker.users/getTotalCaloriesBurnedAtRest/"+userId;
        URL url;
        double calories = 0.0;
        HttpURLConnection conn = null;
        try
        {
            url = new URL(BASE_URL+path);
            conn=(HttpURLConnection)url.openConnection();
            setConnectionParameters(conn,"GET","");
            conn.setRequestProperty("Accept", "text/plain; charset=utf-8");
            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error in getTotalCaloriesBurnedAtRest",errorResult);
                return calories;
            }
            calories = Double.parseDouble(readResponse(conn));
        }
        catch(Exception ex)
        {
            calories = 0.0;
            ex.printStackTrace();
            Log.i("error in RestClient -> getTotalCaloriesBurnedAtRest",ex.getMessage());

        }
        finally
        {
            conn.disconnect();
        }

        return  calories;
    }

    public static String getReportOnDuration(Integer userId,String fromDate,String toDate)
    {
        String path = "calorietracker.report/createReportForDurationAndUser/"+userId+"/"+fromDate+"/"+toDate;
        URL url;
        String json="";
        HttpURLConnection conn = null;
        try
        {
            url = new URL(BASE_URL+path);
            conn=(HttpURLConnection)url.openConnection();
            setConnectionParameters(conn,"GET","");
            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error in getReportOnDuration",errorResult);
                return json;
            }
            json = readResponse(conn);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> getReportOnDuration",ex.getMessage());

        }
        finally
        {
            conn.disconnect();
        }

        return  json;
    }
    public static String getReportOnGivenDate(Integer userId,String date)
    {
        String path = "calorietracker.report/createReportOnCaloriesOnGivenDateAndUser/"+userId+"/"+date;
        URL url;
        String json="";
        HttpURLConnection conn = null;
        try
        {
            url = new URL(BASE_URL+path);
            conn=(HttpURLConnection)url.openConnection();
            setConnectionParameters(conn,"GET","");
            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error in getReportOnGivenDate",errorResult);
                return json;
            }
            json = readResponse(conn);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> getReportOnGivenDate",ex.getMessage());

        }
        finally
        {
            conn.disconnect();
        }

        return  json;
    }

    public static Boolean isEmailUnique(String param) {
        String path = "calorietracker.users/findByEmail/"+param;
        URL url;
        Boolean result=true;
        HttpURLConnection conn = null;
        try
        {
            url = new URL(BASE_URL+path);
            conn=(HttpURLConnection)url.openConnection();
            setConnectionParameters(conn,"GET","");
            int responseCode = conn.getResponseCode();
            if(responseCode!=200)
            {
                String errorResult=errorResponse(conn);

                Log.e("Error in isEmailUnique",errorResult);
                return result;
            }
            String json = readResponse(conn);
            Gson gson = new GsonBuilder().setDateFormat(DATE_FORMAT).create();
            Users user = gson.fromJson(json,Users.class);
            if(user != null)
                result = false;


        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in RestClient -> isEmailUnique",ex.getMessage());

        }
        finally
        {
            conn.disconnect();
        }

        return  result;
    }


}
