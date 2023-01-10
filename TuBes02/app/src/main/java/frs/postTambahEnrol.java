package frs;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Users.User;

public class postTambahEnrol {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/enrolments";
    private final Context context;
    private final Gson gson;
    private User user;
    private FRSPresenter presenter;
    private String id;
    private int academic_year;

    public postTambahEnrol(Context context, FRSPresenter presenter){
        this.context = context;
        this.presenter = presenter;
        this.gson = new Gson();
    }

    public void execute(User user, String id, int academic_year){
        try{
            this.user = user;
            this.id = id;
            this.academic_year = academic_year;
            Log.d("url getMatkulEnrol", BASE_URL);
            JSONObject json = new JSONObject();
            json.put("course_id", id);
            json.put("academic_year", academic_year);
            Log.d("printJSONAdd", json.toString(4));
            callVolley(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVolley(JSONObject json) {
        RequestQueue request = Volley.newRequestQueue(this.context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("respon api enroll", response.toString());
                presenter.setMatkulSemestertoAdapter(academic_year);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorBang",error.toString());
//                if(error.toString().equalsIgnoreCase("com.android.volley.ClientError")){
//                    presenter.authFailed();
//                }
//                else if(error.toString().equalsIgnoreCase("com.android.volley.TimeOutError")){
//                    presenter.timeOutError();
//                }
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
        request.add(jsonObjectRequest);
    }
}
