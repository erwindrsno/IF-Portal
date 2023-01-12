package pertemuan;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.Nullable;

import com.android.volley.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PertemuanAPIWorker {
    final String BASE_URL = "https://ifportal.labftis.net/api/v1/";
    final String APPOINTMENTS_DIR = "appointments";
    final String INVITATIONS_DIR = "invitations";
    final String USERS_DIR = "users";
    final String SELF_DIR = "self";
    final String PARTICIPANTS_DIR = "participants";
    final String STT_DATE_DIR = "start-date";
    final String END_DATE_DIR = "end-date";
    final String TIMESLOTS_DIR = "lecturer-time-slots";
    final String LECTURERS_DIR = "lecturers";
    final String ROLE_FILTER = "filter[roles][]";
    final String LIMIT_PARAM = "limit";
    final String OFFSET_PARAM = "offset";
    final String ERROR_APMNT_OVERLAP = "E_OVERLAPPING_SCHEDULE";
    final String ERROR_EXIST_PREV = "E_EXIST";
    final String ERROR_NOT_EXIST = "E_NOT_EXIST";
    final String ERROR_UNAUTHORIZED = "E_UNAUTHORIZED";

    final String MONDAY = "mon";
    final String TUESDAY = "tue";
    final String WEDNESDAY = "wed";
    final String THURSDAY = "thu";
    final String FRIDAY = "fri";
    final String SATURDAY = "sat";
    final String SUNDAY = "sun";

    public static final String ROLE_STUDENT = "student";
    public static final String ROLE_LECTURER = "lecturer";
    private final PertemuanPresenter presenter;
    private final Gson gson;
    private final RequestQueue q;

    public PertemuanAPIWorker(PertemuanPresenter presenter) {
        this.presenter = presenter;
        this.gson = new Gson();
        this.q = Volley.newRequestQueue(this.presenter.getUIContext());
    }

    public void getUsers(String role, @Nullable ArrayList<OtherUser> users, int offset)
            throws JSONException, MalformedURLException {

        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon()
                .appendPath(USERS_DIR)
                .appendQueryParameter(ROLE_FILTER, role)
                .appendQueryParameter(LIMIT_PARAM, "" + 10);
        if (offset > 0) {
            builder.appendQueryParameter(OFFSET_PARAM, "" + offset);
        }
        Uri URI = builder.build();
        URL requestURL = new URL(URI.toString());

        JsonArrayRequest jsonRequest = new JsonArrayRequest(requestURL.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onUsersResult(response, role, users, offset);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        this.q.add(jsonRequest);
    }

    private void onUsersResult(JSONArray array, String requestedRole, ArrayList<OtherUser> users,
                               int offset) {
        JSONArray resultArr = array;
        if (users == null) users = new ArrayList<>();
        for (int i = 0; i < resultArr.length(); i++) {
            JSONObject userJson = resultArr.optJSONObject(i);
            if (userJson != null) {
                String id = userJson.optString("id");
                String name = userJson.optString("name");
                String email = userJson.optString("email");
                JSONArray rolesJson = userJson.optJSONArray("roles");
                ArrayList<String> roles = new ArrayList<>();
                if (rolesJson != null) {
                    for (int j = 0; j < rolesJson.length(); j++) {
                        String role = rolesJson.optString(j);
                        roles.add(role);
                    }
                }
                OtherUser user = new OtherUser(id, name, email, roles);
                users.add(user);
            }
        }
        if (resultArr.length() >= 10) {
            try {
                getUsers(requestedRole, users, offset + 10);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        } else {
            this.presenter.setUsersList(users, requestedRole);
        }
    }

    public void createAppointment(String title, String startDateTime, String endDateTime,
                                  @Nullable String description,
                                  @Nullable ArrayList<String> idUndangan)
            throws JSONException, MalformedURLException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("title", title);
        jsonBody.put("start_datetime", startDateTime);
        jsonBody.put("end_datetime", endDateTime);
        if (description != null) jsonBody.put("description", description);
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(APPOINTMENTS_DIR)
                .build();
        URL requestURL = new URL(URI.toString());

        Log.d("debug", ""+idUndangan.size());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestURL.toString(), jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String idPertemuan = getAppointmentId(response);
                        if (idUndangan != null && idUndangan.size() > 0) {
                            presenter.createInvitation(idPertemuan, idUndangan);
                        } else {
                            presenter.showPertemuanCreationSuccess();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            String jsonErrorStr = new String(error.networkResponse.data);
                            try {
                                JSONObject jsonError = new JSONObject(jsonErrorStr);
                                int code = error.networkResponse.statusCode;
                                if (code / 100 == 4) {
                                    String title = "Client Error";
                                    String errcode = jsonError.optString("errcode");
                                    String msg = "Terdapat kesalahan input";
                                    if (errcode.equals(ERROR_APMNT_OVERLAP)) {
                                        msg = "Jadwal tumpang tindih dengan jadwal Anda yang lain.";
                                    }
                                    presenter.showErrorMsg(title, msg);
                                } else if (code / 100 == 5) {
                                    String title = "Server Error";
                                    presenter.showErrorMsg(title, "Request gagal dibuat karena" +
                                            " kendala pada server.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        q.add(request);
    }

    public void addParticipants(String idPertemuan, ArrayList<String> idUndangan)
            throws JSONException, MalformedURLException {
        JSONObject jsonBody = new JSONObject();
        JSONArray participantIds = new JSONArray(idUndangan);
        jsonBody.put("appointment_id", idPertemuan);
        jsonBody.put("participants", participantIds);

        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(APPOINTMENTS_DIR).appendPath(idPertemuan)
                .appendPath(PARTICIPANTS_DIR)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonArrayRequestWithJsonBody jsonRequest = new JsonArrayRequestWithJsonBody(Request.Method.POST,
                requestURL.toString(),
                jsonBody,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        presenter.showInvitationSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            String jsonErrorStr = new String(error.networkResponse.data);
                            try {
                                JSONObject jsonError = new JSONObject(jsonErrorStr);
                                int code = error.networkResponse.statusCode;
                                if (code / 100 == 4) {
                                    String title = "Client Error";
                                    String errcode = jsonError.optString("errcode");
                                    String msg = "Terdapat kesalahan input";
                                    if (errcode.equals(ERROR_EXIST_PREV)) {
                                        msg = "Peserta sudah pernah diundang.";
                                    } else if (errcode.equals(ERROR_NOT_EXIST)) {
                                        msg = "Pertemuan atau peserta tidak ditemukan.";
                                    }
                                    presenter.showErrorMsg(title, msg);
                                } else if (code / 100 == 5) {
                                    String title = "Server Error";
                                    presenter.showErrorMsg(title, "Request gagal dibuat karena" +
                                            " kendala pada server.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        this.q.add(jsonRequest);

    }

    private String getAppointmentId(JSONObject object) {
        return object.optString("id");
    }

    public void getAppointments(String startDate, String endDate) throws MalformedURLException {
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(APPOINTMENTS_DIR)
                .appendPath(STT_DATE_DIR).appendPath(startDate)
                .appendPath(END_DATE_DIR).appendPath(endDate)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonArrayRequest jsonRequest = new JsonArrayRequest(requestURL.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onAppointmentsResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        this.q.add(jsonRequest);
    }

    private void onAppointmentsResult(JSONArray response) {
        JSONArray resultArr = response;

        ArrayList<Pertemuan> pertemuanList = new ArrayList<Pertemuan>();
        for (int i = 0; i < resultArr.length(); i++) {
            JSONObject pertemuanObj = resultArr.optJSONObject(i);
            if (pertemuanObj != null) {
                String id = pertemuanObj.optString("id");
                String title = pertemuanObj.optString("title");
                String desc = pertemuanObj.optString("desc");
                if (desc.equals("null")) desc = null;
                String startDateTime = pertemuanObj.optString("start_datetime");
                String endDateTime = pertemuanObj.optString("end_datetime");
                Pertemuan p = new Pertemuan(id, title, startDateTime, endDateTime, desc);
                pertemuanList.add(p);
            }
        }
        this.presenter.setPertemuanList(pertemuanList);
    }

    public void getInvitations() throws MalformedURLException {
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(APPOINTMENTS_DIR)
                .appendPath(INVITATIONS_DIR)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonArrayRequest jsonRequest = new JsonArrayRequest(requestURL.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onInvitationsResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        this.q.add(jsonRequest);
    }

    private void onInvitationsResult(JSONArray response) {
        JSONArray resultArr = response;

        ArrayList<Pertemuan> pertemuanList = new ArrayList<Pertemuan>();
        for (int i = 0; i < resultArr.length(); i++) {
            JSONObject pertemuanObj = resultArr.optJSONObject(i);
            if (pertemuanObj != null) {
                String id = pertemuanObj.optString("appointment_id");
                String title = pertemuanObj.optString("title");
                String desc = pertemuanObj.optString("desc");
                if (desc.equals("null")) desc = null;
                String startDateTime = pertemuanObj.optString("start_datetime");
                String endDateTime = pertemuanObj.optString("end_datetime");
                boolean isAttending = pertemuanObj.optBoolean("attending");
                Pertemuan p = new UndanganPertemuan(id, title, startDateTime, endDateTime, desc,
                        isAttending);
                pertemuanList.add(p);
            }
        }
        this.presenter.setPertemuanList(pertemuanList);
    }

    public void getAppointmentDetail(String id) throws MalformedURLException {
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(APPOINTMENTS_DIR).appendPath(id)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestURL.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        onAppointmentDetailResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        this.q.add(jsonRequest);
    }

    private void onAppointmentDetailResult(JSONObject response) {
        String title = response.optString("title");
        this.presenter.updateTitleDetail(title);

        String organizer = response.optString("organizer_name");
        this.presenter.updateOrganizerDetail(organizer);

        String startDateTime = response.optString("start_datetime");
        String endDateTime = response.optString("end_datetime");
        this.presenter.updateTimeDetail(startDateTime, endDateTime);

        String description = response.optString("description");
        if (!description.equals("null")) this.presenter.updateDescriptionDetail(description);
    }

    public void postAcceptInvitation(String idUndangan) throws MalformedURLException, JSONException {
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(APPOINTMENTS_DIR).appendPath(idUndangan)
                .appendPath(PARTICIPANTS_DIR).appendPath(this.presenter.getLoggedInUserId())
                .build();
        URL requestURL = new URL(URI.toString());

        JSONObject jsonBody = new JSONObject();
        jsonBody.put("appointment_id", idUndangan);
        jsonBody.put("participant_id", this.presenter.getLoggedInUserId());
        jsonBody.put("attending", true);

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PATCH,
                requestURL.toString(), jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        presenter.showInviteAcceptSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            String jsonErrorStr = new String(error.networkResponse.data);
                            try {
                                JSONObject jsonError = new JSONObject(jsonErrorStr);
                                int code = error.networkResponse.statusCode;
                                if (code / 100 == 4) {
                                    String title = "Client Error";
                                    String errcode = jsonError.optString("errcode");
                                    String msg = "Terdapat kesalahan input";
                                    if (errcode.equals(ERROR_UNAUTHORIZED)) {
                                        msg = "Kredensial otentikasi tidak valid.";
                                    } else if (errcode.equals(ERROR_APMNT_OVERLAP)) {
                                        msg = "Jadwal pertemuan ini tumpang tindih dengan jadwal" +
                                                "Anda yang lain.";
                                    } else if (errcode.equals(ERROR_NOT_EXIST)) {
                                        msg = "Partisipan tidak ditemukan.";
                                    }
                                    presenter.showErrorMsg(title, msg);
                                } else if (code / 100 == 5) {
                                    String title = "Server Error";
                                    presenter.showErrorMsg(title, "Request gagal dibuat karena" +
                                            " kendala pada server.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        q.add(request);
    }

    public void getLoggedInUserIdAndName() throws MalformedURLException {
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(USERS_DIR).appendPath(SELF_DIR)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonObjectRequest jsonRequest = new JsonObjectRequest(requestURL.toString(),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        String id = response.optString("id");
                        String name = response.optString("name");
                        presenter.setLoggedInUserId(id);
                        presenter.setLoggedInUserName(name);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String jsonErrorStr = new String(error.networkResponse.data);
                        try {
                            JSONObject jsonError = new JSONObject(jsonErrorStr);
                            int code = error.networkResponse.statusCode;
                            if (code / 100 == 4) {
                                String title = "Client Error";
                                String errcode = jsonError.optString("errcode");
                                String msg = "Terdapat kesalahan input";
                                if (errcode.equals(ERROR_NOT_EXIST)) {
                                    msg = "User tidak ditemukan.";
                                }
                                presenter.showErrorMsg(title, msg);
                            } else if (code / 100 == 5) {
                                String title = "Server Error";
                                presenter.showErrorMsg(title, "Request gagal dibuat karena" +
                                        " kendala pada server.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        this.q.add(jsonRequest);
    }

    public void getAppointmentParticipants(String appointmentId) throws MalformedURLException {
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(APPOINTMENTS_DIR).appendPath(appointmentId)
                .appendPath(PARTICIPANTS_DIR)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonArrayRequest jsonRequest = new JsonArrayRequest(requestURL.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onAppointmenParticipantsResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String jsonErrorStr = new String(error.networkResponse.data);
                        try {
                            JSONObject jsonError = new JSONObject(jsonErrorStr);
                            int code = error.networkResponse.statusCode;
                            if (code / 100 == 4) {
                                String title = "Client Error";
                                String errcode = jsonError.optString("errcode");
                                Log.d("debug", errcode);
                                if (errcode.equals(ERROR_UNAUTHORIZED)) {
                                    presenter.removeParticipantField();
                                } else {
                                    String msg = "Terdapat kesalahan input";
                                    presenter.showErrorMsg(title, msg);
                                }
                            } else if (code / 100 == 5) {
                                String title = "Server Error";
                                presenter.showErrorMsg(title, "Request gagal dibuat karena" +
                                        " kendala pada server.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };
        this.q.add(jsonRequest);
    }

    private void onAppointmenParticipantsResult(JSONArray response) {
        ArrayList<String> invitees = new ArrayList<>();
        ArrayList<String> attendees = new ArrayList<>();
        for (int i = 0; i < response.length(); i++) {
            JSONObject invitee = response.optJSONObject(i);
            if (invitee != null) {
                String name = invitee.optString("name");
                invitees.add(name);
                if (invitee.optBoolean("attending")) attendees.add(name);
            }
        }
        this.presenter.updateInviteeDetail(invitees, attendees);
    }

    public void getTimeslots(String userId) throws MalformedURLException {
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TIMESLOTS_DIR).appendPath(LECTURERS_DIR).appendPath(userId)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonArrayRequest jsonRequest = new JsonArrayRequest(requestURL.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onTimeslotsResult(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        String jsonErrorStr = new String(error.networkResponse.data);
                        try {
                            JSONObject jsonError = new JSONObject(jsonErrorStr);
                            int code = error.networkResponse.statusCode;
                            if (code / 100 == 4) {
                                String title = "Client Error";
                                String errcode = jsonError.optString("errcode");
                                Log.d("debug", errcode);
                                if (errcode.equals(ERROR_UNAUTHORIZED)) {
                                    presenter.removeParticipantField();
                                } else {
                                    String msg = "Terdapat kesalahan input";
                                    presenter.showErrorMsg(title, msg);
                                }
                            } else if (code / 100 == 5) {
                                String title = "Server Error";
                                presenter.showErrorMsg(title, "Request gagal dibuat karena" +
                                        " kendala pada server.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };
        this.q.add(jsonRequest);
    }

    private void onTimeslotsResult(JSONArray response) {
        ArrayList<TimeSlot> mondaySlots = new ArrayList<>();
        ArrayList<TimeSlot> tuesdaySlots = new ArrayList<>();
        ArrayList<TimeSlot> wednesdaySlots = new ArrayList<>();
        ArrayList<TimeSlot> thursdaySlots = new ArrayList<>();
        ArrayList<TimeSlot> fridaySlots = new ArrayList<>();
        ArrayList<TimeSlot> saturdaySlots = new ArrayList<>();
        ArrayList<TimeSlot> sundaySlots = new ArrayList<>();

        for (int i = 0; i < response.length(); i++) {
            JSONObject timeSlotJson = response.optJSONObject(i);
            if (timeSlotJson != null) {
                String id = timeSlotJson.optString("id");
                String day = timeSlotJson.optString("day");
                String startTime = timeSlotJson.optString("start_time");
                String endTime = timeSlotJson.optString("end_time");
                TimeSlot ts = new TimeSlot(id, startTime, endTime, day);
                switch (day){
                    case MONDAY:
                        mondaySlots.add(ts);
                        break;
                    case TUESDAY:
                        tuesdaySlots.add(ts);
                        break;
                    case WEDNESDAY:
                        wednesdaySlots.add(ts);
                        break;
                    case THURSDAY:
                        thursdaySlots.add(ts);
                        break;
                    case FRIDAY:
                        fridaySlots.add(ts);
                        break;
                    case SATURDAY:
                        saturdaySlots.add(ts);
                        break;
                    case SUNDAY:
                        sundaySlots.add(ts);
                        break;
                }
            }
        }
        this.presenter.updateTimeSlots(mondaySlots, tuesdaySlots, wednesdaySlots, thursdaySlots,
                fridaySlots, saturdaySlots, sundaySlots);
    }

    public void postAddTimeSlot(String day, String startTime, String endTime)
            throws JSONException, MalformedURLException {
        JSONObject jsonBody = new JSONObject();

        jsonBody.put("day", day);
        jsonBody.put("start_time", startTime);
        jsonBody.put("end_time", endTime);

        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(TIMESLOTS_DIR)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestURL.toString(), jsonBody,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        presenter.showTimeSlotSuccess();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error != null) {
                            String jsonErrorStr = new String(error.networkResponse.data);
                            try {
                                JSONObject jsonError = new JSONObject(jsonErrorStr);
                                int code = error.networkResponse.statusCode;
                                if (code / 100 == 4) {
                                    String title = "Client Error";
                                    String errcode = jsonError.optString("errcode");
                                    String msg = "Terdapat kesalahan input";
                                    if (errcode.equals(ERROR_APMNT_OVERLAP)) {
                                        msg = "Jadwal tumpang tindih dengan jadwal Anda yang lain.";
                                    }
                                    presenter.showErrorMsg(title, msg);
                                } else if (code / 100 == 5) {
                                    String title = "Server Error";
                                    presenter.showErrorMsg(title, "Request gagal dibuat karena" +
                                            " kendala pada server.");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                String auth = "Bearer " + presenter.getLoggedInUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        q.add(request);
    }

    static class JsonArrayRequestWithJsonBody extends JsonRequest<JSONArray> {

        public JsonArrayRequestWithJsonBody(int method, String url,
                                            @Nullable JSONObject jsonRequest,
                                            Response.Listener<JSONArray> listener,
                                            Response.ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener,
                    errorListener);
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data,
                        HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONArray(jsonString),
                        HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException | JSONException e) {
                return Response.error(new ParseError(e));
            }
        }
    }
}
