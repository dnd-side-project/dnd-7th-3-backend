package com.dnd.mountclim.domain.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateParsing {

    public static String nowDateToyyyyMMdd(){
        Date nowDate = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        return simpleDateFormat.format(nowDate);
    }

    public static String weekAgoDateToyyyyMMdd(){
        Calendar cal = java.util.Calendar.getInstance();
        DateFormat format = new java.text.SimpleDateFormat("yyyy-MM-dd");
        cal.add(cal.DATE, -7);

        return format.format(cal.getTime());
    }
}