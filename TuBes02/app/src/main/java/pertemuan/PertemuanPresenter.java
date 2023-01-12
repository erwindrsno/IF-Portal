package pertemuan;

import android.content.Context;

import androidx.annotation.Nullable;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import Users.*;

public class PertemuanPresenter {
    private final PertemuanUI ui;
    private final User user;
    private String userName;
    private String userId;
    private final PertemuanAPIWorker apiWorker;
    private static final String JOIN_DELIMITER = "\n";
    private ArrayList<OtherUser> studentsList;
    private ArrayList<OtherUser> lecturersList;
    private ArrayList<Pertemuan> pertemuanList;

    public PertemuanPresenter(PertemuanUI ui, User user) {
        this.ui = ui;
        this.user = user;
        this.studentsList = new ArrayList<>();
        this.lecturersList = new ArrayList<>();
        this.apiWorker = new PertemuanAPIWorker(this);
        this.pertemuanList = new ArrayList<>();
        this.getLoggedInUserIdFromServer();
    }

    private void getLoggedInUserIdFromServer() {
        try {
            this.apiWorker.getLoggedInUserIdAndName();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public boolean loggedInUserIsLecturer() {
        return this.user instanceof Dosen;
    }

    public Context getUIContext() {
        return this.ui.getUIContext();
    }

    public String getLoggedInUserToken() {
        return this.user.getToken();
    }

    public void getUsersFromServer() {
        try {
            this.apiWorker.getUsers(OtherUser.ROLE_LECTURER, null, 0);
            this.apiWorker.getUsers(OtherUser.ROLE_STUDENT, null, 0);
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

    public ArrayList<OtherUser> getStudentsList() {
        return this.studentsList;
    }

    public ArrayList<OtherUser> getLecturerList() {
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

    public void getThisWeekAppointmentsFromServer() {
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

    public void getInvitationsFromServer() {
        try {
            this.apiWorker.getInvitations();
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
        if (description == null) description = "";
        this.ui.updateDescDetail(description);
    }

    public String getPertemuanId(int i) {
        return this.pertemuanList.get(i).getId();
    }

    public void showPertemuanCreationSuccess() {
        String msg = "Pertemuan berhasil dibuat.";
        this.ui.showPertemuanSuccessMsg(msg);
    }

    public void showInvitationSuccess() {
        String msg = "Undangan berhasil dibuat.";
        this.ui.showPertemuanSuccessMsg(msg);
    }

    public void createInvitation(String idPertemuan, ArrayList<String> idUndangan) {
        try {
            this.apiWorker.addParticipants(idPertemuan, (idUndangan.size() > 0) ? idUndangan : null);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void postAcceptInvitation(String idUndangan) {
        try {
            this.apiWorker.postAcceptInvitation(idUndangan);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getLoggedInUserId() {
        return this.userId;
    }

    public void setLoggedInUserId(String id) {
        this.userId = id;
    }

    public void showInviteAcceptSuccess() {
        String msg = "Undangan berhasil diterima.";
        this.ui.showInvitationSuccessMsg(msg);
    }

    public void updateInviteeDetail(ArrayList<String> invitees,
                                    ArrayList<String> attendees) {
        String inviteesStr = String.join(JOIN_DELIMITER, invitees);
        String attendeesStr = String.join(JOIN_DELIMITER, attendees);
        this.ui.updateDetailFragmentInvitees(inviteesStr, attendeesStr);
    }

    public void removeParticipantField() {
        this.ui.removeParticipantDetail();
    }

    public void getParticipantsFromServer(String id) {
        try {
            this.apiWorker.getAppointmentParticipants(id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void getTimeslotsFromServer(String userId) {
        try {
            this.apiWorker.getTimeslots(userId);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void updateTimeSlots(ArrayList<TimeSlot> mondaySlots,
                                ArrayList<TimeSlot> tuesdaySlots,
                                ArrayList<TimeSlot> wednesdaySlots,
                                ArrayList<TimeSlot> thursdaySlots,
                                ArrayList<TimeSlot> fridaySlots,
                                ArrayList<TimeSlot> saturdaySlots,
                                ArrayList<TimeSlot> sundaySlots) {
        Collections.sort(mondaySlots);
        Collections.sort(tuesdaySlots);
        Collections.sort(wednesdaySlots);
        Collections.sort(thursdaySlots);
        Collections.sort(fridaySlots);
        Collections.sort(saturdaySlots);
        Collections.sort(sundaySlots);
        this.ui.updateTimeSlots(mondaySlots, tuesdaySlots, wednesdaySlots, thursdaySlots,
                fridaySlots, saturdaySlots, sundaySlots);
    }

    public String getLoggedInUserName() {
        return this.userName;
    }

    public void setLoggedInUserName(String name) {
        this.userName = name;
    }

    public void addTimeSlot(int dayIdx, String startTime, String endTime) {
        String day = IFPortalDateTimeFormatter.DAYS_ABBRV[dayIdx];
        String startTimeStr = IFPortalDateTimeFormatter.appendZone(startTime,
                IFPortalDateTimeFormatter.GMT_OFFSET_JAKARTA);
        String endTimeStr = IFPortalDateTimeFormatter.appendZone(endTime,
                IFPortalDateTimeFormatter.GMT_OFFSET_JAKARTA);
        try {
            this.apiWorker.postAddTimeSlot(day, startTimeStr, endTimeStr);
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public void showTimeSlotSuccess() {
        String msg = "Time Slot berhasil ditambahkan.";
        this.ui.showTimeSlotSuccessMsg(msg);
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

        void showPertemuanSuccessMsg(String msg);

        void showInvitationSuccessMsg(String msg);

        void updateDetailFragmentInvitees(String invitees, String attendees);

        void removeParticipantDetail();

        void updateTimeSlots(ArrayList<TimeSlot> mondaySlots, ArrayList<TimeSlot> tuesdaySlots,
                             ArrayList<TimeSlot> wednesdaySlots, ArrayList<TimeSlot> thursdaySlots,
                             ArrayList<TimeSlot> fridaySlots, ArrayList<TimeSlot> saturdaySlots,
                             ArrayList<TimeSlot> sundaySlots);

        void showTimeSlotSuccessMsg(String msg);
    }
}
