package com.example.tubes_02;

import androidx.annotation.Nullable;

public class Pertemuan {
    private String id;
    private String title;
    private String description;
    private String startDateTime;
    private String endDateTime;

    public Pertemuan(String id, String title, String startDateTime, String endDateTime,
                     @Nullable String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDateTime() {
        return startDateTime;
    }

    public String getEndDateTime() {
        return endDateTime;
    }

    public String getPertemuanStartStr(){
        String monthStr = IFPortalDateTimeFormatter.getMonthStringFromInt(
                IFPortalDateTimeFormatter.getMonthFromDateTime(startDateTime));
        int day = IFPortalDateTimeFormatter.getDayFromDateTime(startDateTime);
        int year = IFPortalDateTimeFormatter.getYearFromDateTime(startDateTime);
        String pertemuanTime = String.format("%2d %s %d", day, monthStr, year);
        return pertemuanTime;
    }
}
