package com.st.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.st.R;

public class FilelListAdapter extends BaseAdapter {
	
	private Context mContext;
	private ArrayList<String> mList;
	public FilelListAdapter(Context context, ArrayList<String> list) {
		super();
		mContext = context;
		mList = list;
	}

	public FilelListAdapter(Context context) {
		super();
		mContext = context;
	}

	public void setDatas (ArrayList<String> mlist) {
		mList = mlist;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList == null ? null : mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	ViewHolder holder = null;
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_file_list, parent, false);
			holder.tv_item = (TextView) convertView.findViewById(R.id.tv_file);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tv_item.setText(mList.get(position));
		return convertView;
	}

	private class ViewHolder {
		TextView tv_item;
	}
}
