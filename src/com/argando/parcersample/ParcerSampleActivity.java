package com.argando.parcersample;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.format.Time;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.argando.parcersample.backgroundupdate.ParceService;
import com.argando.parcersample.data.DataNameHelper;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.database.LeagueDataSource;
import com.argando.parcersample.scoretable.MatchListFragment;
import com.argando.parcersample.scoretable.MenuFragment;
import com.argando.parcersample.scoretable.ScoreFragment;

import java.io.File;

public class ParcerSampleActivity extends FragmentActivity {
    private static final String LOG_TAG = ParcerSampleActivity.class.getSimpleName();
    private Toast mInternetConnectionToast;
    private Activity mActivity;
    MatchListFragment mMatchFragment;
    private MenuFragment mMenuFragment;
    private LeagueDataSource mLeagueDataSource;
    private ProgressBar mUpdatingBar;
    private TextView mLastUpdateInfo;
    private ActivityNotifier mActivityNotifier;
    public static final String UPDATE_END = "com.argando.footballparcer.UPDATE_DATA";
    public static final String UPDATE_START = "com.argando.footballparcer.START_UPDATE_DATA";
    private ViewPager mPager;
    private ScreenSlidePagerAdapter mPagerAdapter;
    private static final int NUM_PAGES = 3;
    private ScoreFragment mScoreFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        mActivity = this;
        setContentView(R.layout.main);
        mUpdatingBar = (ProgressBar) findViewById(R.id.updateProgressBar);
        mLastUpdateInfo = (TextView) findViewById(R.id.lastUpdate);
        Log.w(LOG_TAG, " = " + mActivity.getCacheDir().toString());
        DataNameHelper.EXTERNAL_CACHE_DIR = mActivity.getCacheDir().toString();
        LeaguesHandler.mListLeauges = Cache.INSTANCE.readFromFile(new File(DataNameHelper.EXTERNAL_CACHE_DIR));
        resetUpdateService(500);
        initUIComponents();
        mLeagueDataSource = new LeagueDataSource(this);
        mLeagueDataSource.open();
        mLeagueDataSource.createData();
        mLeagueDataSource.getAllComments();
        mLeagueDataSource.close();
        IntentFilter filter = new IntentFilter();
        filter.addAction(UPDATE_END);
        filter.addAction(UPDATE_START);
        registerReceiver(mActivityNotifier = new ActivityNotifier(), filter);
        // Instantiate a ViewPager and a PagerAdapter.
        mMatchFragment = new MatchListFragment();
        mScoreFragment = new ScoreFragment();
        mMenuFragment = new MenuFragment();
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setCurrentItem(1);
    }

    private void initUIComponents() {
        mInternetConnectionToast = Toast.makeText(getBaseContext(), DataNameHelper.NO_INTERNET_CONNECTION, DataNameHelper.NO_INTERNER_CONNNECTION_TOAST_TIME);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void resetUpdateService(int time) {
        Intent parseService = new Intent(this, ParceService.class);
        stopService(parseService);
        parseService.putExtra("time", time);
        startService(parseService);
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        unregisterReceiver(mActivityNotifier);
        super.onDestroy();
    }

    public class ActivityNotifier extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UPDATE_END)) {
                LeaguesHandler.mListLeauges = Cache.INSTANCE.readFromFile(new File(DataNameHelper.EXTERNAL_CACHE_DIR));
                ParcerSampleActivity.this.mMatchFragment.updateData();
                mUpdatingBar.setVisibility(View.INVISIBLE);
                Time today = new Time(Time.getCurrentTimezone());
                today.setToNow();
                mLastUpdateInfo.setText("Updated " + today.monthDay + "/" + today.month + "/" + today.year + "  " + today.format("%k:%M:%S"));
            } else if (intent.getAction().equals(UPDATE_START)) {
                mUpdatingBar.setVisibility(View.VISIBLE);
            }
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(android.support.v4.app.FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0:
                    return mMenuFragment;
                case 1:
                    return mMatchFragment;
                case 2:
                    return mScoreFragment;
                default:
                    return mMatchFragment;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    public void updateScoreData() {
        mScoreFragment.updateData();
        mPager.setCurrentItem(2, true);
    }
}