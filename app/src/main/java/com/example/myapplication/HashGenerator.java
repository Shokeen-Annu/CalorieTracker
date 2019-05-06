package com.example.myapplication;

import android.util.Log;

import java.security.MessageDigest;

public class HashGenerator {

    public static String hashCodeGenerator(String input)
    {
        String hashedInput = "";
        try
        {
            MessageDigest messageDigest=MessageDigest.getInstance("MD5");
            messageDigest.update(input.getBytes());

            byte[]  bytes=messageDigest.digest();

            StringBuilder builder=new StringBuilder();

            for(int i=0;i<bytes.length;i++)
            {
                builder.append(Integer.toString((bytes[i]&0xff)+0x100,16).substring(1));
            }

            hashedInput = builder.toString();
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
            Log.i("error in hash generator",ex.getMessage());
        }
        return hashedInput;
    }
}
