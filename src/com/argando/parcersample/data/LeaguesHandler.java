package com.argando.parcersample.data;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeaguesHandler {
    public static List<League> mListLeauges;
    public static String mTime;

    @Nullable
    public static Match getMatchById(int id) {
        for (League league : mListLeauges) {
            for (Match match : league.getMatches()) {
                if (match.getId() == id) {
                    return match;
                }
            }
        }
        return null;
    }

    public static String match;
}
