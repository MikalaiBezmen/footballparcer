package com.argando.parcersample;

import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.TagNode;

// test

public class League
{
	private List<Match>	mMatches	= new ArrayList<Match>();
	private final String mName;
	private TagNode mLegueHtmlCode;
	
	public League(String name)
	{
		mName = name;
	} 

	public boolean addMatch(Match match)
	{
		if (match == null || !match.isMatchValid())
		{
			return false;
		}
		return mMatches.add(match);
	}

	public boolean deleteMatch(Match match)
	{
		if (match == null)
		{
			return false;
		}
		return mMatches.remove(match);
	}

	public Match getMatch(int i)
	{
		if (i >= 0 && i < mMatches.size())
		{
			return mMatches.get(i);
		}
		return null;
	}
	
	public List<Match> getMatches()
	{
		return mMatches;
	}

	public boolean isMatchInLegue(Match match)
	{
		if (match == null)
		{
			return false;
		}
		return mMatches.contains(match);
	}

	public void cleanLeague()
	{
		mMatches.clear();
	}

	public int getSize()
	{
		return mMatches.size();
	}

	/**
	 * @return the name
	 */
	public String getName()
	{
		return mName;
	}

	/**
	 * @return the legueHtmlCode
	 */
	public TagNode getLegueHtmlCode()
	{
		return mLegueHtmlCode;
	}

	/**
	 * @param legueHtmlCode the legueHtmlCode to set
	 */
	public void setLegueHtmlCode(TagNode legueHtmlCode)
	{
		this.mLegueHtmlCode = legueHtmlCode;
	}
}
