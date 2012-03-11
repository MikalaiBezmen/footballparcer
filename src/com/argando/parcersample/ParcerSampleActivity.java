package com.argando.parcersample;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class ParcerSampleActivity extends Activity {
	String text;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// Находим кнопку

		Button button = (Button) findViewById(R.id.parse);

		// Регистрируем onClick слушателя

		button.setOnClickListener(myListener);

	}

	// Диалог ожидания

	private ProgressDialog pd;

	// Слушатель OnClickListener для нашей кнопки

	private OnClickListener myListener = new OnClickListener() {

		public void onClick(View v) {

			// Показываем диалог ожидания

			pd = ProgressDialog.show(ParcerSampleActivity.this, "Working...",
					"request to server", true, false);

			// Запускаем парсинг

			new ParseSite().execute("http://www.football.ua");

		}

	};

	private class ParseSite extends AsyncTask<String, Void, List<String>> {

		// Фоновая операция

		protected List<String> doInBackground(String... arg) {

			HtmlCleaner cleaner = new HtmlCleaner();
			TagNode rootNode = cleaner.clean("http://www.stackoverflow.com");
			TagNode[] match = rootNode.getElementsByName("a", true);
			if (match == null)
				text = "empty match";
			else
			{
				 for (int i = 0; match != null && i < match.length; i++)
				 text += match[i].getText().toString();
			}

			return null;
		}

		// Событие по окончанию парсинга

		protected void onPostExecute(List<String> output) {

			// Убираем диалог загрузки

			pd.dismiss();
			Toast toast = Toast.makeText(getBaseContext(), text, 5000);
			toast.show();

		}

	}
}