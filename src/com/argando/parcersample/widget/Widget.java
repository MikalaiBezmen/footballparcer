package com.argando.parcersample.widget;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.argando.parcersample.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Widget extends AppWidgetProvider{
    private static final String LOG_TAG = Widget.class.getSimpleName();

    public static final String ACTION_WIDGET_BUTTON_UP = "ButtonUpWidgetAction";
    public static final String ACTION_WIDGET_BUTTON_DOWN = "ButtonDownWdidgetAction";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Log.w(LOG_TAG, "Widget::onUpdate");

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget);

        Intent btnUpIntent = new Intent(context, Widget.class);
        btnUpIntent.setAction(ACTION_WIDGET_BUTTON_UP);

        PendingIntent btnUpPendgingIntent = PendingIntent.getBroadcast(context, 0, btnUpIntent, 0);

        remoteViews.setOnClickPendingIntent(R.id.btnUp, btnUpPendgingIntent);

        appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.w(LOG_TAG, "Widget::onReceive");

        if (intent.getAction().equals(ACTION_WIDGET_BUTTON_UP)){
            Toast.makeText(context, "Button up on click!", Toast.LENGTH_LONG).show();
        } else if (intent.getAction().equals(ACTION_WIDGET_BUTTON_DOWN)) {
            Toast.makeText(context, "Button down on click!", Toast.LENGTH_LONG).show();
        }

        super.onReceive(context, intent);
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
