package com.argando.parcersample.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import com.argando.parcersample.R;

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
        for (int i = 0; i < mLinks.length; i++) {
            Log.i("i", " i");
            ContentValues values = new ContentValues();
            values.put(SQLLiteHelper.LEAGUE_NAME, mLinks[i]);
            long insertId = database.insert(SQLLiteHelper.LEAGUE_TABLE, null,
                    values);
//            Cursor cursor = database.query(SQLLiteHelper.LEAGUE_TABLE,
//                    allColumns, SQLLiteHelper.LEAGUE_ID + " = " + insertId, null,
//                    null, null, null);
//            cursor.moveToFirst();
//            cursor.close();
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
