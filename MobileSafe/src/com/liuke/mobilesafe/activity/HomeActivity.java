package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.utils.MD5Utils;

public class HomeActivity extends Activity {

	private GridView gvHome;
	HomeAdapter mAdapter;
	private String[] mItems = new String[] { "手机防盗", "通讯卫士", "软件管理", "进程管理",
			"流量管理", "手机杀毒", "缓存清理", "高级工具", "设置中心" };

	private int[] mPics = new int[] { R.drawable.home_safe,
			R.drawable.home_callmsgsafe, R.drawable.home_apps,
			R.drawable.home_taskmanager, R.drawable.home_netmanager,
			R.drawable.home_trojan, R.drawable.home_sysoptimize,
			R.drawable.home_tools, R.drawable.home_settings };
	private SharedPreferences mPref;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		initView();
	}
	private void initView() {
		// TODO Auto-generated method stub
		gvHome=(GridView) findViewById(R.id.gvHome); 
		mAdapter=new HomeAdapter(mItems, mPics, this);
		gvHome.setAdapter(mAdapter);
		gvHome.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				switch (position) {
				case 8:
					startActivity(new Intent(HomeActivity.this, SettingActivity.class));
					break;
				case 7:
					startActivity(new Intent(HomeActivity.this, AToolsActivity.class));
					break;
				case 0:
					showPasswordDialog();
					break;
				case 1:
					startActivity(new Intent(HomeActivity.this, CallSafeActivity.class));
					break;
				case 2:
					startActivity(new Intent(HomeActivity.this, AppManagerActivity.class));
					break;
				case 3:
					startActivity(new Intent(HomeActivity.this,TaskActivity.class));
					break;
				case 5:
					startActivity(new Intent(HomeActivity.this, AntivirusActivity.class));
					break;
				case 4:
					startActivity(new Intent(HomeActivity.this, TrafficManagerActivity.class));
					break;
				case 6:
					startActivity(new Intent(HomeActivity.this, ClearCacheActivity.class));
					break;
				default:
					break;
				}
			}
			
		});
	}

	protected void showPasswordDialog() {
		String password = mPref.getString("password", null);
		if(!TextUtils.isEmpty(password)){
			showPassWordInputDialog();
		}else{
			showPasswordSetDialog();
		}
	}

	private void showPassWordInputDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		View view=View.inflate(this, R.layout.dialog_input_password, null);
		builder.setView(view);
		final EditText etPwd=(EditText)view.findViewById(R.id.etPassword);
		Button btnOk=(Button) view.findViewById(R.id.btnOk);
		Button btnCancel=(Button) view.findViewById(R.id.btnCancel);
		final AlertDialog dialog = builder.create();
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pwd = etPwd.getText().toString();
				if(!TextUtils.isEmpty(pwd)){
					String savedPwd = mPref.getString("password", null);
					if(savedPwd.equals(MD5Utils.encode(pwd))){
						Toast.makeText(HomeActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					}else{
						etPwd.setError("密码错误");
					}
				}
			}
		});
		
		dialog.show();
	}
	private void showPasswordSetDialog() {
		AlertDialog.Builder builder=new AlertDialog.Builder(this);
		View view=View.inflate(this, R.layout.dialog_set_password, null);
		builder.setView(view);
		final EditText etPwd=(EditText)view.findViewById(R.id.etPassword);
		final EditText etConfirmPwd=(EditText) view.findViewById(R.id.etConfirmPwd);
		Button btnOk=(Button) view.findViewById(R.id.btnOk);
		Button btnCancel=(Button) view.findViewById(R.id.btnCancel);
		final AlertDialog dialog = builder.create();
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btnOk.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String pwd=etPwd.getText().toString();
				String confirmPwd=etConfirmPwd.getText().toString();
				if(TextUtils.isEmpty(pwd)){
					etPwd.setError("请输入密码");
				}  
				else if(TextUtils.isEmpty(confirmPwd)){
					etConfirmPwd.setError("请确认密码");
				}  
				else if(!TextUtils.isEmpty(pwd)&&!TextUtils.isEmpty(pwd)){
					if(pwd.equals(confirmPwd)){
						Toast.makeText(HomeActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
						mPref.edit().putString("password",MD5Utils.encode(pwd)).commit();
						dialog.dismiss();
						startActivity(new Intent(HomeActivity.this, LostFindActivity.class));
					}else{
						Toast.makeText(HomeActivity.this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
					}
				}
			}
		});
		
		dialog.show();
	}

	class HomeAdapter extends BaseAdapter{
		String []items;
		int []pics;
		Context context;

		public HomeAdapter(String[] items, int[] pics, Context context) {
			super();
			this.items = items;
			this.pics = pics;
			this.context = context;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return items.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(HomeActivity.this, R.layout.home_list_item, null);
			TextView tvItem=(TextView) view.findViewById(R.id.tvItem);
			ImageView ivItem=(ImageView) view.findViewById(R.id.ivItem);
			tvItem.setText(items[position]);
			ivItem.setImageResource(pics[position]);
			return view;
		}

	}


}
