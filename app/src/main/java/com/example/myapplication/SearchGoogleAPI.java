package com.example.myapplication;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class SearchGoogleAPI {

    private static final String API_KEY = "AIzaSyCx9HNyzF_006lrNdry__PCiCcjfdqGCr0";
    private static final String SEARCH_ID = "014722066971212957494:lj73ddbeok8";

    public static String search(String keyword,String[] params,String[] values)
    {
        keyword = keyword.replace(" ","+");
        URL url = null;
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
            url = new URL("https://www.googleapis.com/customsearch/v1?key="+API_KEY+"&cx="+SEARCH_ID+"&q="+keyword+parameters);
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

    public static String getSnippet(String input)
    {
        String snippet="";

        try
        {
            JSONObject jsonObject = new JSONObject(input);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if(jsonArray!=null && jsonArray.length()>0)
            {
                snippet = jsonArray.getJSONObject(0).getString("snippet");
            }
        }
        catch (Exception ex)
        {
            snippet="No Information found";
            ex.printStackTrace();
        }
        return snippet;
    }

    public static String getImage(String input)
    {
        String image="";

        String thumbNail="";
        try
        {
            JSONObject jsonObject = new JSONObject(input);
            JSONArray jsonArray = jsonObject.getJSONArray("items");

            if(jsonArray!=null && jsonArray.length()>0)
            {
                image = jsonArray.getJSONObject(0).getString("image");
                thumbNail = new JSONObject(image).getString("thumbnailLink");
            }
        }
        catch (Exception ex)
        {
            image="No Information found";
            ex.printStackTrace();
        }
        return thumbNail;
    }
}
