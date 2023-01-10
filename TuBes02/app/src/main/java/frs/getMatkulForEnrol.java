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

public class getMatkulForEnrol {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/courses?";
    private final Context context;
    private final Gson gson;
    private User user;
    private FRSPresenter presenter;
    private String search;

    public getMatkulForEnrol(Context context, FRSPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void executeAwal(User user){
        try{
            this.user = user;
            this.BASE_URL+="limit=10";
            Log.d("url getMatkulEnrol", BASE_URL);
            JSONObject json = new JSONObject(this.gson.toJson(user));
            Log.d("printJSON", json.toString(4));
            callVolley1();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVolley1() {
        RequestQueue request = Volley.newRequestQueue(this.context);
        JsonArrayRequest jsonRequest = new JsonArrayRequest(BASE_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("token get info get", user.getToken());
                Log.d("Respon API", request.toString());
                try {
                    for(int idx = 0; idx< response.length(); idx++){
                        JSONObject tempJ = response.getJSONObject(idx);
                        String id = tempJ.getString("id");
                        int semester = tempJ.getInt("semester");
                        String code = tempJ.getString("code");
                        String name = tempJ.getString("name");
                        MataKuliahEnrol matkulTemp = new MataKuliahEnrol(id, code, name, semester);
                        presenter.addMatkulE(matkulTemp);
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

    public void executeSearch(User user, String search){
        try{
            this.user = user;
            this.search = search;
            this.BASE_URL+="filter[name]="+search+"&limit=10";
            Log.d("url getMatkulEnrol", BASE_URL);
            JSONObject json = new JSONObject(this.gson.toJson(user));
            Log.d("printJSON", json.toString(4));
            callVolley();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callVolley() {
        RequestQueue request = Volley.newRequestQueue(this.context);
        JsonArrayRequest jsonRequest = new JsonArrayRequest(BASE_URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("token get info get", user.getToken());
                Log.d("Respon API", request.toString());
                try {
                    for(int idx = 0; idx< response.length(); idx++){
                        JSONObject tempJ = response.getJSONObject(idx);
                        String id = tempJ.getString("id");
                        int semester = tempJ.getInt("semester");
                        String code = tempJ.getString("code");
                        String name = tempJ.getString("name");
                        MataKuliahEnrol matkulTemp = new MataKuliahEnrol(id, code, name, semester);
                        presenter.addMatkulE(matkulTemp);
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
