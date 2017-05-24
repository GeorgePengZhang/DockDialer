package com.pachong.mycontacts.adapter;

import java.util.List;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.pachong.mycontacts.R;
import com.pachong.mycontacts.bean.FavoritesBean;
import com.pachong.mycontacts.utils.ContactsPhotoLoader;


/**
 * @ClassName: CallsLogAdapter
 * @Description: TODO
 * @author: steven zhang
 * @date: Sep 27, 2016 2:31:16 PM
 */
public class FavoritesAdapter extends BaseAdapter implements OnClickListener {
	
	private List<FavoritesBean> mList;
	private Context mContext;
	private IOnItemInfoClickListener mListener;
	
	public FavoritesAdapter(Context context, List<FavoritesBean> list, IOnItemInfoClickListener listener) {
		mContext = context;
		mList = list;
		mListener = listener;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.favorites_item, null);
			
			viewHolder = new ViewHolder();
			viewHolder.image = (ImageView) convertView.findViewById(R.id.id_image);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.info = (ImageView) convertView.findViewById(R.id.id_callog_info);
			
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		FavoritesBean bean = (FavoritesBean) getItem(position);
		
        
        //设置用户名字
        viewHolder.name.setText(bean.getName());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        //------------------------------------
		
        //设置用户头像
		String photouri = bean.getPhotouri();
		if (photouri != null) {
			ContactsPhotoLoader.getInstance().loadImage(mContext, viewHolder.image, Uri.parse(photouri), bean.getId(), R.drawable.calllog_contacts_img_default);
		} else {
			viewHolder.image.setImageResource(R.drawable.calllog_contacts_img_default);
		}
		
		return convertView;
	}
	
	private static class ViewHolder {
		private ImageView image;
		private TextView name;
		private ImageView info;
	}
    
    public interface IOnItemInfoClickListener {
    	public void onItemInfoClick(View v);
    }
    
    public void setOnItemInfoClickListener(IOnItemInfoClickListener listener) {
    	mListener = listener;
    }

	@Override
	public void onClick(View v) {
		if (mListener != null) {
			mListener.onItemInfoClick(v);
		}
	}
}
