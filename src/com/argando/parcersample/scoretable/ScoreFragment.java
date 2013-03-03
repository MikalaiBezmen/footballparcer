package com.argando.parcersample.scoretable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.*;
import android.webkit.WebView;
import android.widget.FrameLayout;
import com.argando.parcersample.CustomWebViewClient;
import com.argando.parcersample.data.LeaguesHandler;

public class ScoreFragment extends Fragment
{
    private static final String LOG_TAG = ScoreFragment.class.getSimpleName();
	private WebView									myWebView;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
        if (savedInstanceState == null) {
            super.onCreate(savedInstanceState);
            Log.i(LOG_TAG, "onCreate");
            myWebView = new WebView(this.getActivity());
            myWebView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            myWebView.setWebViewClient(new CustomWebViewClient());
            myWebView.loadUrl(LeaguesHandler.match);
        }
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
       /* if (myWebView == null) {
            myWebView = new WebView(this.getActivity());
        } else {
            ((ViewGroup) myWebView.getParent()).removeView(myWebView);
        }*/
		return myWebView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
        Log.i(LOG_TAG, "onActivityCreated");
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView()
	{
        Log.i(LOG_TAG, "onDestroyView");
		super.onDestroy();
	}
}
