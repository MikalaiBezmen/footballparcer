package com.argando.parcersample.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.argando.parcersample.R;
import com.argando.parcersample.data.League;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.data.Match;

import java.util.ArrayList;
import java.util.List;

@TargetApi(11)
public class WidgetRemoteFactory implements RemoteViewsService.RemoteViewsFactory{
    public final static String LOG_TAG = WidgetRemoteFactory.class.getSimpleName();

//    private static final int mCount = 10;
//    private List<WidgetItem> mWidgetItems = new ArrayList<WidgetItem>();
    private List<Match> mWidgetItems = new ArrayList<Match>();

    private Context mContext;
    private int mAppWidgetId;

    public WidgetRemoteFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

        Log.w(LOG_TAG, "WidgetRemoteFactory");
    }

    @Override
    public void onCreate() {
        mWidgetItems.clear();

        for (League league : LeaguesHandler.mListLeauges) {
            for (Match match : league.getMatches()) {
//                mWidgetItems.add(new String(match.getFirstTeam() + ':' + match.getSecondTeam()));
                mWidgetItems.add(match);
            }
        }

        Log.w(LOG_TAG, String.valueOf(mWidgetItems.size()));
    }

    @Override
    public void onDataSetChanged() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void onDestroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getCount() {
        return mWidgetItems.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        rv.setTextViewText(R.id.team1, mWidgetItems.get(i).getFirstTeam());
        rv.setTextViewText(R.id.time, mWidgetItems.get(i).getDate());
        rv.setTextViewText(R.id.team2, mWidgetItems.get(i).getSecondTeam());

        // Return the remote views object.
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
