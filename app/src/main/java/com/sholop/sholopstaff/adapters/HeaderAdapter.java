package com.sholop.sholopstaff.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.utilities.Util;


/**
 * Created by Pooyan on 8/18/2016. each cell is representing a month day
 */
public class HeaderAdapter extends BaseAdapter {
    private Context mContext;

    String []weekdays;

    Typeface fontRegular;
    public HeaderAdapter(Context c) {
        mContext = c;
        Resources res = mContext.getResources();
        weekdays = res.getStringArray(R.array.gregorian_week_days);

        fontRegular = Util.getInstance().getFontRegular(c);
    }

    public int getCount() {
        return 7;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View headerView; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        headerView = inflater.inflate(R.layout.week_header_item, null);

        TextView header = (TextView) headerView.findViewById(R.id.header);
        header.setText(String.valueOf(weekdays[position]));
        header.setTypeface(fontRegular);
        return headerView;
    }
}