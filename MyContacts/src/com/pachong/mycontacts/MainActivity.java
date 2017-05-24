package com.pachong.mycontacts;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.TabEntity;
import com.flyco.tablayout.utils.ViewFindUtils;
import com.pachong.mycontacts.fragment.CallsLogListFragment;
import com.pachong.mycontacts.fragment.ContactsPhoneListFragment;
import com.pachong.mycontacts.fragment.DialerFragment;
import com.pachong.mycontacts.fragment.FavoritesListFragment;
import com.pachong.mycontacts.fragment.SettingsFragment;

public class MainActivity extends FragmentActivity {

	
	private int[] mTitlesTextIds = {R.string.dialer, R.string.calllog, R.string.contacts, R.string.favorites, R.string.settings};
    private int[] mTitlesIconIds = {
            R.drawable.top_tab_dialer, R.drawable.top_tab_calllog,
            R.drawable.top_tab_contacts, R.drawable.top_tab_favorites, R.drawable.top_tab_settings};
	private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<CustomTabEntity>();
	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();
	
	private CommonTabLayout mTopTabLayout;
	private View mDecorView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mFragments.add(new DialerFragment());
		mFragments.add(new CallsLogListFragment());
		mFragments.add(new ContactsPhoneListFragment());
		mFragments.add(new FavoritesListFragment());
		mFragments.add(new SettingsFragment());
            
		
		for (int i = 0; i < mTitlesTextIds.length; i++) {
            mTabEntities.add(new TabEntity(getString(mTitlesTextIds[i]), mTitlesIconIds[i], mTitlesIconIds[i]));
        }
		
		
		mDecorView = getWindow().getDecorView();
		mTopTabLayout = ViewFindUtils.find(mDecorView, R.id.id_top_tab);
		mTopTabLayout.setTabData(mTabEntities, this, R.id.id_content, mFragments);
		
	}

}
