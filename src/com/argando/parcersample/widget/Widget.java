package com.argando.parcersample.widget;


import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.*;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.argando.parcersample.R;
import com.argando.parcersample.data.League;
import com.argando.parcersample.data.LeaguesHandler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@TargetApi(11)
/**
 * Widget data observer just notifies an update for all weather widgets when it detects a change.
 */
class WidgetDataProviderObserver extends ContentObserver {
    private AppWidgetManager mAppWidgetManager;
    private ComponentName mComponentName;

    WidgetDataProviderObserver(AppWidgetManager mgr, ComponentName cn, Handler h) {
        super(h);

        mAppWidgetManager   = mgr;
        mComponentName      = cn;
    }

    @Override
    public void onChange(boolean selfChange) {
        // The data has changed, so notify the widget that the collection view needs to be updated.
        // In response, the factory's onDataSetChanged() will be called which will requery the
        // cursor for the new data.
        mAppWidgetManager.notifyAppWidgetViewDataChanged(
                mAppWidgetManager.getAppWidgetIds(mComponentName), R.id.matchesView);
    }
}

@TargetApi(11)
/**
 * widget's AppWidgetProvider for showing matches.
 */
public class Widget extends AppWidgetProvider {
    private final String LOG_TAG = Widget.class.getSimpleName();

    public static String ACTION_WIDGET_BTN_LEFT   = "com.argando.parcersample.widget.ACTION_WIDGET_BTN_LEFT";
    public static String ACTION_WIDGET_BTN_RIGHT  = "com.argando.parcersample.widget.ACTION_WIDGET_BTN_RIGHT";
    public static String ACTION_WIDGET_LIST_CLICK = "com.argando.parcersample.widget.ACTION_WIDGET_LIST_CLICK";

    private static HandlerThread sWorkerThread;
    private static Handler sWorkerQueue;
    private static WidgetDataProviderObserver sDataObserver;

    private static int mCurrentLeague = -1; // -1 means that need to show all matches of all league

    public Widget() {
        // Start the worker thread
        sWorkerThread = new HandlerThread("WidgetProvider-worker");
        sWorkerThread.start();
        sWorkerQueue = new Handler(sWorkerThread.getLooper());
    }

    @Override
    public void onEnabled(Context context) {
        // Register for external updates to the data to trigger an update of the widget.  When using
        // content providers, the data is often updated via a background service, or in response to
        // user interaction in the main app.  To ensure that the widget always reflects the current
        // state of the data, we must listen for changes and update ourselves accordingly.
        final ContentResolver r = context.getContentResolver();
        if (sDataObserver == null) {
            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
            final ComponentName cn = new ComponentName(context, Widget.class);
            sDataObserver = new WidgetDataProviderObserver(mgr, cn, sWorkerQueue);
            r.registerContentObserver(WidgetDataProvider.CONTENT_URI, true, sDataObserver);

            Log.w(LOG_TAG, "Widget::onEnabled()");
        }
    }

    //!!!!!!!!!!!!!!!!!!!!!!!! REFACTORING NEED !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
    @Override
    public void onReceive(Context ctx, Intent intent) {
        final String action = intent.getAction();

        Log.w(LOG_TAG, "Widget::onReceive()::"+action);

        if (action.equals(ACTION_WIDGET_BTN_LEFT)) {
            // update name of the league
            if (mCurrentLeague == 0) {
                mCurrentLeague--;

                RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.widget);
                rv.setTextViewText(R.id.league, "Все лиги");

                updateWidget(ctx, rv);

                // update matches for selected league...
                final Context context = ctx;
                sWorkerQueue.removeMessages(0);
                sWorkerQueue.post(new Runnable() {
                    @Override
                    public void run() {
                        final ContentResolver r = context.getContentResolver();
                        final List<League> leagues = LeaguesHandler.mListLeauges;

                        r.delete(WidgetDataProvider.CONTENT_URI, "", new String[]{""} );
                        // We disable the data changed observer temporarily since each of the updates
                        // will trigger an onChange() in our data observer.
                        int k = 0;
                        r.unregisterContentObserver(sDataObserver);
                        for (int i = 0; i < leagues.size(); i++) {
                            for (int j = 0; j < leagues.get(i).getSize(); j++) {
                                final Uri uri = ContentUris.withAppendedId(WidgetDataProvider.CONTENT_URI, k);
                                final ContentValues values = new ContentValues();

                                values.put(WidgetDataProvider.Columns.TEAM1, leagues.get(i).getMatch(j).getFirstTeam());
                                values.put(WidgetDataProvider.Columns.TIME, leagues.get(i).getMatch(j).getDate());
                                values.put(WidgetDataProvider.Columns.TEAM2, leagues.get(i).getMatch(j).getSecondTeam());
                                values.put(WidgetDataProvider.Columns.SCORE, leagues.get(i).getMatch(j).getScore1() + ':' + leagues.get(i).getMatch(j).getScore2());
                                values.put(WidgetDataProvider.Columns.MATCH_STATUS, leagues.get(i).getMatch(j).isOnlineStatus());

                                r.insert(uri, values);
//                                r.update(uri, values, null, null);
                                k++;
                            }
                        }
                        r.registerContentObserver(WidgetDataProvider.CONTENT_URI, true, sDataObserver);

                        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                        final ComponentName cn = new ComponentName(context, Widget.class);
                        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.matchesView);
                    }
                });

            } else if (mCurrentLeague != -1) {
                mCurrentLeague--;

                RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.widget);
                rv.setTextViewText(R.id.league, LeaguesHandler.mListLeauges.get(mCurrentLeague).getName());

