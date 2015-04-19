package com.multimedia.busy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.android.internal.telephony.ITelephony;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.widget.Toast;

public class Sendmsganddisconnect extends BroadcastReceiver{
Database datab;
private ITelephony telephonyService;

@Override
public void onReceive(Context context, Intent intent) {
						// TODO Auto-generated method stub
		if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
				TelephonyManager.EXTRA_STATE_RINGING)) {
			try {
				//if state of the phone is ringing the below method is called for implementation.
				sendmsganddisconnect(context, intent);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// silent(context);

		}
		//if state of the phone is idle nothing needs to be done.
		else if (intent.getStringExtra(TelephonyManager.EXTRA_STATE).equals(
				TelephonyManager.EXTRA_STATE_IDLE)
				|| (intent.getStringExtra(TelephonyManager.EXTRA_STATE)
						.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))) {
			//Toast.makeText(context, "call hangup", Toast.LENGTH_LONG).show();
		}
	
}

	//Function that handles the conditions to disconnect and send a message to the caller.
	private void sendmsganddisconnect(Context context, Intent intent) throws ParseException {

		// TODO Auto-generated method stub
		String incomingnumber = intent
				.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
		String starttime;
		String endtime;
		String Getdata;
		Getdata = "";
		endtime="";
		starttime="";
		
		//reading values from the database.
		datab = new Database(context);
		datab.open();
		Cursor C = datab.returndata();
		if (C.moveToFirst()) {
			do {
				starttime=C.getString(0);
				endtime=C.getString(1);
				Getdata = C.getString(2);
			} while (C.moveToNext());
		}
		datab.close();
		Calendar c = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd, HH:mm:ss");
		String strDate = sdf.format(c.getTime());
		
		//Parsing all the datetime data in a single format for comparison.
		Date one = sdf.parse(starttime);
		Date two = sdf.parse(endtime);
		Date three=sdf.parse(strDate);
		
		//Checking if the call is in the meeting time set by the user.
		if (three.after(one) && three.before(two)) {

			TelephonyManager telephony = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			try {
				Class cls = Class.forName(telephony.getClass().getName());
				Method m = cls.getDeclaredMethod("getITelephony");
				m.setAccessible(true);
				telephonyService = (ITelephony) m.invoke(telephony);
				
				telephonyService.endCall();
			} catch (Exception e) {
				e.printStackTrace();
			}

			SmsManager manager = SmsManager.getDefault();
			manager.sendTextMessage(incomingnumber, null, Getdata, null, null);
			
			}
            
      

			
		}
		
		

		

	}

