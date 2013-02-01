package com.argando.parcersample.widget;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;

class WidgetData {
    String mTeam1;
    String mTeam2;
    String mTime; // may be score
    String mScore;
    int    mStatusMatch;

    WidgetData(String team1, String time, String team2, String score, int statusMatch) {
        mTeam1  =   team1;
        mTime   =   time;
        mTeam2  =   team2;
        mScore  =   score;
        mStatusMatch = statusMatch;
    }
}

/**
 * The AppWidgetProvider for widget.
 */
public class WidgetDataProvider extends ContentProvider{
    public static final String LOG_TAG = WidgetDataProvider.class.getSimpleName();

    public static final Uri CONTENT_URI = Uri.parse("content://com.argando.parcersample.widget.WidgetDataProvider");

    public static class Columns {
        public static final String TEAM1    = "team1";
        public static final String TIME     = "time"; // in this column may be score
        public static final String TEAM2    = "team2";
        public static final String SCORE    = "score";
        public static final String MATCH_STATUS = "status_match";
    }

    private static final ArrayList<WidgetData> mData = new ArrayList<WidgetData>();

    @Override
    public boolean onCreate() {

//        mData.add(new WidgetData("Dynamo", "3-0", "Barcelona"));
//        mData.add(new WidgetData("FC PNX", "5-1", "Barcelona"));
//        mData.add(new WidgetData("Elastiko", "11-0", "Barcelona"));
//        mData.add(new WidgetData("Team #41", "1-0", "Barcelona"));
//        mData.add(new WidgetData("AAHHAAH", "2-1", "Barcelona"));
//        for (League league : LeaguesHandler.mListLeauges) {
//            for (Match match : league.getMatches()) {
//                mData.add(new WidgetData(match.getFirstTeam(), match.getDate(), match.getSecondTeam()));
//            }
//        }

        Log.w(LOG_TAG, "WidgetDataProvider::onCreate()");

        return true;
    }

    @Override
    public synchronized Cursor query(Uri uri, String[] projection, String selection,
                                     String[] selectionArgs, String sortOrder) {
        assert(uri.getPathSegments().isEmpty());

        final MatrixCursor c = new MatrixCursor(
                new String[]{ Columns.TEAM1, Columns.TIME, Columns.TEAM2, Columns.SCORE, Columns.MATCH_STATUS});
        for (int i = 0; i < mData.size(); ++i) {
            final WidgetData data = mData.get(i);
            c.addRow(new Object[]{ data.mTeam1, data.mTime, data.mTeam2, data.mScore, data.mStatusMatch });
        }
        return c;
    }

    @Override
    public String getType(Uri uri) {
        return "vnd.android.cursor.dir/vnd.widget.match";
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        mData.add(new WidgetData(   values.getAsString(Columns.TEAM1),
                                    values.getAsString(Columns.TIME),
                                    values.getAsString(Columns.TEAM2),
                                    values.getAsString(Columns.SCORE),
                                    values.getAsInteger(Columns.MATCH_STATUS)));

        // notify all listeners of changes and return itemUri:
        Uri itemUri = ContentUris.withAppendedId(uri, Integer.parseInt(uri.getPathSegments().get(0)));
        getContext().getContentResolver().notifyChange(itemUri, null);

        return itemUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int numOfDeleted = mData.size();

        mData.clear();

        // notify all listeners of changes:
        if (numOfDeleted > 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numOfDeleted;
    }

    @Override
    public synchronized int update(Uri uri, ContentValues values, String selection,
                                   String[] selectionArgs) {
        assert(uri.getPathSegments().size() == 1);

        final int index = Integer.parseInt(uri.getPathSegments().get(0));
        final MatrixCursor c = new MatrixCursor(
                new String[]{ Columns.TEAM1, Columns.TIME, Columns.TEAM2,Columns.SCORE, Columns.MATCH_STATUS});

        assert(0 <= index && index < mData.size());

        final WidgetData data = mData.get(index);

        data.mTeam1 = values.getAsString(Columns.TEAM1);
        data.mTime  = values.getAsString(Columns.TIME);
        data.mTeam2 = values.getAsString(Columns.TEAM2);
        data.mScore = values.getAsString(Columns.SCORE);
        data.mStatusMatch = values.getAsInteger(Columns.MATCH_STATUS);

        // Notify any listeners that the data backing the content provider has changed, and return
        // the number of rows affected.
        getContext().getContentResolver().notifyChange(uri, null);

        return 1;
    }

}
