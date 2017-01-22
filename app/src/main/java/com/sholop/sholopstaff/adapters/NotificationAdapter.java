package com.sholop.sholopstaff.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.activities.SingleAppointmentActivity;
import com.sholop.sholopstaff.config.AppConfig;
import com.sholop.sholopstaff.config.AppController;
import com.sholop.sholopstaff.objects.Notification;
import com.sholop.sholopstaff.utilities.ImageHelper;
import com.sholop.sholopstaff.utilities.NotificationsDialog;
import com.sholop.sholopstaff.utilities.SessionManager;
import com.sholop.sholopstaff.utilities.Util;


public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Notification> mItemList;
    Context context;
    NotificationsDialog notificationsDialog;
    SessionManager session;

    private int lastPosition = -1;

    enum ACTIONS {CLEAR_NOTIFICATION}

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        TextView title, content;
        NetworkImageView image;

        View container;

        public NotificationViewHolder(View rowView) {
            super(rowView);
            title = (TextView) rowView.findViewById(R.id.notification_title);
            content = (TextView) rowView.findViewById(R.id.notification_content);
            image = (NetworkImageView) rowView.findViewById(R.id.notification_image);
            container = rowView.findViewById(R.id.notification_container);
        }
    }


    public NotificationAdapter(Context context, List<Notification> itemList, NotificationsDialog nd) {
        mItemList = itemList;
        this.context = context;

        session = new SessionManager(this.context);

        this.notificationsDialog = nd;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View rowView ;

        rowView = LayoutInflater.from(context).inflate(R.layout.notification_item, parent, false);
        return new NotificationViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        final Notification notification = mItemList.get(position);

        Typeface tfRegular = Util.getInstance().getFontRegular(context);

        if(viewHolder instanceof NotificationViewHolder){
            NotificationViewHolder nvh = (NotificationViewHolder) viewHolder;
            nvh.title.setText(notification.getTitle());
            nvh.content.setText(notification.getContent());
            ImageHelper.loadNetworkImage(context, nvh.image, notification.getImageUrl());

            nvh.title.setTypeface(tfRegular);
            nvh.content.setTypeface(tfRegular);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent targetIntent;
                    if(notification.getType().equals(AppConfig.NOTIFICATION_TYPES.APPOINTMENT.name())){
                        targetIntent = new Intent(context, SingleAppointmentActivity.class);
                        targetIntent.putExtra(AppConfig.EXTRA_KEY_APPOINTMENT_ID, notification.getAppointmentId());
                        targetIntent.putExtra(AppConfig.EXTRA_KEY_NOTIFICATION_ID, notification.getId());
                        context.startActivity(targetIntent);
                    }else{
                        //we should update main activity here
                    }

                    if( notificationsDialog != null){
                        notificationsDialog.dismiss();
                    }

                    if(!notification.isRead()){
                        //clear notification
                        HashMap<String, String> data = new HashMap<>();
                        data.put("notification_id", String.valueOf(notification.getId()));
                        data.put("read", "Y");
                        data.put("user_id", String.valueOf(session.getCurrentUserID()));
                        postAction(ACTIONS.CLEAR_NOTIFICATION, data);

                        notification.setRead(true);
                        ((NotificationViewHolder) viewHolder).container.setBackgroundColor(ContextCompat.getColor(context, R.color.schemeWhite));
                    }
                }
            });

            if(!notification.isRead()){
                ((NotificationViewHolder) viewHolder).container.setBackgroundColor(ContextCompat.getColor(context, R.color.schemeBlue));
            }
            setAnimation(nvh.container, position);
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition)
        {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.abc_slide_in_bottom);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    private void postAction(final ACTIONS action , final HashMap<String,String> data) {

        String tag_string_req = "appointment_req";
        String action_URL = "";

        switch (action){
            case CLEAR_NOTIFICATION:
                action_URL = AppConfig.URL_READ_NOTIFICATIONS;
                break;
        }
        StringRequest strReq = new StringRequest(Request.Method.POST,
                action_URL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                switch (action){

                    case CLEAR_NOTIFICATION:
                        break;
                    default:
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
}
