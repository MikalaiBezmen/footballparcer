package com.argando.parcersample;

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

public class SampleCategorizeListViewActivity extends Fragment
{

	public final static String		ITEM_TITLE		= "title";
	public final static String		ITEM_CAPTION	= "caption";
	public final static String		ITEM_LINK		= "link";
	public final static String		SOPCAST_LINK	= "sopcast_link";
	ListView						list;
	private Fragment				me;

	private Toast					toast;

	private OnlineWebViewListener	mOnlineWebViewListener;

	interface OnlineWebViewListener
	{
		void onCreate(String link);

		void startSopcast(String url);
	}

	public Map<String, ?> createItem(String title, String caption, String is, String link, String sopcast)
	{
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		item.put("IS", is);
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
		intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + ApkName.toString())),
				"application/vnd.android.package-archive");

		startActivity(intent);
	}

	private class Downloader extends AsyncTask<String, Void, List<String>>
	{
		protected List<String> doInBackground(String... arg)
		{
			try
			{

				String urlpath = "http://www.ex.ua/get/786058334082/25994713";
				String ApkName = "SopCast.apk";

				URL url = new URL(urlpath.toString());
				// Your given URL.
				HttpURLConnection c = (HttpURLConnection) url.openConnection();
				c.setRequestMethod("GET");
				c.setDoOutput(true);
				c.connect();
				// Connection Complete here.!
				// Toast.makeText(getApplicationContext(),
				// "HttpURLConnection complete.", Toast.LENGTH_SHORT).show();
				String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
				File file = new File(PATH); // PATH = /mnt/sdcard/download/
				if (!file.exists())
				{
					file.mkdirs();
				}
				File outputFile = new File(file, ApkName.toString());
				FileOutputStream fos = new FileOutputStream(outputFile);
				// Toast.makeText(getApplicationContext(), "SD Card Path: " +
				// outputFile.toString(), Toast.LENGTH_SHORT).show();
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
				// till here, it works fine - .apk is download to my sdcard in
				// download file.
				// So plz Check in DDMS tab and Select your Emualtor.
				// Toast.makeText(getApplicationContext(),
				// "Download Complete on SD Card.!", Toast.LENGTH_SHORT).show();
				// download the APK to sdcard then fire the Intent.
			}
			catch (IOException e)
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
			List<ResolveInfo> resolveinfo_list = getActivity().getPackageManager().queryIntentActivities(intent, 0);

			boolean find = false;
			for (ResolveInfo info : resolveinfo_list)
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
		}
		catch (ActivityNotFoundException e)
		{
			Toast.makeText(getActivity().getApplicationContext(), "There was a problem loading the application: " + application_name,
					Toast.LENGTH_SHORT).show();
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
				// getActivity().findViewById(R.id.container).setVisibility(View.GONE);
				// getActivity().findViewById(R.id.containerForMach).setVisibility(View.VISIBLE);
				// TODO Auto-generated method stub
				// Toast.makeText(getActivity().getApplicationContext(), LeaguesHandler.getMatchById((int) arg3).toString(), 20000).show();
				ScoreFragment fragment = new ScoreFragment();

				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				// fragmentTransaction.remove(me);
				fragmentTransaction.add(R.id.containerForMach, fragment);
				// fragmentTransaction.replace(R.id.container, fragment);
				fragmentTransaction.addToBackStack(null);

				fragmentTransaction.commit();

				// ((ParcerSampleActivity)getActivity()).contentView.invalidate();

			}

			public void startSopcast(String url)
			{
				startApplication("sop", url);
			}
		};

		List<League> leagues = LeaguesHandler.listLeauges;

		if (leagues == null)
			onDestroy();

		SeparatedListAdapter adapter = new SeparatedListAdapter(this.getActivity());

		int counterForMatchId = 0;
		for (int i = 0; i < leagues.size(); i++)
		{
			List<Map<String, ?>> listItems = new LinkedList<Map<String, ?>>();
			for (int j = 0; j < leagues.get(i).getSize(); j++)
			{
				String team = leagues.get(i).getMatch(j).getFirstTeam() + " | " + leagues.get(i).getMatch(j).getScore1() + " - "
						+ leagues.get(i).getMatch(j).getScore2() + " | " + leagues.get(i).getMatch(j).getSecondTeam();
				String time = leagues.get(i).getMatch(j).getDate();
				String is = "0";
				if (leagues.get(i).getMatch(j).isOnlineStatus() == 1)
				{
					is = "1";
				}
				else if (leagues.get(i).getMatch(j).isOnlineStatus() == 2)
				{
					is = "2";
				}
				counterForMatchId++;
				listItems.add(createItem(team, time, is, leagues.get(i).getMatch(j).linkForOnline, leagues.get(i).getMatch(j).linkToSopcast));
				leagues.get(i).getMatch(j).setId(counterForMatchId);
				Log.w("id", leagues.get(i).getMatch(j).getFirstTeam() + "  " + leagues.get(i).getMatch(j).getSecondTeam() + " id = "
						+ leagues.get(i).getMatch(j).getId());
			}
			adapter.addSection(leagues.get(i).getName(), new SimpleAdapter(mOnlineWebViewListener, this.getActivity(), listItems,
					R.layout.list_complex, new String[] { ITEM_TITLE, ITEM_CAPTION, ITEM_LINK, SOPCAST_LINK }, new int[] { R.id.list_complex_title,
							R.id.list_complex_caption, R.id.web_view, R.id.sop_cast }));
			counterForMatchId++;
		}

		// create our list and custom adapter
		list = new ListView(this.getActivity());
		list.setDivider(getResources().getDrawable(R.drawable.list_items_divider));
		list.setDividerHeight(10);
		list.setAdapter(adapter);
		list.setItemsCanFocus(true);

		toast = Toast.makeText(getActivity().getApplicationContext(), "file downloaded", Toast.LENGTH_LONG);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
		{
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		return list;
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroy();
		// getActivity().findViewById(R.id.containerForMach).setVisibility(View.GONE);
		// getActivity().findViewById(R.id.container).setVisibility(View.VISIBLE);
	}
}