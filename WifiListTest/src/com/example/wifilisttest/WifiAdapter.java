package com.example.wifilisttest;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class WifiAdapter extends BaseAdapter {
	private Drawable dUserIcon;

	private Context mContext;
	private LayoutInflater mInflater;
	private List<ScanResult> mWifiEntityList;

	public WifiAdapter(Context context, List<ScanResult> list) {
		mContext = context;
		mInflater = LayoutInflater.from(context);
		mWifiEntityList = list;

		initBitmaps();
	}

	private void initBitmaps() {
		if (mContext != null) {
			Resources res = mContext.getResources();
			dUserIcon = res.getDrawable(R.drawable.wifi);
		}
	}

	public void setInfoList(List<ScanResult> list) {
		mWifiEntityList = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return mWifiEntityList.size();
	}

	@Override
	public Object getItem(int position) {
		return mWifiEntityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.wifi_item, parent, false);
			ImageView icon = (ImageView) convertView.findViewById(R.id.wifi_icon);
			TextView title = (TextView) convertView.findViewById(R.id.wifi_name);

			viewHolder = new ViewHolder(icon, title);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.index = position;
		ScanResult info = mWifiEntityList.get(position);
		viewHolder.title.setText(info.SSID);
		return convertView;
	}

	static public class ViewHolder {
		public int index;
		public ImageView icon;
		public TextView title;

		public ViewHolder(ImageView pIcon, TextView pTitle) {
			icon = pIcon;
			title = pTitle;
		}
	}
}
