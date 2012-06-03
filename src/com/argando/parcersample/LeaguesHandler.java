package com.argando.parcersample;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeaguesHandler
{
	public static List<League> listLeauges;
	
	@Nullable
	public static Match getMatchById(int id)
	{
		for (League league : listLeauges)
		{
			for (Match match : league.getMatches())
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
