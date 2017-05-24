package com.pachong.mycontacts;

import android.content.Context;
import android.graphics.Color;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceGroup;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class PrefSet extends PreferenceGroup{

	private static final String TAG = "PrefSet";

	public PrefSet(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public PrefSet(Context context, AttributeSet attrs) {
		this(context, attrs, android.R.attr.preferenceCategoryStyle);
	}

	public PrefSet(Context context) {
		this(context, null);
	}

	@Override
	protected boolean onPrepareAddPreference(Preference preference) {
		if (preference instanceof PreferenceCategory) {
			throw new IllegalArgumentException(
					"Cannot add a " + TAG + " directly to a " + TAG);
		}

		return super.onPrepareAddPreference(preference);
	}

	@Override
	public boolean isEnabled() {
		return true;
	}

	@Override
	public boolean isSelectable() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void setTitle(CharSequence title) {
		// TODO Auto-generated method stub
		super.setTitle(title);
	}

	
	@Override
	protected void onBindView(View view) {
		// TODO Auto-generated method stub
		super.onBindView(view);
		Log.d("oju", "t his is textview"+view.toString());
		try {
			TextView tv = (TextView)view.findViewById(android.R.id.title);
			tv.setSingleLine(false);
		   // tv.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
			tv.setTextSize(20);
			tv.setTextColor(Color.YELLOW);
		} catch (Exception e) {
			// TODO: handle exception
		}		
	}


}
