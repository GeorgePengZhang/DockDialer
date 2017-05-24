package com.pachong.mycontacts;

import android.content.Context;
import android.preference.ListPreference;
import android.util.AttributeSet;

public class MyListPreference extends ListPreference {

	@Override
	protected void onDialogClosed(boolean positiveResult) {
	    super.onDialogClosed(positiveResult);
	    android.util.Log.d("bunwei",this+" positiveResult="+positiveResult);
	    if(!positiveResult)
	    {
	        return ;
	    }
	    String kk = getValue();
	    Utils.AREACODE = kk.substring(kk.indexOf('(')+1,kk.lastIndexOf(')'));
		Utils.AREAPLACE = getEntry()==null?null:getEntry().toString();
		if(nnn!=null)
		{
		    nnn.onChange(this);
		}
		
	}

	public MyListPreference(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public MyListPreference(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public void reset()
	{
	    this.setValue(null);
	    //android.util.Log.d("bunwei",this+"  reset");
	}
	
    private NNN nnn ;
    public void setNNN(NNN n)
    {
        nnn = n ;
    }

}
