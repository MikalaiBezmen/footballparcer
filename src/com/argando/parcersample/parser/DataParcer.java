package com.argando.parcersample.parser;

import android.util.Log;
import com.argando.parcersample.data.DataNameHelper;
import com.argando.parcersample.data.League;
import com.argando.parcersample.data.Match;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataParcer
{
	private static final String	LOG						= "DataParcer";

	private static final String	mSiteFootballTable		= "http://www.football.ua/scoreboard/";
	private static final String	mSiteFootballSopcast	= "http://www.livefootball.ws/";
	private static URL			mSiteUrl;
	private HtmlCleaner			mHtmlHelper;
	private TagNode				mRootElement;

	private void setUrl(String url)
	{
		try
		{
			mSiteUrl = new URL(url);
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(LOG, "there is a problem with mSiteULR = " + mSiteFootballTable);
		}
	}

	public DataParcer()
	{
		mHtmlHelper = new HtmlCleaner();
		setUrl(mSiteFootballTable);
	}

	@NotNull
	public List<League> parceScoreboard()
	{
		getRootElement();
		TagNode scoreTable = getScoreTable();
		TagNode[] leaguesData = getLeagueData(scoreTable);
		List<League> leagues = getLeagues(leaguesData);
		initLiveFootballMainPage(leagues);
		return leagues;
	}

	private void initLiveFootballMainPage(@NotNull List<League> leagues)
	{
		setUrl(mSiteFootballSopcast);
		getRootElement();
		TagNode[] mainSop = mRootElement.getElementsByAttValue(DataNameHelper.ID, DataNameHelper.MAIN_SOP, true, false);
        if (mainSop.length == 0)
        {
            Log.w(LOG,"football.ws doesn't sent answer");
            return;
        }
		TagNode[] sopElement = mainSop[0].getElementsByAttValue(DataNameHelper.CLASS, "base custom", true, false);
		for (int i = 0; i < leagues.size(); i++)
		{
			for (int j = 0; j < leagues.get(i).getSize(); j++)
			{
				if (leagues.get(i).getMatch(j).isOnlineStatus() == 1)
				{
					String team1 = leagues.get(i).getMatch(j).getFirstTeam();
					String team2 = leagues.get(i).getMatch(j).getSecondTeam();
                    findMatchesForSopcast(team1, team2, sopElement, leagues.get(i).getMatch(j).linkToSopcast);
				}
			}
		}
	}

	private String findMatchesForSopcast(String team1, String team2, @NotNull TagNode[] sopElement, List<String> results)
	{
		TagNode[] element;
		for (int i = 0; i < sopElement.length; i++)
		{
			element = sopElement[i].getElementsByName(DataNameHelper.DIV, false);
			String name = element[0].getText().toString().trim();
			if (name.contains(team1) && name.contains(team2))
			{
                Log.i(LOG, "looking for sopcast link for match " + team1 + " " + team2);
				for (TagNode aTag : sopElement[i].getElementsByName("a", true))
				{
					String link = aTag.getAttributeByName("href");
					if (link != null && link.length() > 0)
                    {
						openSopcastLink(link, results);
                    }
				}
			}
		}
		return "";
	}

    private void openSopcastLink(String link, List<String> results) {
        setUrl(link);
        getRootElement();

        for (TagNode aTag : mRootElement.getElementsByName("a", true)) {
            String sopLink = aTag.getAttributeByName("href");
            if (sopLink != null && sopLink.length() > 0 && sopLink.contains("sop://broker.sopcast.com:"))
            {
                Log.i(LOG, "sopcast link detected " + sopLink);
                results.add(sopLink);
            }
        }
    }

	private boolean getRootElement()
	{
		try
		{
			mRootElement = mHtmlHelper.clean(mSiteUrl);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		return false;
	}

	private TagNode getScoreTable()
	{
		TagNode[] scoreTable = mRootElement.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.BCC, true, false);
		return scoreTable[0];
	}

	private TagNode[] getLeagueData(@NotNull TagNode scoreTable)
	{
		return scoreTable.getElementsByName(DataNameHelper.DIV, false);
	}

	@NotNull
	private List<League> getLeagues(@NotNull TagNode[] leaguesData)
	{
		List<League> leagues = new ArrayList<League>();
		League newLeague = null;
		Match newMatch;

		for (TagNode aLeaguesData : leaguesData)
		{
			if (aLeaguesData.getAttributeByName(DataNameHelper.CLASS).equals(DataNameHelper.BLINE))
			{
				if (aLeaguesData.getElementsByName(DataNameHelper.A, false).length == 0)
				{
					newLeague = new League(aLeaguesData.getElementsByName(DataNameHelper.H1, true)[0].getText().toString().trim());
				} else
				{
					newLeague = new League(aLeaguesData.getElementsByName(DataNameHelper.A, true)[0].getText().toString().trim());
				}
				leagues.add(newLeague);
				Log.w(LOG, "add legue " + newLeague.getName());
			} else if (aLeaguesData.getAttributeByName(DataNameHelper.CLASS).equals(DataNameHelper.TABLOLINE1))
			{
				String date = "";
				String team1 = "";
				String team2 = "";
				String score1 = "";
				String score2 = "";
				int isOnline = 0;
				TagNode[] dataDate = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLODATE, true, false);
				if (dataDate[0] != null)
				{
					date = dataDate[0].getText().toString().trim();
				}

				TagNode[] dataTeam1 = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOTEAM1, true, false);
				if (dataTeam1[0] != null)
				{
					team1 = dataTeam1[0].getText().toString().trim();
				}

				TagNode[] dataTeam2 = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOTEAM2, true, false);
				if (dataTeam2[0] != null)
				{
					team2 = dataTeam2[0].getText().toString().trim();
				}

				TagNode[] scoreData = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOC, true, false);

				String scoreLink = "no link";
				if (scoreData[0] != null)
				{
					// Need refactoring
					TagNode score[] = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOGSCORE, true, false);

					for (TagNode aTag : scoreData[0].getElementsByName("a", true))
					{
						String link = aTag.getAttributeByName("href");
						if (link != null && link.length() > 0) scoreLink = link;
					}

					isOnline = 2;
					if (score.length < 1)
					{
						score = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLORSCORE, true, false);
						isOnline = 1;
					}
					if (score.length < 1)
					{
						score = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOGRAYSCORE, true, false);
						isOnline = 0;
					}

					if (score.length < 1)
						Log.w(LOG, "can't find score for match" + team1 + " - " + team2 + "league = " + newLeague.getName());
					if (score[0] != null && score[1] != null)
					{
						score1 = score[0].getText().toString().trim();
						score2 = score[1].getText().toString().trim();
					}
				}
				if (!date.isEmpty() && !team1.isEmpty() && !team2.isEmpty() && !score1.isEmpty() && !score2.isEmpty())
				{
					newMatch = new Match(date, team1, team2, score1, score2, newLeague.getName(), isOnline, scoreLink);
					newLeague.addMatch(newMatch);
					Log.w(LOG, "match added" + team1 + " - " + team2 + "league = " + newLeague.getName() + "score = " + score1 + ":" + score2 + " isOnline " + isOnline);
				}
			}
		}
		return leagues;
	}

	public void getDataForMatch(int id)
	{
		TagNode[] scoreTable = mRootElement.getElementsByAttValue(DataNameHelper.CLASS, "wblock", true, false);
	}
}
