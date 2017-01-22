package com.sholop.sholopstaff.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.adapters.AppointmentAdapter;
import com.sholop.sholopstaff.adapters.CustomDrawerAdapter;
import com.sholop.sholopstaff.config.AppConfig;
import com.sholop.sholopstaff.config.AppController;
import com.sholop.sholopstaff.gcm.MakeNotificationRead;
import com.sholop.sholopstaff.objects.Appointment;
import com.sholop.sholopstaff.objects.DrawerItem;
import com.sholop.sholopstaff.utilities.CalendarDialog;
import com.sholop.sholopstaff.utilities.NotificationsDialog;
import com.sholop.sholopstaff.utilities.SessionManager;
import com.sholop.sholopstaff.utilities.Util;


public class MainActivity extends AppCompatActivity implements CalendarParentActivity {

	int currentPage = 0, notificationsCount = 0;
	private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    RecyclerView appointmentsRecyclerView;
    AppointmentAdapter appointmentAdapter;
	boolean loading = false;
	SessionManager session;
	CalendarDialog calendar;
	NotificationsDialog notificationsDialog;

	private DrawerLayout mDrawer;
	private ListView mDrawerList;

    View appointmentLoadingLayout,
			calendarPopUPLayout,
			noAppointmentsTodayLayout,
			actionBar,
			appointmentLoadingError,
			notificationView;

    TextView gregorianDateString,
			appointmentLoadingIcon,
			calendarPopUpIcon,
			searchVisitorIcon,
			noAppointmentToday,
			drawerButton,
			inviteButton,
			appointmentLoadingErrorMessage,
			appointmentLoadingErrorRetry,
			notificationsBell,
			notificationsCountTextView;

	EditText searchVisitors;

    ArrayList<Appointment> appointments;

	private BroadcastReceiver mMessageReceiver;

	public enum ACTIONS {GET_APPOINTMENTS}
	public enum BELL_ACTIONS {CLEAR, STEP_UP, STEP_DOWN}

	int []owlImageArray={R.drawable.owl1, R.drawable.owl2,R.drawable.owl3,R.drawable.owl4};

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        appointments = new ArrayList<>();
		session = new SessionManager(this);

		calendar = new CalendarDialog(this);
		notificationsDialog = new NotificationsDialog(this);

        appointmentsRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		setupRecyclerView(appointmentsRecyclerView);

		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		Typeface fontAwesome = Util.getInstance().getFontAwesome(this);
		Typeface fontRegular = Util.getInstance().getFontRegular(this);
		Typeface fontBold = Util.getInstance().getFontBold(this);

		//init action bar
		actionBar = findViewById(R.id.action_bar);
		notificationView = actionBar.findViewById(R.id.notifications_view);
		drawerButton = (TextView) actionBar.findViewById(R.id.drawer_bars);
		inviteButton = (TextView) actionBar.findViewById(R.id.invite_button);
		notificationsBell = (TextView) actionBar.findViewById(R.id.notifications_bell);
		notificationsCountTextView = (TextView) actionBar.findViewById(R.id.notifications_count);

		drawerButton.setTypeface(fontAwesome);
		inviteButton.setTypeface(fontAwesome);
		notificationsBell.setTypeface(fontAwesome);
		notificationsCountTextView.setTypeface(fontRegular);

		drawerButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mDrawer.openDrawer(Gravity.RIGHT);
			}
		});

		inviteButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent inviteIntent = new Intent(MainActivity.this, InviteActivity.class);
				startActivity(inviteIntent);
			}
		});

		String []drawerTitles = getResources().getStringArray(R.array.drawer_titles);
		String []drawerIcons = getResources().getStringArray(R.array.drawer_icons);
		ArrayList<DrawerItem> dataList = new ArrayList<DrawerItem>();
		for( int i=0; i<drawerIcons.length; i++){
			dataList.add(new DrawerItem(drawerTitles[i], drawerIcons[i]));
		}
		mDrawerList.setAdapter(new CustomDrawerAdapter(
				this,
				R.layout.drawer_list_item,
				dataList));
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		appointmentLoadingIcon = (TextView) findViewById(R.id.appointment_loading);
		appointmentLoadingLayout = findViewById(R.id.appointment_loading_layout);
		noAppointmentToday = (TextView) findViewById(R.id.no_appointment_today);
		noAppointmentsTodayLayout = findViewById(R.id.no_appointment_today_layout);
		appointmentLoadingError = findViewById(R.id.error_happened);
		appointmentLoadingErrorMessage = (TextView) findViewById(R.id.error_happened_message);
		appointmentLoadingErrorRetry = (TextView) findViewById(R.id.retry_icon);

		calendarPopUPLayout = findViewById(R.id.calendar_pop_up);
		calendarPopUpIcon = (TextView) findViewById(R.id.calendar_pop_up_icon);

		searchVisitors = (EditText) findViewById(R.id.filter_visitors_edittext);
		searchVisitorIcon = (TextView) findViewById(R.id.filter_visitors_icon);

		gregorianDateString = (TextView) findViewById(R.id.gregorian_date_string);
		gregorianDateString.setTypeface(fontBold);

		gregorianDateString.setText(calendar.getCurrentMonth().getGregorianDateString());
		calendarPopUpIcon.setTypeface(fontAwesome);

		appointmentLoadingIcon.setTypeface(fontAwesome);

		searchVisitors.setTypeface(fontRegular);
		searchVisitorIcon.setTypeface(fontAwesome);
		appointmentLoadingErrorRetry.setTypeface(fontAwesome);

		noAppointmentToday.setTypeface(fontRegular);
		appointmentLoadingErrorMessage.setTypeface(fontRegular);

		Util.getInstance().rotateAnimate(appointmentLoadingIcon, true, this);

		calendarPopUPLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DisplayMetrics displayMetrics = new DisplayMetrics();
				calendar.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

				int width = displayMetrics.widthPixels;
				int height = displayMetrics.heightPixels;
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

		notificationView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				DisplayMetrics displayMetrics = new DisplayMetrics();
				if( notificationsCount > 0 ){
					notificationsDialog = new NotificationsDialog(MainActivity.this);
				}
				notificationsDialog.getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

				int width = displayMetrics.widthPixels;
				int height = displayMetrics.heightPixels ;
				notificationsDialog.show();
				updateBell(BELL_ACTIONS.CLEAR);

				notificationsDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
				notificationsDialog.getWindow().setGravity(Gravity.BOTTOM);
				notificationsDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

				notificationsDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
					}
				});
			}
		});

		final TextWatcher searchVisitorWatcher = new TextWatcher() {
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			public void onTextChanged(CharSequence s, int start, int before, int count) {
				currentPage = 0;
				getAppointments();
			}

			public void afterTextChanged(Editable s) {
			}
		};

		searchVisitors.addTextChangedListener(searchVisitorWatcher);

		final ImageView owl = (ImageView) findViewById(R.id.ghost);

		Util.getInstance().animateOwl(owl);
		final Handler handler = new Handler();
		Runnable runnable = new Runnable() {
			int i=0;
			public void run() {
				owl.setImageResource(owlImageArray[i]);
				i++;
				if(i>owlImageArray.length-1) {
					i=0;
				}
				handler.postDelayed(this, 2000);  //for interval...
			}
		};
		handler.postDelayed(runnable, 2000);

		//This is the handler that will manager to process the broadcast intent
		mMessageReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {

				// Extract data included in the Intent
				String message = intent.getStringExtra(AppConfig.EXTRA_KEY_MESSAGE);

				//do other stuff here
				Util.getInstance().shakeAnimate(notificationsBell, true, getApplicationContext());
				updateBell(BELL_ACTIONS.STEP_UP);
			}
		};

		//read today's appointments
		getAppointments();

		if ( getIntent().getIntExtra(AppConfig.EXTRA_KEY_NOTIFICATION_ID, 0) != 0 ){
			//means the activity is called by notification and we should make notification read.
		}

		/*if (checkPlayServices()) {
			registerGCM(session.getCurrentUserID());
		}*/

		int notificationId = getIntent().getIntExtra(AppConfig.EXTRA_KEY_NOTIFICATION_ID, -1);
		if(notificationId != -1){
			//this activity is launched by a notification
			MakeNotificationRead makeNotificationRead = new MakeNotificationRead(this, notificationId, true);
			makeNotificationRead.clearNotification();
		}

		appointmentLoadingErrorRetry.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getAppointments();
			}
		});
	}

	@Override
	public void calendarDaySelected() {

		appointments.clear();
		appointmentAdapter.notifyDataSetChanged();
		currentPage = 0;

		gregorianDateString.setText(calendar.getCurrentMonth().getGregorianDateString());

		getAppointments();
	}

	private void getAppointments() {
		//post request
		HashMap<String, String> data = new HashMap<>();
		data.put("page", String.valueOf(currentPage));
		data.put("user_id", String.valueOf(session.getCurrentUserID()));
		data.put("date_string", calendar.getCurrentMonth().getSelectedDateString());
		data.put("visitor_name", searchVisitors.getText().toString());
		postAction(ACTIONS.GET_APPOINTMENTS, data);
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}

	@Override
	public void onResume(){
		super.onResume();
		this.registerReceiver(mMessageReceiver, new IntentFilter(AppConfig.EXTRA_KEY_RECEIVER_INTENT));
	}

	//Must unregister onPause()
	@Override
	protected void onPause() {
		super.onPause();
		this.unregisterReceiver(mMessageReceiver);
	}

    private void setupRecyclerView(RecyclerView recyclerView) {
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(mLayoutManager);
        appointmentAdapter = new AppointmentAdapter(MainActivity.this, appointments);
        recyclerView.setAdapter(appointmentAdapter);
    }

	private void postAction(final ACTIONS action , final HashMap<String,String> data) {

		String tag_string_req = "appointment_req";
		String action_URL = "";
		setUpLoading(true);

		switch (action){
			case GET_APPOINTMENTS:
				action_URL = AppConfig.URL_READ_APPOINTMENTS;
				break;
		}
		StringRequest strReq = new StringRequest(Request.Method.POST,
				action_URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				setUpLoading(false);

				switch (action){

					case GET_APPOINTMENTS:
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
				appointmentLoadingError.setVisibility(View.VISIBLE);
				//show the error
				if (error instanceof com.android.volley.TimeoutError) {
					Toast.makeText(MainActivity.this,
							"Time out error!", Toast.LENGTH_LONG).show();
				} else {
					//show the error
					Toast.makeText(MainActivity.this,
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
		if( Util.getInstance().isNetworkAvailable(MainActivity.this)) {
			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
		}
	}

	private void updateList(String response) {
		try {
			// Check for error node in json
			JSONObject jObj = new JSONObject(response);
			boolean error = jObj.getBoolean("error");
			appointments.clear();

			if (error) {
				// Error in receiving posts. Get the error message
				String errorMsg = jObj.getString("desc");
				Toast.makeText(this,
						errorMsg, Toast.LENGTH_LONG).show();
			} else if (jObj.isNull("posts")) {
//				Toast.makeText(this, "There is no new posts", Toast.LENGTH_SHORT).show();
				noAppointmentsTodayLayout.setVisibility(View.VISIBLE);
			} else {
				JSONArray jsonPosts = jObj.getJSONArray("posts");
				for (int i = 0; i < jsonPosts.length(); i++) {
					appointments.add(i, new Appointment(jsonPosts.getJSONObject(i)));
				}
				//update list view
				appointmentAdapter.notifyDataSetChanged();
				noAppointmentsTodayLayout.setVisibility(View.GONE);
			}

			if(jObj.isNull("unread_notifications") || jObj.getString("unread_notifications").equals("")){
				setUnreadNotifications(0);
			}else{
				setUnreadNotifications(jObj.getInt("unread_notifications"));
			}

		} catch (JSONException e) {
			// JSON error
			e.printStackTrace();
//			Toast.makeText(this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
			appointmentLoadingError.setVisibility(View.VISIBLE);
		}
	}

	private void setUnreadNotifications( int un ){
		if( un <= 0 ){
			notificationsCount = 0;
			notificationsCountTextView.setVisibility(View.GONE);
		}else{
			notificationsCount = un;
			notificationsCountTextView.setVisibility(View.VISIBLE);
			notificationsCountTextView.setText(String.valueOf(notificationsCount));
		}
	}

	private void updateBell(BELL_ACTIONS action){
		switch (action){
			case CLEAR:
				notificationsCount = 0;
				notificationsCountTextView.setVisibility(View.GONE);
				break;
			case STEP_DOWN:
				if( (notificationsCount--) == 1 ){
					notificationsCountTextView.setVisibility(View.GONE);
				}else{
					notificationsCountTextView.setText(String.valueOf(notificationsCount));
				}
				break;
			case STEP_UP:
				notificationsCountTextView.setVisibility(View.VISIBLE);
				notificationsCountTextView.setText(String.valueOf(++notificationsCount));
				break;
			default:
				//do nothing.
				break;
		}
	}

	private void setUpLoading(boolean loading){
		this.loading = loading;
		if( loading ){
			appointmentLoadingError.setVisibility(View.GONE);
			noAppointmentsTodayLayout.setVisibility(View.GONE);
			appointmentLoadingLayout.setVisibility(View.VISIBLE);
		}else{
			appointmentLoadingLayout.setVisibility(View.GONE);
		}
	}

	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}

		/** Swaps fragments in the main content view */
		private void selectItem(int position) {

			switch (position){
				case 0: //invite
					Intent inviteIntent = new Intent(MainActivity.this, InviteActivity.class);
					startActivity(inviteIntent);
					break;
				case 1: //about us
					Intent aboutUsIntent = new Intent(MainActivity.this, AboutUsActivity.class);
					startActivity(aboutUsIntent);
					break;
				case 2: //log out
					session.setLogin(false);
					Intent loginActivity = new Intent(MainActivity.this, LoginActivity.class);
					startActivity(loginActivity);
					finish();
					break;
				default:
					Toast.makeText(MainActivity.this, "Not implemented" + position, Toast.LENGTH_SHORT).show();
					break;

			}
			mDrawer.closeDrawer(Gravity.RIGHT);
		}

	}

	private boolean checkPlayServices() {
		GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
		int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
		if (resultCode != ConnectionResult.SUCCESS) {
			if (apiAvailability.isUserResolvableError(resultCode)) {
				apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST)
						.show();
			} else {
				Toast.makeText(getApplicationContext(), "This device is not supported. Google Play Services not installed!", Toast.LENGTH_LONG).show();
				finish();
			}
			return false;
		}
		return true;
	}

	// starting the service to register with GCM
	/*private void registerGCM(final int userID) {

		new AsyncTask<Void, Void, String>() {
			@Override
			protected String doInBackground(Void... params) {

				Intent intent = new Intent(MainActivity.this, GcmIntentService.class);
				intent.putExtra(AppConfig.EXTRA_KEY_USER_ID, String.valueOf(userID));
				startService(intent);

				return "";
			}

			@Override
			protected void onPostExecute(String msg) {
			}
		}.execute(null, null, null);
	}*/

	@Override
	public void onBackPressed() {
		//check if drawer is open
		if(mDrawer.isDrawerOpen(Gravity.RIGHT)) {
			//drawer is open
			mDrawer.closeDrawer(Gravity.RIGHT);
			return;
		}
		super.onBackPressed();
	}
}