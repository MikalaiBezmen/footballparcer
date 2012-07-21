package com.argando.parcersample;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ParcerSampleActivity extends FragmentActivity
{
	@NotNull
	String			text	= "";

	@NotNull
	List<League>	leagues	= new ArrayList<League>();

	Toast			internetConnectionToast;

	public View		contentView;

	private Activity mActivity;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mActivity = this;
		setContentView(R.layout.main);
		Button button = (Button) findViewById(R.id.parse);
		button.setOnClickListener(myListener);
		internetConnectionToast = Toast.makeText(getBaseContext(), DataNameHelper.NO_INTERNET_CONNECTION, DataNameHelper.NO_INTERNER_CONNNECTION_TOAST_TIME);
		ViewGroup view = (ViewGroup) getWindow().getDecorView();
		contentView = view.getChildAt(0);
		FragmentManager fragmentManager = getSupportFragmentManager();
		if (null == fragmentManager.findFragmentByTag(SampleCategorizeListViewActivity.class.getSimpleName()))
		{
			startParce();
		}
	}

	@Override
	public void onResume()
	{
		super.onResume();
		//		startParce();
	}

	private ProgressDialog	progresDialog;

	@NotNull
	private OnClickListener	myListener	= new OnClickListener()
										{
											public void onClick(View v)
											{
												if (isOnline())
												{
													startParce();
												}
												else
												{
													LeaguesHandler.listLeauges = Cache.INSTANCE.readFromFile(mActivity.getExternalCacheDir());
													internetConnectionToast.show();
													showResultsList();
												}
											}

										};

	public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		return netInfo != null && netInfo.isConnectedOrConnecting() ? true : false;
	}

	public void startParce()
	{
		if (isOnline())
		{
			text = "";
			progresDialog = ProgressDialog.show(ParcerSampleActivity.this, "Working...", "request to server", true, false);
			new ParseSite().execute("http://www.football.ua");
		}
		else
		{
			internetConnectionToast.show();
			LeaguesHandler.listLeauges = Cache.INSTANCE.readFromFile(mActivity.getExternalCacheDir());
			showResultsList();
		}
	}

	private class ParseSite extends AsyncTask<String, Void, List<String>>
	{
		@Nullable
		protected List<String> doInBackground(String... arg)
		{
			DataParcer dataParcer = new DataParcer();
			leagues = dataParcer.parceScoreboard();
			LeaguesHandler.listLeauges = leagues;

			Cache.INSTANCE.cacheResults(leagues, mActivity.getExternalCacheDir());
			return null;
		}

		protected void onPostExecute(List<String> output)
		{
			progresDialog.dismiss();
			showResultsList();
		}
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
}