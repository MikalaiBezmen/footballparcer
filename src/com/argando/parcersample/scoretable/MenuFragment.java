package com.argando.parcersample.scoretable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.argando.parcersample.ParcerSampleActivity;
import com.argando.parcersample.R;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.database.LeagueDataSource;

import java.util.List;

public class MenuFragment extends Fragment {
    private View mMainView;
    private ListView mMenuList;
    private String[] mLeaguesArray;
    private String[] mLinks = {"http://football.ua/champions_league.html",
            "http://football.ua/uefa.html",
            "http://football.ua/ukraine.html",
            "http://football.ua/england.html",
            "http://football.ua/argentina.html",
            "http://football.ua/brazil.html",
            "http://football.ua/germany.html",
            "http://football.ua/italy.html",
            "http://football.ua/spain.html",
            "http://football.ua/netherlands.html",
            "http://football.ua/portugal.html",
            "http://football.ua/russia.html",
            "http://football.ua/northamerica.html",
            "http://football.ua/turkey.html",
            "http://football.ua/france.html"
    };
    private ArrayAdapter arrayAdapter;
    LeagueDataSource mLeagueDataSource;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            mLeagueDataSource = new LeagueDataSource(getActivity());
            mMenuList = new ListView(this.getActivity());
            mLeagueDataSource.open();
            List<String> values = mLeagueDataSource.getAllComments();
            Log.i(LOG_TAG, " size = " + values.size());
            mLeagueDataSource.close();
            mLeaguesArray = new String[]{getString(R.string.champions_league),
                    getString(R.string.uefa_league),
                    getString(R.string.ukraine_championship),
                    getString(R.string.england_championship),
                    getString(R.string.argentina_championship),
                    getString(R.string.brazil_championship),
                    getString(R.string.germany_championship),
                    getString(R.string.italy_championship),
                    getString(R.string.spain_championship),
                    getString(R.string.netherlands_championship),
                    getString(R.string.portugal_championship),
                    getString(R.string.russia_championship),
                    getString(R.string.north_america_championship),
                    getString(R.string.turkey_championship),
                    getString(R.string.france_championship)
            };
            arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, android.R.id.text1, values);
            mMenuList.setAdapter(arrayAdapter);
            mMenuList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    LeaguesHandler.match = mLinks[i];
                    onSelectLeague();
                }
            });
        return mMenuList;
    }

    @Override
    public void onActivityCreated(android.os.Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMenuList.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i(LOG_TAG, "onTouch menuFragment");
                return getActivity().onTouchEvent(motionEvent);
            }
        });
    }


    public void onSelectLeague() {
        ((ParcerSampleActivity)getActivity()).updateScoreData();
    }

    private static final String LOG_TAG = MenuFragment.class.getSimpleName();
}
