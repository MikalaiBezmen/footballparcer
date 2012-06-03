package com.argando.parcersample;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeaguesHandler
{
	public static List<League> listLeauges;
	
	@Nullable
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
	
	public static String match ;
}
