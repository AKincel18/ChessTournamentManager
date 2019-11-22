package com.example.adam.chesstournamentmanager.staticdata;

import java.util.Calendar;
import java.util.Date;

public class FormatDateToString {

    public static String parseDateFromDatabase(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return parseDateFromDatePicker((calendar.get(Calendar.YEAR)),
                (calendar.get(Calendar.MONTH)),
                (calendar.get(Calendar.DAY_OF_MONTH)));
    }

    public static String parseDateFromDatePicker(int year, int month, int day) {
        month++; //begin month value is 0
        String monthString = (month >= 10) ? String.valueOf(month) : Constans.ZERO + month;
        String dayString = (day >= 10) ? String.valueOf(day) : Constans.ZERO + day;
        return dayString + Constans.DASH + monthString + Constans.DASH + year;
    }

}
