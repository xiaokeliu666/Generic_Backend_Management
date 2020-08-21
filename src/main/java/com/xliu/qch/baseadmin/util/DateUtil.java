package com.xliu.qch.baseadmin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Date Util
 */
public class DateUtil {

    // current date
    public static Date getNowDate(){
        Calendar cal = Calendar.getInstance();
        return cal.getTime();
    }

    // get date n years ago
    public static Date getNowDateMinusYear(Integer n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR,- n);
        return cal.getTime();
    }

    // get date n years later
    public static Date getNowDateAddYear(Integer n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR,+ n);
        return cal.getTime();
    }

    // get date n months ago
    public static Date getNowDateMinusMonth(Integer n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,- n);
        return cal.getTime();
    }

    // get date n months later
    public static Date getNowDateAddMonth(Integer n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.MONTH,+ n);
        return cal.getTime();
    }

    // get date n weeks ago
    public static Date getNowDateMinusWeek(Integer n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR,- n);
        return cal.getTime();
    }

    // get date n weeks later
    public static Date getNowDateAddWeek(Integer n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.WEEK_OF_YEAR,+ n);
        return cal.getTime();
    }

    // get date n days ago
    public static Date getNowDateMinusDay(Integer n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,- n);
        return cal.getTime();
    }

    // get date n days later
    public static Date getNowDateAddDay(Integer n){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR,+ n);
        return cal.getTime();
    }

    public static void main(String[] args) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("Current:" + dateFormat.format(DateUtil.getNowDate()));
        System.out.println("One year ago:" + dateFormat.format(DateUtil.getNowDateMinusYear(1)));
        System.out.println("One month ago:" + dateFormat.format(DateUtil.getNowDateMinusMonth(1)));
        System.out.println("One week ago:" + dateFormat.format(DateUtil.getNowDateMinusWeek(1)));
        System.out.println("One day ago:" + dateFormat.format(DateUtil.getNowDateMinusDay(1)));
    }
}
