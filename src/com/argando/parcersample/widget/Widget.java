package com.argando.parcersample.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import com.argando.parcersample.R;
import com.argando.parcersample.data.League;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.data.Match;

import java.util.ArrayList;
import java.util.List;

public class Widget extends AppWidgetProvider{
    private static final String LOG_TAG = Widget.class.getSimpleName();

    public static final String ACTION_WIDGET_BUTTON_UP = "ButtonUpWidgetAction";
    public static final String ACTION_WIDGET_BUTTON_DOWN = "ButtonDownWdidgetAction";

    static private List<String> mMathces = new ArrayList<String>();
    static private int offset = 0;

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.w(LOG_TAG, "Widget::onUpdate");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        String str = null;

        for (League league : LeaguesHandler.mListLeauges)
        {
            for (Match match : league.getMatches())
            {
//                Log.w(LOG_TAG, match.getFirstTeam() + "-" + match.getSecondTeam());
                str = match.getFirstTeam() + '-' + match.getSecondTeam();
                mMathces.add(str);
//                remoteViews.setTextViewText(R.id.textView, match.getFirstTeam() + "-" + match.getSecondTeam());
            }
        }

        Log.w(LOG_TAG, String.valueOf(mMathces.size()));

        Intent intentBtnUp = new Intent(context, Widget.class);
        intentBtnUp.setAction(ACTION_WIDGET_BUTTON_UP);

        PendingIntent pendingIntentBtnUp = PendingIntent.getBroadcast(context, 0, intentBtnUp, 0);

        remoteViews.setOnClickPendingIntent(R.id.btnUp, pendingIntentBtnUp);

        Intent intent = new Intent(context, Widget.class);
        intent.setAction(ACTION_WIDGET_BUTTON_DOWN);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        remoteViews.setOnClickPendingIntent(R.id.btnDown, pendingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(LOG_TAG, "Widget::onReceive");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        if (intent.getAction().equals(ACTION_WIDGET_BUTTON_UP)){
            offset++;
            updateMatches();
//            remoteViews.setTextViewText(R.id.textView, "Button up on click");
//            Toast.makeText(context, "Button up on click!", Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(ACTION_WIDGET_BUTTON_DOWN)) {
            offset--;
            updateMatches();
//            remoteViews.setTextViewText(R.id.textView, "Button down on click");
//            Toast.makeText(context, "Button down on click!", Toast.LENGTH_LONG).show();
        }

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName thisAppWidget = new ComponentName(context.getPackageName(), Widget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

        super.onReceive(context, intent);
    }

    private void updateMatches() {
        if (offset < 0)
            offset = 0;

        if (offset >= (mMathces.size() - 5)) {
            offset = mMathces.size() - 5;
        }

        Log.w(LOG_TAG, String.valueOf(offset));
        Log.w(LOG_TAG, String.valueOf(mMathces.size()));
//        Log.w(LOG_TAG, String.valueOf(offset - mMathces.size()));
    }


//    public static class UpdateService extends Service {
//        private static final String LOG_TAG = UpdateService.class.getSimpleName();
//
//        @Override
//        public void onStart(Intent intent, int startId) {
//            RemoteViews updateViews = buildUpdate(this);
//            AppWidgetManager.getInstance(this).updateAppWidget(new ComponentName(this, Widget.class), updateViews);
//
//            Log.w(LOG_TAG, "UpdateService::onStart");
//        }
//
//        public RemoteViews buildUpdate(Context context) {
//            RemoteViews remoteViews;
//            DateFormat format = SimpleDateFormat.getTimeInstance(SimpleDateFormat.MEDIUM, Locale.getDefault());
//
//            remoteViews = new RemoteViews( context.getPackageName(), R.layout.widget );
//            remoteViews.setTextViewText( R.id.textView, "Time = " + format.format( new Date()));
//
//            return remoteViews;
//        }
//
//        @Override
//        public IBinder onBind(Intent intent) {
//            return null;
//        }
//    }
}
