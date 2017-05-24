package com.pachong.mycontacts;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;


public class AreaCodeActivity extends PreferenceActivity implements Preference.OnPreferenceChangeListener,Preference.OnPreferenceClickListener,NNN{
	
	PrefSet prefSet;
	
	private MyListPreference citiesab ;
	private MyListPreference citiesci ;
	private MyListPreference citieskm ;
	private MyListPreference citiesnr ;
	private MyListPreference citiessz ;

	@Override
	public void onChange(MyListPreference preference)
	{
		android.util.Log.d("bunwei","AREACODE="+Utils.AREACODE+",AREAPLACE="+Utils.AREAPLACE);
		//prefSet.setTitle("Preset Area Code Is "+Utils.AREAPLACE);
		if("Metro Manila, Laguna-San Pedro, Bulacan-Obando (02)".equals(Utils.AREAPLACE))
		{
			this.setTitle("Preset Area Code is "+"Metro Manila, Lagun...(02)");
			//this.setTitle("Preset Area Code is "+Utils.AREAPLACE);
		}else
		{
			this.setTitle("Preset Area Code is "+Utils.AREAPLACE);
		}
		if(preference==citiesab)
		{
			citiesci.reset();
			citieskm.reset();
			citiesnr.reset();
			citiessz.reset();
		}else if(preference==citiesci)
		{
			citiesab.reset();
			citieskm.reset();
			citiesnr.reset();
			citiessz.reset();
		}else if(preference==citieskm)
		{
			citiesci.reset();
			citiesab.reset();
			citiesnr.reset();
			citiessz.reset();
		}else if(preference==citiesnr)
		{
			citiesci.reset();
			citieskm.reset();
			citiesab.reset();
			citiessz.reset();
		}else if(preference==citiessz)
		{
			citiesci.reset();
			citieskm.reset();
			citiesnr.reset();
			citiesab.reset();
		}
	}

	private boolean DEB =  true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		addPreferencesFromResource(R.xml.area_list);
		if("Metro Manila, Laguna-San Pedro, Bulacan-Obando (02)".equals(Utils.AREAPLACE))
		{
			this.setTitle("Preset Area Code is "+"Metro Manila, Lagun...(02)");
			//this.setTitle("Preset Area Code is "+Utils.AREAPLACE);
		}else
		{
			this.setTitle("Preset Area Code is "+Utils.AREAPLACE);
		}
		//this.setTitle("Preset Area Code is "+Utils.AREAPLACE);

		PreferenceManager manager = getPreferenceManager();
		//prefSet = (PrefSet)manager.findPreference("select_area");
//		prefSet = new PrefSet(getApplication());
//		prefSet =(PrefSet)manager.findPreference("select_area");
//		prefSet.setTitle("Preset Area Code is "+Utils.AREAPLACE);

		citiesab = (MyListPreference) manager.findPreference("citiesab");
		citiesci = (MyListPreference) manager.findPreference("citiesci");
		citieskm = (MyListPreference) manager.findPreference("citieskm");
		citiesnr = (MyListPreference) manager.findPreference("citiesnr");
		citiessz = (MyListPreference) manager.findPreference("citiessz");
		try {
			citiesab.setOnPreferenceChangeListener(AreaCodeActivity.this);
			citiesab.setOnPreferenceClickListener(AreaCodeActivity.this);
			citiesab.setNNN(this);

			citiesci.setOnPreferenceChangeListener(AreaCodeActivity.this);
			citiesci.setOnPreferenceClickListener(AreaCodeActivity.this);
			citiesci.setNNN(AreaCodeActivity.this);

			citieskm.setOnPreferenceChangeListener(AreaCodeActivity.this);
			citieskm.setOnPreferenceClickListener(AreaCodeActivity.this);
			citieskm.setNNN(AreaCodeActivity.this);

			citiesnr.setOnPreferenceChangeListener(AreaCodeActivity.this);
			citiesnr.setOnPreferenceClickListener(AreaCodeActivity.this);
			citiesnr.setNNN(AreaCodeActivity.this);

			citiessz.setOnPreferenceChangeListener(AreaCodeActivity.this);
			citiessz.setOnPreferenceClickListener(AreaCodeActivity.this);
			citiessz.setNNN(AreaCodeActivity.this);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public boolean onPreferenceChange (Preference preference, Object newValue)
	{
		//Log.d("bunwei","onPreferenceChange newValue="+newValue);
		return true ;
	}
	@Override
	public boolean onPreferenceClick (Preference preference) 
	{

		return true;
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		finish();  
	}


	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}






}
