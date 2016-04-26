package com.liuke.mobilesafe.fragment;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.bean.AppInfo;
import com.liuke.mobilesafe.db.dao.AppLockDao;
import com.liuke.mobilesafe.engine.AppInfoParser;

public class UnLockFragment extends Fragment {

	private ListView lvUnLock;
	private TextView tvUnLocks;
	private List<AppInfo> unLockApps;
	private AppLockDao dao;
	private UnLockedAdapter mAdapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater
				.inflate(R.layout.fragment_unlock, container, false);
		lvUnLock = (ListView) view.findViewById(R.id.lv_unlock);
		tvUnLocks = (TextView) view.findViewById(R.id.tv_unlock_count);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		initData();
		mAdapter = new UnLockedAdapter();
		lvUnLock.setAdapter(mAdapter);
	}

	private void initData() {

		List<AppInfo> appInfos = AppInfoParser.getAppInfos(getActivity());
		unLockApps = new ArrayList<AppInfo>();
		dao = new AppLockDao(getActivity());
		for (AppInfo info : appInfos) {
			String packageName = info.getPackageName();
			boolean locked = dao.isLocked(packageName);
			if (!locked) {
				unLockApps.add(info);
			}
		}

	}

	class UnLockedAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			tvUnLocks.setText("Î´¼ÓËø(" + unLockApps.size() + ")");
			return unLockApps.size();
		}

		@Override
		public AppInfo getItem(int position) {
			return unLockApps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final View view;
			ViewHolder holder;
			if (convertView == null) {
				holder = new ViewHolder();
				view = View.inflate(getActivity(), R.layout.item_unlock_fragment,
						null);
				holder.ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
				holder.ibUnLock = (ImageButton) view
						.findViewById(R.id.ib_unlock);
				holder.tvName = (TextView) view.findViewById(R.id.tv_name);
				view.setTag(holder);
			} else {
				view = convertView;
				holder = (ViewHolder) view.getTag();
			}
			holder.ivIcon.setBackground(unLockApps.get(position).getIcon());
			holder.tvName.setText(unLockApps.get(position).getApkName());
			holder.ibUnLock.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					TranslateAnimation translateAnimation = new TranslateAnimation(
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 1,
							Animation.RELATIVE_TO_SELF, 0,
							Animation.RELATIVE_TO_SELF, 0);
					translateAnimation.setDuration(500);
					view.startAnimation(translateAnimation);
					new Thread() {
						public void run() {
							SystemClock.sleep(500);
							getActivity().runOnUiThread(new Runnable() {
								@Override
								public void run() {
									dao.addLock(unLockApps.get(position)
											.getPackageName());
									unLockApps.remove(position);
									mAdapter.notifyDataSetChanged();
								}
							});
						};
					}.start();

				}
			});
			return view;
		}
	}

	static class ViewHolder {
		TextView tvName;
		ImageView ivIcon;
		ImageButton ibUnLock;
	}

}
