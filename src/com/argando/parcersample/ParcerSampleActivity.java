package com.argando.parcersample;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;
import com.argando.parcersample.backgroundupdate.ParceService;
import com.argando.parcersample.data.DataNameHelper;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.scoretable.SampleCategorizeListViewActivity;

import java.io.File;

public class ParcerSampleActivity extends FragmentActivity {
    private static final String LOG_TAG = ParcerSampleActivity.class.getSimpleName();
    private Toast mInternetConnectionToast;
    private Activity mActivity;
    SampleCategorizeListViewActivity fragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "onCreate");
        mActivity = this;
        setContentView(R.layout.main);
        Log.w(LOG_TAG, " = " + mActivity.getCacheDir().toString());
        DataNameHelper.EXTERNAL_CACHE_DIR = mActivity.getCacheDir().toString();
        LeaguesHandler.mListLeauges = Cache.INSTANCE.readFromFile(new File(DataNameHelper.EXTERNAL_CACHE_DIR));
        resetUpdateService(500);
    }

    private void initUIComponents()
    {
        mInternetConnectionToast = Toast.makeText(getBaseContext(), DataNameHelper.NO_INTERNET_CONNECTION, DataNameHelper.NO_INTERNER_CONNNECTION_TOAST_TIME);
    }

    @Override
    public void onResume() {
        super.onResume();
        showResultsList();
    }

    private void resetUpdateService(int time) {
        Intent parseService = new Intent(this, ParceService.class);
        stopService(parseService);
        parseService.putExtra("time", time);
        startService(parseService);
    }

    private void showResultsList() {
        Log.i(LOG_TAG, "showResultsList");
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment != null) {
            fragment.onAttach(this);
            fragment.updateData();
        } else {
            fragment = new SampleCategorizeListViewActivity();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commit();
        }
    }

    @Override
    protected void onDestroy() {
        Log.i(LOG_TAG, "onDestroy");
        super.onDestroy();
    }
}