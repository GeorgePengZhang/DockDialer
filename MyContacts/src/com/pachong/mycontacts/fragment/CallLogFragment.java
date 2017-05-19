package com.pachong.mycontacts.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.flyco.tablayout.SegmentTabLayout;
import com.flyco.tablayout.utils.ViewFindUtils;
import com.pachong.mycontacts.R;

public class CallLogFragment extends Fragment implements OnClickListener {
	
	private SegmentTabLayout mSegmentTabLayout;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.calllog_layout, null);
		
		String[] titles = getResources().getStringArray(R.array.calllogtitles);
		mSegmentTabLayout = ViewFindUtils.find(view, R.id.id_segment_tab);
		mSegmentTabLayout.setTabData(titles);
		
		view.findViewById(R.id.id_clear).setOnClickListener(this);
		
		return view;
	}

	@Override
	public void onClick(View v) {
		Toast.makeText(getActivity(), "hehe", Toast.LENGTH_SHORT).show();
	}

}
