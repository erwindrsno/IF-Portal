package frs;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Users.User;

public class getAcademicYear {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/academic-years";
    private final Context context;
    private final Gson gson;
    private User user;
    private FRSPresenter presenter;
    private int academic_year_active;

    public getAcademicYear(Context context, FRSPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void execute(User user){
        try{
            this.user = user;
            Log.d("url students", BASE_URL);
            JSONObject json = new JSONObject(this.gson.toJson(user));
            Log.d("printJSON", json.toString(4));
            callVolley();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callVolley() {
        RequestQueue request = Volley.newRequestQueue(this.context);
        JsonObjectRequest jsonRequest = new JsonObjectRequest(BASE_URL, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("token get info post", user.getToken());
                Log.d("Respon API", request.toString());
                String res = response.toString();
                Log.d("activeyearBoss", res);
                try {
                    int active = response.getInt("active_year");
                    Log.d("activeYearNih", active+"");
                    presenter.setActive_year(active);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorBang", error.toString());
//                presenter.authFailed();
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                String auth = "Bearer " + presenter.getUserToken();
                headers.put("Authorization", auth);
                Log.d("bearer", auth);
                return headers;
            }
        };

        jsonRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 55000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 55000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {

            }
        });
        request.add(jsonRequest);
    }
}