package com.argando.parcersample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class ParcerSampleActivity extends Activity
{
	String		date;
	String		firstTeam;
	String		secondTeam;
	String		result;
	String		result2;
	String		text	= "";

	List<Match>	matches	= new ArrayList<Match>();

	@Override
	public void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Находим кнопку

		Button button = (Button) findViewById(R.id.parse);

		// Регистрируем onClick слушателя

		button.setOnClickListener(myListener);

	}

	// Диалог ожидания

	private ProgressDialog	pd;

	// Слушатель OnClickListener для нашей кнопки

	private OnClickListener	myListener	= new OnClickListener()
										{

											public void onClick(View v)
											{

												// Показываем диалог ожидания
												text = "";
												pd = ProgressDialog.show(ParcerSampleActivity.this, "Working...", "request to server", true, false);

												// Запускаем парсинг

												new ParseSite().execute("http://www.football.ua");

											}

										};

	private class ParseSite extends AsyncTask<String, Void, List<String>>
	{

		// Фоновая операция

		protected List<String> doInBackground(String... arg)
		{

			DataParcer dataParcer = new DataParcer();
			dataParcer.parceScoreboard();
			return null;
		}

		// HtmlCleaner cleaner = new HtmlCleaner();
		//
		// TagNode rootNode;
		// try {
		// rootNode = cleaner.clean(new URL("http://www.football.ua"));
		// TagNode[] match = rootNode.getElementsByName("div", true);
		// if (match == null)
		// text = "empty match";
		// else {
		// for (int i = 0; match != null && i < match.length; i++)
		// if ("ogame autblock".equals(match[i]
		// .getAttributeByName("class"))) {
		// TagNode subdiv[] = match[i].getElementsByName(
		// "div", true);
		// for (int j = 0; subdiv != null && j < subdiv.length; j++) {
		// if ("mathcdt".equals(subdiv[j]
		// .getAttributeByName("class")))
		// date = subdiv[j].getText().toString();
		// if ("omatch omatchleft".equals(subdiv[j]
		// .getAttributeByName("class")))
		// firstTeam = subdiv[j].getText().toString();
		// if ("matchright".equals(subdiv[j]
		// .getAttributeByName("class")))
		// secondTeam = subdiv[j].getText().toString();
		// if ("matchc".equals(subdiv[j]
		// .getAttributeByName("class")))
		// result2 = subdiv[j].getText().toString()
		// .trim();
		// }
		// matches.add(new Match(date, firstTeam, secondTeam,
		// result2));
		// }
		// }
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
		//
		// return null;
		// }

		// Событие по окончанию парсинга

		protected void onPostExecute(List<String> output)
		{

			// Убираем диалог загрузки
			Toast toast;
			pd.dismiss();
			toast = Toast.makeText(getBaseContext(), "AAA", 20000);
			toast.show();
		}
	}
}