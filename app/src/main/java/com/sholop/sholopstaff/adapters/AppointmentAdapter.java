package com.sholop.sholopstaff.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;

import java.util.List;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.activities.SingleAppointmentActivity;
import com.sholop.sholopstaff.config.AppConfig;
import com.sholop.sholopstaff.objects.Appointment;
import com.sholop.sholopstaff.utilities.ImageHelper;
import com.sholop.sholopstaff.utilities.Util;


public class AppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Appointment> mItemList;
    Context context;
    private int lastPosition = -1;

    public class AppointmentViewHolder extends RecyclerView.ViewHolder {

        TextView visitorName, time, appointmentTitle, appointmentStatus, userDisplayName;
        View container;
        NetworkImageView visitorImage;

        public AppointmentViewHolder(View rowView) {
            super(rowView);
            visitorName = (TextView) rowView.findViewById(R.id.visitor_name);
            time = (TextView) rowView.findViewById(R.id.time);
            appointmentTitle = (TextView) rowView.findViewById(R.id.appointment_title);
            appointmentStatus = (TextView) rowView.findViewById(R.id.appointment_status);
            userDisplayName = (TextView) rowView.findViewById(R.id.user_display_name);
            visitorImage = (NetworkImageView) rowView.findViewById(R.id.visitor_image);
            container = rowView.findViewById(R.id.container);
        }
    }


    public AppointmentAdapter(Context context, List<Appointment> itemList) {
        mItemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View rowView ;

        rowView = LayoutInflater.from(context).inflate(R.layout.appointment_item, parent, false);
        return new AppointmentViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Appointment appointment = mItemList.get(position);

        Typeface tfRegular = Util.getInstance().getFontRegular(context);

        if(viewHolder instanceof AppointmentViewHolder){
            AppointmentViewHolder avh = (AppointmentViewHolder) viewHolder;
            avh.visitorName.setText(appointment.getDisplayName());
            avh.time.setText(appointment.getDisplayTime());
            avh.userDisplayName.setText(appointment.getUser().getDisplayName());
            avh.appointmentTitle.setText(appointment.getTitle());
            avh.appointmentStatus.setText(appointment.getStatus());
            ImageHelper.loadNetworkImage(context, avh.visitorImage, appointment.getImageURL());

            avh.visitorName.setTypeface(tfRegular);
            avh.time.setTypeface(tfRegular);
            avh.appointmentTitle.setTypeface(tfRegular);
            avh.userDisplayName.setTypeface(tfRegular);
            avh.appointmentStatus.setTypeface(tfRegular);

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent singlePost = new Intent(context, SingleAppointmentActivity.class);
                    singlePost.putExtra(AppConfig.EXTRA_KEY_APPOINTMENT_ID, appointment.getId());
                    context.startActivity(singlePost);
                }
            });
            setAnimation(avh.container, position);
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

}
