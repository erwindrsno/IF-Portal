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

import pengumuman.model.ListPengumuman;

public class GetPengumuman {
    final String BASE_URL = "https://ifportal.labftis.net/api/v1/announcements";
    private final Context context;
    private final Gson gson;
    private PengumumanPresenter presenter;

    public GetPengumuman(Context context, PengumumanPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void execute(){
        try{
            Log.d("masukTaskAnnouncements",true+"");
            Log.d("tokenpresenter",this.presenter.user.getToken());
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
                    arrData = objResponse.getJSONArray("data");
                    ArrayList<ListPengumuman> arrListPengumuman = new ArrayList<>();
//                    ListPengumuman pengumuman = gson.fromJson(arrData.getString(0), ListPengumuman.class);
//                    Log.d("tagPengumuman",pengumuman.getTags().get(0).getTag());
                    for (int i = 0; i < arrData.length(); i++) {
                        ListPengumuman listPengumuman = gson.fromJson(arrData.getString(i), ListPengumuman.class);
                        arrListPengumuman.add(listPengumuman);
                    }
                    Log.d("masukVolley",true+"");
                    presenter.getListFromAPI(arrListPengumuman);
//                    Log.d("printPengumuman",arrData.toString());
                } catch(JSONException ex){
                    ex.printStackTrace();
                }
//                Log.d("token user post", user.getToken());
//                JSONObject obj;
//                JSONArray arrData = new JSONArray();
//                JSONObject objData1 = new JSONObject();
//                JSONArray arrTag = new JSONArray();
//                JSONObject objTag = new JSONObject();
//                JSONObject tag2 = new JSONObject();
//                try{
//                    obj = new JSONObject(response);
//                    arrData = obj.getJSONArray("data");
//                    objData1 = arrData.getJSONObject(0);
//                    arrTag = objData1.getJSONArray("tags");
//                    tag2 = arrTag.getJSONObject(0);
//                    Log.d("ResponAPISatu",objData1.toString());
//                    Log.d("Respon API", tag2.toString());
//                } catch(Exception ex){
//                    ex.printStackTrace();
//                }
//                Pengumuman[] array_pengumuman = gson.fromJson(response,Pengumuman[].class);
//                ArrayList<Pengumuman> arrListPengumuman = (ArrayList<Pengumuman>) Arrays.asList(array_pengumuman);
//                presenter.setArrayListPengumuman(arrListPengumuman);
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
