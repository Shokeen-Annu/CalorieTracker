package com.example.myapplication;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PlacesAPI {

    private static final String API_KEY = "AIzaSyCUHHgC_CuHXFp52v7CgyWjEP7S_RzkULw";

    public static String callAPI(String[] params,String[] values)
    {

        URL url;
        HttpURLConnection conn = null;
        String result="";
        String parameters = "";
        if(params!=null && values !=null)
        {
            for(int i=0;i<params.length;i++) {
                parameters += "&";
                parameters += params[i];
                parameters += "=";
                parameters += values[i];
            }
        }
        try
        {
            url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/json?key="+API_KEY+parameters);
            conn = (HttpURLConnection)url.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("Accept","application/json");
            Scanner scanner = new Scanner(conn.getInputStream());
            while(scanner.hasNextLine())
            {
                result += scanner.nextLine();
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        finally {

            conn.disconnect();
        }
        return result;
    }

    public static List<Park> getParks(String input)
    {
        List<Park> parks = null;

        try
        {
            JSONObject jsonObject = new JSONObject(input);
            JSONArray jsonArray = jsonObject.getJSONArray("results");

            if(jsonArray!=null && jsonArray.length()>0)
            {
                parks = new ArrayList<>();
                for(int i=0;i<jsonArray.length();i++)
                {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    JSONObject geometry = obj.getJSONObject("geometry");
                    JSONObject loc = geometry.getJSONObject("location");
                    double lat = loc.getDouble("lat");
                    double lng = loc.getDouble("lng");
                    Park park = new Park(lat,lng,obj.getString("name"));
                    parks.add(park);
                }
            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return parks;
    }
}
