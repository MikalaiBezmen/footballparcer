package com.argando.parcersample.widget;

import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;
import com.argando.parcersample.R;

public class Widget extends AppWidgetProvider{
    private static final String LOG_TAG = Widget.class.getSimpleName();

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, UpdateService.class));

        Log.w(LOG_TAG, "Widget::onUpdate(...)");
    }

    public static class UpdateService extends Service {
        private static final String LOG_TAG = UpdateService.class.getSimpleName();

        @Override
        public void onStart(Intent intent, int startId) {
            RemoteViews updateViews = buildUpdate(this);
            AppWidgetManager.getInstance(this).updateAppWidget(new ComponentName(this, Widget.class), updateViews);

            Log.w(LOG_TAG, "UpdateService::onStart");
        }

        public RemoteViews buildUpdate(Context context) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);

            views.setTextViewText(R.id.textView, "test");

            return views;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
}
