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

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import Users.User;

public class GetPengumuman {
    final String BASE_URL = "https://ifportal.labftis.net/api/v1/announcements";
    private final Context context;
    private final Gson gson;
    private User user;
    private PengumumanPresenter presenter;

    public GetPengumuman(Context context, PengumumanPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void execute(User user){
        try{
            this.user = user;
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
                Log.d("token user post", user.getToken());
                Log.d("Respon API", request.toString());
                Pengumuman[] array_pengumuman = gson.fromJson(response,Pengumuman[].class);
                ArrayList<Pengumuman> arrListPengumuman = (ArrayList<Pengumuman>) Arrays.asList(array_pengumuman);
                presenter.setArrayListPengumuman(arrListPengumuman);
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
                headers.put("Authorization", user.getToken());
                return headers;
            }
        };
        request.add(stringRequest);
    }
}
