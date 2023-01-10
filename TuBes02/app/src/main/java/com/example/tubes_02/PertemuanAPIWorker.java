package com.example.tubes_02;

import android.net.Uri;
import android.util.Log;

import com.android.volley.*;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PertemuanAPIWorker {
    final String BASE_URL = "https://ifportal.labftis.net/api/v1/";
    final String APPOINTMENTS_DIR = "appointments";
    final String USERS_DIR = "users";
    final String STT_DATE_DIR = "start-date";
    final String END_DATE_DIR = "end-date";
    final String ROLE_FILTER = "filter[roles][]";
    final String ERROR_APMNT_OVERLAP = "E_OVERLAPPING_SCHEDULE";
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

    public void getUsers(String role) throws JSONException, MalformedURLException {

        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(USERS_DIR)
                .appendQueryParameter(ROLE_FILTER, role)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonArrayRequest jsonRequest = new JsonArrayRequest(requestURL.toString(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        onUsersResult(response, role);
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
                String auth = "Bearer " + presenter.getUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        this.q.add(jsonRequest);
    }

    private void onUsersResult(JSONArray array, String requestedRole) {
        JSONArray resultArr = array;
        ArrayList<OtherUser> users = new ArrayList<>();
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
        this.presenter.setUsersList(users, requestedRole);
    }

    public void createAppointment(String title, String startDateTime, String endDateTime,
                                  String description, ArrayList<String> idUndangan)
            throws JSONException, MalformedURLException {
        JSONObject jsonBody = new JSONObject();
        jsonBody.put("title", title);
        jsonBody.put("start_datetime", startDateTime);
        jsonBody.put("end_datetime", endDateTime);
        jsonBody.put("description", description);
        Uri URI = Uri.parse(BASE_URL).buildUpon()
                .appendPath(APPOINTMENTS_DIR)
                .build();
        URL requestURL = new URL(URI.toString());

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                requestURL.toString(), jsonBody, this::onCreateAppointmentResult,
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
                                    if(errcode.equals(ERROR_APMNT_OVERLAP)){
                                        msg = "Jadwal tumpang tindih dengan jadwal Anda yang lain.";
                                    }
                                    presenter.showErrorMsg(title, msg);
                                } else if (code/100 == 5){
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
                String auth = "Bearer " + presenter.getUserToken();
                headers.put("Authorization", auth);
                return headers;
            }
        };

        q.add(request);
    }

    private void onCreateAppointmentResult(JSONObject object) {
        Log.d("debug", object.toString());
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
                String auth = "Bearer " + presenter.getUserToken();
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
                String startDateTime = pertemuanObj.optString("start_datetime");
                String endDateTime = pertemuanObj.optString("end_datetime");
                Pertemuan p = new Pertemuan(id, title, startDateTime, endDateTime, desc);
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
                String auth = "Bearer " + presenter.getUserToken();
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
        this.presenter.updateDescriptionDetail(description);
    }
}
