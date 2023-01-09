package com.example.tubes_02;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;

import Users.*;

public class PertemuanPresenter {
    private final PertemuanUI ui;
    private final User user;
    private final PertemuanAPIWorker apiWorker;
    private ArrayList<OtherUser> studentsList;
    private ArrayList<OtherUser> lecturersList;
    private ArrayList<Pertemuan> pertemuanList;

    public PertemuanPresenter(PertemuanUI ui, User user) {
        this.ui = ui;
//        this.user = user;
        this.user = new Dosen(null, null, null);
        this.user.setToken("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjp7InVzZXJfaWQiOiIxNDkzZTMzZC0zMjJhLTQ5NDktODc5YS04MmM2MTk4ODIyMmEiLCJyb2xlIjoic3R1ZGVudCJ9LCJpYXQiOjE2NzMwODEzMzl9.tKaaEsEf0_ZdE81wTGR1eL5vy6j0qKmQ-SfN2EZQPgE");
        this.studentsList = new ArrayList<>();
        this.lecturersList = new ArrayList<>();
        this.apiWorker = new PertemuanAPIWorker(this);
        this.pertemuanList = new ArrayList<>();
    }

    public Context getUIContext() {
        return this.ui.getUIContext();
    }

    public String getUserToken() {
        return this.user.getToken();
    }

    public void getUsersFromServer() {
        try {
            this.apiWorker.getUsers(OtherUser.ROLE_LECTURER);
            this.apiWorker.getUsers(OtherUser.ROLE_STUDENT);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setUsersList(ArrayList<OtherUser> users, String role) {
        switch (role) {
            case OtherUser.ROLE_LECTURER:
                lecturersList = users;
                break;
            case OtherUser.ROLE_STUDENT:
                studentsList = users;
                break;
        }
    }

    public void hlmTambahAddSpinner(String role) {
        this.ui.fragmentTambahAddSpinner(role);
    }

    public ArrayList<OtherUser> getMahasiswaList() {
        return this.studentsList;
    }

    public ArrayList<OtherUser> getDosenList() {
        return this.lecturersList;
    }

    public ArrayList<OtherUser> getAllUsersList() {
        ArrayList<OtherUser> allUsers = new ArrayList<>();
        allUsers.addAll(studentsList);
        allUsers.addAll(lecturersList);
        return allUsers;
    }

    public void showTambahPartisipanDialog() {
        this.ui.showDialog();
    }

    public String getUserId(String role, int pos) {
        switch (role) {
            case OtherUser.ROLE_LECTURER:
                return this.lecturersList.get(pos).getId();
            case OtherUser.ROLE_STUDENT:
                return this.studentsList.get(pos).getId();
            default:
                return null;
        }
    }

    public void createAppointment(String judul, String tanggal, String jamMulai, String jamSelesai,
                                  String deskripsi, @Nullable ArrayList<String> idUndangan) {
        int startDay = IFPortalDateTimeFormatter.getDayFromDate(tanggal);
        int startMonth = IFPortalDateTimeFormatter.getMonthFromDate(tanggal);
        int startYear = IFPortalDateTimeFormatter.getYearFromDate(tanggal);

        String startDateTime = IFPortalDateTimeFormatter.formatDateTime(startYear, startMonth,
                startDay, IFPortalDateTimeFormatter.getHourFromTime(jamMulai),
                IFPortalDateTimeFormatter.getMinuteFromTime(jamMulai),
                IFPortalDateTimeFormatter.GMT_OFFSET_JAKARTA);
        String endDateTime = IFPortalDateTimeFormatter.formatDateTime(startYear, startMonth,
                startDay, IFPortalDateTimeFormatter.getHourFromTime(jamSelesai),
                IFPortalDateTimeFormatter.getMinuteFromTime(jamSelesai),
                IFPortalDateTimeFormatter.GMT_OFFSET_JAKARTA);

        if (deskripsi.length() < 1) deskripsi = null;

        try {
            this.apiWorker.createAppointment(judul, startDateTime, endDateTime, deskripsi, idUndangan);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void showErrorMsg(String title, String msg) {
        this.ui.showErrorMsg(title, msg);
    }

    public ArrayList<Pertemuan> getPertemuanList() {
        return this.pertemuanList;
    }

    public String getPertemuanTitle(int idx) {
        return this.pertemuanList.get(idx).getTitle();
    }

    public String getPertemuanTime(int idx) {
        return this.pertemuanList.get(idx).getPertemuanStartStr();
    }

    public void getThisWeekScheduleFromServer() {
        Calendar thisWeek = Calendar.getInstance();
        thisWeek.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        int currYear = thisWeek.get(Calendar.YEAR);
        int currMonth = thisWeek.get(Calendar.MONTH) + 1;
        int mondayDate = thisWeek.get(Calendar.DAY_OF_MONTH);
        String startDate = IFPortalDateTimeFormatter.formatDate(currYear, currMonth, mondayDate);
        String endDate = IFPortalDateTimeFormatter.formatDate(currYear, currMonth, mondayDate + 6);
        try {
            this.apiWorker.getAppointments(startDate, endDate);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void setPertemuanList(ArrayList<Pertemuan> pertemuanList) {
        this.pertemuanList = pertemuanList;
        this.ui.updatePertemuanList(pertemuanList);
    }

    public void getPertemuanDetailsFromServer(String id) {
        try {
            this.apiWorker.getAppointmentDetail(id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void updateTitleDetail(String title) {
        this.ui.updateTitleDetail(title);
    }

    public void updateOrganizerDetail(String organizer) {
        this.ui.updateOrganizerDetail(organizer);
    }

    public void updateTimeDetail(String startDateTime, String endDateTime) {
        String monthStr = IFPortalDateTimeFormatter.getMonthStringFromInt(
                IFPortalDateTimeFormatter.getMonthFromDateTime(startDateTime));
        int day = IFPortalDateTimeFormatter.getDayFromDateTime(startDateTime);
        int year = IFPortalDateTimeFormatter.getYearFromDateTime(startDateTime);
        String pertemuanDate = String.format("%2d %s %d", day, monthStr, year);
        
        String pertemuanStartTime = startDateTime.substring(11, 16);
        String pertemuanEndTime = endDateTime.substring(11, 16);
        this.ui.updateTimeDetail(pertemuanDate, pertemuanStartTime, pertemuanEndTime);
    }

    public void updateDescriptionDetail(String description) {
        if (description == null) description ="";
        this.ui.updateDescDetail(description);
    }

    public String getPertemuanId(int i) {
        return this.pertemuanList.get(i).getId();
    }


    // =============================================================================================
    // Interface untuk UI
    public interface PertemuanUI {
        Context getUIContext();

        void fragmentTambahAddSpinner(String role);

        void showDialog();

        void showErrorMsg(String title, String msg);

        void updatePertemuanList(ArrayList<Pertemuan> pertemuanList);

        void updateTitleDetail(String title);

        void updateOrganizerDetail(String organizer);
        
        void updateTimeDetail(String pertemuanDate, String pertemuanStartTime, String pertemuanEndTime);

        void updateDescDetail(String description);
    }
}
