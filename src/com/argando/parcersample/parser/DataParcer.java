package com.argando.parcersample.parser;

import android.util.Log;
import com.argando.parcersample.data.DataNameHelper;
import com.argando.parcersample.data.League;
import com.argando.parcersample.data.Match;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DataParcer {
    private static final String LOG = "DataParcer";

    private static final String mSiteFootballTable = "http://www.football.ua/scoreboard/";
    private static final String mSiteFootballSopcast = "http://www.livefootball.ws/";
    private static URL mSiteUrl;
    private HtmlCleaner mHtmlHelper;
    private TagNode mRootElement;

    private void setUrl(String url) {
        try {
            mSiteUrl = new URL(url);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Log.w(LOG, "there is a problem with mSiteULR = " + mSiteFootballTable);
        }
    }

    public DataParcer() {
        mHtmlHelper = new HtmlCleaner();
        setUrl(mSiteFootballTable);
    }

    public List<League> parceScoreboard() {
        getRootElement();
        TagNode scoreTable = getScoreTable();
        TagNode[] leaguesData = getLeagueData(scoreTable);
        List<League> leagues = getLeagues(leaguesData);
        initLiveFootballMainPage(leagues);
        return leagues;
    }

    private void initLiveFootballMainPage(List<League> leagues) {
        setUrl(mSiteFootballSopcast);
        getRootElement();
        TagNode[] mainSop = mRootElement.getElementsByAttValue(DataNameHelper.ID, DataNameHelper.MAIN_SOP, true, false);
        if (mainSop.length == 0) {
            Log.w(LOG, "football.ws doesn't sent answer");
            return;
        }
        TagNode[] sopElement = mainSop[0].getElementsByAttValue(DataNameHelper.CLASS, "base custom", true, false);
        for (League league : leagues) {
            for (int j = 0; j < league.getSize(); j++) {
                if (league.getMatch(j).isOnlineStatus() == 1) {
                    String team1 = league.getMatch(j).getFirstTeam();
                    String team2 = league.getMatch(j).getSecondTeam();
                    findMatchesForSopcast(team1, team2, sopElement, league.getMatch(j).linkToSopcast);
                }
            }
        }
    }

    private String findMatchesForSopcast(String team1, String team2, TagNode[] sopElement, List<String> results) {
        TagNode[] element;
        for (TagNode aSopElement : sopElement) {
            element = aSopElement.getElementsByName(DataNameHelper.DIV, false);
            String name = element[0].getText().toString().trim();
            if (name.contains(team1) && name.contains(team2)) {
                Log.i(LOG, "looking for sopcast link for match " + team1 + " " + team2);
                for (TagNode aTag : aSopElement.getElementsByName("a", true)) {
                    String link = aTag.getAttributeByName("href");
                    if (link != null && link.length() > 0) {
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
            if (sopLink != null && sopLink.length() > 0 && sopLink.contains("sop://broker.sopcast.com:")) {
                Log.i(LOG, "sopcast link detected " + sopLink);
                results.add(sopLink);
            }
        }
    }

    private boolean getRootElement() {
        try {
            mRootElement = mHtmlHelper.clean(mSiteUrl);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private TagNode getScoreTable() {
        TagNode[] scoreTable = mRootElement.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.STATISTIC, true, false);
        return scoreTable[0];
    }

    private TagNode[] getLeagueData(TagNode scoreTable) {
        TagNode[] tbody = scoreTable.getElementsByName(DataNameHelper.TBODY, true);
        return tbody[0].getElementsByName(DataNameHelper.TR, false);
    }

    private List<League> getLeagues(TagNode[] leaguesData) {

        List<League> leagues = new ArrayList<League>();
        League newLeague = null;
        Match newMatch;
        for (TagNode aLeaguesData : leaguesData) {
            TagNode[] leagueNameElement = aLeaguesData.getElementsByName(DataNameHelper.SPAN, true);
            if (leagueNameElement.length > 0) {
                if (leagueNameElement[0].getAttributeByName(DataNameHelper.CLASS).equals(DataNameHelper.FOOTBALL)) {
                    newLeague = new League(aLeaguesData.getElementsByName(DataNameHelper.SPAN, true)[0].getText().toString().trim());
                    leagues.add(newLeague);
                    Log.i(LOG, "add legue " + newLeague.getName());
                }
            } else {
                String date;
                String team1;
                String team2;
                String score1;
                String score2;
                String scoreLink = "no link";
                int isOnline = 0;
                TagNode[] team1Element = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOTEAM1, true, false);
                TagNode[] team2Element = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOTEAM2, true, false);
                team1 = team1Element[0].getText().toString().trim();
                team2 = team2Element[0].getText().toString().trim();

                TagNode[] dataElement = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.CALIGN, true, false);
                date = dataElement[0].getText().toString().trim();
                date = date.substring(date.indexOf("\n") + 1, date.length());

                TagNode linkElement[] = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.MATCH_LINK, true, false);
                for (TagNode aTag : linkElement[0].getElementsByName("a", true)) {
                    String link = aTag.getAttributeByName("href");
                    if (link != null && link.length() > 0) scoreLink = link;
                }

                TagNode[] scoreElement = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOGRAYSCORE, true, false);
                if (scoreElement.length == 0) {
                    isOnline = 1;
                    scoreElement = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLORSCORE, true, false);
                    if (scoreElement.length == 0) {
                        isOnline = 2;
                        scoreElement = aLeaguesData.getElementsByAttValue(DataNameHelper.CLASS, DataNameHelper.TABLOGSCORE, true, false);
                    }
                }
                String score = scoreElement[0].getText().toString().trim();
                score1 = score.substring(0, 1);
                score2 = score.substring(score.length() - 1, score.length());
                if (!date.isEmpty() && !team1.isEmpty() && !team2.isEmpty() && !score1.isEmpty() && !score2.isEmpty()) {
                    newMatch = new Match(date, team1, team2, score1, score2, newLeague.getName(), isOnline, scoreLink);
                    newLeague.addMatch(newMatch);
                    Log.i(LOG, "match added" + team1 + " - " + team2 + "league = " + newLeague.getName() + "score = " + score1 + ":" + score2 + " isOnline " + isOnline);
                }
            }
        }
        return leagues;
    }
}
