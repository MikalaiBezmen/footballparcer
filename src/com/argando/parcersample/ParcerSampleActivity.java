package com.argando.parcersample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.argando.parcersample.backgroundupdate.ParceService;
import com.argando.parcersample.data.DataNameHelper;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.network.NetworkChecker;
import com.argando.parcersample.parser.DataParcer;
import com.argando.parcersample.scoretable.SampleCategorizeListViewActivity;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class ParcerSampleActivity extends FragmentActivity
{
	private static final String LOG_TAG = ParcerSampleActivity.class.getSimpleName();
	private Toast mInternetConnectionToast;
    private View mContentView;
	private Activity mActivity;
	private Button mRefreshButton;
    private Button mRefreshNow;
	private TextView mLastUpdate;
	private EditText mUpdateTime;
    private Handler mUIHandler;
    SampleCategorizeListViewActivity fragment;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
		mActivity = this;
		setContentView(R.layout.main);
		mRefreshButton = (Button) findViewById(R.id.parse);
        mRefreshNow = (Button) findViewById(R.id.parse_now);
		mLastUpdate = (TextView) findViewById(R.id.last_update);
		mUpdateTime = (EditText) findViewById(R.id.refresh_time);
		mRefreshButton.setOnClickListener(myListener);
		mInternetConnectionToast = Toast.makeText(getBaseContext(), DataNameHelper.NO_INTERNET_CONNECTION, DataNameHelper.NO_INTERNER_CONNNECTION_TOAST_TIME);
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		Log.w(LOG_TAG, " = " + mActivity.getCacheDir().toString());
        DataNameHelper.EXTERNAL_CACHE_DIR = mActivity.getCacheDir().toString();
		LeaguesHandler.mListLeauges = Cache.INSTANCE.readFromFile(new File(DataNameHelper.EXTERNAL_CACHE_DIR));
		mLastUpdate.setText("Last Updated :  " + LeaguesHandler.mTime);

		mUpdateTime.setInputType(InputType.TYPE_NULL);
        mUIHandler = new Handler();
		mUpdateTime.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mUpdateTime.setInputType(InputType.TYPE_CLASS_TEXT);
				mUpdateTime.onTouchEvent(event); // call native handler
				return true; // consume touch even
			}
		});

        mRefreshNow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                Runnable parce = new Runnable() {
                    @Override
                    public void run() {
                        DataParcer dataParcer = new DataParcer();
                        LeaguesHandler.mListLeauges = dataParcer.parceScoreboard();
                        Cache.INSTANCE.cacheResults(LeaguesHandler.mListLeauges, DataNameHelper.EXTERNAL_CACHE_DIR);
                        mUIHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                fragment.updateData();
                                Toast.makeText(getApplicationContext(), "data updated", 3000);
                            }
                        });
                    }
                };
                Thread thread = new Thread(parce);
                thread.start();
            }
        });
        resetUpdateService();
	}

	@Override
	public void onResume()
	{
		super.onResume();
        showResultsList();
	}

	@NotNull
	private OnClickListener	myListener	= new OnClickListener()
										{
											public void onClick(View v)
											{
												if (NetworkChecker.isConnected(mActivity.getApplicationContext()))
												{
													LeaguesHandler.mListLeauges = Cache.INSTANCE.readFromFile(mActivity.getExternalCacheDir());
													showResultsList();
													mLastUpdate.setText("Last Updated :  " + LeaguesHandler.mTime);
													resetUpdateService();
												}
												else
												{
													mInternetConnectionToast.show();
												}
											}

										};

	private void resetUpdateService()
	{
		Intent parseService = new Intent(this, ParceService.class);
		stopService(parseService);
		Log.i(LOG_TAG, "reset update time to " + Integer.valueOf(mUpdateTime.getText().toString()));
		parseService.putExtra("time", Integer.valueOf(mUpdateTime.getText().toString()));
		startService(parseService);
	}

	private void showResultsList()
	{
        Log.i(LOG_TAG, "showResultsList");
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (fragment != null)
		{
            fragment.updateData();
		}

        fragment = new SampleCategorizeListViewActivity();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.replace(R.id.container, fragment);
		fragmentTransaction.commit();
	}

	@Override
	protected void onDestroy()
	{
        Log.i(LOG_TAG, "onDestroy");
		super.onDestroy();
	}
}