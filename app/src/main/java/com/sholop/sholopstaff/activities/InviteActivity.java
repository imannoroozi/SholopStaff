package com.sholop.sholopstaff.activities;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.adapters.InviteGuestAdapter;
import com.sholop.sholopstaff.config.AppConfig;
import com.sholop.sholopstaff.config.AppController;
import com.sholop.sholopstaff.objects.Guest;
import com.sholop.sholopstaff.utilities.CalendarDialog;
import com.sholop.sholopstaff.utilities.SessionManager;
import com.sholop.sholopstaff.utilities.Util;


public class InviteActivity extends AppCompatActivity implements CalendarParentActivity {

	boolean loading = false;
	SessionManager session;
	CalendarDialog calendar;

    View calendarPopUPLayout,
			actionBar,
			checkButton;

	View appTitleLayout, addGuestLayout/*, visitorNameLayout, visitorEmailLayout, visitorTellLayout*/;

	View createLayout, creatingLayout;

	EditText appTitle, /*visitorName, visitorEmail, visitorTell,*/ appLocation;

	//icons
	TextView appTitleIcon, appLocationIcon, appHourIcon, appMinIcon, checkButtonIcon, checkButtonText, creatingIcon, addGuestIcon,
		//actionbar icons
		backAction;

    TextView gregorianDateString,
			calendarPopUpIcon, meetingSpecHeader, guestsHeader,
			hourHeader, minuteHeader, addGuestHeaderS;

	Spinner appointmentHour, appointmentMin;

	RecyclerView inviteGuestsView;

	public enum ACTIONS {CREATE_APPOINTMENT}

	ArrayList<Guest> guests;
	InviteGuestAdapter inviteGuestAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_invite);

		session = new SessionManager(this);

		calendar = new CalendarDialog(this);

		inviteGuestsView = (RecyclerView) findViewById(R.id.invite_guests_view);
		guests = new ArrayList<>();
		guests.add(new Guest());
		final LinearLayoutManager mLayoutManager = new LinearLayoutManager(InviteActivity.this);
		inviteGuestsView.setLayoutManager(mLayoutManager);
		inviteGuestAdapter = new InviteGuestAdapter(InviteActivity.this, guests);
		inviteGuestsView.setAdapter(inviteGuestAdapter);

		final Typeface fontAwesome = Util.getInstance().getFontAwesome(this);
		final Typeface fontRegular = Util.getInstance().getFontRegular(this);
		final Typeface fontBold = Util.getInstance().getFontBold(this);

		//init action bar
		actionBar = findViewById(R.id.action_bar);

		calendarPopUPLayout = findViewById(R.id.calendar_pop_up);
		calendarPopUpIcon = (TextView) findViewById(R.id.calendar_pop_up_icon);

		gregorianDateString = (TextView) findViewById(R.id.gregorian_date_string);

		appTitle = (EditText) findViewById(R.id.app_title);
//		visitorName = (EditText) findViewById(R.id.app_visitor_name);
//		visitorEmail = (EditText) findViewById(R.id.app_visitor_email);
//		visitorTell = (EditText) findViewById(R.id.app_visitor_tell);
		appLocation = (EditText) findViewById(R.id.app_location);

		checkButtonText = (TextView) findViewById(R.id.check_button_text);
		checkButton = findViewById(R.id.check_button);
		creatingIcon = (TextView) findViewById(R.id.creating_icon);
		creatingIcon.setTypeface(fontAwesome);
		addGuestIcon = (TextView) findViewById(R.id.add_guest_icon);
		addGuestIcon.setTypeface(fontAwesome);

		Util.getInstance().rotateAnimate(creatingIcon, true, InviteActivity.this);

		//icons
		appTitleIcon = (TextView) findViewById(R.id.app_title_icon);
