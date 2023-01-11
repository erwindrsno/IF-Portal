package frs;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import Users.User;

public class getMatkulSemester {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/enrolments/academic-years/";
    private final Context context;
    private final Gson gson;
    private User user;
    private FRSPresenter presenter;
    private int academic_year;

    public getMatkulSemester(Context context, FRSPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void execute(User user, int academic_year){
        try{
            this.user = user;
            Log.d("academic_year api", academic_year+"");
            this.BASE_URL+=academic_year;
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
        JsonArrayRequest jsonRequest = new JsonArrayRequest(BASE_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("token get info post", user.getToken());
                Log.d("Respon API", request.toString());
                try {
                    for(int idx = 0; idx< response.length(); idx++){
                        JSONObject tempJ = response.getJSONObject(idx);
                        String course_id = tempJ.getString("course_id");
                        int academic_year = tempJ.getInt("academic_year");
                        String score = tempJ.getString("score");
                        String code = tempJ.getString("code");
                        String name = tempJ.getString("name");
                        MataKuliah matkulTemp = new MataKuliah(course_id,code,name,academic_year,score);
                        presenter.addMatkul(matkulTemp);
                    }
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
