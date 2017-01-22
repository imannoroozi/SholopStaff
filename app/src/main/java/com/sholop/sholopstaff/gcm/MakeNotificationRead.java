package com.sholop.sholopstaff.gcm;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

import com.sholop.sholopstaff.config.AppConfig;
import com.sholop.sholopstaff.config.AppController;
import com.sholop.sholopstaff.utilities.Util;

/**
 * Created by Pooyan on 9/22/2016.
 */
public class MakeNotificationRead {

    Context context;
    int notifID;
    boolean read;

    public MakeNotificationRead( Context ctx, int notifID, boolean read){
        this.notifID = notifID;
        context = ctx;
        this.read = read;
    }

    public void clearNotification() {

        String tag_string_req = "appointment_req";
        String action_URL = "";

        action_URL = AppConfig.URL_READ_NOTIFICATIONS;

        StringRequest strReq = new StringRequest(Request.Method.POST,
                action_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //show the error
                if (error instanceof com.android.volley.TimeoutError) {
                } else {
                    //show the error
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to url
                HashMap<String, String> data = new HashMap<>();
                data.put("action", "CLEAR_NOTIFICATION");
                data.put("notification_id", String.valueOf(notifID));
                data.put("read", read ? "Y" : "N");
                return data;
            }
        };
        if( Util.getInstance().isNetworkAvailable(context)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }
}
