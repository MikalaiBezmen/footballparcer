package com.argando.parcersample.backgroundupdate;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.argando.parcersample.Cache;
import com.argando.parcersample.ParcerSampleActivity;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.parser.DataParcer;

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
        Intent updateData = new Intent();
        updateData.setAction(ParcerSampleActivity.UPDATE_START);
        mContext.sendBroadcast(updateData);
		mRunning = true;
		DataParcer dataParcer = new DataParcer();
		LeaguesHandler.mListLeauges = dataParcer.parceScoreboard();
		Cache.INSTANCE.cacheResults(LeaguesHandler.mListLeauges, mCacheDir);
        updateData.setAction(ParcerSampleActivity.UPDATE_END);
        mContext.sendBroadcast(updateData);
		mRunning = false;
	}
}
