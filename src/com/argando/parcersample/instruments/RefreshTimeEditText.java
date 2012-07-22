package com.argando.parcersample.instruments;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * User: argando
 * Date: 22.07.12
 * Time: 14:01
 */
public class RefreshTimeEditText extends EditText
{
	private final NumberTextWatcher mNumberTextWatcher = new NumberTextWatcher();
	public RefreshTimeEditText(Context context)
	{
		super(context);
	}

	public RefreshTimeEditText(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public RefreshTimeEditText(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	private void addTextWatcher()
	{
		 this.addTextChangedListener(mNumberTextWatcher);
	}

	private class NumberTextWatcher implements TextWatcher
	{
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
		{
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
		{

		}

		@Override
		public void afterTextChanged(Editable editable)
		{
			if (editable.length()>3)
			{

			}
		}
	}
}
