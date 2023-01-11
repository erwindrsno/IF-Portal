package frs;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Users.User;

public class getSemester {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/students/email/";
    private final Context context;
    private final Gson gson;
    private User user;
    private FRSPresenter presenter;

    public getSemester(Context context, FRSPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void execute(User user){
        try{
            this.user = user;
            String emailTemp = user.getEmail();
            this.BASE_URL+=emailTemp+"";
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
        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("token get info post", user.getToken());
                Log.d("Respon API", request.toString());
                Log.d("response dari API", response);
                int len = response.length();
                String a = response.substring(len-6, len-1);
                int initial_year = Integer.parseInt(a);
                Log.d("initial year", initial_year+"");
                presenter.setInitialYear(initial_year);
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

        stringRequest.setRetryPolicy(new RetryPolicy() {
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
        request.add(stringRequest);
    }
}
