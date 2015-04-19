package com.multimedia.busy;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;




import android.app.Activity;
import android.app.IntentService;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CalendarContract.Events;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;
import android.view.View.OnClickListener;

;

public class MainActivity extends Activity {

	private Spinner m_spinner_calender;

	private Button m_button_getEvents;
	private Button m_event1;
	private Button m_event2;
	private Button m_event3;
	
	
	String begin1, begin2, begin3;
	String end1, end2, end3;
	Database datab;
	private TextView displ2;
	SharedPreferences prf;
	private ImageView img;
	private ImageButton m_cal_button;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//getWindow().getDecorView().setBackgroundColor(Color.BLUE);
		getCalendars();
		callCalendar();
		geteventdetails();
		saveeventdata1();
		saveeventdata2();
		saveeventdata3();
		// callCalendar();
		displ2 = (TextView) findViewById(R.id.m_messages);
		displ2.setVisibility(View.GONE);
		// img.setVisibility(View.GONE);
		// text4.setVisibility(View.GONE);

	}

	//This calls the internal calendar application of android if user chooses to add a new calendar.
	private void callCalendar() {
		// TODO Auto-generated method stub

		m_cal_button=(ImageButton) findViewById(R.id.m_cal_button);
		m_cal_button.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(Intent.ACTION_VIEW, android.net.Uri
						.parse("content://com.android.calendar/time/")));
			}
		});
	}

	//Populates the data in the spinner i.e shows the name of calendars to the user in the spinner.
	private void populateCalendarSpinner() {
		if (m_calendars == null) {
			displ2.setText("no calendar found");
		} else if (m_calendars != null) {
			m_spinner_calender = (Spinner) this
					.findViewById(R.id.m_spinner_calendar);
			ArrayAdapter l_arrayAdapter = new ArrayAdapter(
					this.getApplicationContext(), R.layout.spinner_layout,
					m_calendars);
			l_arrayAdapter.setDropDownViewResource(R.layout.spinner_layout);
			m_spinner_calender.setAdapter(l_arrayAdapter);
			m_spinner_calender.setSelection(0);
			m_spinner_calender
					.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> p_parent,
								View p_view, int p_pos, long p_id) {
							m_selectedCalendarId = m_calendars[(int) p_id].id;
							m_event1.setVisibility(View.GONE);
							m_event2.setVisibility(View.GONE);
							m_event3.setVisibility(View.GONE);
							displ2.setVisibility(View.GONE);

						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {
						}
					});
		}
	}

	//This is called when a user presses the getevent button in the application.
	private void geteventdetails() {
		m_button_getEvents = (Button) findViewById(R.id.m_get_events);
		m_button_getEvents.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				geteventsfromcalendar();

			}
		});
	}

	//Function that is called on click of the 1st event button.
	private void saveeventdata1() {
		m_event1 = (Button) findViewById(R.id.m_setevent1);
		m_event1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				savesharedpref(begin1, end1);
				
			}
		});
	}
	
	
	
		//Function that is called on click of the 2nd event button.
	private void saveeventdata2() {
		m_event2 = (Button) findViewById(R.id.m_setevent2);
		m_event2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				savesharedpref(begin2, end2);
				
			}
		});
	}

	//Function that is called on click of the 3rd event button.
	private void saveeventdata3() {
		m_event3 = (Button) findViewById(R.id.m_setevent3);
		m_event3.setOnClickListener(new View.OnClickListener() {


			@Override
			public void onClick(View v) {
				savesharedpref(begin3, end3);
			}
		});
	}

	//Function called by 3 buttons to store the data in shared preferences.
	public void savesharedpref(String Begin,String End)
	{
		prf = getSharedPreferences("event_details", MODE_PRIVATE);
		SharedPreferences.Editor edit = prf.edit();
		edit.putString("key_begin", Begin);
		edit.putString("key_end", End);
		edit.commit();
		
		Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
		vibe.vibrate(200);
		Intent intent = new Intent(getBaseContext(), SavetextMessage.class);
		startActivityForResult(intent, 0);
		
	}
	
	
	private Calendarclass m_calendars[];
	private String m_selectedCalendarId = "0";
