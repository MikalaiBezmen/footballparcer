package com.argando.parcersample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.FrameLayout;
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
		// WebSettings settings = myWebView.getSettings();
		// settings.setDefaultTextEncodingName("utf-8");
		// myWebView.getSettings().setJavaScriptEnabled(true);
		// myWebView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);
		// myWebView.getSettings().setBuiltInZoomControls(true);
		// myWebView.setInitialScale(4);
		// myWebView.getSettings().setDefaultZoom(ZoomDensity.FAR);
		final View zoom = myWebView.getZoomControls();
		mContentView.addView(zoom, ZOOM_PARAMS);
		zoom.setVisibility(View.GONE);
		myWebView.loadUrl(LeaguesHandler.match);
	}

	@Nullable
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState)
	{
		if (container == null)
		{
			// We have different layouts, and in one of them this
			// fragment's containing frame doesn't exist. The fragment
			// may still be created from its saved state, but there is
			// no reason to try to create its view hierarchy because it
			// won't be displayed. Note this is not needed -- we could
			// just run the code below, where we would create and return
			// the view hierarchy; it would just never be used.
			return null;
		}

		// return inflater.inflate(R.layout.mach_online, container, false);
		return myWebView;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState)
	{
		super.onActivityCreated(savedInstanceState);
		if (getActivity().findViewById(R.id.container) != null)
		{
			getActivity().findViewById(R.id.container).setVisibility(View.GONE);
			getActivity().findViewById(R.id.containerForMach).setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onDestroyView()
	{
		super.onDestroy();
		getActivity().findViewById(R.id.containerForMach).setVisibility(View.GONE);
		getActivity().findViewById(R.id.container).setVisibility(View.VISIBLE);
	}

}
