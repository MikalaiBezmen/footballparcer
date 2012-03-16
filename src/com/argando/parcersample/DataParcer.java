package com.argando.parcersample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.util.Log;

public class DataParcer
{
	private static final String	LOG		= DataParcer.class.getName();

	private static final String	mSite	= "http://www.football.ua/scoreboard/";
	private static URL			mSiteUrl;
	private HtmlCleaner			mHtmlHelper;
	private TagNode				mRootElement;
	static
	{
		try
		{
			mSiteUrl = new URL(mSite);
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(LOG, "there is a problem with mSiteULR = " + mSite);
		}
	}

	public DataParcer()
	{
		mHtmlHelper = new HtmlCleaner();
	}

	public DataParcer(URL mPage)
	{
		this();
		try
		{
			mSiteUrl = new URL(mSite);
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			Log.w(LOG, "there is a problem with mPageULR = " + mPage);
		}
	}

	public boolean parceScoreboard()
	{
		getRootElement();
		TagNode scoreTable = getScoreTable();
		TagNode[] leaguesData = getLeagueData(scoreTable);
		List<League> legues = getLeagues(leaguesData);
		return true;
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
		TagNode[] scoreTable = mRootElement.getElementsByAttValue(HtmlHelper.CLASS, HtmlHelper.BCC, true, false);
		return scoreTable[0];
	}

	private TagNode[] getLeagueData(TagNode scoreTable)
	{
		TagNode[] rootElementForLeague = scoreTable.getElementsByName(HtmlHelper.DIV, false);
		return rootElementForLeague;
	}

	private List<League> getLeagues(TagNode[] leaguesData)
	{
		List<League> leagues = new ArrayList<League>();
		League newLeague = null;
		Match newMatch;

		for (int i = 0; i < leaguesData.length; i++)
		{
			if (leaguesData[i].getAttributeByName(HtmlHelper.CLASS).equals(HtmlHelper.BLINE))
			{
				newLeague = new League(leaguesData[i].getElementsByName(HtmlHelper.A, true)[0].getText().toString().trim());
				leagues.add(newLeague);
			}
			else if (leaguesData[i].getAttributeByName(HtmlHelper.CLASS).equals(HtmlHelper.TABLOLINE1))
			{
				String date = "";
				String team1 = "";
				String team2 = "";
				String score1 = "";
				String score2 = "";
				TagNode[] dateData = leaguesData[i].getElementsByAttValue(HtmlHelper.CLASS, HtmlHelper.TABLODATE, true, false);
				if (dateData[0] != null)
				{
					date = dateData[0].getText().toString().trim();
				}

				TagNode[] dateTeam1 = leaguesData[i].getElementsByAttValue(HtmlHelper.CLASS, HtmlHelper.TABLOTEAM1, true, false);
				if (dateTeam1[0] != null)
				{
					team1 = dateTeam1[0].getText().toString().trim();
				}

				TagNode[] dateTeam2 = leaguesData[i].getElementsByAttValue(HtmlHelper.CLASS, HtmlHelper.TABLOTEAM2, true, false);
				if (dateTeam2[0] != null)
				{
					team2 = dateTeam2[0].getText().toString().trim();
				}

				TagNode[] scoreData = leaguesData[i].getElementsByAttValue(HtmlHelper.CLASS, HtmlHelper.TABLOC, true, false);
				if (scoreData[0] != null)
				{
					TagNode[] score = leaguesData[i].getElementsByAttValue(HtmlHelper.CLASS, HtmlHelper.TABLOGSCORE, true, false);
					if (score.length < 1)
					{
						score = leaguesData[i].getElementsByAttValue(HtmlHelper.CLASS, HtmlHelper.TABLORSCORE, true, false);
					}
					else if (score.length < 1)
					{
						score = leaguesData[i].getElementsByAttValue(HtmlHelper.CLASS, HtmlHelper.TABLOGRAYSCORE, true, false);
					}
					
					if (score[0] != null && score[1] != null)
					{
						score1 = score[0].getText().toString().trim();
						score2 = score[1].getText().toString().trim();
					}
				}

				if (!date.isEmpty() && !team1.isEmpty() && !team2.isEmpty() && !score1.isEmpty() && !score2.isEmpty())
				{
					newMatch = new Match(date, team1, team2, score1, score2, newLeague.getName(), true);
					if (newLeague != null)
					{
						newLeague.addMatch(newMatch);
					}
				}
			}
		}
		return leagues;
	}
}