//Function to retrieve a list of all calendar account present in users device.
	private void getCalendars() {
		String[] l_projection = new String[] { "_id", "calendar_displayName" };
		Uri l_calendars;
		if (Build.VERSION.SDK_INT >= 8) {
			l_calendars = Uri.parse("content://com.android.calendar/calendars");
		} else {
			l_calendars = Uri.parse("content://calendar/calendars");
		}

		Cursor m_calcursor = this.managedQuery(l_calendars, l_projection,
				null, null, null);
		
		//if condition to check and change the display if there are no calendars present in users device.
		if (m_calcursor.getCount() == 0) {
			
			m_cal_button=(ImageButton) findViewById(R.id.m_cal_button);
			m_cal_button.setVisibility(View.VISIBLE);
			img = (ImageView) findViewById(R.id.m_image);
			img.setImageResource(R.drawable.nocalendar);
			img.setVisibility(View.VISIBLE);
			//condition to check and change the display if there are  calendars present in users device.
		} else if (m_calcursor.getCount() != 0) {
			m_spinner_calender = (Spinner) this
					.findViewById(R.id.m_spinner_calendar);
			m_button_getEvents = (Button) findViewById(R.id.m_get_events);
			m_spinner_calender.setVisibility(View.VISIBLE);
			m_button_getEvents.setVisibility(View.VISIBLE);
			
			if (m_calcursor.moveToFirst()) {
				m_calendars = new Calendarclass[m_calcursor.getCount()];
				String l_calName;
				String l_calId;
				int l_cnt = 0;
				int l_nameCol = m_calcursor.getColumnIndex(l_projection[1]);
				int l_idCol = m_calcursor.getColumnIndex(l_projection[0]);
				do {
					l_calName = m_calcursor.getString(l_nameCol);
					l_calId = m_calcursor.getString(l_idCol);
					m_calendars[l_cnt] = new Calendarclass(l_calName, l_calId);
					++l_cnt;
				} while (m_calcursor.moveToNext());
			}
			populateCalendarSpinner();
		}

	}

	//Function to retrieve three events from the calendar selected by the user.
	private void geteventsfromcalendar() {
		Uri m_eventUri;
		if (Build.VERSION.SDK_INT >= 8) {
			m_eventUri = Uri.parse("content://com.android.calendar/events");

		} else {

			m_eventUri = Uri.parse("content://calendar/events");

		}

		Uri.Builder builder = Uri.parse(
				"content://com.android.calendar/instances/when").buildUpon();
		long now = new Date().getTime();
		// Time span will display only the events for the next 24 hrs.
		//ContentUris.appendId(builder, now- (DateUtils.DAY_IN_MILLIS * 1)	- (DateUtils.HOUR_IN_MILLIS * 1));
		ContentUris.appendId(builder, now- (DateUtils.HOUR_IN_MILLIS * 1));
		ContentUris.appendId(builder, now + (DateUtils.DAY_IN_MILLIS * 1)
				+ (DateUtils.HOUR_IN_MILLIS * 1));

		String[] l_projection = new String[] { "title", "dtstart", "dtend" };
		Cursor m_calcursor = this.managedQuery(builder.build(),
				l_projection, "calendar_id=" + m_selectedCalendarId, null,
				"dtstart ASC, dtend DESC");
		// Cursor l_managedCursor = this.managedQuery(l_eventUri, l_projection,
		// null, null, null);
		if (m_calcursor.getCount() == 0) {
			displ2.setVisibility(View.VISIBLE);
			displ2.setText("No Entry found");

		}
		if (m_calcursor.moveToFirst()) {
			int m_cnt = 0;
			String m_title;
			String m_begin;
			String m_end;
			StringBuilder l_displayText = new StringBuilder();
			int m_colTitle = m_calcursor.getColumnIndex(l_projection[0]);
			int m_colBegin = m_calcursor.getColumnIndex(l_projection[1]);
			int m_colEnd = m_calcursor.getColumnIndex(l_projection[2]);
						do {
				m_title = m_calcursor.getString(m_colTitle);
				m_begin = getDateTimeStr(m_calcursor.getString(m_colBegin));
				m_end = getDateTimeStr(m_calcursor.getString(m_colEnd));
				String res = m_title + "\n" + m_begin + "\n" + m_end + "";
				
				//Populate buttons with event details.
				if (m_cnt == 0) {
					m_event1.setVisibility(View.VISIBLE);
					m_event1.setText(res.toString());
					begin1 = m_begin;
					end1 = m_end;
				} else if (m_cnt == 1) {
					m_event2.setVisibility(View.VISIBLE);
					m_event2.setText(res.toString());
					begin2 = m_begin;
					end2 = m_end;
				} else if (m_cnt == 2) {
					m_event3.setVisibility(View.VISIBLE);
					m_event3.setText(res.toString());
					begin3 = m_begin;
					end3 = m_end;
				}
				++m_cnt;
			} while (m_calcursor.moveToNext() && m_cnt < 3);
			displ2.setVisibility(View.VISIBLE);
			displ2.setText("Please select an event from below");
		}
	}

	//Managing the date time format 
	private static final String DATE_TIME_FORMAT = "yyyy MMM dd, HH:mm:ss";

	

	public static String getDateTimeStr(String m_time_in_millis) {
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_TIME_FORMAT);
		Date m_time = new Date(Long.parseLong(m_time_in_millis));
		return sdf.format(m_time);
	}

}