//		visitorNameIcon = (TextView) findViewById(R.id.app_visitor_name_icon);
//		visitorEmailIcon = (TextView) findViewById(R.id.app_visitor_email_icon);
//		visitorTellIcon = (TextView) findViewById(R.id.app_visitor_tell_icon);
		appLocationIcon = (TextView) findViewById(R.id.app_location_icon);
		appHourIcon = (TextView) findViewById(R.id.app_hour_icon);
		appMinIcon = (TextView) findViewById(R.id.app_min_icon);
		checkButtonIcon = (TextView) findViewById(R.id.check_button_icon);

		meetingSpecHeader = (TextView) findViewById(R.id.appointment_spec_subject);
		guestsHeader = (TextView) findViewById(R.id.visitor_spec_subject);
		hourHeader = (TextView) findViewById(R.id.hour_header);
		minuteHeader = (TextView) findViewById(R.id.minute_header);
		addGuestHeaderS = (TextView) findViewById(R.id.add_guest_text);

		backAction = (TextView) actionBar.findViewById(R.id.back_action);
		//Views of fields
		appTitleLayout = findViewById(R.id.app_title_layout);
//		visitorNameLayout = findViewById(R.id.app_visitor_name_layout);
//		visitorEmailLayout = findViewById(R.id.app_visitor_email_layout);
//		visitorTellLayout = findViewById(R.id.app_visitor_tell_layout);

		createLayout = findViewById(R.id.create_layout);
		creatingLayout = findViewById(R.id.creating_appointment_layout);

		addGuestLayout = findViewById( R.id.add_guest_layout);

		appTitle.setTypeface(fontRegular);
//		visitorName.setTypeface(fontRegular);
//		visitorEmail.setTypeface(fontRegular);
//		visitorTell.setTypeface(fontRegular);
		appLocation.setTypeface(fontRegular);
		checkButtonText.setTypeface(fontRegular);
		meetingSpecHeader.setTypeface(fontRegular);
		guestsHeader.setTypeface(fontRegular);
		hourHeader.setTypeface(fontRegular);
		minuteHeader.setTypeface(fontRegular);
		addGuestHeaderS.setTypeface(fontRegular);

		appTitleIcon.setTypeface(fontAwesome);
