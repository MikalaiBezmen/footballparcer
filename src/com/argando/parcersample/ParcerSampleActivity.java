package com.argando.parcersample;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.LinearLayout;
import android.widget.Toast;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ParcerSampleActivity extends FragmentActivity
{
	String			date;
	String			firstTeam;
	String			secondTeam;
	String			result;
	String			result2;
	@NotNull
	String			text	= "";

	@NotNull
	List<League>	leagues	= new ArrayList<League>();

	Toast			internetConnectionToast;

	public View		contentView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		Button button = (Button) findViewById(R.id.parse);

		button.setOnClickListener(myListener);

		internetConnectionToast = Toast.makeText(getBaseContext(), "There is no internet connection", 20000);

		ViewGroup view = (ViewGroup) getWindow().getDecorView();

		contentView = (LinearLayout) view.getChildAt(0);

		FragmentManager fragmentManager = getSupportFragmentManager();

		if (null == fragmentManager.findFragmentByTag(SampleCategorizeListViewActivity.class.getSimpleName()))
		{
			startParce();
		}
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
													// text = "";
													// progresDialog = ProgressDialog.show(ParcerSampleActivity.this, "Working...",
													// "request to server",
													// true, false);
													// new ParseSite().execute("http://www.football.ua");
												}
												else
												{
													internetConnectionToast.show();
												}
											}

										};

	public boolean isOnline()
	{
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting())
		{
			return true;
		}
		return false;
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
			return null;
		}

		protected void onPostExecute(List<String> output)
		{
			progresDialog.dismiss();

			FragmentManager fragmentManager = getSupportFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
			SampleCategorizeListViewActivity fragment = new SampleCategorizeListViewActivity();
			// fragmentTransaction.add(R.id.container, fragment);
			fragmentTransaction.add(R.id.container, fragment, SampleCategorizeListViewActivity.class.getSimpleName());
			// fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();

		}
	}
}