package com.sholop.sholopstaff.utilities;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.activities.CalendarParentActivity;
import com.sholop.sholopstaff.adapters.DayAdapter;
import com.sholop.sholopstaff.adapters.HeaderAdapter;
import com.sholop.sholopstaff.objects.Month;

/**
 * Created by Pooyan on 10/26/2015.*/

public class CalendarDialog extends Dialog{

    private Context context;
    private Typeface fontBold;
    private Typeface font;

    String selectedDateString;

    private CalendarDialog thisObject;

    Month currentMonth;
    int diffMonth = 0;

    View calendarLayout;
    TextView gregorianMonthTitle, nextMonth, preMonth;
    GridView daysGridView;

    public CalendarDialog(Context context) {
        super(context);
        this.context = context;

        thisObject = this;

        currentMonth = new Month(context, diffMonth);
        selectedDateString = currentMonth.getSelectedDateString();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_dialog);

        fontBold = Util.getInstance().getFontBold(context);
        font = Util.getInstance().getFontRegular(context);

        calendarLayout = findViewById(R.id.calendar_layout);

        Typeface awesome = Util.getInstance().getFontAwesome(context);
        Typeface droid = Util.getInstance().getFontRegular(context);
        nextMonth = (TextView) findViewById(R.id.next_month);
        preMonth = (TextView) findViewById(R.id.previous_month);

        gregorianMonthTitle = (TextView) findViewById(R.id.gregorian_month_title);
        gregorianMonthTitle.setTypeface(font);

        nextMonth.setTypeface(awesome);
        preMonth.setTypeface(awesome);

        daysGridView = (GridView) findViewById(R.id.gridview);

        setUpCalendar();

        GridView weekDayGridView = (GridView) findViewById(R.id.header_gridview);
        weekDayGridView.setAdapter(new HeaderAdapter(context));

    }

    private void setUpCalendar(){
        currentMonth = new Month(context, diffMonth);

        gregorianMonthTitle.setText(currentMonth.getGregorianMonthTitle());

        daysGridView.setAdapter(new DayAdapter(context, currentMonth, selectedDateString));

        daysGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                currentMonth.setSelectedDayIndex( position);
                selectedDateString = currentMonth.getSelectedDateString();

                ( (CalendarParentActivity) context).calendarDaySelected();

                thisObject.dismiss();
            }
        });

        nextMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diffMonth += 1;
                setUpCalendar();
            }
        });
        preMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                diffMonth -= 1;
                setUpCalendar();
            }
        });

    }

    public Month getCurrentMonth() {
        return currentMonth;
    }

    public void setCurrentMonth(Month currentMonth) {
        this.currentMonth = currentMonth;
    }
}
