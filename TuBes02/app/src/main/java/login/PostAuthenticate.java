package login;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

import Users.User;

public class PostAuthenticate{
    final String BASE_URL = "https://ifportal.labftis.net/api/v1/authenticate";
    private final Context context;
    private final Gson gson;
    private User user;
    private LoginPresenter presenter;

    public PostAuthenticate(Context context, LoginPresenter presenter){
        this.context = context;
        this.gson = new Gson();
        this.presenter = presenter;
    }

    public void execute(User user){
        try{
            this.user = user;
            JSONObject json = new JSONObject(this.gson.toJson(user));
            Log.d("printJSON", json.toString(4));
            callVolley(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void callVolley(JSONObject json) {
        RequestQueue request = Volley.newRequestQueue(this.context);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, BASE_URL, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                presenter.setUserToken(response.toString().substring(10,response.toString().length() - 2));
                presenter.authSuccess();
                Log.d("token user post", user.getToken());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("errorBang",error.toString());
//                try {
//                    String body = new String(error.networkResponse.data, "utf-8");
//                    Log.d("errorBang", body);
//                } catch (UnsupportedEncodingException e) {
//                    e.printStackTrace();
//                }
//                Log.d("errorBang", body.substring(12,body.length()-2));
//                String body1 = body.substring(12,body.length()-2);
//                if(body1.equalsIgnoreCase("E_AUTH_FAILED")){
//                    presenter.authFailed();
//                }
//                else{
//                    presenter.timeOutError();
//                }
                if(error.toString().equalsIgnoreCase("com.android.volley.ClientError")){
                    presenter.authFailed();
                }
                else if(error.toString().equalsIgnoreCase("com.android.volley.TimeOutError")){
                    presenter.timeOutError();
                }
            }
        });
//        jsonObjectRequest.setRetryPolicy(new RetryPolicy() {
//            @Override
//            public int getCurrentTimeout() {
//                return 55000;
//            }
//
//            @Override
//            public int getCurrentRetryCount() {
//                return 55000;
//            }
//
//            @Override
//            public void retry(VolleyError error) throws VolleyError {
//
//            }
//        });
        request.add(jsonObjectRequest);
    }
}