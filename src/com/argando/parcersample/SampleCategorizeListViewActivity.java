package com.argando.parcersample;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SampleCategorizeListViewActivity extends Fragment
{

	public final static String	ITEM_TITLE		= "title";
	public final static String	ITEM_CAPTION	= "caption";
	ListView					list;
	private Fragment me;

	public Map<String, ?> createItem(String title, String caption, String is)
	{
		Map<String, String> item = new HashMap<String, String>();
		item.put(ITEM_TITLE, title);
		item.put(ITEM_CAPTION, caption);
		item.put("IS", is);
		return item;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		me = this;
		List<League> leagues = LeaguesHandler.listLeauges;

		SeparatedListAdapter adapter = new SeparatedListAdapter(this.getActivity());

		int counterForMatchId = 0;
		for (int i = 0; i < leagues.size(); i++)
		{
			List<Map<String, ?>> listItems = new LinkedList<Map<String, ?>>();
			for (int j = 0; j < leagues.get(i).getSize(); j++)
			{
				String team = leagues.get(i).getMatch(j).getFirstTeam() + " | " + leagues.get(i).getMatch(j).getScore1() + " - "
						+ leagues.get(i).getMatch(j).getScore2() + " | " + leagues.get(i).getMatch(j).getSecondTeam();
				String time = leagues.get(i).getMatch(j).getDate();
				String is = "0";
				if (leagues.get(i).getMatch(j).isOnlineStatus())
				{
					is = "1";
				}
				counterForMatchId++;
				listItems.add(createItem(team, time, is));
				leagues.get(i).getMatch(j).setId(counterForMatchId);
				Log.w("id", leagues.get(i).getMatch(j).getFirstTeam() + "  " + leagues.get(i).getMatch(j).getSecondTeam() + " id = "
						+ leagues.get(i).getMatch(j).getId());
			}
			adapter.addSection(leagues.get(i).getName(), new SimpleAdapter(this.getActivity(), listItems, R.layout.list_complex, new String[] {
					ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
			counterForMatchId++;
		}

		// create our list and custom adapter
		list = new ListView(this.getActivity());
		list.setAdapter(adapter);
		list.setOnItemClickListener(new OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3)
			{
				LeaguesHandler.match = LeaguesHandler.getMatchById((int) arg3).linkForOnline;
				getActivity().findViewById(R.id.container).setVisibility(View.GONE);
				getActivity().findViewById(R.id.containerForMach).setVisibility(View.VISIBLE);
				// TODO Auto-generated method stub
				Toast.makeText(getActivity().getApplicationContext(), LeaguesHandler.getMatchById((int) arg3).toString(), 20000).show();

				ScoreFragment fragment = new ScoreFragment();

				FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
				FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
				fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left,
						android.R.anim.slide_out_right);
				fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//				fragmentTransaction.remove(me);
				fragmentTransaction.add(R.id.containerForMach, fragment);
//				fragmentTransaction.replace(R.id.container, fragment);
				fragmentTransaction.addToBackStack(null);

				fragmentTransaction.commit();
			}
		});
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

		return list;
	}

}