package com.multimedia.busy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Deactivate extends ActionBarActivity {
	Database datab;
	private Button m_yes,m_no;
	String starttime = null;
	String endtime = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_deactivate);
		try {
			Checkactivated();
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		m_yes=(Button)findViewById(R.id.m_yes);
		m_yes.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				deactivate();
				Toast.makeText(getBaseContext(), "Deactivated Succesfully", Toast.LENGTH_LONG)
				.show();
				skip();
				
			}
		});
		m_no=(Button)findViewById(R.id.m_no);
		m_no.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				skip();
			}
		});
		
	}

	//Checks the condition if the application is in activity state or not. 
	private void Checkactivated() throws ParseException {
		// TODO Auto-generated method stub
		datab = new Database(getBaseContext());
		datab.open();
		Cursor C = datab.returndata();
		if (C.moveToFirst()) {
			do {
				starttime=C.getString(0);
				endtime=C.getString(1);
			} while (C.moveToNext());
		}
		datab.close();
		if(starttime!=null)
		{
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd, HH:mm:ss");
		String strDate = sdf.format(c.getTime());
		//Parsing all the datetime data in a single format for comparison.
		Date one = sdf.parse(starttime);
		Date two=sdf.parse(strDate);
		Date three=sdf.parse(endtime);
		//Condition when the deactivate screen should be displayed.
		if(two.before(one)||two.before(three)){
			
		}
		else
		{
			skip();
		}
		}
		else if(starttime==null)
		{
			skip();
		}
		
	}

	//Method allows the program to skip to the next class.
	private void skip() {
		// TODO Auto-generated method stub
		startActivity(new Intent(this, SplashScreen.class));	
	}

	//method deletes the existing data from database when user chooses to deactivate the application.
	private void deactivate()  {
		// TODO Auto-generated method stub
		datab.open();
		datab.delete();
		datab.close();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.deactivate, menu);
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
}