                updateWidget(ctx, rv);

                // update matches for selected league...
                final Context context = ctx;
                sWorkerQueue.removeMessages(0);
                sWorkerQueue.post(new Runnable() {
                    @Override
                    public void run() {
                        if (mCurrentLeague == -1) {
                            final ContentResolver r = context.getContentResolver();
                            final List<League> leagues = LeaguesHandler.mListLeauges;

                            r.delete(WidgetDataProvider.CONTENT_URI, "", new String[]{""} );

                            int k = 0;
                            // We disable the data changed observer temporarily since each of the updates
                            // will trigger an onChange() in our data observer.
                            r.unregisterContentObserver(sDataObserver);
                            for (int i = 0; i < leagues.size(); i++) {
                                for (int j = 0; j < leagues.get(i).getSize(); j++) {
                                    final Uri uri = ContentUris.withAppendedId(WidgetDataProvider.CONTENT_URI, k);
                                    final ContentValues values = new ContentValues();

                                    values.put(WidgetDataProvider.Columns.TEAM1, leagues.get(i).getMatch(j).getFirstTeam());
                                    values.put(WidgetDataProvider.Columns.TIME, leagues.get(i).getMatch(j).getDate());
                                    values.put(WidgetDataProvider.Columns.TEAM2, leagues.get(i).getMatch(j).getSecondTeam());
                                    values.put(WidgetDataProvider.Columns.SCORE, leagues.get(i).getMatch(j).getScore1() + ':' + leagues.get(i).getMatch(j).getScore2());
                                    values.put(WidgetDataProvider.Columns.MATCH_STATUS, leagues.get(i).getMatch(j).isOnlineStatus());

                                    r.insert(uri, values);
//                                    r.update(uri, values, null, null);
                                    k++;
                                }
                            }
                            r.registerContentObserver(WidgetDataProvider.CONTENT_URI, true, sDataObserver);

                            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                            final ComponentName cn = new ComponentName(context, Widget.class);
                            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.matchesView);
                        } else {
                            final ContentResolver r = context.getContentResolver();
                            final League league = LeaguesHandler.mListLeauges.get(mCurrentLeague);

                            r.delete(WidgetDataProvider.CONTENT_URI, "", new String[]{""} );

                            // We disable the data changed observer temporarily since each of the updates
                            // will trigger an onChange() in our data observer.
                            r.unregisterContentObserver(sDataObserver);
                            for (int i = 0; i < league.getSize(); i++) {
                                final Uri uri = ContentUris.withAppendedId(WidgetDataProvider.CONTENT_URI, i);
                                final ContentValues values = new ContentValues();

                                values.put(WidgetDataProvider.Columns.TEAM1, league.getMatch(i).getFirstTeam());
                                values.put(WidgetDataProvider.Columns.TIME, league.getMatch(i).getDate());
                                values.put(WidgetDataProvider.Columns.TEAM2, league.getMatch(i).getSecondTeam());
                                values.put(WidgetDataProvider.Columns.SCORE, league.getMatch(i).getScore1() + ':' + league.getMatch(i).getScore2());
                                values.put(WidgetDataProvider.Columns.MATCH_STATUS, league.getMatch(i).isOnlineStatus());

                                r.insert(uri, values);
//                                r.update(uri, values, null, null);
                            }
                            r.registerContentObserver(WidgetDataProvider.CONTENT_URI, true, sDataObserver);

                            final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                            final ComponentName cn = new ComponentName(context, Widget.class);
                            mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.matchesView);
                        }
                    }
                });
            }
