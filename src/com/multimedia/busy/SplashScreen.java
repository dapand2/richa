package com.multimedia.busy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.Spinner;
import android.widget.VideoView;

public class SplashScreen extends ActionBarActivity {
private VideoView m_video;
Database datab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash_screen);
				
		try {
		//Setting a video view and setting the path of the video.
			m_video = (VideoView) this.findViewById(R.id.m_video);
			m_video.setVideoURI(Uri.parse("android.resource://" + getPackageName()
					+ "/" + R.raw.m_movie));
			

			m_video.setOnCompletionListener(new OnCompletionListener() {

				public void onCompletion(MediaPlayer mp) {
					skip();
				}

			});
			m_video.start();
		} catch (Exception ex) {
			skip();
		}

	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.splash_screen, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		skip();
		return true;
	}

	private void skip() {
		//Skip is called when the video either finishes or user swipes on the screen.
		if (isFinishing())
			return;
		startActivity(new Intent(this, MainActivity.class));
		finish();
	}
}
