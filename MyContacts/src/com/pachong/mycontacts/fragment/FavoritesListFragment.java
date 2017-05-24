package com.pachong.mycontacts.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.pachong.mycontacts.ContactTileLoaderFactory;
import com.pachong.mycontacts.R;
import com.pachong.mycontacts.adapter.FavoritesAdapter;
import com.pachong.mycontacts.adapter.FavoritesAdapter.IOnItemInfoClickListener;
import com.pachong.mycontacts.bean.FavoritesBean;
import com.pachong.mycontacts.service.ContactsSyncService;
import com.pachong.mycontacts.utils.ContactsUtils;


/**
 * @ClassName: CallsLogListFragment
 * @Description: TODO
 * @author: steven zhang
 * @date: Sep 27, 2016 2:31:44 PM
 */
public class FavoritesListFragment extends Fragment implements OnItemClickListener, IOnItemInfoClickListener {
	
	private MyQueryHandler myQueryHandler;
	private ListView mListView;
	private List<FavoritesBean> mDataList;
	private TextView mEmptyView;
	private FavoritesAdapter favoritesAdapter;
	private boolean mRefreshDataRequired = true;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.favorites_layout, container, false);
		
		mListView = (ListView) view.findViewById(R.id.id_listview);
		mDataList = new ArrayList<FavoritesBean>();
		
		mEmptyView = (TextView) view.findViewById(R.id.id_empty);
	    mListView.setEmptyView(mEmptyView);

		myQueryHandler = new MyQueryHandler(getActivity().getContentResolver(), this);
		
		mListView.setOnItemClickListener(this);
	    
	    IntentFilter filter = new IntentFilter(ContactsSyncService.UPDATE_CONTACTS);
	    LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mBroadcastReceiver, filter);
	    
		return view;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mBroadcastReceiver);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Intent intent = new Intent(getActivity(), ContactsSyncService.class);
	    getActivity().startService(intent);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		refreshData();
	}

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Log.d("TAG", "UPDATE_CONTACTS favorites:"+action+",isResumed():"+isResumed());
			if (ContactsSyncService.UPDATE_CONTACTS.equals(action)) {
				mRefreshDataRequired = true;
			}
		}
	};
	
	//异步查询联系人数据库
	private static class MyQueryHandler extends AsyncQueryHandler {
		private WeakReference<FavoritesListFragment> weakReference;

		public MyQueryHandler(ContentResolver cr, FavoritesListFragment fragment) {
			super(cr);
			weakReference = new WeakReference<FavoritesListFragment>(fragment);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			super.onQueryComplete(token, cookie, cursor);
			FavoritesListFragment fragment = weakReference.get();
			if (fragment != null) {
				fragment.queryComplete(token, cookie, cursor);
			}
		}
	}
	
	private void queryComplete(int token, Object cookie, Cursor cursor) {
		mDataList.clear();
		
		if (cursor != null && cursor.getCount() > 0) {
			
			while (cursor.moveToNext()) {
				FavoritesBean bean = FavoritesBean.getBeanFromCursor(cursor);
				mDataList.add(bean);
				Log.d("TAG", "baen:"+bean);
			}
			
			cursor.close();
			updateListView();
		} else {
			FavoritesAdapter adapter = (FavoritesAdapter) mListView.getAdapter();
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	private void refreshData() {
		if (mRefreshDataRequired) {
			mRefreshDataRequired = false;
			myQueryHandler.cancelOperation(0);
			ContactTileLoaderFactory.createStrequentPhoneOnlyLoader(myQueryHandler);
		}
	}
	
	private void updateListView() {
		favoritesAdapter = new FavoritesAdapter(getActivity(), mDataList, this);
		mListView.setAdapter(favoritesAdapter);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		FavoritesBean item = (FavoritesBean) favoritesAdapter.getItem(position);
		String number = item.getNumber();
		Intent callIntent = ContactsUtils.getCallIntent(number);
		startActivity(callIntent);
	}

	@Override
	public void onItemInfoClick(View v) {
		int id = v.getId();
		Integer position = (Integer) v.getTag();
		
		if (id == R.id.id_callog_info) {
			FavoritesBean item = (FavoritesBean) favoritesAdapter.getItem(position);
			Uri lookupUri = Contacts.getLookupUri(item.getId(), item.getLookupuri());
			Intent intent = new Intent(Intent.ACTION_VIEW, lookupUri);
	        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
	        startActivity(intent);
		}
	}
}
