package com.sholop.sholopstaff.activities;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sholop.sholopstaff.R;
import com.sholop.sholopstaff.adapters.CropOptionAdapter;
import com.sholop.sholopstaff.adapters.GuestAdapter;
import com.sholop.sholopstaff.config.AppConfig;
import com.sholop.sholopstaff.config.AppController;
import com.sholop.sholopstaff.gcm.MakeNotificationRead;
import com.sholop.sholopstaff.objects.Appointment;
import com.sholop.sholopstaff.utilities.CropOption;
import com.sholop.sholopstaff.utilities.ImageHelper;
import com.sholop.sholopstaff.utilities.RoundedNetworkImageView;
import com.sholop.sholopstaff.utilities.SessionManager;
import com.sholop.sholopstaff.utilities.Util;


public class SingleAppointmentActivity extends AppCompatActivity {

	boolean loading = false;
	SessionManager session;

	private Uri visitorImageUri;
	private Bitmap photo;
	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;

    View appointmentLoadingLayout, appointmentLoadingError, appointmentContainer;
    TextView appointmentLoadingIcon, appointmentLoadingErrorMessage, retryIcon;

	RecyclerView guestsView;

	RoundedNetworkImageView visitorImage;
	NetworkImageView corporationLogo;

	//titles
	TextView subjectTitle, userDisplayNameTitle, dateTimeTitle, locationTitle, statusTitle,guestsHeader ;

	//values
	TextView visitorName, visitorContact, subject, userDisplayName, dateTime, location, status;

	int appointmentID;

    Appointment appointment;
	GuestAdapter guestAdapter;

	public enum ACTIONS {GET_SINGLE_APPOINTMENT, UPLOAD_VISITOR_IMAGE}

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_single_appointment);

		session = new SessionManager(this);

		appointmentID = getIntent().getExtras().getInt(AppConfig.EXTRA_KEY_APPOINTMENT_ID);

		Typeface fontAwesome = Util.getInstance().getFontAwesome(this);
		Typeface fontRegular = Util.getInstance().getFontRegular(this);
		Typeface fontBold = Util.getInstance().getFontBold(this);

		appointmentLoadingIcon = (TextView) findViewById(R.id.appointment_loading);
		appointmentLoadingLayout = findViewById(R.id.appointment_loading_layout);
		appointmentLoadingErrorMessage = (TextView) findViewById(R.id.error_happened_message);
		appointmentLoadingError = findViewById(R.id.error_happened);
		retryIcon = (TextView) findViewById(R.id.retry_icon);
		appointmentContainer = findViewById(R.id.appointment_container);

		guestsView = (RecyclerView) findViewById(R.id.guests_view);
		retryIcon.setTypeface(fontAwesome);

		//titles
		subjectTitle = (TextView) findViewById(R.id.subject_title);
		userDisplayNameTitle = (TextView) findViewById(R.id.user_display_name_title);
		dateTimeTitle = (TextView) findViewById(R.id.date_time_title);
		locationTitle = (TextView) findViewById(R.id.location_title);
		statusTitle = (TextView) findViewById(R.id.status_title);
		guestsHeader = (TextView) findViewById(R.id.guests_header);

		//values
		visitorName = (TextView) findViewById(R.id.visitor_name);
		visitorContact = (TextView) findViewById(R.id.visitor_email_tell);
		subject = (TextView) findViewById(R.id.subject);
		userDisplayName = (TextView) findViewById(R.id.user_display_name);
		dateTime = (TextView) findViewById(R.id.date_time);
		location = (TextView) findViewById(R.id.location);
		status = (TextView) findViewById(R.id.status);

		visitorImage = (RoundedNetworkImageView) findViewById(R.id.visitor_image);
		corporationLogo = (NetworkImageView) findViewById(R.id.company_logo);

		appointmentLoadingIcon.setTypeface(fontAwesome);

		appointmentLoadingErrorMessage.setTypeface(fontRegular);

		subject.setTypeface(fontBold);
		dateTime.setTypeface(fontRegular);
		userDisplayName.setTypeface(fontRegular);
		location.setTypeface(fontRegular);
		status.setTypeface(fontRegular);

		subjectTitle.setTypeface(fontAwesome);
		userDisplayNameTitle.setTypeface(fontAwesome);
		dateTimeTitle.setTypeface(fontAwesome);
		locationTitle.setTypeface(fontAwesome);
		statusTitle.setTypeface(fontAwesome);

		guestsHeader.setTypeface(fontBold);

		Util.getInstance().rotateAnimate(appointmentLoadingIcon, true, this);

		//read this appointment. this action would clear (make as read) related notification as well
		if( appointmentID > 0 ) {
			getAppointment();
		}else{
			appointmentLoadingError.setVisibility(View.VISIBLE);
			appointmentLoadingLayout.setVisibility(View.GONE);
		}

		int notificationId = getIntent().getIntExtra(AppConfig.EXTRA_KEY_NOTIFICATION_ID, -1);
		if(notificationId != -1){
			//this activity is launched by a notification
			MakeNotificationRead makeNotificationRead = new MakeNotificationRead(this, notificationId, true);
			makeNotificationRead.clearNotification();
		}

		//pick image implementations
		final String [] items			= new String [] {"Take from camera", "Select from gallery"};
		ArrayAdapter<String> adapter	= new ArrayAdapter<> (this, android.R.layout.select_dialog_item,items);
		AlertDialog.Builder builder		= new AlertDialog.Builder(this);

		builder.setTitle("Select Image");
		builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
			public void onClick( DialogInterface dialog, int item ) { //pick from camera
				if (item == 0) {
					Intent intent 	 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

					visitorImageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),
							"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));

					intent.putExtra(MediaStore.EXTRA_OUTPUT, visitorImageUri);

					try {
						intent.putExtra("return-data", true);

						startActivityForResult(intent, PICK_FROM_CAMERA);
					} catch (ActivityNotFoundException e) {
						e.printStackTrace();
					}
				} else { //pick from proje
					Intent intent = new Intent();

					intent.setType("image/*");
					intent.setAction(Intent.ACTION_GET_CONTENT);

					startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
				}
			}
		} );

		final AlertDialog dialog = builder.create();

