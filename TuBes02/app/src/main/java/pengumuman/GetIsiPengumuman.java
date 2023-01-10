package pengumuman;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import pengumuman.model.Pengumuman;

public class GetIsiPengumuman {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/announcements";
    private final Context context;
    private final Gson gson;
    private PengumumanPresenter presenter;
    private Pengumuman pengumuman;

    public GetIsiPengumuman(Context context, PengumumanPresenter presenter, Pengumuman pengumuman){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
        this.pengumuman = pengumuman;
    }

    public void execute(){
        try{
            callVolley();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callVolley() {
        RequestQueue request = Volley.newRequestQueue(this.context);

        String query = BASE_URL + "/" +this.pengumuman.getId();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, query, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject objResponse = new JSONObject(response);
                    String content = objResponse.getString("content");
                    pengumuman.setContent(content);
                    presenter.sendPengumuman(pengumuman);
//                    Log.d("isi konten",content);
                } catch(JSONException ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Log.d("errorBang", error.toString());
                try {
                    String body = new String(error.networkResponse.data,"UTF-8");
                    Log.d("bodyErrorResponse",body);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }){
            @Override
            public Map<String,String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Authorization", "Bearer " + presenter.user.getToken());
                return headers;
            }
        };
        request.add(stringRequest);
    }
}
