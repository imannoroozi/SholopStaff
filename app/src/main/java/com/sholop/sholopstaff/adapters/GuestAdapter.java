package com.sholop.sholopstaff.adapters;

import android.content.Context;
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
import com.sholop.sholopstaff.objects.Guest;
import com.sholop.sholopstaff.utilities.ImageHelper;
import com.sholop.sholopstaff.utilities.Util;


public class GuestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Guest> mItemList;
    Context context;
    private int lastPosition = -1;

    public class GuestViewHolder extends RecyclerView.ViewHolder {

        TextView guestName, time, appointmentTitle, appointmentStatus, guestEmailTell;
        View container;
        NetworkImageView guestImage;

        public GuestViewHolder(View rowView) {
            super(rowView);
            guestName = (TextView) rowView.findViewById(R.id.visitor_name);
            guestEmailTell = (TextView) rowView.findViewById(R.id.visitor_email_tell);
            guestImage = (NetworkImageView) rowView.findViewById(R.id.visitor_image);
            container = rowView.findViewById(R.id.container);
        }
    }


    public GuestAdapter(Context context, List<Guest> itemList) {
        mItemList = itemList;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View rowView ;

        rowView = LayoutInflater.from(context).inflate(R.layout.guest_item, parent, false);
        return new GuestViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final Guest guest = mItemList.get(position);

        Typeface tfRegular = Util.getInstance().getFontRegular(context);

        if(viewHolder instanceof GuestViewHolder){
            GuestViewHolder avh = (GuestViewHolder) viewHolder;
            avh.guestName.setText(guest.getDisplayName());
            avh.guestEmailTell.setText(guest.getEmail() + " / " + guest.getTell());
            ImageHelper.loadNetworkImage(context, avh.guestImage, guest.getImageAddress());

            avh.guestName.setTypeface(tfRegular);
            avh.guestEmailTell.setTypeface(tfRegular);

            /*viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent singlePost = new Intent(context, SingleAppointmentActivity.class);
                    singlePost.putExtra(AppConfig.EXTRA_KEY_APPOINTMENT_ID, guest.getID());
                    context.startActivity(singlePost);
                }
            });*/
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
