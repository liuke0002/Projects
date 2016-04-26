package com.liuke.mobilesafe.activity;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.bean.TrafficInfo;
import com.liuke.mobilesafe.engine.TrafficInfoParser;

public class TrafficManagerActivity extends Activity {

	@ViewInject(R.id.ll_loading)
	private LinearLayout llLoad;
	@ViewInject(R.id.lv_traffic)
	private ListView lvTraffic;
	private List<TrafficInfo> infos;
	private TrafficAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}

	private void initData() {
		new Thread() {
			public void run() {
				infos = TrafficInfoParser
						.getTrafficInfos(TrafficManagerActivity.this);
				System.out.println(infos.size());
				Collections.sort(infos, new Comparator<TrafficInfo>() {
					@Override
					public int compare(TrafficInfo lhs, TrafficInfo rhs) {
						return lhs.getTotalTraffics() > rhs.getTotalTraffics() ? -1
								: (lhs.getTotalTraffics() == rhs
										.getTotalTraffics())?0:1;
					}

				});
				mAdapter = new TrafficAdapter();
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						llLoad.setVisibility(View.GONE);
						lvTraffic.setAdapter(mAdapter);
					}
				});
			};
		}.start();
	}

	private void initView() {
		setContentView(R.layout.activity_traffic_manager);
		ViewUtils.inject(this);
	}

	class TrafficAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public TrafficInfo getItem(int position) {
			// TODO Auto-generated method stub
			return infos.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			View view;
			if(convertView==null){
				holder = new ViewHolder();
				view=View.inflate(TrafficManagerActivity.this, R.layout.item_traffic, null);
				holder.ivIcon=(ImageView) view.findViewById(R.id.iv_icon);
				holder.tvApkName=(TextView) view.findViewById(R.id.tv_apkname);
				holder.tvDown=(TextView) view.findViewById(R.id.tv_down);
				holder.tvUp=(TextView) view.findViewById(R.id.tv_up);
				holder.tvTotal=(TextView) view.findViewById(R.id.tv_total);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			TrafficInfo info = infos.get(position);
			holder.ivIcon.setBackground(info.getIcon());
			holder.tvUp.setText("ÉÏ´«  "+Formatter.formatFileSize(TrafficManagerActivity.this, info.getUpTraffics()));
			holder.tvDown.setText("ÏÂÔØ  "+Formatter.formatFileSize(TrafficManagerActivity.this, info.getDownTraffics()));
			holder.tvTotal.setText(Formatter.formatFileSize(TrafficManagerActivity.this, info.getTotalTraffics()));
			holder.tvApkName.setText(info.getApkName());
			return view;
		}
	}
	static class ViewHolder{
		ImageView ivIcon;
		TextView tvUp;
		TextView tvDown;
		TextView tvTotal;
		TextView tvApkName;
	}

}
