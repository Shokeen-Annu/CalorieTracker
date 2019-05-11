package com.example.myapplication;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    static final String DATE_FORMAT = "yyyy-MM-dd'T'hh:mm:ss'+'hh:mm";
    public static Date formatDate(String date) throws ParseException
    {
        SimpleDateFormat format = new SimpleDateFormat(DATE_FORMAT);
        format.setLenient(false);
        Date formattedDate = format.parse(date.trim());
        return formattedDate;
    }
}
