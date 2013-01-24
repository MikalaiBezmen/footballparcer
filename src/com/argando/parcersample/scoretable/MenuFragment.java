package com.argando.parcersample.scoretable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import com.argando.parcersample.R;
import com.argando.parcersample.data.LeaguesHandler;

public class MenuFragment extends Fragment {
    private View mMainView;
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        return mMainView = inflater.inflate(R.layout.menu, null);
    }
    @Override
    public void onActivityCreated(android.os.Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
        mMainView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i(LOG_TAG, "onTouch menuFragment");
                return getActivity().onTouchEvent(motionEvent);
            }
        });
    }

   /* private enum Leagues
    {
        Champion_League
                {
                    @Override
                    public String getName()
                    {
                        return getString (R.string.champions_league);
                    };
                },
        Europe_League,
        Ukraine,
        England,
        Argentina,
        Brazil,
        Germany,
        Italy,
        Spain,
        Netherlands,
        Portugal,
        Russia,
        North_America,
        Turkey,
        France;

        public abstract String getName();
    }*/

    private static final String LOG_TAG = MenuFragment.class.getSimpleName();
}