//		visitorNameIcon.setTypeface(fontAwesome);
//		visitorEmailIcon.setTypeface(fontAwesome);
//		visitorTellIcon.setTypeface(fontAwesome);
		appLocationIcon.setTypeface(fontAwesome);
		appHourIcon.setTypeface(fontAwesome);
		appMinIcon.setTypeface(fontAwesome);
		checkButtonIcon.setTypeface(fontAwesome);

		backAction.setTypeface(fontAwesome);

		gregorianDateString.setText(calendar.getCurrentMonth().getGregorianDateString());
		gregorianDateString.setTypeface(fontBold);
		calendarPopUpIcon.setTypeface(fontAwesome);

		appointmentHour = (Spinner) findViewById(R.id.app_hour);
		appointmentMin = (Spinner) findViewById(R.id.app_min);

		String[] minutes = getResources().getStringArray(R.array.minutes);
		ArrayAdapter<String> minAdapter = new ArrayAdapter<String>(this, R.layout.time_spinner_item, minutes){
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				((TextView) v).setTypeface(fontRegular);
				return v;
			}
			public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
				View v =super.getDropDownView(position, convertView, parent);
				((TextView) v).setTypeface(fontRegular);
				return v;
			}
		};
		appointmentMin.setAdapter(minAdapter);

		String[] hours = new String[24];
		for( int i=0; i<hours.length; i++){
			hours[i] = i<10 ? "0"+i : String.valueOf(i);
		}
		ArrayAdapter<String> hourAdapter = new ArrayAdapter<String>(this, R.layout.time_spinner_item, hours){
			public View getView(int position, View convertView, ViewGroup parent) {
				View v = super.getView(position, convertView, parent);
				((TextView) v).setTypeface(fontRegular);
				return v;
			}
			public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
				View v =super.getDropDownView(position, convertView, parent);
				((TextView) v).setTypeface(fontRegular);
				return v;
			}
		};
		appointmentHour.setAdapter(hourAdapter);
		appointmentHour.setSelection(10, true);

		backAction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		addGuestLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				guests.add(new Guest());
				inviteGuestAdapter.notifyItemInserted(guests.size()-1);
			}
		});
		calendarPopUPLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DisplayMetrics displayMetrics = new DisplayMetrics();
				calendar.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

				int width = displayMetrics.widthPixels;
				calendar.show();


				calendar.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);
				calendar.getWindow().setGravity(Gravity.BOTTOM);
				calendar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

				calendar.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
					}
				});
			}
		});

		checkButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(validateInputs()){
					createAppointment();
				}
			}
		});

	}

	public boolean validateInputs(){
		boolean retVal = true;

		//check for empty fields
		/*if( visitorName.getText().toString().equals("") ){
			visitorNameLayout.setBackground( ContextCompat.getDrawable(InviteActivity.this, R.drawable.rounded_field_invalid) );
			retVal = false;
		}else{
			visitorNameLayout.setBackground( ContextCompat.getDrawable(InviteActivity.this, R.drawable.rounded_field) );
		}*/

		if( appTitle.getText().toString().equals("") ){
			appTitleLayout.setBackground( ContextCompat.getDrawable(InviteActivity.this, R.drawable.rounded_field_invalid) );
			retVal = false;
		}else{
			appTitleLayout.setBackground( ContextCompat.getDrawable(InviteActivity.this, R.drawable.rounded_field) );
		}

		//check for email and tell
		/*if( visitorTell.getText().toString().equals("") && visitorEmail.getText().toString().equals("") ){
			Toast.makeText(InviteActivity.this, getString(R.string.fill_tell_or_email), Toast.LENGTH_SHORT).show();
			retVal = false;
		}

		//validate email
		if( !android.util.Patterns.EMAIL_ADDRESS.matcher(visitorEmail.getText().toString()).matches() ){
			visitorEmailLayout.setBackground( ContextCompat.getDrawable(InviteActivity.this, R.drawable.rounded_field_invalid) );
			retVal = false;
		}else{
			visitorEmailLayout.setBackground( ContextCompat.getDrawable(InviteActivity.this, R.drawable.rounded_field) );
		}*/

		return retVal;
	}

	@Override
	public void calendarDaySelected() {
		gregorianDateString.setText(calendar.getCurrentMonth().getGregorianDateString());
	}

	private void createAppointment() {
		//post request
		HashMap<String, String> data = new HashMap<>();
		data.put("input_user_id", String.valueOf(session.getCurrentUserID()));
		data.put("app_date_string", calendar.getCurrentMonth().getSelectedDateString());
		data.put("app_time_string", String.valueOf(appointmentHour.getSelectedItem()) + String.valueOf(appointmentMin.getSelectedItem()));
		data.put("app_title", appTitle.getText().toString());
//		data.put("visitor_name", visitorName.getText().toString());
//		data.put("visitor_email", visitorEmail.getText().toString());
//		data.put("visitor_tell", visitorTell.getText().toString());
		String guestString = "";
		for( Guest guest : guests){
			guestString += guest.toString()+"/";
		}
		data.put("guests", guestString);
		data.put("app_location", appLocation.getText().toString());
		postAction(ACTIONS.CREATE_APPOINTMENT, data);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onResume(){
		super.onResume();
	}

	//Must unregister onPause()
	@Override
	protected void onPause() {
		super.onPause();
	}


	private void postAction(final ACTIONS action , final HashMap<String,String> data) {

		String tag_string_req = "appointment_req";
		String action_URL = "";
		setUpLoading(true);

		switch (action){
			case CREATE_APPOINTMENT:
				action_URL = AppConfig.URL_READ_APPOINTMENTS;
				break;
		}
		StringRequest strReq = new StringRequest(Request.Method.POST,
				action_URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				setUpLoading(false);

				switch (action){

					case CREATE_APPOINTMENT:
						try {
							JSONObject jObj = new JSONObject(response);
							boolean error = jObj.getBoolean("error");

							String errorMsg = null;
							errorMsg = jObj.getString("desc");
							Toast.makeText(InviteActivity.this,errorMsg, Toast.LENGTH_LONG).show();
							if (!error) {
								finish();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

						break;
					default:
						break;
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				setUpLoading(false);
				//show the error
				if (error instanceof com.android.volley.TimeoutError) {
					Toast.makeText(InviteActivity.this,
							getString(R.string.time_out_error), Toast.LENGTH_LONG).show();
				} else {
					//show the error
					Toast.makeText(InviteActivity.this,
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
		if( Util.getInstance().isNetworkAvailable(InviteActivity.this)) {
			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
		}
	}

	private void setUpLoading(boolean loading){
		this.loading = loading;
		if( loading ){
			creatingLayout.setVisibility(View.VISIBLE);
			createLayout.setVisibility(View.GONE);
		}else{
			creatingLayout.setVisibility(View.GONE);
			createLayout.setVisibility(View.VISIBLE);
		}
	}



	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}
}