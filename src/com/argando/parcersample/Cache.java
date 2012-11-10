package com.argando.parcersample;

import android.util.Log;
import com.argando.parcersample.data.League;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.data.Match;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
//TODO remove hard string
public enum Cache
{
	INSTANCE;
	private static final String LOG_TAG = Cache.class.getSimpleName();
	public void cacheResults(List<League> leagues, String cacheDir)
	{
		JSONObject jsMainObj = new JSONObject();
		try
		{
			Calendar calendar = Calendar.getInstance();
			jsMainObj.put("current_time", calendar.get(Calendar.DATE) + "." + calendar.get(Calendar.MONTH) + "." + calendar.get(Calendar.YEAR) +  " " + calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE));
			JSONArray jsListLeagues = new JSONArray();
			for (League aLeague : leagues)
			{
				JSONObject jsLeague = new JSONObject();
				jsLeague.put("league_name", aLeague.getName());

				JSONArray listMatch = new JSONArray();
				for (Match aMatch : aLeague.getMatches())
				{
					JSONObject match = new JSONObject();
					match.put("id", aMatch.getId());
					match.put("first_team", aMatch.getFirstTeam());
					match.put("second_team", aMatch.getSecondTeam());
					match.put("first_score", aMatch.getScore1());
					match.put("second_score", aMatch.getScore2());
					match.put("online_status", aMatch.isOnlineStatus());
					match.put("date", aMatch.getDate());
                    JSONArray listSopcastLinks = new JSONArray();
                    int counter = 0;
                    for (String aSopcastLink : aMatch.linkToSopcast)
                    {
                        JSONObject link = new JSONObject();
                        Log.i(LOG_TAG, "sopcast link write to cache = " + counter + " " + aSopcastLink);
                        link.put("sopcastlinks", aSopcastLink);
                        listSopcastLinks.put(link);
                        counter++;
                    }
                    match.put("sopcast_links", listSopcastLinks);
					match.put("link_to_text_translation", aMatch.linkForOnline);
					Log.w("ParserSample", aMatch.linkForOnline);
					match.put("link_to_video_translation", aMatch.linkToSopcast);
					listMatch.put(match);
				}
				jsLeague.put("matches", listMatch);
				jsListLeagues.put(jsLeague);
			}
			jsMainObj.put("leagues", jsListLeagues);
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		writeToFile(jsMainObj, cacheDir);
	}

	private void writeToFile(JSONObject jsonObj, String cacheDir)
	{
		try
		{
			File f = new File(cacheDir + "football.json");
			Log.w(LOG_TAG, "write  to " + cacheDir + "/football.json");
			if (f.exists())
			{
				f.delete();
			}
			FileWriter writer = new FileWriter(cacheDir + "/football.json");
			writer.write(jsonObj.toString());
			writer.flush();
			writer.close();

		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public List<League> readFromFile(File file)
	{
		try
		{
			Log.w(LOG_TAG,"read from " + file + "/football.json");
			DataInputStream dataIn = new DataInputStream(new FileInputStream(file + "/football.json"));
			Writer writer = new StringWriter();
			char[] buffer = new char[1024];
			try
			{
				Reader reader = new BufferedReader(new InputStreamReader(dataIn, "UTF-8"));
				int n;
				while ((n = reader.read(buffer)) != -1)
				{
					writer.write(buffer, 0, n);
				}
			} finally
			{
				dataIn.close();
			}
			String jsonString = writer.toString();
			return getResultsFromString(jsonString);
		} catch (IOException e)
		{
			Log.e(LOG_TAG,"Problem finding file");
		}
		return new ArrayList<League>();
	}

	private List<League> getResultsFromString(String jsonString)
	{
		List<League> leagues = new ArrayList<League>();
		try
		{
			JSONObject jObject = new JSONObject(jsonString);
			LeaguesHandler.mTime = jObject.getString("current_time");
			JSONArray jsLeagues = jObject.getJSONArray("leagues");
			for (int i = 0; i < jsLeagues.length(); i++)
			{
				JSONObject league = (JSONObject) jsLeagues.get(i);
				String name = league.getString("league_name");
				Log.i(LOG_TAG,"reading league " + name);
				League tempLeague = new League(name);

				JSONArray jsMatches = league.getJSONArray("matches");
				for (int j = 0; j < jsMatches.length(); j++)
				{
					JSONObject match = (JSONObject) jsMatches.get(j);
					int id = match.getInt("id");
                    Log.i(LOG_TAG,"reading match id = " + id);
					String firstTeam = match.getString("first_team");
					String secondTeam = match.getString("second_team");
                    Log.w(LOG_TAG,"reading match team  " + firstTeam + " " + secondTeam );
					String score1 = match.getString("first_score");
					String score2 = match.getString("second_score");
                    Log.w(LOG_TAG,"reading match score  " + score1 + " " + score2 );
					int onlineStatus = match.getInt("online_status");
                    Log.w(LOG_TAG,"reading match status =  " + onlineStatus);
                    JSONArray listSopcastLinks = match.getJSONArray("sopcast_links");
                    List<String> sopcastLinks = new ArrayList<String>();
                    for (int k = 0; k < listSopcastLinks.length(); k++)
                    {
                        JSONObject link = (JSONObject) listSopcastLinks.get(k);
                        Log.w(LOG_TAG,"reading match sopcast link #" + k + " link = " + link);
                        sopcastLinks.add(link.getString("sopcastlinks"));
                    }
					String linkForOnline = match.getString("link_to_text_translation");
                    Log.w(LOG_TAG,"reading match link_to_text_translation =  " + linkForOnline);
					String date = match.getString("date");
                    Log.w(LOG_TAG,"reading match date =  " + date);
					Match tempMatch = new Match(date, firstTeam, secondTeam, score1, score2, name, onlineStatus, linkForOnline);
					tempMatch.setId(id);
                    tempMatch.linkToSopcast = sopcastLinks;
					tempLeague.addMatch(tempMatch);
				}
				leagues.add(tempLeague);
			}
			return leagues;
		} catch (JSONException e)
		{
			e.printStackTrace();
		}
		return new ArrayList<League>();
	}
}