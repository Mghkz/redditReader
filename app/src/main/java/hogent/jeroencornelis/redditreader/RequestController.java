package hogent.jeroencornelis.redditreader;

import android.content.Context;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

/**
 * Created by Jeroen-Lenovo on 27/11/2015.
 */
public class RequestController {
    Context mContext;
    RequestQueue queue;
    String text;
    String url ="http://www.google.com";

    public RequestController(Context mContext) {
        this.mContext = mContext;
        queue = Volley.newRequestQueue(this.mContext);
    }



    // Request a string response from the provided URL.
    private StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    // Display the first 500 characters of the response string.
                    text = ("Response is: "+ response.substring(0,500));
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            text = ("That didn't work!");
        }
    });

    public String getRequest(){
        queue.add(stringRequest);
        return text;

    }
}
