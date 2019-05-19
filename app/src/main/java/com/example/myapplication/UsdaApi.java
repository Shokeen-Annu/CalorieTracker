package com.example.myapplication;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class UsdaApi {

    final static String API_KEY = "SYI14fx3syyFQanaHsxX0CWKHCt4j3vqT6wNw57L";

    public static String search(String method,String[] params,String[] values)
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
            url = new URL("https://api.nal.usda.gov/ndb/"+method+"/?api_key="+API_KEY+parameters);
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

    public static String getFoodId(String searchJson)
    {
        String foodId="";

        try
        {
            JSONObject jsonObject = new JSONObject(searchJson);
            JSONObject listObject = jsonObject.getJSONObject("list");

            if(listObject!=null)
            {
                foodId = listObject.getJSONArray("item").getJSONObject(0).getString("ndbno");
            }
        }
        catch (Exception ex)
        {
            foodId="-1";
            ex.printStackTrace();
        }
        return foodId;
    }

    public static Food getFoodNutrients(String param) {

        Food food = new Food();

        try
        {
            JSONObject jsonObject = new JSONObject(param);
            JSONObject listObject = jsonObject.getJSONObject("report").getJSONObject("food");

            if(listObject!=null)
            {
                food.setName(listObject.getString("name"));
                JSONArray nutrients = listObject.getJSONArray("nutrients");
                String servingAmountFat="";
                Double fatAmount=null;
                BigDecimal servAmtCal=null;
                Boolean flagCal = false;
                Boolean flagFat = false;
                for(int i=0; i <nutrients.length();i++)
                {
                    JSONObject obj = nutrients.getJSONObject(i);
                    String nutrient_nameCal = "Energy";
                    String nutrient_nameFat = "Total lipid (fat)";
                    if(obj.getString("name").equals(nutrient_nameCal))
                    {
                        String calories = obj.getString("value");
                        String calorieUnit=obj.getString("unit");
                        if(calorieUnit.equals("kcal")) {
                            if (calorieUnit.equals("kcal"))
                                food.setCalorieamount(Integer.parseInt(calories) * 1000);
                            else if (calorieUnit.equals("cal"))
                                food.setCalorieamount(Integer.parseInt(calories));
                            else
                                throw new Exception("Something went wrong");

                            JSONArray measures = obj.getJSONArray("measures");
                            String servingUnit = measures.getJSONObject(0).getString("label");
                            String servingAmount = measures.getJSONObject(0).getString("value");
                            food.setServingunit(servingUnit);
                            servAmtCal = new BigDecimal(Integer.parseInt(servingAmount));
                            food.setServingamount(servAmtCal);
                            flagCal = true;
                        }
                    }
                    else if(obj.getString("name").equals(nutrient_nameFat))
                    {
                        String fat = obj.getString("value");
                        fatAmount = Double.parseDouble(fat);

                        JSONArray measuresFat = obj.getJSONArray("measures");
                        servingAmountFat = measuresFat.getJSONObject(0).getString("value");
                        flagFat=true;
                    }
                    else
                    {
                        if(flagCal&&flagFat)
                            break;
                    }


                }

                Double newFatAmount = 0.0;

                //Calculating fat
                if(flagCal && flagFat) {
                    BigDecimal serveAmtFat = new BigDecimal(Double.parseDouble(servingAmountFat));
                    if (serveAmtFat != servAmtCal) {
                        newFatAmount = (fatAmount / serveAmtFat.doubleValue()) * servAmtCal.doubleValue();
                    }
                }
                else
                {
                    throw new Exception("Something went wrong");
                }
                food.setFat(newFatAmount.intValue());

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return food;
    }
}
