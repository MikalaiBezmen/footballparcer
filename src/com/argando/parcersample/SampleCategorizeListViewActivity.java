package com.argando.parcersample;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;


public class SampleCategorizeListViewActivity extends Fragment
{

	public final static String	ITEM_TITLE		= "title";
	public final static String	ITEM_CAPTION	= "caption";
	ListView					list;

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

		List<League> leagues = LeaguesHandler.listLeauges;

		SeparatedListAdapter adapter = new SeparatedListAdapter(this.getActivity());

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
				listItems.add(createItem(team, time, is));
			}
			adapter.addSection(leagues.get(i).getName(), new SimpleAdapter(this.getActivity(), listItems, R.layout.list_complex, new String[] {
					ITEM_TITLE, ITEM_CAPTION }, new int[] { R.id.list_complex_title, R.id.list_complex_caption }));
		}

		// create our list and custom adapter
		list = new ListView(this.getActivity());
		list.setAdapter(adapter);
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