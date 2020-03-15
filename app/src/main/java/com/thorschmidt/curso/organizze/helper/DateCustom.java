package com.thorschmidt.curso.organizze.helper;

import java.text.SimpleDateFormat;

public class DateCustom {

    public static String getCurrentDate(){
        long date = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        return dateFormat.format(date);
    }

    public static String getDayFromStringDate(String date){
        String[] dateArray = date.split("\\.");
        return dateArray[0]; // returning day
    }
    public static String getMonthFromStringDate(String date){
        String[] dateArray = date.split("\\.");
        return dateArray[1]; // returning month
    }
    public static String getYearFromStringDate(String date){
        String[] dateArray = date.split("\\.");
        return dateArray[2]; // returning year
    }
}
