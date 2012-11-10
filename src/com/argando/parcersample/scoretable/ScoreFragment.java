package com.argando.parcersample.scoretable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;
import com.argando.parcersample.CustomWebViewClient;
import com.argando.parcersample.R;
import com.argando.parcersample.data.LeaguesHandler;
import org.jetbrains.annotations.Nullable;

public class ScoreFragment extends Fragment
{

	private WebView									myWebView;
	private static final FrameLayout.LayoutParams	ZOOM_PARAMS	= new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
																		ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);

		myWebView = new WebView(this.getActivity());
		myWebView.setWebViewClient(new CustomWebViewClient());

		final FrameLayout mContentView = (FrameLayout) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
		final View zoom = myWebView.getZoomControls();
		mContentView.addView(zoom, ZOOM_PARAMS);
		zoom.setVisibility(View.GONE);
		myWebView.loadUrl(LeaguesHandler.match);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
        myWebView = new WebView(this.getActivity());
        myWebView.setWebViewClient(new CustomWebViewClient());

        final FrameLayout mContentView = (FrameLayout) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
        final View zoom = myWebView.getZoomControls();
        mContentView.addView(zoom, ZOOM_PARAMS);
        zoom.setVisibility(View.GONE);
        myWebView.loadUrl(LeaguesHandler.match);

		return myWebView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroy();
	}

}
