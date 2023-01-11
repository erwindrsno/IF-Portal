package frs;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

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

public class deleteEnrolMatkul {
    private String BASE_URL = "https://ifportal.labftis.net/api/v1/enrolments/";
    private final Context context;
    private final Gson gson;
    private User user;
    private FRSPresenter presenter;
    private String id;
    private int academic_year;

    public deleteEnrolMatkul(Context context, FRSPresenter presenter){
        this.context = context;
        this.presenter = presenter;
        this.gson = new Gson();
    }

    public void execute(User user, String id, int academic_year){
        try{
            this.user = user;
            this.id = id;
            this.academic_year = academic_year;
            this.BASE_URL+=id+"/academic-years/"+academic_year;;
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

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, BASE_URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("respon api enroll", response.toString());

                Context context = presenter.context;
                CharSequence text = "Berhasil Hapus Enrol Matakuliah!";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                presenter.setMatkulSemestertoAdapter(academic_year);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorBang",error.toString());
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
