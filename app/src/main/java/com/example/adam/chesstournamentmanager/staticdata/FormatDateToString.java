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
        String monthString = (month >= 10) ? String.valueOf(month) : Constants.ZERO + month;
        String dayString = (day >= 10) ? String.valueOf(day) : Constants.ZERO + day;
        return dayString + Constants.DASH + monthString + Constants.DASH + year;
    }

}