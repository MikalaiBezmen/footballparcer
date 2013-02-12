package com.argando.parcersample.backgroundupdate;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
import android.util.Log;
import com.argando.parcersample.Cache;
import com.argando.parcersample.ParcerSampleActivity;
import com.argando.parcersample.parser.DataParcer;
import com.argando.parcersample.data.LeaguesHandler;

/**
 * User: argando
 * Date: 21.07.12
 * Time: 21:22
 */
public class ParseThread implements Runnable
{
	private static final String LOG_TAG = ParseThread.class.getSimpleName();
	private boolean mRunning = false;
	Thread mSimpleThread;
	private String mCacheDir;
    private Context mContext;

	public void startParse(String cacheDir, Context context)
	{
		mCacheDir = cacheDir;
        mContext = context;
		startParse();
	}

	public void startParse()
	{
		Log.w(LOG_TAG, mRunning + " start parse" );
		if (!mRunning)
		{
			Log.w(LOG_TAG,  "new thread" );
			mSimpleThread = new Thread(this);
			mSimpleThread.start();
		}
	}

	public void run()
	{
		Log.w(LOG_TAG,  "run" );
		mRunning = true;
		DataParcer dataParcer = new DataParcer();
		LeaguesHandler.mListLeauges = dataParcer.parceScoreboard();
		Cache.INSTANCE.cacheResults(LeaguesHandler.mListLeauges, mCacheDir);
        Intent updateData = new Intent();
        updateData.setAction(ParcerSampleActivity.UPDATE_ACTION);
        mContext.sendBroadcast(updateData);
		mRunning = false;
	}
}
