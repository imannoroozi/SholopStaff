package com.sholop.sholopstaff.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.List;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.objects.Guest;
import com.sholop.sholopstaff.utilities.Util;


public class InviteGuestAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Guest> mItemList;
    Context context;
    InviteGuestAdapter thisObject;

    public class GuestViewHolder extends RecyclerView.ViewHolder {

        EditText guestName, guestEmail, guestTell;

        //Icons
        TextView guestNameIcon, guestEmailIcon, guestTellIcon, removeIcon;
        View container;

        public GuestViewHolder(View rowView) {
            super(rowView);
            guestName = (EditText) rowView.findViewById(R.id.app_visitor_name);
            guestEmail = (EditText) rowView.findViewById(R.id.app_visitor_email);
            guestTell = (EditText) rowView.findViewById(R.id.app_visitor_tell);

            removeIcon = (TextView) rowView.findViewById(R.id.remove_icon);
            guestNameIcon = (TextView) rowView.findViewById(R.id.app_visitor_name_icon);
            guestEmailIcon = (TextView) rowView.findViewById(R.id.app_visitor_email_icon);
            guestTellIcon = (TextView) rowView.findViewById(R.id.app_visitor_tell_icon);

            Typeface tfRegular = Util.getInstance().getFontRegular(context);
            Typeface tfAwesome = Util.getInstance().getFontAwesome(context);

            guestName.setTypeface(tfRegular);
            guestEmail.setTypeface(tfRegular);
            guestTell.setTypeface(tfRegular);

            removeIcon.setTypeface(tfAwesome);
            guestNameIcon.setTypeface(tfAwesome);
            guestEmailIcon.setTypeface(tfAwesome);
            guestTellIcon.setTypeface(tfAwesome);

        }
    }


    public InviteGuestAdapter(Context context, List<Guest> itemList) {
        mItemList = itemList;
        this.context = context;
        thisObject = this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View rowView ;

        rowView = LayoutInflater.from(context).inflate(R.layout.guest_invite_item, parent, false);
        return new GuestViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {
        final Guest guest = mItemList.get(position);

        if(viewHolder instanceof GuestViewHolder){
            GuestViewHolder avh = (GuestViewHolder) viewHolder;
            avh.guestName.setText(guest.getDisplayName());
            avh.guestEmail.setText(guest.getEmail());
            avh.guestTell.setText(guest.getTell());

            TextWatcher nameWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    mItemList.get(position).setDisplayName(s.toString());
                }
            };

            TextWatcher emailWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    mItemList.get(position).setEmail(s.toString());
                }
            };

            TextWatcher tellWatcher = new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}
                @Override
                public void afterTextChanged(Editable s) {
                    mItemList.get(position).setTell(s.toString());
                }
            };

            avh.guestName.addTextChangedListener(nameWatcher);
            avh.guestEmail.addTextChangedListener(emailWatcher);
            avh.guestTell.addTextChangedListener(tellWatcher);

            avh.removeIcon.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mItemList.remove(position);
                    thisObject.notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

}
