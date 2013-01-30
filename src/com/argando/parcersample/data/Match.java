package com.argando.parcersample.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * @author argando
 */
public class Match {
    private String mDate;
    private String mFirstTeam;
    private String mSecondTeam;
    private String mScore1;
    private String mScore2;
    private String mLeague;            // Maybe league contains match
    private int mOnlineStatus = 0;        // 0 - wait, 1 - online , 2 - end
    private int id;
    public List<String> linkToSopcast = new ArrayList<String>();
    public String linkForOnline = "";

    private int matchValidation;

    public Match(String data) {
        mDate = data;
    }

    public Match(String date, String firstTeam, String secondTeam, String score1, String score2, int onlineStatus, String linkForOnline) {
        this.mDate = date;
        this.mFirstTeam = firstTeam;
        this.mSecondTeam = secondTeam;
        this.mScore1 = score1;
        this.mScore2 = score2;
        this.mOnlineStatus = onlineStatus;
        this.linkForOnline = linkForOnline;
        isMatchValid();
    }

    public Match(String date, String firstTeam, String secondTeam, String score1, String score2, String league, int onlineStatus, String linkForOnline) {
        this(date, firstTeam, secondTeam, score1, score2, onlineStatus, linkForOnline);
        this.mLeague = league;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    // ===Validation region===//
    public boolean isMatchValid() {
        if (!isDateValid() || !isFirstTeamValid() || !isSecondTeamValid() || !isResultValid() || !isLegueValid()) {
            matchValidation = 0;
            return false;
        }
        matchValidation = 1;
        return true;
    }

    private boolean isDateValid() {
        // ADD regexp for checking valid data input
        return !mDate.isEmpty();
    }

    private boolean isFirstTeamValid() {
        return true;
    }

    private boolean isSecondTeamValid() {
        return true;
    }

    private boolean isResultValid() {
        return true;
    }

    private boolean isLegueValid() {
        return true;
    }

    // ===Getters region===//
    public String getDate() {
        return this.mDate;
    }

    public String getFirstTeam() {
        return this.mFirstTeam;
    }

    public String getSecondTeam() {
        return this.mSecondTeam;
    }

    @NotNull
    public String getResult() {
        return this.mScore1 + mScore2;
    }

    public String getScore1() {
        return this.mScore1;
    }

    public String getScore2() {
        return this.mScore2;
    }

    public String getLegue() {
        return this.mLeague;
    }

    public int isOnlineStatus() {
        return this.mOnlineStatus;
    }

    public int getMatchValidation() {
        return this.matchValidation;
    }

    @NotNull
    public String toString() {
        return getFirstTeam() + " " + getSecondTeam() + " " + linkForOnline;
    }
}
