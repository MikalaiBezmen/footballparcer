package com.argando.parcersample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.util.Log;

public class DataParcer
{
	private static final String	LOG		= DataParcer.class.getName();

	private static final String	mSite	= "http://www.football.ua";
	private static URL			mSiteUrl;
	private HtmlCleaner			mHtmlHelper;
	private TagNode				mRootElement;
	static
	{
		try
		{
			mSiteUrl = new URL(mSite);
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w(LOG, "there is a problem with mSiteULR = " + mSite);
		}
	}

	public DataParcer()
	{
		mHtmlHelper = new HtmlCleaner();
	}

	private boolean getRootElement()
	{
		try
		{
			mRootElement = mHtmlHelper.clean(mSiteUrl);
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	private 
}
