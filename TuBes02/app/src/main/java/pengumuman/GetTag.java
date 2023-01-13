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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import pengumuman.model.Pengumuman;
import pengumuman.model.Tag;

public class GetTag {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/tags";
    private final Context context;
    private final Gson gson;
    private PengumumanPresenter presenter;
    private String page;

    public GetTag(Context context, PengumumanPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void execute(){
        Log.d("masukGetTag",true+"");
        try{
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
//                JSONObject objResponse;
                JSONArray arrTag;
                try{
//                    objResponse = new JSONObject(response);
                    arrTag = new JSONArray(response);
                    Log.d("objResponse",arrTag.getString(0));
                    ArrayList<Tag> arrayListTag = new ArrayList<>();
                    for (int i = 0; i < arrTag.length(); i++) {
                        Tag tag = gson.fromJson(arrTag.getString(i), Tag.class);
                        arrayListTag.add(tag);
                    }
                    presenter.sendTag(arrayListTag);
                } catch(JSONException ex){
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
