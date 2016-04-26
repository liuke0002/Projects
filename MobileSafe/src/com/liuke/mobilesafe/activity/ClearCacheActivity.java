package com.liuke.mobilesafe.activity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.utils.UIUtils;

public class ClearCacheActivity extends Activity {

	private PackageManager packageManager;
	private List<CacheInfo>infos;
	@ViewInject(R.id.lv_cache)
	private ListView lvCache;
	@ViewInject(R.id.ll_loading)
	private LinearLayout llLoad;
	@ViewInject(R.id.btn_clear_all)
	private Button btnClearAll;
	private CacheAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_clear_cache);
		ViewUtils.inject(this);
	}
	@Override
	protected void onResume() {
		super.onStart();
		initView();
		btnClearAll.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					Method method=PackageManager.class.getDeclaredMethod("freeStorageAndNotify", long.class,IPackageDataObserver.class);
					method.invoke(packageManager, Integer.MAX_VALUE,new IPackageDataObserver.Stub() {
						
						@Override
						public void onRemoveCompleted(String packageName, boolean succeeded)
								throws RemoteException {
							
						}
					});
					mAdapter.notifyDataSetChanged();
					UIUtils.showToast("«Â¿ÌÕÍ±œ", ClearCacheActivity.this);
					
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	private void initView() {
		packageManager = getPackageManager();
		infos=new ArrayList<CacheInfo>();
		new Thread(){
			public void run() {
				List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
				mAdapter=new CacheAdapter();
				for(PackageInfo info:installedPackages){
					getCatchSize(info);
				}
				runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						llLoad.setVisibility(View.GONE);
						lvCache.setAdapter(mAdapter);
					}
				});
			};
		}.start();
	}

	private void getCatchSize(PackageInfo info) {
		try {
			Method method = PackageManager.class.getDeclaredMethod("getPackageSizeInfo", String.class,IPackageStatsObserver.class);
			method.invoke(packageManager, info.packageName,new MyIPackageStatsObserver(info));
		}catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class MyIPackageStatsObserver extends IPackageStatsObserver.Stub{
		private PackageInfo info;
		public MyIPackageStatsObserver(PackageInfo info) {
			super();
			this.info = info;
		}
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			long cacheSize = pStats.cacheSize;
			if(cacheSize>0){
				CacheInfo cacheInfo = new CacheInfo();
				cacheInfo.icon=info.applicationInfo.loadIcon(packageManager);
				cacheInfo.apkName=info.applicationInfo.loadLabel(packageManager).toString();
				cacheInfo.cacheSize=cacheSize;
				cacheInfo.packageName=info.packageName;
				infos.add(cacheInfo);
			}

		}
	}
	static class CacheInfo{
		Drawable icon;
		String apkName;
		String packageName;
		long cacheSize;
	}
	
	class CacheAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return infos.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			View view;
			if(convertView==null){
				view = View.inflate(ClearCacheActivity.this, R.layout.item_clear_cache, null);
				holder=new ViewHolder();
				holder.tvApkName=(TextView) view.findViewById(R.id.tv_name);
				holder.ivIcon=(ImageView) view.findViewById(R.id.iv_icon);
				holder.ibClear=(ImageButton) view.findViewById(R.id.ib_clear);
				holder.tvCacheSize=(TextView) view.findViewById(R.id.tv_cache_size);
				view.setTag(holder);
			}else{
				view=convertView;
				holder=(ViewHolder) view.getTag();
			}
			holder.tvApkName.setText(infos.get(position).apkName);
			holder.tvCacheSize.setText("ª∫¥Ê  "+Formatter.formatFileSize(ClearCacheActivity.this, infos.get(position).cacheSize));
			holder.ibClear.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					Intent detailIntent = new Intent();
					detailIntent
							.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
					detailIntent
							.addCategory(Intent.CATEGORY_DEFAULT);
					detailIntent.setData(Uri.parse("package:"
							+ infos.get(position).packageName));
					startActivity(detailIntent);
				}
			});
			holder.ivIcon.setBackground(infos.get(position).icon);
			return view;
		}
		
	}
	static class ViewHolder{
		ImageButton ibClear;
		TextView tvCacheSize;
		TextView tvApkName;
		ImageView ivIcon;
	}
	

}
