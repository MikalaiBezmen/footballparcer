package com.argando.parcersample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.FrameLayout;

public class ScoreFragment extends Fragment
{

	private WebView									myWebView;
	private static final FrameLayout.LayoutParams	ZOOM_PARAMS	= new FrameLayout.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
																		ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.BOTTOM);

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		myWebView = new WebView(getActivity().getApplicationContext());

		final FrameLayout mContentView = (FrameLayout) getActivity().getWindow().getDecorView().findViewById(android.R.id.content);
		
//		Button button = (Button) this.getView().findViewById(R.id.start);

		// Регистрируем onClick слушателя

//		button.setOnClickListener(new OnClickListener()
//		{
			
//			public void onClick(View v)
//			{
				final View zoom = myWebView.getZoomControls();
				mContentView.addView(zoom, ZOOM_PARAMS);
				zoom.setVisibility(View.GONE);
//				URL url = new URL(LeaguesHandler.match);
				Log.w("sss", LeaguesHandler.match);
				myWebView.loadUrl(LeaguesHandler.match);
//				myWebView.loadData(summary, "text/html", null);
//			}
//		});
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
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

		//return inflater.inflate(R.layout.mach_online, container, false);
		return myWebView;
	}
	
	
	 @Override
	    public void onActivityCreated(Bundle savedInstanceState) {
	        super.onActivityCreated(savedInstanceState);
	        
	    }
	@Override
	 public void onDestroyView ()
	 {
		 super.onDestroy();
		 getActivity().findViewById(R.id.containerForMach).setVisibility(View.GONE);
		 getActivity().findViewById(R.id.container).setVisibility(View.VISIBLE);
	 }

}
