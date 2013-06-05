package com.argando.parcersample.scoretable;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;
import com.argando.parcersample.ParcerSampleActivity;
import com.argando.parcersample.R;
import com.argando.parcersample.data.DataNameHelper;
import com.argando.parcersample.data.League;
import com.argando.parcersample.data.LeaguesHandler;
import com.argando.parcersample.data.Preferences;
import com.argando.parcersample.network.NetworkChecker;
import com.argando.parcersample.network.SopCastLauncher;
import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MatchListFragment extends Fragment
{

	public final static String ITEM_TITLE = "title";
	public final static String ITEM_CAPTION = "caption";
	public final static String ITEM_LINK = "link";
	public final static String SOPCAST_LINK = "sopcast_link";
	public final static String IS_ONLINE = "is";
    private static final String LOG_TAG = MatchListFragment.class.getSimpleName();
	ListView list;
	private Fragment me;

	private Toast toast;

	private OnlineWebViewListener mOnlineWebViewListener;
    private Toast mInternetConnectionToast;

    interface OnlineWebViewListener
	{
		void onCreate(String link);

		void startSopcast(String url);
	}

	@NotNull
	public Map<String, ?> createItem(String title, String caption, String is, String link, List<String> sopcast)
	{
		Map<String, Object> item = new HashMap<String, Object>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		item.put(IS_ONLINE, is);
		item.put(ITEM_LINK, link);
		item.put(SOPCAST_LINK, sopcast);
		return item;
	}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            super.onCreate(savedInstanceState);
            Log.i(LOG_TAG, "onCreate");
            me = this;
            mInternetConnectionToast = Toast.makeText(getActivity().getApplicationContext(), DataNameHelper.NO_INTERNET_CONNECTION, DataNameHelper.NO_INTERNER_CONNNECTION_TOAST_TIME);

            mOnlineWebViewListener = new OnlineWebViewListener() {

                public void onCreate(String link) {
                    if (!NetworkChecker.isConnected(getActivity().getApplicationContext())) {
                        mInternetConnectionToast.show();
                        return;
                    }
                    Log.i(LOG_TAG, "show browser");
                    LeaguesHandler.match = link;
                    ((ParcerSampleActivity)getActivity()).updateScoreData();
                }

                public void startSopcast(String url) {
                    final WeakReference<Fragment> fragmentActivityWeakReference = new WeakReference<Fragment>(me);
                    SopCastLauncher sopCastLauncher = new SopCastLauncher(fragmentActivityWeakReference);
                    sopCastLauncher.startApplication("sop", url);
                }
            };

            List<League> leagues = LeaguesHandler.mListLeauges;

            if (leagues == null) {
                onDestroy();
                return;
            }
        }
    }

    @Override
    public void onActivityCreated (Bundle savedInstanceState)
    {
         super.onActivityCreated(savedInstanceState);
         Log.i(LOG_TAG, "onActivityCreated");
         updateData();
    }


    public void updateData()
    {
        Log.i(LOG_TAG, "updateData");
        List<League> leagues = LeaguesHandler.mListLeauges;
        if (this.getActivity() == null) return;
        SeparatedListAdapter adapter = new SeparatedListAdapter(this.getActivity());

        int counterForMatchId = 0;
        for (int i = 0; i < leagues.size(); i++)
        {
            List<Map<String, ?>> listItems = new LinkedList<Map<String, ?>>();
            for (int j = 0; j < leagues.get(i).getSize(); j++)
            {
                String team = leagues.get(i).getMatch(j).getFirstTeam() + " | " + leagues.get(i).getMatch(j).getScore1() + " - " + leagues.get(i).getMatch(j).getScore2() + " | " + leagues.get(i).getMatch(j).getSecondTeam();
                String time = leagues.get(i).getMatch(j).getDate();
                String is = "0";
                if (leagues.get(i).getMatch(j).isOnlineStatus() == 1)
                {
                    is = "1";
                } else if (leagues.get(i).getMatch(j).isOnlineStatus() == 2)
                {
                    is = "2";
                }
                counterForMatchId++;
                listItems.add(createItem(team, time, is, leagues.get(i).getMatch(j).linkForOnline, leagues.get(i).getMatch(j).linkToSopcast));
                leagues.get(i).getMatch(j).setId(counterForMatchId);
                Log.w("id", leagues.get(i).getMatch(j).getFirstTeam() + "  " + leagues.get(i).getMatch(j).getSecondTeam() + " id = " + leagues.get(i).getMatch(j).getId() + " SOPCAST LINKS COUNT = "  + leagues.get(i).getMatch(j).linkToSopcast.size());
            }
            adapter.addSection(leagues.get(i).getName(), new SimpleAdapter(mOnlineWebViewListener, this.getActivity(), listItems, Preferences.Black.getResultListLayout(), new String[]{ITEM_TITLE, ITEM_CAPTION, ITEM_LINK, SOPCAST_LINK, SOPCAST_LINK}, new int[]{R.id.list_complex_title, R.id.list_complex_caption, R.id.web_view, R.id.sop_cast, R.id.spinner}));
            counterForMatchId++;
        }

        // create our list and custom adapter
        list.setAdapter(adapter);
        list.setDivider(getResources().getDrawable(R.drawable.list_items_divider));
        list.setDividerHeight(4);
        list.setItemsCanFocus(true);
        list.setSelector(android.R.color.transparent);
        list.setFastScrollEnabled(false);
        list.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.i(LOG_TAG, "OnTouchListView");
                return getActivity().onTouchEvent(motionEvent);
            }
        });
    }

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
        Log.i(LOG_TAG, "onCreateView");
        list = new ListView(this.getActivity());
		return list;
	}

	@Override
	public void onDestroyView()
	{
        Log.i(LOG_TAG, "onDestroyView");
		super.onDestroy();
	}
}