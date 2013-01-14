package widget;

import android.appwidget.AppWidgetProvider;


public class Widget extends AppWidgetProvider{
    /*
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        context.startService(new Intent(context, UpdateService.class));
    }

    public static class UpdateService extends Service {

        @Override
        public void onStart(Intent intent, int startId) {
            RemoteViews updateViews = buildUpdate(this);
            AppWidgetManager.getInstance(this).updateAppWidget(new ComponentName(this, Widget.class), updateViews);
        }

        public RemoteViews buildUpdate(Context context) {

            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget);


            views.setTextViewText(R.id.matches, "test");

            return views;
        }

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }
    }
    */
}
