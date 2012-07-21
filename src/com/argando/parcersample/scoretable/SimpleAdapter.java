package com.argando.parcersample.scoretable;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.*;
import com.argando.parcersample.scoretable.SampleCategorizeListViewActivity;
import com.argando.parcersample.scoretable.SampleCategorizeListViewActivity.OnlineWebViewListener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SimpleAdapter extends BaseAdapter implements Filterable
{
	private int[]							mTo;
	private String[]						mFrom;
	private ViewBinder						mViewBinder;

	private List<? extends Map<String, ?>>	mData;

	private int								mResource;
	private int								mDropDownResource;
	private LayoutInflater					mInflater;

	private SimpleFilter					mFilter;
	private ArrayList<Map<String, ?>>		mUnfilteredData;

	OnlineWebViewListener					mOnlineWebViewListener;

	public SimpleAdapter(OnlineWebViewListener onlineWebViewListener, @NotNull Context context, List<? extends Map<String, ?>> data, int resource,
			String[] from, int[] to)
	{
		mOnlineWebViewListener = onlineWebViewListener;
		mData = data;
		mResource = mDropDownResource = resource;
		mFrom = from;
		mTo = to;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public int getCount()
	{
		return mData.size();
	}

	public Object getItem(int position)
	{
		return mData.get(position);
	}

	public long getItemId(int position)
	{
		return position;
	}

	@Nullable
	public View getView(int position, View convertView, ViewGroup parent)
	{
		return createViewFromResource(position, convertView, parent, mResource);
	}

	@Nullable
	private View createViewFromResource(int position, @Nullable View convertView, ViewGroup parent, int resource)
	{
		View v;
		if (convertView == null)
		{
			v = mInflater.inflate(resource, parent, false);

			final int[] to = mTo;
			final int count = to.length;
			final View[] holder = new View[count];

			for (int i = 0; i < count; i++)
			{
				holder[i] = v.findViewById(to[i]);
			}
			v.setTag(holder);
		}
		else
		{
			v = convertView;
		}

		bindView(position, v);

		return v;
	}

	public void setDropDownViewResource(int resource)
	{
		this.mDropDownResource = resource;
	}

	@Nullable
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent)
	{
		return createViewFromResource(position, convertView, parent, mDropDownResource);
	}

	private void bindView(int position, @NotNull View view)
	{
		final Map dataSet = mData.get(position);
		if (dataSet == null)
		{
			return;
		}

		final ViewBinder binder = mViewBinder;
		final View[] holder = (View[]) view.getTag();
		final String[] from = mFrom;
		final int[] to = mTo;
		final int count = to.length;

		for (int i = 0; i < count; i++)
		{
			final View v = holder[i];
			if (v != null)
			{
				final Object data = dataSet.get(from[i]);
				final String text = data == null ? "" : data.toString();
				if (text == null)
				{
					// text = "";
				}

				boolean bound = false;
				if (binder != null)
				{
					bound = binder.setViewValue(v, data, text);
				}

				if (!bound)
				{
					if (v instanceof Checkable)
					{
						if (data instanceof Boolean)
						{
							((Checkable) v).setChecked((Boolean) data);
						}
						else
						{
							throw new IllegalStateException(v.getClass().getName() + " should be bound to a Boolean, not a " + data.getClass());
						}
					}
					else if (v instanceof ImageButton)
					{
						Object check = mData.get(position).get("sopcast_link");
						if (check != null && !check.toString().isEmpty() )
						{
							v.setVisibility(View.VISIBLE);
							v.setOnClickListener(new OnClickListener()
							{
								public void onClick(View v)
								{
									mOnlineWebViewListener.startSopcast(text);
								}
							});
						}
						else
						{
							v.setVisibility(View.GONE);
						}
					}
					else if (v instanceof Button)
					{
						((Button) v).setOnClickListener(new OnClickListener()
						{

							public void onClick(View v)
							{
								mOnlineWebViewListener.onCreate(text);
							}
						});
					}
					else if (v instanceof TextView)
					{
						// Note: keep the instanceof TextView check at the bottom of these
						// ifs since a lot of views are TextViews (e.g. CheckBoxes).

						if (mData.get(position).get(SampleCategorizeListViewActivity.IS_ONLINE).equals("0"))
						{
							setViewTextGrey((TextView) v, text);
						}
						else if (mData.get(position).get(SampleCategorizeListViewActivity.IS_ONLINE).equals("2"))
						{
							setViewTextRed((TextView) v, text);
						}
						else
						{
							setViewText((TextView) v, text);
						}
					}
					else if (v instanceof ImageView)
					{
						if (data instanceof Integer)
						{
							setViewImage((ImageView) v, (Integer) data);
						}
						else
						{
							setViewImage((ImageView) v, text);
						}
					}
					else
					{
						throw new IllegalStateException(v.getClass().getName() + " is not a " + " view that can be bounds by this SimpleAdapter");
					}
				}
			}
		}
	}

	public ViewBinder getViewBinder()
	{
		return mViewBinder;
	}

	public void setViewBinder(ViewBinder viewBinder)
	{
		mViewBinder = viewBinder;
	}

	public void setViewImage(@NotNull ImageView v, int value)
	{
		v.setImageResource(value);
	}

	public void setViewImage(@NotNull ImageView v, String value)
	{
		try
		{
			v.setImageResource(Integer.parseInt(value));
		}
		catch (NumberFormatException nfe)
		{
			v.setImageURI(Uri.parse(value));
		}
	}

	public void setViewText(@NotNull TextView v, String text)
	{
		{
			v.setTextColor(Color.WHITE);
			v.setShadowLayer(1,1,1, Color.GREEN);
		}
		v.setText(text);
	}
	
	public void setViewTextRed(@NotNull TextView v, String text)
	{
		{
			v.setTextColor(Color.WHITE);
			v.setShadowLayer(1,1,1, Color.RED);
		}
		v.setText(text);
	}
	
	public void setViewTextGrey(@NotNull TextView v, String text)
	{
		{
			v.setTextColor(Color.WHITE);
			v.setShadowLayer(1,1,1, Color.BLACK);
		}
		v.setText(text);
	}

	public Filter getFilter()
	{
		if (mFilter == null)
		{
			mFilter = new SimpleFilter();
		}
		return mFilter;
	}

	public static interface ViewBinder
	{
		boolean setViewValue(View view, Object data, String textRepresentation);
	}

	private class SimpleFilter extends Filter
	{

		@NotNull
		@Override
		protected FilterResults performFiltering(@Nullable CharSequence prefix)
		{
			FilterResults results = new FilterResults();

			if (mUnfilteredData == null)
			{
				mUnfilteredData = new ArrayList<Map<String, ?>>(mData);
			}

			if (prefix == null || prefix.length() == 0)
			{
				ArrayList<Map<String, ?>> list = mUnfilteredData;
				results.values = list;
				results.count = list.size();
			}
			else
			{
				String prefixString = prefix.toString().toLowerCase();

				ArrayList<Map<String, ?>> unfilteredValues = mUnfilteredData;
				int count = unfilteredValues.size();

				ArrayList<Map<String, ?>> newValues = new ArrayList<Map<String, ?>>(count);

				for (int i = 0; i < count; i++)
				{
					Map<String, ?> h = unfilteredValues.get(i);
					if (h != null)
					{

						int len = mTo.length;

						for (int j = 0; j < len; j++)
						{
							String str = (String) h.get(mFrom[j]);

							String[] words = str.split(" ");
							int wordCount = words.length;

							for (int k = 0; k < wordCount; k++)
							{
								String word = words[k];

								if (word.toLowerCase().startsWith(prefixString))
								{
									newValues.add(h);
									break;
								}
							}
						}
					}
				}

				results.values = newValues;
				results.count = newValues.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, @NotNull FilterResults results)
		{
			// noinspection unchecked
			mData = (List<Map<String, ?>>) results.values;
			if (results.count > 0)
			{
				notifyDataSetChanged();
			}
			else
			{
				notifyDataSetInvalidated();
			}
		}
	}
}