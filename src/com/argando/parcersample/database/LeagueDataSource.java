package com.argando.parcersample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class LeagueDataSource {

    // Database fields
    private SQLiteDatabase database;
    private SQLLiteHelper dbHelper;
    private String[] allColumns = {SQLLiteHelper.LEAGUE_ID, SQLLiteHelper.LEAGUE_NAME};
    private String[] mLinks = {"http://football.ua/champions_league.html",
            "http://football.ua/uefa.html",
            "http://football.ua/ukraine.html",
            "http://football.ua/england.html",
            "http://football.ua/argentina.html",
            "http://football.ua/brazil.html",
            "http://football.ua/germany.html",
            "http://football.ua/italy.html",
            "http://football.ua/spain.html",
            "http://football.ua/netherlands.html",
            "http://football.ua/portugal.html",
            "http://football.ua/russia.html",
            "http://football.ua/northamerica.html",
            "http://football.ua/turkey.html",
            "http://football.ua/france.html"
    };

    public LeagueDataSource(Context context) {
        dbHelper = new SQLLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public void createData() {
        ContentValues values = new ContentValues();
        Cursor mCursor = database.rawQuery("SELECT * FROM " + SQLLiteHelper.LEAGUE_TABLE, null);
        Boolean exists = false;
        if (mCursor.moveToFirst()) exists = true;
        for (int i = 0; i < mLinks.length; i++) {
            values.put(SQLLiteHelper.LEAGUE_NAME, mLinks[i]);
            if (exists)
            {
                database.update(SQLLiteHelper.LEAGUE_TABLE, values, "_id =" + i, null);
            }else
            {
                database.insert(SQLLiteHelper.LEAGUE_TABLE, null, values);
            }
        }
    }

    public List<String> getAllComments() {
        List<String> comments = new ArrayList<String>();

        Cursor cursor = database.query(SQLLiteHelper.LEAGUE_TABLE,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            String comment = cursorToString(cursor);
            comments.add(comment);
            cursor.moveToNext();
        }
        cursor.close();
        return comments;
    }

    private String cursorToString(Cursor cursor) {
        return cursor.getString(1);
    }
}
