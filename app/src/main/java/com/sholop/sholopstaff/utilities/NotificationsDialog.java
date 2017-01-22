package com.sholop.sholopstaff.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.adapters.NotificationAdapter;
import com.sholop.sholopstaff.config.AppConfig;
import com.sholop.sholopstaff.config.AppController;
import com.sholop.sholopstaff.objects.Notification;

/**
 * Created by Pooyan on 10/26/2015.*/

public class NotificationsDialog extends Dialog{

    private enum ACTIONS {GET_NOTIFICATIONS}

    private Context context;
    private RecyclerView notificationsRecyclerView;
    NotificationAdapter notificationAdapter;

    private boolean loading = false;

    View notificationLoadingError, notificationLoading, noNotification;
    TextView notificationLoadingIcon, noNotificationTextView, notificationsHeaderBell, notificationsHeader;

    SessionManager session;

    ArrayList<Notification> notifications;

    private Typeface fontBold, font, awesome;

    private NotificationsDialog thisObject;


    public NotificationsDialog(Context context) {
        super(context);
        this.context = context;

        session = new SessionManager(context);

        fontBold = Util.getInstance().getFontBold(context);
        font = Util.getInstance().getFontRegular(context);
        awesome = Util.getInstance().getFontAwesome(context);

        notifications = new ArrayList<>();
        thisObject = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifications_dialog);

        notificationLoadingError = findViewById(R.id.notification_error_layout);
        notificationLoading = findViewById(R.id.notification_loading_layout);
        noNotification = findViewById(R.id.no_notification_layout);
        notificationLoadingIcon = (TextView) findViewById(R.id.notification_loading);
        noNotificationTextView = (TextView) findViewById(R.id.no_notifications);

        notificationsHeaderBell = (TextView) findViewById(R.id.notifications_header_bell);
        notificationsHeader = (TextView) findViewById(R.id.notification_header_text);
        notificationsHeaderBell.setTypeface(awesome);
        notificationsHeader.setTypeface(fontBold);

        notificationsRecyclerView = (RecyclerView) findViewById(R.id.notification_recycler_view);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        notificationsRecyclerView.setLayoutManager(mLayoutManager);
        notificationAdapter = new NotificationAdapter(context, notifications, this);
        notificationsRecyclerView.setAdapter(notificationAdapter);

        notificationLoadingIcon.setTypeface(awesome);

        Util.getInstance().rotateAnimate(notificationLoadingIcon, true, context);

        getNotifications();
    }

    private void getNotifications() {
        //post request
        HashMap<String, String> data = new HashMap<>();
        data.put("user_id", String.valueOf(session.getCurrentUserID()));
//		data.put("visitor_name", searchVisitors.getText().toString());
        postAction(ACTIONS.GET_NOTIFICATIONS, data);
    }

    private void postAction(final ACTIONS action , final HashMap<String,String> data) {

        String tag_string_req = "appointment_req";
        String action_URL = "";
        setUpLoading(true);

        switch (action){
            case GET_NOTIFICATIONS:
                action_URL = AppConfig.URL_READ_NOTIFICATIONS;
                break;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                action_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                setUpLoading(false);

                switch (action){

                    case GET_NOTIFICATIONS:
                        updateList(response);
                        break;
                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                setUpLoading(false);
                notificationLoadingError.setVisibility(View.VISIBLE);
                //show the error
                if (error instanceof com.android.volley.TimeoutError) {
                    Toast.makeText(context,
                            "Time out error!", Toast.LENGTH_LONG).show();
                } else {
                    //show the error
                    Toast.makeText(context,
                            error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to url
                data.put("action", action.name());
                return data;
            }
        };
        if( Util.getInstance().isNetworkAvailable(context)) {
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
        }
    }

    private void setUpLoading(boolean loading){
        this.loading = loading;
        if( loading ){
            notificationLoadingError.setVisibility(View.GONE);
            noNotification.setVisibility(View.GONE);
            notificationLoading.setVisibility(View.VISIBLE);
        }else{
            notificationLoading.setVisibility(View.GONE);
        }
    }

    private void updateList(String response) {
        try {
            // Check for error node in json
            JSONObject jObj = new JSONObject(response);
            boolean error = jObj.getBoolean("error");

            if (error) {
                // Error in receiving posts. Get the error message
                String errorMsg = jObj.getString("desc");
                Toast.makeText(context,
                        errorMsg, Toast.LENGTH_LONG).show();
            } else if (jObj.isNull("notifications")) {
                noNotification.setVisibility(View.VISIBLE);
            } else {
                JSONArray jsonPosts = jObj.getJSONArray("notifications");
                for (int i = 0; i < jsonPosts.length(); i++) {
                    notifications.add(0, new Notification(jsonPosts.getJSONObject(i)));
                }
                //update list view
                notificationAdapter.notifyDataSetChanged();
                noNotification.setVisibility(View.GONE);
                Util.getInstance().shakeAnimate(notificationsHeaderBell, true, context);
            }
        } catch (JSONException e) {
            // JSON error
            e.printStackTrace();
            notificationLoadingError.setVisibility(View.VISIBLE);
        }
    }
}
