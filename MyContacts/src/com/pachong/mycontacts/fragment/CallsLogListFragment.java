package com.pachong.mycontacts.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import android.content.AsyncQueryHandler;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
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

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.ViewFindUtils;
import com.flyco.tablayout.widget.SwipeListView;
import com.pachong.mycontacts.R;
import com.pachong.mycontacts.adapter.CallsLogAdapter;
import com.pachong.mycontacts.bean.CallsLogBean;
import com.pachong.mycontacts.service.ContactsSyncService;
import com.pachong.mycontacts.utils.ContactsUtils;


/**
 * @ClassName: CallsLogListFragment
 * @Description: TODO
 * @author: steven zhang
 * @date: Sep 27, 2016 2:31:44 PM
 */
public class CallsLogListFragment extends Fragment implements OnItemClickListener, OnTabSelectListener {
	
	public static final String CALLLOG_PHONE = "calllog_phone";
	public static final int CALLLOG_CODE = 2000;

	private MyQueryHandler myQueryHandler;
	private SwipeListView mListView;
	private List<CallsLogBean> mAllCallsDataList;
	private List<CallsLogBean> mMissedCallsDataList;
	private TextView mEmptyView;
	private CallsLogAdapter callsLogAdapter;
	private SegmentTabLayout mSegmentTabLayout;
	private boolean mRefreshDataRequired = true;
	private int mCurrentTabPostion = 0;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.calllog_layout, container, false);
		mListView = (SwipeListView) view.findViewById(R.id.id_listview);
		mAllCallsDataList = new ArrayList<CallsLogBean>();
		mMissedCallsDataList = new ArrayList<CallsLogBean>();
		
		mEmptyView = (TextView) view.findViewById(R.id.id_empty);
	    mListView.setEmptyView(mEmptyView);

	    String[] titles = getResources().getStringArray(R.array.calllogtitles);
		mSegmentTabLayout = ViewFindUtils.find(view, R.id.id_segment_tab);
		mSegmentTabLayout.setTabData(titles);
		mSegmentTabLayout.setOnTabSelectListener(this);
		mSegmentTabLayout.setCurrentTab(mCurrentTabPostion);
	    
		myQueryHandler = new MyQueryHandler(getActivity().getContentResolver(), this);
		
		mListView.setOnItemClickListener(this);
		
		Intent intent = new Intent(getActivity(), ContactsSyncService.class);
	    getActivity().startService(intent);
	    
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
	public void onResume() {
		super.onResume();
		refreshData();
	}
	
	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (ContactsSyncService.UPDATE_CONTACTS.equals(action)) {
				mRefreshDataRequired = true;
			}
		}
		
	};
	
	//异步查询联系人数据库
	private static class MyQueryHandler extends AsyncQueryHandler {
		private WeakReference<CallsLogListFragment> weakReference;

		public MyQueryHandler(ContentResolver cr, CallsLogListFragment fragment) {
			super(cr);
			weakReference = new WeakReference<CallsLogListFragment>(fragment);
		}

		@Override
		protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
			super.onQueryComplete(token, cookie, cursor);
			CallsLogListFragment fragment = weakReference.get();
			if (fragment != null) {
				fragment.queryComplete(token, cookie, cursor);
			}
			
		}
	}
	
	private void queryComplete(int token, Object cookie, Cursor cursor) {
		if (cursor != null && cursor.getCount() > 0) {
			mAllCallsDataList.clear();
			mMissedCallsDataList.clear();
			
			while (cursor.moveToNext()) {
				CallsLogBean bean = CallsLogBean.getBeanFromCursor(cursor);
				if (!mAllCallsDataList.contains(bean)) {
					mAllCallsDataList.add(bean);
					int type = bean.getType();
					if (CallLog.Calls.MISSED_TYPE == type) {
						mMissedCallsDataList.add(bean);
					}
				}
			}
			
			cursor.close();
			int position = mSegmentTabLayout.getCurrentTab();
			updateListView(position);
		}
	}
	
	private void refreshData() {
		if (mRefreshDataRequired) {
			mRefreshDataRequired = false;
			myQueryHandler.cancelOperation(0);
			myQueryHandler.startQuery(0, null, ContactsUtils.Calls.CONTENT_URI, ContactsUtils.Calls.PROJECTION_PRIMARY, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		}
	}
	
	private void updateListView(int position) {
		mCurrentTabPostion = position;
		switch (position) {
		case 1:
			callsLogAdapter = new CallsLogAdapter(getActivity(), mMissedCallsDataList, mListView.getRightViewWidth());
			break;
		case 0:
		default:
			callsLogAdapter = new CallsLogAdapter(getActivity(), mAllCallsDataList, mListView.getRightViewWidth());
			break;
		}
		
		mListView.setAdapter(callsLogAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
	}

	@Override
	public void onTabSelect(int position) {
		updateListView(position);
	}

	@Override
	public void onTabReselect(int position) {
		
	}
}
