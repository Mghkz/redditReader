package hogent.jeroencornelis.redditreader;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;

import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Random;

/**
 * Created by Jeroen-Lenovo on 27/11/2015.
 */
public class SubredditService extends Service {
    // Binder given to clients
    private final IBinder mBinder = new LocalBinder();
    private String text;
    private StringBuffer buffer;


    public class LocalBinder extends Binder {
        SubredditService getService() {
            // Return this instance of LocalService so clients can call public methods
            return SubredditService.this;
        }
    }

    @Override
    public void onCreate() {
        buffer = new StringBuffer("");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }


    public String getJsonSubreddit(String rNaam) {
        String url = "https://www.reddit.com/r/" + rNaam + "/new.json";
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(url,null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            VolleyLog.v("Response:%n %s", response.toString(4));
                            text = response.toString();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });

        AppController.getInstance().addToRequestQueue(jsObjRequest);
        return text;
    }


}
