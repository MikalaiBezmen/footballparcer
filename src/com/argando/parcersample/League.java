package com.argando.parcersample;

import java.util.ArrayList;
import java.util.List;

public class League
{
	private List<Match>	mMatches	= new ArrayList<Match>();

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
			return null;
		}
		return mMatches.get(i);
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
}
