package com.argando.parcersample.backgroundupdate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

/**
 * User: argando
 * Date: 21.07.12
 * Time: 16:56
 */
public class ParceService extends Service
{
	private static final String LOG_TAG = ParceService.class.getSimpleName();
	private static final int DEFAULT_INTERVAL = 1000 * 60 * 5;
	private static final int FIRST_RUN = 1000 * 1;
	private int REQUEST_CODE = 111223232;
	private int mInterval;

	private AlarmManager alarmManager;

	@Override
	public void onCreate()
	{
		super.onCreate();
		mInterval = DEFAULT_INTERVAL;
		Log.v(LOG_TAG, "onCreate(..)");
	}

	@Override
	public IBinder onBind(Intent intent)
	{
		Log.v(LOG_TAG, "onBind(..)");
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId)
	{
		super.onStart(intent, startId);
		Log.v(LOG_TAG, "onStart(..)");
		if (intent != null)
		{
			Bundle bundle = intent.getExtras();
			if (bundle != null)
			{
				//mInterval = bundle.getInt("time") * 1000;
				Log.i(LOG_TAG, "reset time updated to " + mInterval);
			}
		}
		startService();
	}

	@Override
	public void onDestroy()
	{
		if (alarmManager != null)
		{
			Intent intent = new Intent(this, ParceService.class);
			alarmManager.cancel(PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0));
		}
		Log.v(LOG_TAG, "Service onDestroy(). Stop AlarmManager at " + new java.sql.Timestamp(System.currentTimeMillis()).toString());
	}

	private void startService()
	{
		Intent intent = new Intent(this, UpdateReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, intent, 0);
		alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
		alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + FIRST_RUN, mInterval, pendingIntent);

		Log.v(LOG_TAG, "AlarmManger started at " + new java.sql.Timestamp(System.currentTimeMillis()).toString() + " with interval = " + mInterval);
	}

	//	@Override
	//	public int onStartCommand(Intent intent, int flags, int startId) {
	//		return Service.START_STICKY;
	//	}
}
