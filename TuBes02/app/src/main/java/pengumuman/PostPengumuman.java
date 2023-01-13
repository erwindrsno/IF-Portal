package pengumuman;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pengumuman.model.Pengumuman;
import pengumuman.model.Tag;

public class PostPengumuman {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/announcements";
    private final Context context;
    private final Gson gson;
    private PengumumanPresenter presenter;
    private String page;
    private Pengumuman pengumumanBaru;

    public PostPengumuman(Context context, PengumumanPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void execute(String title, String content, String tagId){
        try{
            this.pengumumanBaru = new Pengumuman(title,content,tagId);
            Log.d("psotjudul",title);
            Log.d("psotisi",content);
            JSONObject json = new JSONObject(this.gson.toJson(pengumumanBaru));
            Log.d("printJSON", json.toString(4));
//            callVolley(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callVolley(JSONObject json) {
        RequestQueue request = Volley.newRequestQueue(this.context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d("token user post", user.getToken());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorBang",error.toString());
            }
        });

        JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
//                Log.d("token user post", user.getToken());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorBang",error.toString());
            }
        });
        request.add(jsonObjectRequest);
    }
}
