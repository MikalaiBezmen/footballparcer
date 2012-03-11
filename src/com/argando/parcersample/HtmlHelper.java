package com.argando.parcersample;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.widget.Toast;

public class HtmlHelper {

	  TagNode rootNode;



	  //�����������

	  public HtmlHelper(URL htmlPage) throws IOException

	  {

	    //������ ������ HtmlCleaner

	    HtmlCleaner cleaner = new HtmlCleaner();

	    //��������� html ��� �����

	    rootNode = cleaner.clean(htmlPage);

	  }



	  List<TagNode> getLinksByClass(String CSSClassname)

	  {

	    List<TagNode> linkList = new ArrayList<TagNode>();



	    //�������� ��� ������

	    TagNode linkElements[] = rootNode.getElementsByName("a", true);

	    for (int i = 0; linkElements != null && i < linkElements.length; i++)

	    {

	      //�������� ������� �� �����

	      String classType = linkElements[i].getAttributeByName("class");

	      //���� ������� ���� � �� ������������ ��������, �� ��������� � ������

	      if (classType != null && classType.equals(CSSClassname))

	      {

	        linkList.add(linkElements[i]);

	      }

	    }

	    return linkList;

	  }

//	    void getAllMatches (String CssClassName)
//	    {
//	    	TagNode match = rootNode.findElementHavingAttribute(CssClassName, true);
//	    	Toast toast = Toast.makeText(, text, duration)
//	    			toast.show();
//			List<TagNode> linkList = new ArrayList<TagNode>();
//	    }
	}