//		visitorImage.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				dialog.show();
//			}
//		});

		retryIcon.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if( appointmentID > 0 ) {
					getAppointment();
				}else{
					appointmentLoadingError.setVisibility(View.VISIBLE);
					appointmentLoadingLayout.setVisibility(View.GONE);
				}
			}
		});
	}


	private void getAppointment() {
		//post request
		HashMap<String, String> data = new HashMap<>();
		data.put("user_id", String.valueOf(session.getCurrentUserID()));
		data.put("appointment_id", String.valueOf(appointmentID));
//		data.put("visitor_name", searchVisitors.getText().toString());
		postAction(ACTIONS.GET_SINGLE_APPOINTMENT, data);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.action_bar_menu, menu);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()){
			case R.id.action_more: {
				return true;
			}

			case android.R.id.home:
//				NavUtils.navigateUpFromSameTask(this);
				finish();
				return true;
			case R.id.action_search: {

				return true;
			}
		}
		return super.onOptionsItemSelected(item);
	}
	@Override
	public void onDestroy()
	{
		super.onDestroy();
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) return;

		switch (requestCode) {
			case PICK_FROM_CAMERA:
				doCrop();

				break;

			case PICK_FROM_FILE:
				visitorImageUri = data.getData();

				doCrop();

				break;

			case CROP_FROM_CAMERA:

				Uri selectedImageUri = data.getData();
				photo = BitmapFactory.decodeFile(getPath(selectedImageUri));

				Bundle extras = data.getExtras();

				if (extras != null) {
					photo = extras.getParcelable("data");
				}

				HashMap<String, String> params = new HashMap<>();
				params.put("appointment_id", String.valueOf(appointmentID));
				params.put("user_id", String.valueOf(session.getCurrentUserID()));

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
				byte[] imageBytes = baos.toByteArray();
				String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
				params.put("image", encodedImage);

				postAction(ACTIONS.UPLOAD_VISITOR_IMAGE, params);
				File f = new File(visitorImageUri.getPath());

				if (f.exists()) f.delete();

				break;

		}
	}

	public String getPath(Uri uri) {
		if( uri == null ) {
			return null;
		}
		String[] projection = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		if( cursor != null ){
			int column_index = cursor
					.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			cursor.moveToFirst();
			return cursor.getString(column_index);
		}
		return uri.getPath();
	}



	private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities( intent, 0 );

		int size = list.size();

		if (size == 0) {
			Toast.makeText(this, "Can not find image crop app", Toast.LENGTH_SHORT).show();

			return;
		} else {
			intent.setData(visitorImageUri);

			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			if (size == 1) {
				Intent i 		= new Intent(intent);
				ResolveInfo res	= list.get(0);

				i.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

				startActivityForResult(i, CROP_FROM_CAMERA);
			} else {
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title 	= getPackageManager().getApplicationLabel(res.activityInfo.applicationInfo);
					co.icon		= getPackageManager().getApplicationIcon(res.activityInfo.applicationInfo);
					co.appIntent= new Intent(intent);

					co.appIntent.setComponent( new ComponentName(res.activityInfo.packageName, res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose Crop App");
				builder.setAdapter( adapter, new DialogInterface.OnClickListener() {
					public void onClick( DialogInterface dialog, int item ) {
						startActivityForResult( cropOptions.get(item).appIntent, CROP_FROM_CAMERA);
					}
				});

				builder.setOnCancelListener( new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel( DialogInterface dialog ) {

						if (visitorImageUri != null ) {
							getContentResolver().delete(visitorImageUri, null, null );
							visitorImageUri = null;
						}
					}
				} );

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}


	private void postAction(final ACTIONS action , final HashMap<String,String> data) {

		String tag_string_req = "appointment_req";
		String action_URL = "";
		setUpLoading(true);

		switch (action){
			case GET_SINGLE_APPOINTMENT:
				action_URL = AppConfig.URL_READ_APPOINTMENTS;
				break;
			case UPLOAD_VISITOR_IMAGE:
				action_URL = AppConfig.URL_READ_APPOINTMENTS;
				break;
		}
		StringRequest strReq = new StringRequest(Request.Method.POST,
				action_URL, new Response.Listener<String>() {

			@Override
			public void onResponse(String response) {

				setUpLoading(false);

				switch (action){

					case GET_SINGLE_APPOINTMENT:
						updateView(response);
						break;
					case UPLOAD_VISITOR_IMAGE:
						//set image to visitor
						setVisitorImage(response);
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
					Toast.makeText(SingleAppointmentActivity.this,
							"Time out error!", Toast.LENGTH_LONG).show();
				} else {
					//show the error
					Toast.makeText(SingleAppointmentActivity.this,
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
		if( Util.getInstance().isNetworkAvailable(SingleAppointmentActivity.this)) {
			// Adding request to request queue
			AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
		}
	}

	private void updateView(String response) {
		try {
			// Check for error node in json
			JSONObject jObj = new JSONObject(response);
			boolean error = jObj.getBoolean("error");

			if (error) {
				// Error in receiving posts. Get the error message
				String errorMsg = jObj.getString("desc");
				Toast.makeText(this,
						errorMsg, Toast.LENGTH_LONG).show();
			} else if (jObj.isNull("appointment")) {
//				Toast.makeText(this, "There is no new posts", Toast.LENGTH_SHORT).show();
			} else {
				JSONObject jsonApp = jObj.getJSONObject("appointment");
				appointment = new Appointment(jsonApp);

				//update view
				guestAdapter = new GuestAdapter(SingleAppointmentActivity.this, appointment.getGuests());
				final LinearLayoutManager mLayoutManager = new LinearLayoutManager(SingleAppointmentActivity.this);
				guestsView.setLayoutManager(mLayoutManager);
				guestsView.setAdapter(guestAdapter);

				subject.setText(appointment.getTitle());
				userDisplayName.setText(appointment.getUser().getDisplayName());
				dateTime.setText(appointment.getDisplayDate(SingleAppointmentActivity.this) + "  " + appointment.getDisplayTime());
				location.setText(appointment.getLocation());

				String []englishStatuses = getResources().getStringArray(R.array.english_appointment_statuses);
				String []endglishStatusesDisplay = getResources().getStringArray(R.array.persian_appointment_statuses);

				status.setText(endglishStatusesDisplay[Arrays.asList(englishStatuses).indexOf(appointment.getStatus())]);

				ImageHelper.loadNetworkImage(SingleAppointmentActivity.this, corporationLogo, appointment.getCorporationLogoUrl());
			}
		} catch (JSONException e) {
			// JSON error
			e.printStackTrace();
//			Toast.makeText(this, "Json error: " + e.getMessage(), Toast.LENGTH_LONG).show();
			appointmentLoadingError.setVisibility(View.VISIBLE);
		}
	}

	private void setVisitorImage( String response){
		// Check for error node in json
		JSONObject jObj = null;
		try {
			jObj = new JSONObject(response);
			boolean error = jObj.getBoolean("error");

			if (error) {
				// Error in receiving posts. Get the error message
				String errorMsg = jObj.getString("desc");
				Toast.makeText(this,
						errorMsg, Toast.LENGTH_LONG).show();
			}else{
				String imageUrl = jObj.getString("file_path");
				ImageHelper.loadNetworkImage(SingleAppointmentActivity.this, visitorImage, imageUrl);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}
	private void setUpLoading(boolean loading){
		this.loading = loading;
		if( loading ){
			appointmentLoadingError.setVisibility(View.GONE);
			appointmentLoadingLayout.setVisibility(View.VISIBLE);
		}else{
			appointmentLoadingLayout.setVisibility(View.GONE);
		}
	}


}