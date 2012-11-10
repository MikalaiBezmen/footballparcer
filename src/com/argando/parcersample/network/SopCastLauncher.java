package com.argando.parcersample.network;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.widget.Toast;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: argando
 * Date: 11/8/12
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
public class SopCastLauncher {
    private WeakReference<Fragment> mActivity;

    public SopCastLauncher(WeakReference<Fragment> activity) {
        mActivity = activity;
    }

    private void launchComponent(String packageName, String name, String url) {
        Intent launch_intent = new Intent("android.intent.action.MAIN");
        launch_intent.addCategory("android.intent.category.LAUNCHER");
        launch_intent.setComponent(new ComponentName(packageName, name));
        launch_intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        launch_intent.setDataAndType(Uri.parse(url), "application/sop");
        mActivity.get().startActivity(launch_intent);
    }

    public void InstallApplication() {
        String ApkName = "SopCast.apk";
        String PackageName = "com.argando.sopcast";
        Uri packageURI = Uri.parse(PackageName.toString());
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, packageURI);
        intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/download/" + ApkName.toString())), "application/vnd.android.package-archive");

        mActivity.get().startActivity(intent);
    }

    private class Downloader extends AsyncTask<String, Void, List<String>> {
        @Nullable
        protected List<String> doInBackground(String... arg) {
            try {
                //TODO create own repo
                String urlpath = "http://download.easetuner.com/download/SopCast.apk";
                String ApkName = "SopCast.apk";

                URL url = new URL(urlpath.toString());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.setDoOutput(true);
                c.connect();
                // Connection Complete here.!
                String PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/download/";
                File file = new File(PATH); // PATH = /mnt/sdcard/download/
                if (!file.exists()) {
                    file.mkdirs();
                }
                File outputFile = new File(file, ApkName.toString());
                FileOutputStream fos = new FileOutputStream(outputFile);
                InputStream is = c.getInputStream();

                // Get from Server and Catch In Input Stream Object.
                byte[] buffer = new byte[1024];
                int len1 = 0;
                while ((len1 = is.read(buffer)) != -1) {
                    fos.write(buffer, 0, len1); // Write In FileOutputStream.
                }
                fos.close();
                is.close();
            } catch (IOException e) {
            }
            return null;
        }

        protected void onPostExecute(List<String> output) {
            InstallApplication();
        }
    }

    public void startApplication(String application_name, String url) {
        try {
            Intent intent = new Intent("android.intent.action.MAIN");
            intent.addCategory("android.intent.category.LAUNCHER");

            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            List<ResolveInfo> resolveInfoList = mActivity.get().getActivity().getPackageManager().queryIntentActivities(intent, 0);

            boolean find = false;
            for (ResolveInfo info : resolveInfoList) {
                if (info.activityInfo.packageName.contains(application_name)) {
                    launchComponent(info.activityInfo.packageName, info.activityInfo.name, url);
                    find = true;
                    break;
                }
            }
            if (!find) {
                Toast.makeText(mActivity.get().getActivity().getApplicationContext(), "Starting downloading sopcast player to /download/", Toast.LENGTH_SHORT).show();
                new Downloader().execute("");
            }
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mActivity.get().getActivity().getApplicationContext(), "There was a problem loading the application: " + application_name, Toast.LENGTH_SHORT).show();
        }
    }
}
