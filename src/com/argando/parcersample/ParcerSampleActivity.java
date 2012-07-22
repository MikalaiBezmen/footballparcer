package com.argando.parcersample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.argando.parcersample.scoretable.SampleCategorizeListViewActivity;
import org.jetbrains.annotations.NotNull;

public class ParcerSampleActivity extends FragmentActivity
{
	private static final String LOG_TAG = ParcerSampleActivity.class.getSimpleName();
	private Toast mInternetConnectionToast;
    private View mContentView;
	private Activity mActivity;
	private Button mRefreshButton;
	private TextView mLastUpdate;
	private EditText mUpdateTime;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(R.layout.main);
		mRefreshButton = (Button) findViewById(R.id.parse);
		mLastUpdate = (TextView) findViewById(R.id.last_update);
		mUpdateTime = (EditText) findViewById(R.id.refresh_time);
		mRefreshButton.setOnClickListener(myListener);
		mInternetConnectionToast = Toast.makeText(getBaseContext(), DataNameHelper.NO_INTERNET_CONNECTION, DataNameHelper.NO_INTERNER_CONNNECTION_TOAST_TIME);
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		mContentView = view.getChildAt(0);
		Log.w(LOG_TAG, " = " + mActivity.getExternalCacheDir().toString());
		/*Intent parseService = new Intent(this, ParceService.class);
		startService(parseService);*/
		LeaguesHandler.mListLeauges = Cache.INSTANCE.readFromFile(mActivity.getExternalCacheDir());
		showResultsList();
		mLastUpdate.setText("Last Updated :  " + LeaguesHandler.mTime);

		mUpdateTime.setInputType(InputType.TYPE_NULL);

		mUpdateTime.setOnTouchListener(new View.OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mUpdateTime.setInputType(InputType.TYPE_CLASS_TEXT);
				mUpdateTime.onTouchEvent(event); // call native handler
				return true; // consume touch even
			}
		});

	}

	@Override
	public void onResume()
	{
		super.onResume();
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
		FragmentManager fragmentManager = getSupportFragmentManager();

		//TODO reimplement this code, do not remove old fragment, just update info for adapter
		Fragment tempSolution = fragmentManager.findFragmentByTag(SampleCategorizeListViewActivity.class.getSimpleName());
		if (tempSolution != null)
		{
			fragmentManager.beginTransaction().remove(tempSolution).commit();
		}

		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		SampleCategorizeListViewActivity fragment = new SampleCategorizeListViewActivity();
		fragmentTransaction.add(R.id.container, fragment, SampleCategorizeListViewActivity.class.getSimpleName());
		fragmentTransaction.commit();
	}

	@Override
	protected void onDestroy()
	{
		super.onDestroy();
	}
}