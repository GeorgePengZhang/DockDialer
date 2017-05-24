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
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.CallLog.Calls;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.utils.ViewFindUtils;
import com.flyco.tablayout.widget.SwipeListView;
import com.pachong.mycontacts.R;
import com.pachong.mycontacts.adapter.CallsLogAdapter;
import com.pachong.mycontacts.adapter.CallsLogAdapter.IOnItemInfoClickListener;
import com.pachong.mycontacts.bean.CallsLogBean;
import com.pachong.mycontacts.service.ContactsSyncService;
import com.pachong.mycontacts.utils.AsyncTaskExecutor;
import com.pachong.mycontacts.utils.AsyncTaskExecutors;
import com.pachong.mycontacts.utils.ContactsUtils;


/**
 * @ClassName: CallsLogListFragment
 * @Description: TODO
 * @author: steven zhang
 * @date: Sep 27, 2016 2:31:44 PM
 */
public class CallsLogListFragment extends Fragment implements OnItemClickListener, OnTabSelectListener, IOnItemInfoClickListener {
	
	public static final String CALLLOG_PHONE = "calllog_phone";
	public static final int CALLLOG_CODE = 2000;
	private static final int CALLLOG_MAX_ITEM = 10;

	private MyQueryHandler myQueryHandler;
	private SwipeListView mListView;
	private List<CallsLogBean> mAllCallsDataList;
	private List<CallsLogBean> mMissedCallsDataList;
	private TextView mEmptyView;
	private CallsLogAdapter callsLogAdapter;
	private SegmentTabLayout mSegmentTabLayout;
	private boolean mRefreshDataRequired = true;
	private int mCurrentTabPostion = 0;
	private List<List<Long>> mAllCallsMapList;
	private List<List<Long>> mMissedCallsMapList;
	private View mClearBtn;
	private AsyncTaskExecutor mAsyncTaskExecutor;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.calllog_layout, container, false);
		mAsyncTaskExecutor = AsyncTaskExecutors.createThreadPoolExecutor();
		
		mListView = (SwipeListView) view.findViewById(R.id.id_listview);
		mAllCallsDataList = new ArrayList<CallsLogBean>();
		mMissedCallsDataList = new ArrayList<CallsLogBean>();
		mAllCallsMapList = new ArrayList<List<Long>>();
		mMissedCallsMapList = new ArrayList<List<Long>>();
		
		mEmptyView = (TextView) view.findViewById(R.id.id_empty);
	    mListView.setEmptyView(mEmptyView);

	    String[] titles = getResources().getStringArray(R.array.calllogtitles);
		mSegmentTabLayout = ViewFindUtils.find(view, R.id.id_segment_tab);
		mSegmentTabLayout.setTabData(titles);
		mSegmentTabLayout.setOnTabSelectListener(this);
		mSegmentTabLayout.setCurrentTab(mCurrentTabPostion);
	    
		myQueryHandler = new MyQueryHandler(getActivity().getContentResolver(), this);
		
		mListView.setOnItemClickListener(this);
		mClearBtn = view.findViewById(R.id.id_clear);
		mClearBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ClearCallLogDialog.show(getFragmentManager());
			}
		});
		
	    
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
			Log.d("TAG", "UPDATE_CONTACTS call:"+action+",isResumed():"+isResumed());
			if (ContactsSyncService.UPDATE_CONTACTS.equals(action)) {
				mRefreshDataRequired = true;
				if (isResumed()) {
					refreshData();
				}
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
		mAllCallsDataList.clear();
		mMissedCallsDataList.clear();
		clean(mAllCallsMapList);
		clean(mMissedCallsMapList);
		
		if (cursor != null && cursor.getCount() > 0) {
			List<Long> beanMap = null;
			
			while (cursor.moveToNext()) {
				CallsLogBean bean = CallsLogBean.getBeanFromCursor(cursor);
				//TODO 解决错位后的bug
				if (!mAllCallsDataList.contains(bean)) {
					mAllCallsDataList.add(bean);
					int type = bean.getType();
					beanMap = new ArrayList<Long>();
					mAllCallsMapList.add(beanMap);
					if (CallLog.Calls.MISSED_TYPE == type) {
						mMissedCallsDataList.add(bean);
						mMissedCallsMapList.add(beanMap);
					} 
				} else {
					int index = mAllCallsDataList.indexOf(bean);
					if (index < mAllCallsDataList.size()-1) {
						beanMap = mAllCallsMapList.get(index);
					}
				}
				beanMap.add(bean.getId());
			}
			
			cursor.close();
			
			int position = mSegmentTabLayout.getCurrentTab();
			updateListView(position);
			mClearBtn.setVisibility(View.VISIBLE);
		} else {
			CallsLogAdapter adapter = (CallsLogAdapter) mListView.getAdapter();
			if (adapter != null) {
				adapter.notifyDataSetChanged();
			}
			mClearBtn.setVisibility(View.INVISIBLE);
		}
	}
	
	private void refreshData() {
		if (mRefreshDataRequired) {
			mRefreshDataRequired = false;
			myQueryHandler.cancelOperation(0);
			Uri uri = ContactsUtils.Calls.CONTENT_URI.buildUpon().appendQueryParameter("limit", Integer.toString(1000)).build();
			myQueryHandler.startQuery(0, null, uri, ContactsUtils.Calls.PROJECTION_PRIMARY, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
		}
	}
	
	
	private void updateListView(int position) {
		mCurrentTabPostion = position;
		switch (position) {
		case 1:
			callsLogAdapter = new CallsLogAdapter(getActivity(), mMissedCallsDataList, mListView.getRightViewWidth(), this);
			break;
		case 0:
		default:
			callsLogAdapter = new CallsLogAdapter(getActivity(), mAllCallsDataList, mListView.getRightViewWidth(), this);
			break;
		}
		
		mListView.setAdapter(callsLogAdapter);
	}

	private void clean(List<List<Long>> list) {
		for (int i = 0; i < list.size(); i++) {
			List<Long> map = list.get(i);
			map.clear();
		}
		list.clear();
	}
	
	private void removeCalllogItem(int position) {
		List<Long> beanMap = null;
		if (mCurrentTabPostion == 0) {
			beanMap = mAllCallsMapList.get(position);
		} else if (mCurrentTabPostion == 1) {
			beanMap = mMissedCallsMapList.get(position);
		}
		
		if (beanMap == null) {
			return;
		}
		
		int size = beanMap.size();
		long[] ids = new long[size];
		for (int i = 0; i < size; i++) {
			ids[i] = beanMap.get(i);
		}
		
        final StringBuilder callIds = new StringBuilder();
        for (int i = 0; i < size; i++) {
            if (callIds.length() != 0) {
                callIds.append(",");
            }
            callIds.append(ids[i]);
        }
        mAsyncTaskExecutor.submit(1,
            new AsyncTask<Void, Void, Void>() {
                @Override
                public Void doInBackground(Void... params) {
                    getActivity().getContentResolver().delete(Calls.CONTENT_URI,
                            Calls._ID + " IN (" + callIds + ")", null);
                    return null;
                }

                @Override
                public void onPostExecute(Void result) {
                	
                }
            });
    }
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		CallsLogBean item = (CallsLogBean) callsLogAdapter.getItem(position);
		String number = item.getNumber();
		Intent callIntent = ContactsUtils.getCallIntent(number);
		startActivity(callIntent);
	}

	@Override
	public void onTabSelect(int position) {
		updateListView(position);
	}

	@Override
	public void onTabReselect(int position) {
		
	}

	@Override
	public void onItemInfoClick(View v) {
		int id = v.getId();
		Integer position = (Integer) v.getTag();
		
		List<Long> beanMap = null;
		if (mCurrentTabPostion == 0) {
			beanMap = mAllCallsMapList.get(position);
		} else if (mCurrentTabPostion == 1) {
			beanMap = mMissedCallsMapList.get(position);
		}
		
		if (beanMap == null) {
			return;
		}
		
		int size = Math.min(CALLLOG_MAX_ITEM, beanMap.size());
		long[] ids = new long[size];
		for (int i = 0; i < size; i++) {
			ids[i] = beanMap.get(i);
			Log.d("TAG", "ids[i]:"+ids[i]);
		}
		
		if (id == R.id.id_callog_info) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
//			Uri uri = ContentUris.withAppendedId(ContactsUtils.Calls.CONTENT_URI, item.getId());
//			intent.setDataAndType(uri, CallLog.Calls.CONTENT_ITEM_TYPE);
			intent.setType(CallLog.Calls.CONTENT_ITEM_TYPE);
			intent.putExtra("EXTRA_CALL_LOG_IDS", ids);
			startActivity(intent);
		} else if (id == R.id.id_item_right) {
			mListView.hiddenRight(mListView.mPreItemView);
			removeCalllogItem(position);
		}
	}
}
