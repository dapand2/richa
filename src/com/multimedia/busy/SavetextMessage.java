package com.multimedia.busy;

import java.text.BreakIterator;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;

public class SavetextMessage extends ActionBarActivity {
	ImageButton submit;
	EditText data;
	private TextView m_errormsg;
	SharedPreferences prf;
	Database datab;
	Spinner m_textmsg;
	String m_settextmessage;
	int spinner_pos;
	String m_messagetobesent;
	private MediaPlayer mp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_text_message);
		
		
		submit = (ImageButton) findViewById(R.id.m_submit);
		data = (EditText) findViewById(R.id.m_data);
		
		m_textmessagesItemSelection();
		//Text change listener used to check of user enters a text in textbox the submit button appers and if he removes the text it disappears
		data.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				if (count == 0) {
					submit.setVisibility(View.GONE);
				}
				if (count > 0) {
					submit.setVisibility(View.VISIBLE);
				}
				m_errormsg = (TextView) findViewById(R.id.m_errormessage);
				m_errormsg.setVisibility(View.GONE);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		submit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
				vibe.vibrate(200);
				
				//getting shared preferences that were saved in main activity.
				prf = getSharedPreferences("event_details", MODE_PRIVATE);

				
				String start = prf.getString("key_begin", "");
				String end = prf.getString("key_end", "");
				
				String getdata = data.getText().toString();
				
				//Condition to check and throw an error if user select a value from spinner and also enter a text in the textbox.
				if (spinner_pos != 0 && getdata.length() != 0) {
					m_errormsg = (TextView) findViewById(R.id.m_errormessage);
					m_errormsg.setVisibility(View.VISIBLE);
					m_errormsg.setText("Please input a single message");

				} 
				//Condition where we set the actual text message that is to be sent to the caller.
				else {
					submit.setImageResource(R.drawable.blue_button);

					if (spinner_pos != 0 && getdata.length() == 0) {

						m_messagetobesent = m_settextmessage;
					}

					else if (spinner_pos == 0 && getdata.length() != 0) {
						m_messagetobesent = getdata;
					}

					//Entering the values in database.
					datab = new Database(getBaseContext());
					datab.open();
					long id = datab.insertdata(start, end, m_messagetobesent);
					//Toast.makeText(getBaseContext(), "data inserted",Toast.LENGTH_LONG).show();
					datab.close();
					Toast.makeText(getBaseContext(), "App activated Scuccesfully", Toast.LENGTH_LONG)
					.show();
					//Plays the mp3 file to confirm success activation of the application.
					mp=MediaPlayer.create(getBaseContext(),R.raw.m_msg);
					mp.start();
					
					
				}

			}
		});

		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.text_messaage, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	//Function to check and return the message that has been selected by the user in the spinner
	public void m_textmessagesItemSelection() {
		m_textmsg = (Spinner) findViewById(R.id.m_textmessage);

		m_textmsg
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {
						submit = (ImageButton) findViewById(R.id.m_submit);
						// conditions to check any changes to spinner and handle the output according to it.
						if (pos == 0) {
							submit.setVisibility(View.GONE);
						} else if (pos != 0) {

							submit.setVisibility(View.VISIBLE);
							spinner_pos = pos;
							m_settextmessage = parent.getItemAtPosition(pos)
									.toString();
						}

						m_errormsg = (TextView) findViewById(R.id.m_errormessage);
						m_errormsg.setVisibility(View.GONE);

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
					}
				});
	}

}
