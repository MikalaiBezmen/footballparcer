package com.argando.parcersample.widget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.argando.parcersample.R;

@TargetApi(11)
/**
 * Service that provides the factory to be bound to the collection service.
 */
public class WidgetService extends RemoteViewsService {
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetRemoteFactory(this.getApplicationContext(), intent);
    }
}


@TargetApi(11)
/**
 * This is the factory that will provide data to the collection widget.
 */
class WidgetRemoteFactory implements RemoteViewsService.RemoteViewsFactory{
    public final static String LOG_TAG = WidgetRemoteFactory.class.getSimpleName();

    private Context mContext;
    private int mAppWidgetId;
    private Cursor mCursor;

    public WidgetRemoteFactory(Context context, Intent intent) {
        mContext = context;
        mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDestroy() {
        if (mCursor != null) {
            mCursor.close();
        }
    }

    @Override
    public int getCount() {
        Log.d(LOG_TAG, String.valueOf(mCursor.getCount()));
        return mCursor.getCount();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        // get the data for this position from the content provider
        String team1 = null;
        String team2 = null;
        String time = null;

        if (mCursor.moveToPosition(i)) {
            final int team1ColIndex = mCursor.getColumnIndex(WidgetDataProvider.Columns.TEAM1);
            final int timeColIndex  = mCursor.getColumnIndex(WidgetDataProvider.Columns.TIME);
            final int team2ColIndex = mCursor.getColumnIndex(WidgetDataProvider.Columns.TEAM2);

            team1   = mCursor.getString(team1ColIndex);
            time    = mCursor.getString(timeColIndex);
            team2   = mCursor.getString(team2ColIndex);
        }

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);

        rv.setTextViewText(R.id.team1, team1);
        rv.setTextViewText(R.id.time, time);
        rv.setTextViewText(R.id.team2, team2);

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

    @Override
    public void onDataSetChanged() {
        // Refresh the cursor
        if (mCursor != null) {
            mCursor.close();
        }
        mCursor = mContext.getContentResolver().query(WidgetDataProvider.CONTENT_URI, null, null, null, null);

        Log.w(LOG_TAG, "WidgetRemoteFactory::onDataSetChanged");
    }
}
