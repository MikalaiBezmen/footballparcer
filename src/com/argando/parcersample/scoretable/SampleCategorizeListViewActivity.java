package com.argando.parcersample.scoretable;

import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.argando.parcersample.R;
import com.argando.parcersample.data.League;
import com.argando.parcersample.data.LeaguesHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SampleCategorizeListViewActivity extends Fragment
{

	public final static String ITEM_TITLE = "title";
	public final static String ITEM_CAPTION = "caption";
	public final static String ITEM_LINK = "link";
	public final static String SOPCAST_LINK = "sopcast_link";
	public final static String IS_ONLINE = "is";
	ListView list;
	private Fragment me;

	private Toast toast;

	private OnlineWebViewListener mOnlineWebViewListener;

	interface OnlineWebViewListener
	{
		void onCreate(String link);

		void startSopcast(String url);
	}

	@NotNull
	public Map<String, ?> createItem(String title, String caption, String is, String link, List<String> sopcast)
	{
		Map<String, Object> item = new HashMap<String, Object>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		item.put(IS_ONLINE, is);
		item.put(ITEM_LINK, link);
		item.put(SOPCAST_LINK, sopcast);
		return item;
	}

	private void launchComponent(String packageName, String name, String url)
	{
		Intent launch_intent = new Intent("android.intent.action.MAIN");
		launch_intent.addCategory("android.intent.category.LAUNCHER");
		launch_intent.setComponent(new ComponentName(packageName, name));
		launch_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		launch_intent.setDataAndType(Uri.parse(url), "application/sop");
		getActivity().startActivity(launch_intent);
	}

	public void InstallApplication()
	{
		String ApkName = "SopCast.apk";
		String PackageName = "com.argando.sopcast";
		Uri packageURI = Uri.parse(PackageName.toString());
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, packageURI);
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + ApkName.toString())), "application/vnd.android.package-archive");

		startActivity(intent);
	}

	private class Downloader extends AsyncTask<String, Void, List<String>>
	{
		@Nullable
		protected List<String> doInBackground(String... arg)
		{
			try
			{
				//TODO create own repo
				String urlpath = "http://download.easetuner.com/download/SopCast.apk";
				String ApkName = "SopCast.apk";

				URL url = new URL(urlpath.toString());
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();
				// Connection Complete here.!
				String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
				File file = new File(PATH); // PATH = /mnt/sdcard/download/
				if (!file.exists())
				{
					file.mkdirs();
				}
				File outputFile = new File(file, ApkName.toString());
				FileOutputStream fos = new FileOutputStream(outputFile);
				InputStream is = c.getInputStream();

				// Get from Server and Catch In Input Stream Object.
				byte[] buffer = new byte[1024];
				int len1 = 0;
				while ((len1 = is.read(buffer)) != -1)
				{
					fos.write(buffer, 0, len1); // Write In FileOutputStream.
				}
				fos.close();
				is.close();
			} catch (IOException e)
			{
			}
			return null;
		}

		protected void onPostExecute(List<String> output)
		{
			toast.show();
			InstallApplication();
		}
	}

	public void startApplication(String application_name, String url)
	{
		try
		{
			Intent intent = new Intent("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");

			intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			List<ResolveInfo> resolveInfoList = getActivity().getPackageManager().queryIntentActivities(intent, 0);

			boolean find = false;
			for (ResolveInfo info : resolveInfoList)
			{
				if (info.activityInfo.packageName.contains(application_name))
				{
					launchComponent(info.activityInfo.packageName, info.activityInfo.name, url);
					find = true;
					break;
				}
			}
			if (!find)
			{
				Toast.makeText(getActivity().getApplicationContext(), "Starting downloading sopcast player to /download/", Toast.LENGTH_SHORT).show();
				new Downloader().execute("");
			}
		} catch (ActivityNotFoundException e)
		{
			Toast.makeText(getActivity().getApplicationContext(), "There was a problem loading the application: " + application_name, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		me = this;

		mOnlineWebViewListener = new OnlineWebViewListener()
		{

			public void onCreate(String link)
			{
				LeaguesHandler.match = link;
				ScoreFragment fragment = new ScoreFragment();

				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				fragmentTransaction.add(R.id.containerForMach, fragment);
				fragmentTransaction.addToBackStack(null);

				fragmentTransaction.commit();
			}

			public void startSopcast(String url)
			{
				startApplication("sop", url);
			}
		};

		List<League> leagues = LeaguesHandler.mListLeauges;

		if (leagues == null)
        {
            onDestroy();
            return;
        }

		SeparatedListAdapter adapter = new SeparatedListAdapter(this.getActivity());

		int counterForMatchId = 0;
		for (int i = 0; i < leagues.size(); i++)
		{
			List<Map<String, ?>> listItems = new LinkedList<Map<String, ?>>();
			for (int j = 0; j < leagues.get(i).getSize(); j++)
			{
				String team = leagues.get(i).getMatch(j).getFirstTeam() + " | " + leagues.get(i).getMatch(j).getScore1() + " - " + leagues.get(i).getMatch(j).getScore2() + " | " + leagues.get(i).getMatch(j).getSecondTeam();
				String time = leagues.get(i).getMatch(j).getDate();
				String is = "0";
				if (leagues.get(i).getMatch(j).isOnlineStatus() == 1)
				{
					is = "1";
				} else if (leagues.get(i).getMatch(j).isOnlineStatus() == 2)
				{
					is = "2";
				}
				counterForMatchId++;
                listItems.add(createItem(team, time, is, leagues.get(i).getMatch(j).linkForOnline, leagues.get(i).getMatch(j).linkToSopcast));
                leagues.get(i).getMatch(j).setId(counterForMatchId);
                Log.w("id", leagues.get(i).getMatch(j).getFirstTeam() + "  " + leagues.get(i).getMatch(j).getSecondTeam() + " id = " + leagues.get(i).getMatch(j).getId() + " SOPCAST LINKS COUNT = "  + leagues.get(i).getMatch(j).linkToSopcast.size());
			}
			adapter.addSection(leagues.get(i).getName(), new SimpleAdapter(mOnlineWebViewListener, this.getActivity(), listItems, R.layout.list_complex, new String[]{ITEM_TITLE, ITEM_CAPTION, ITEM_LINK, SOPCAST_LINK, SOPCAST_LINK}, new int[]{R.id.list_complex_title, R.id.list_complex_caption, R.id.web_view, R.id.sop_cast, R.id.spinner}));
			counterForMatchId++;
		}

		// create our list and custom adapter
		list = new ListView(this.getActivity());
		list.setDivider(getResources().getDrawable(R.drawable.list_items_divider));
		list.setDividerHeight(0);
		list.setAdapter(adapter);
		list.setItemsCanFocus(true);
        list.setFastScrollEnabled(false);

		toast = Toast.makeText(getActivity().getApplicationContext(), "file downloaded", Toast.LENGTH_LONG);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
		{
			return null;
		}

		return list;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroy();
	}
}