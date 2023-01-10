package com.example.tubes_02;

import android.util.Log;

class IFPortalDateTimeFormatter {
    public static final String GMT_OFFSET_JAKARTA = "+0700";
    private static final String dateTimeFormat = "%04d-%02d-%02d %02d:%02d%s";
    private static final String dateFormat = "%04d-%02d-%02d";
    private static final String timeFormatZ = "%02d:%02d%s";
    private static final String timeFormat = "%02d:%02d";
    private static final String[] MONTHS_IDN = new String[]{"Januari", "Februari", "Maret", "April",
            "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};

    public static String formatDateTime(int year, int month, int day, int hour, int minute,
                                 String zoneOffset){
        return String.format(dateTimeFormat, year, month, day, hour, minute, zoneOffset);
    }

    public static String formatDate(int year, int month, int day){
        return String.format(dateFormat, year, month, day);
    }

    public static String formatTime(int hour, int minute){
        return String.format(timeFormat, hour, minute);
    }

    public static int getMinuteFromTime(String timeString){
        return Integer.parseInt(timeString.substring(timeString.lastIndexOf(':') + 1));
    }

    public static int getHourFromTime(String timeString){
        return Integer.parseInt(timeString.substring(0, timeString.lastIndexOf(':')));
    }

    public static int getYearFromDate(String dateString){
        return Integer.parseInt(dateString.substring(0, dateString.indexOf('-')));
    }

    public static int getMonthFromDate(String dateString){
        String monthStr = dateString.substring(dateString.indexOf('-') + 1,
                dateString.lastIndexOf('-'));
        return Integer.parseInt(monthStr);
    }

    public static String getMonthStringFromInt(int month){
        return MONTHS_IDN[month-1];
    }

    public static int getDayFromDate(String dateString){
        String dayStr = dateString.substring(dateString.lastIndexOf('-') + 1);
        return Integer.parseInt(dayStr);
    }

    public static int getMonthFromDateTime(String dateTime){
        return getMonthFromDate(dateTime);
    }

    public static int getDayFromDateTime(String dateTime){
        int startIdx = dateTime.lastIndexOf('-') + 1;
        String dayStr = dateTime.substring(startIdx, startIdx+2);
        return Integer.parseInt(dayStr);
    }

    public static int getYearFromDateTime(String dateTime){
        return getYearFromDate(dateTime);
    }

}
