package com.argando.parcersample.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.argando.parcersample.data.DataNameHelper;

public class SQLLiteHelper extends SQLiteOpenHelper {
    public static final String LEAGUE_TABLE = "leagues";
    public static final String LEAGUE_ID = "_id";
    public static final String LEAGUE_NAME = "name";
    private static final String DB_NAME = "football.db";

    public static final String MATCH_TABLE = "matches";
    public static final String MATCH_ID = "_id";
    public static final String TEAM1_NAME = "team_name1";
    public static final String TEAM2_NAME = "team_name2";
    public static final String SCORE1 = "score1";
    public static final String SCORE2 = "score2";
    public static final String DATE = "date";
    public static final String TEXT_LINK = "text_link";
    public static final String VIDEO_LINK = "video_link";

    private static final int DATABASE_VERSION = 2;

    private static final String DATA_BASE_CREATION_SQL = "create table " + LEAGUE_TABLE
            + "(" + LEAGUE_ID + " integer primary key autoincrement, " + LEAGUE_NAME
            + " text not null);";
    private static final String CREATE_MATCH_TABLE = "create table " + MATCH_TABLE
            + "(" + MATCH_ID + " integer primary key autoincrement, " + TEAM1_NAME
            + " text not null, " + TEAM2_NAME + " text not null, " +
            SCORE1 + " integer, " + SCORE2 + " integer, " + DATE + " text, " +
            TEXT_LINK + " text, " + VIDEO_LINK + " text);";

    public SQLLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DataNameHelper.EXTERNAL_CACHE_DIR + name, factory, version);
        Log.i(LOG_TAG, DataNameHelper.EXTERNAL_CACHE_DIR + name);
    }

    public SQLLiteHelper(Context context) {
        super(context, DB_NAME, null, DATABASE_VERSION);
        Log.i(LOG_TAG, DataNameHelper.EXTERNAL_CACHE_DIR + DB_NAME);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(LOG_TAG, "OnCreate");
        sqLiteDatabase.execSQL(DATA_BASE_CREATION_SQL);
        sqLiteDatabase.execSQL(CREATE_MATCH_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        Log.i(LOG_TAG, "Upgrading database from version " + i + " to "
                + i1 + ", which will destroy all old data");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + LEAGUE_TABLE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MATCH_TABLE);
        onCreate(sqLiteDatabase);
    }

    private static final String LOG_TAG = SQLLiteHelper.class.getSimpleName();
}
