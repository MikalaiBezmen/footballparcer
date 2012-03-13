package com.argando.parcersample;

/**
 * @author argando
 *
 */
public class Match
{
	private String		mDate;
	private String		mFirstTeam;
	private String		mSecondTeam;
	private String		mResult;
	private String		mLegue;			// Maybe league contains match
	private boolean		mOnlineStatus;		// if false than match is off or not started

	private int	matchValidation;

	public Match(String date, String firstTeam, String secondTeam, String result, boolean onlineStatus)
	{
		this.mDate = date;
		this.mFirstTeam = firstTeam;
		this.mSecondTeam = secondTeam;
		this.mResult = result;
		this.mOnlineStatus = onlineStatus;
		isMatchValid();
	}

	public Match(String date, String firstTeam, String secondTeam, String result, String legue, boolean onlineStatus)
	{
		this(date, firstTeam, secondTeam, result, onlineStatus);
		this.mLegue = legue;
	}
	
	
	
	//===Validation region===//
	public boolean isMatchValid()
	{
		if (!isDateValid() || !isFirstTeamValid() || !isSecondTeamValid() || !isResultValid() || !isLegueValid())
		{
			matchValidation = 0;
			return false;
		}
		matchValidation = 1;
		return true;
	}

	private boolean isDateValid()
	{
		// ADD regexp for checking valid data input
		if (mDate.isEmpty())
		{
			return false;
		}
		return true;
	}

	private boolean isFirstTeamValid()
	{
		return true;
	}

	private boolean isSecondTeamValid()
	{
		return true;
	}

	private boolean isResultValid()
	{
		return true;
	}

	private boolean isLegueValid()
	{
		return true;
	}
	
	
	//===Getters region===//
	public String getDate()
	{
		return this.mDate;
	}

	public String getFirstTeam()
	{
		return this.mFirstTeam;
	}

	public String getSecondTeam()
	{
		return this.mSecondTeam;
	}

	public String getResult()
	{
		return this.mResult;
	}

	public String getLegue()
	{
		return this.mLegue;
	}

	public boolean isOnlineStatus()
	{
		return this.mOnlineStatus;
	}

	public int getMatchValidation()
	{
		return this.matchValidation;
	}
}