//            Toast.makeText(ctx, "LEFT BUTTON PRESS " + String.valueOf(mCurrentLeague) , Toast.LENGTH_SHORT).show();
        } else if (action.equals(ACTION_WIDGET_BTN_RIGHT)) {
            // update name of the league
            if (mCurrentLeague != (LeaguesHandler.mListLeauges.size() - 1)) {
                mCurrentLeague++;

                RemoteViews rv = new RemoteViews(ctx.getPackageName(), R.layout.widget);
                rv.setTextViewText(R.id.league, LeaguesHandler.mListLeauges.get(mCurrentLeague).getName());

                updateWidget(ctx, rv);

                // update matches for selected league...
                final Context context = ctx;
                sWorkerQueue.removeMessages(0);
                sWorkerQueue.post(new Runnable() {
                    @Override
                    public void run() {
                        final ContentResolver r = context.getContentResolver();
                        final League league = LeaguesHandler.mListLeauges.get(mCurrentLeague);

                        r.delete(WidgetDataProvider.CONTENT_URI, "", new String[]{""} );

                        // We disable the data changed observer temporarily since each of the updates
                        // will trigger an onChange() in our data observer.
                        r.unregisterContentObserver(sDataObserver);
                        for (int i = 0; i < league.getSize(); i++) {
                            final Uri uri = ContentUris.withAppendedId(WidgetDataProvider.CONTENT_URI, i);
                            final ContentValues values = new ContentValues();

                            values.put(WidgetDataProvider.Columns.TEAM1, league.getMatch(i).getFirstTeam());
                            values.put(WidgetDataProvider.Columns.TIME, league.getMatch(i).getDate());
                            values.put(WidgetDataProvider.Columns.TEAM2, league.getMatch(i).getSecondTeam());
                            values.put(WidgetDataProvider.Columns.SCORE, league.getMatch(i).getScore1() + ':' + league.getMatch(i).getScore2());
                            values.put(WidgetDataProvider.Columns.MATCH_STATUS, league.getMatch(i).isOnlineStatus());

                            r.insert(uri, values);
//                            r.update(uri, values, null, null);
                        }
                        r.registerContentObserver(WidgetDataProvider.CONTENT_URI, true, sDataObserver);

                        final AppWidgetManager mgr = AppWidgetManager.getInstance(context);
                        final ComponentName cn = new ComponentName(context, Widget.class);
                        mgr.notifyAppWidgetViewDataChanged(mgr.getAppWidgetIds(cn), R.id.matchesView);
                    }
                });
            }
//            Toast.makeText(ctx, "RIGHT BUTTON PRESS " + String.valueOf(mCurrentLeague), Toast.LENGTH_SHORT).show();
        } else if (action.equals(ACTION_WIDGET_LIST_CLICK)) {
            Toast.makeText(ctx, "LIST CLICK", Toast.LENGTH_SHORT).show();
        }

        super.onReceive(ctx, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // Update each of the widgets with the remote adapter
        for (int i = 0; i < appWidgetIds.length; ++i) {
            // Specify the service to provide data for the collection widget.  Note that we need to
            // embed the appWidgetId via the data otherwise it will be ignored.
            final Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget);
            rv.setRemoteAdapter(R.id.matchesView, intent);
            // Set the empty view to be displayed if the collection is empty.  It must be a sibling
            // view of the collection view.
            rv.setEmptyView(R.id.matchesView, R.id.empty_view);

            // set current date
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yy");
            rv.setTextViewText(R.id.date, sdf.format(new Date()));

            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // DONT WORK WHY ???? - THINK ABOUT THIS                                      !!!!!!!!!!
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // Bind a click listener template for the contents of the widget list.
            final Intent onClickIntent = new Intent(context, Widget.class);
            onClickIntent.setAction(Widget.ACTION_WIDGET_LIST_CLICK);
            onClickIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);
            onClickIntent.setData(Uri.parse(onClickIntent.toUri(Intent.URI_INTENT_SCHEME)));
            final PendingIntent onClickPendingIntent = PendingIntent.getBroadcast(context, 0, onClickIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setPendingIntentTemplate(R.id.matchesView, onClickPendingIntent);

            // bind a click intent for buttons Left / Right
            final Intent btnLeftClickIntent = new Intent(context, Widget.class);
            btnLeftClickIntent.setAction(Widget.ACTION_WIDGET_BTN_LEFT);
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            // at the end may be PendingIntent.FLAG_UPDATE_CURRENT); - read about this...!!!!!!!!!!!
            //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
            final PendingIntent btnLeftClickPendingIntent = PendingIntent.getBroadcast(context, 0, btnLeftClickIntent, 0);
            rv.setOnClickPendingIntent(R.id.btnLeft, btnLeftClickPendingIntent);

            final Intent btnRightClickIntent = new Intent(context, Widget.class);
            btnRightClickIntent.setAction(Widget.ACTION_WIDGET_BTN_RIGHT);
            final PendingIntent btnRightClickPendingIntent = PendingIntent.getBroadcast(context, 0, btnRightClickIntent, 0);
            rv.setOnClickPendingIntent(R.id.btnRight, btnRightClickPendingIntent);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);
        }

        Log.w(LOG_TAG, "Widget::onUpdate()");

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    public void updateWidget(Context ctx, RemoteViews rv) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(ctx);
        ComponentName thisAppWidget = new ComponentName(ctx.getPackageName(), Widget.class.getName());
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(thisAppWidget);

        appWidgetManager.updateAppWidget(appWidgetIds, rv);
    }

}
