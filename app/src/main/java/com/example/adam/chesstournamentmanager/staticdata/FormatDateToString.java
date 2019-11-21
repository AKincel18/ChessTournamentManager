package com.example.adam.chesstournamentmanager.staticdata;

import java.util.Calendar;
import java.util.Date;

public class FormatDateToString {

    public static String getFormatDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        int month;
        if ((month = calendar.get(Calendar.MONTH)) < 10) {
            month++;
        }
        String monthString = String.valueOf(month);
        String year = String.valueOf(calendar.get(Calendar.YEAR));

        return day + '-' + monthString + '-' + year;
    }
}
