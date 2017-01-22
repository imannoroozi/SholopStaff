package com.sholop.sholopstaff.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.objects.Day;
import com.sholop.sholopstaff.objects.Month;
import com.sholop.sholopstaff.utilities.Util;


/**
 * Created by Pooyan on 8/18/2016. each cell is representing a month day
 */
public class DayAdapter extends BaseAdapter {
    private Context mContext;
    Month month;
    Typeface fontRegular;
    Typeface fontBold;
    String selectedDateString;

    public DayAdapter(Context c, Month m, String selectedDateString) {
        mContext = c;
        month = m;
        fontRegular = Util.getInstance().getFontRegular(mContext);
        fontBold = Util.getInstance().getFontBold(mContext);
        this.selectedDateString = selectedDateString;
    }

    public int getCount() {
        return month.getDays().size();
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View dayView; // Creating an instance for View Object
        LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        dayView = inflater.inflate(R.layout.day_item, null);
        Day day = month.getDays().get(position);
        TextView gregorianDay;
        gregorianDay = (TextView) dayView.findViewById(R.id.gregorian_day);
        gregorianDay.setTypeface(fontRegular);


        if(day.getDay() != null && day.getDay().getDayOfWeek() == 6){
            gregorianDay.setTextColor(ContextCompat.getColor(mContext, R.color.colorAccent));
        }

        if(position == month.getTodayIndex()){
            gregorianDay.setTextColor(ContextCompat.getColor(mContext, R.color.colorPrimaryDark));
            gregorianDay.setTypeface(fontBold);
        }

        gregorianDay.setText(day.getDay() == null ?
                "" : String.valueOf(day.getDay().getGregorianDay()));

        return dayView;
    }
}