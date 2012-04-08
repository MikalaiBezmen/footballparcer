package com.argando.parcersample;

import java.util.List;

public class LeaguesHandler
{
	public static List<League> listLeauges;
	
	public static Match getMatchById(int id)
	{
		for (League leauge : listLeauges)
		{
			for (Match match : leauge.getMatches())
			{
				if (match.getId() == id)
				{
					return match;
				}
			}
		}
		return null;
	}
}
