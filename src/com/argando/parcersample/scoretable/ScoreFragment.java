package com.argando.parcersample.scoretable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import com.argando.parcersample.CustomWebViewClient;
import com.argando.parcersample.data.LeaguesHandler;
import org.jetbrains.annotations.Nullable;

public class ScoreFragment extends Fragment {
    private static final String LOG_TAG = ScoreFragment.class.getSimpleName();
    private WebView myWebView;
    private static final FrameLayout.LayoutParams ZOOM_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            super.onCreate(savedInstanceState);
            Log.i(LOG_TAG, "onCreate");
            myWebView = new WebView(this.getActivity());
            myWebView.setWebViewClient(new CustomWebViewClient());

            final FrameLayout mContentView = (FrameLayout) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
//            final View zoom = myWebView.getZoomControls();
//            mContentView.addView(zoom, ZOOM_PARAMS);
//            zoom.setVisibility(View.GONE);
            myWebView.loadUrl(LeaguesHandler.match);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
       /* if (myWebView == null) {
            myWebView = new WebView(this.getActivity());
        } else {
            ((ViewGroup) myWebView.getParent()).removeView(myWebView);
        }*/
        return myWebView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(LOG_TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        Log.i(LOG_TAG, "onDestroyView");
        super.onDestroy();
    }

}
