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

public class GetPengumuman {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/announcements";
    private final Context context;
    private final Gson gson;
    private PengumumanPresenter presenter;
    private String page;

    public GetPengumuman(Context context, PengumumanPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public GetPengumuman(Context context, PengumumanPresenter presenter, String page){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
        this.page = page;
        this.BASE_URL = this.BASE_URL + "?cursor=" + page + "&limit=5";
        Log.d("url",this.BASE_URL);
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

        StringRequest stringRequest = new StringRequest(Request.Method.GET, BASE_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject objResponse;
                JSONArray arrData = new JSONArray();
                try{
                    objResponse = new JSONObject(response);
                    String nextPage = objResponse.getJSONObject("metadata").getString("next");
                    arrData = objResponse.getJSONArray("data");
                    ArrayList<Pengumuman> arrPengumuman = new ArrayList<>();
                    for (int i = 0; i < arrData.length(); i++) {
                        Pengumuman pengumuman = gson.fromJson(arrData.getString(i), Pengumuman.class);
                        arrPengumuman.add(pengumuman);
                    }
                    if(!nextPage.equals(null)){
                        presenter.addNextPage(nextPage);
                        Log.d("masuk pindah page",true+"");
                    }
                    else{
                        presenter.setNextPageFalse(false);
                    }
                    presenter.getListFromAPI(arrPengumuman);
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
