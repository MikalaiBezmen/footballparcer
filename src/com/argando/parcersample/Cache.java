package com.argando.parcersample;

import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
//TODO remove hard string
public enum Cache
{
	INSTANCE;

	public void cacheResults(List<League> leagues, File file)
	{
		JSONObject jsMainObj = new JSONObject();
		try
		{
			jsMainObj.put("current_time", "");
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
					match.put("second_score", aMatch.getScore1());
					match.put("online_status", aMatch.isOnlineStatus());
					match.put("date", aMatch.getDate());
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
		writeToFile(jsMainObj, file);
	}

	private void writeToFile(JSONObject jsonObj, File file)
	{
		try
		{
			File f = new File(file + "football.json");
			if (f.exists())
			{
				f.delete();
			}
			FileWriter writer = new FileWriter(file + "football.json");
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
			DataInputStream dataIn = new DataInputStream(new FileInputStream(file + "football.json"));
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
			//in Log
			System.out.println("Problem finding file");
		}
		return new ArrayList<League>();
	}

	private List<League> getResultsFromString(String jsonString)
	{
		List<League> leagues = new ArrayList<League>();
		try
		{
			JSONObject jObject = new JSONObject(jsonString);
			JSONArray jsLeagues = jObject.getJSONArray("leagues");
			for (int i = 0; i < jsLeagues.length(); i++)
			{
				JSONObject league = (JSONObject) jsLeagues.get(i);
				String name = league.getString("league_name");
				Log.w("ParserSample", name);
				League tempLeague = new League(name);

				JSONArray jsMatches = league.getJSONArray("matches");
				for (int j = 0; j < jsMatches.length(); j++)
				{
					JSONObject match = (JSONObject) jsMatches.get(j);
					int id = match.getInt("id");
					String firstTeam = match.getString("first_team");
					String secondTeam = match.getString("second_team");
					String score1 = match.getString("first_score");
					String score2 = match.getString("second_score");
					int onlineStatus = match.getInt("online_status");
					String linkForOnline = match.getString("link_to_text_translation");
					String date = match.getString("date");
					Match tempMatch = new Match(date, firstTeam, secondTeam, score1, score2, name, onlineStatus, linkForOnline);
					tempMatch.setId(id);
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