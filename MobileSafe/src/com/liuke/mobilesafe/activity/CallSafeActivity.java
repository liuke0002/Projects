package com.liuke.mobilesafe.activity;

import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.bean.BlackNumInfo;
import com.liuke.mobilesafe.db.dao.BlackNumDao;

public class CallSafeActivity extends Activity {
	
	private ListView blackLv;
	private Button btnAddBlack;
	CallSafeAdapter mAdapter;
	private List<BlackNumInfo> blackList;
	private View dialog_view;
	
	private EditText etBlackNum;
	private CheckBox cbSmsDefend,cbPhoneDefend;
	private Button btnNeg,btnPos;
	private AlertDialog dialog;
	
	private BlackNumDao dao;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_call_safe);
		initView();
		initData();
		setListener();
	}
	private void setListener() {
		// TODO Auto-generated method stub
		btnAddBlack.setOnClickListener(new OnClickListener() {
			


			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new AlertDialog.Builder(CallSafeActivity.this);
				dialog_view = View.inflate(CallSafeActivity.this, R.layout.dialog_add_black_number, null);
				builder.setView(dialog_view);
				dialog = builder.create();
				dialog.show();
				btnNeg=(Button) dialog_view.findViewById(R.id.btnNeg);
				etBlackNum=(EditText) dialog_view.findViewById(R.id.etBlackNum);
				btnPos=(Button) dialog_view.findViewById(R.id.btnPos);
				cbSmsDefend=(CheckBox) dialog_view.findViewById(R.id.cbSmsDefend);
				cbPhoneDefend=(CheckBox) dialog_view.findViewById(R.id.cbPhoneDefend);
				btnNeg.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						dialog.dismiss();
					}
				});
				btnPos.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						String blackNum=etBlackNum.getText().toString();
						String mode="-1";
						if(TextUtils.isEmpty(blackNum)){
							Toast.makeText(CallSafeActivity.this, "黑名单不能为空！", Toast.LENGTH_SHORT).show();
							return ;
						}else{
							if(cbPhoneDefend.isChecked()&&cbSmsDefend.isChecked()){
								mode="1";
							}else if(cbPhoneDefend.isChecked()){
								mode="2";
							}else if(cbSmsDefend.isChecked()){
								mode="3";
							}
							dao.add(blackNum, mode);
							blackList.add(0,new BlackNumInfo(blackNum, mode));
							mAdapter.notifyDataSetChanged();
						}
						dialog.dismiss();
					}
				});
			}
		});
		
	}
	private Handler handler=new Handler(){
		public void handleMessage(android.os.Message msg) {
			mAdapter=new CallSafeAdapter();
			blackLv.setAdapter(mAdapter);
		};
	};
	private void initData() {
		new Thread(){

			public void run() {
				dao = new BlackNumDao(CallSafeActivity.this);
				blackList = dao.queryAll();
				handler.sendEmptyMessage(0);
			};
		}.start();
	}

	private void initView() {
		blackLv=(ListView) findViewById(R.id.list_view);
		btnAddBlack=(Button) findViewById(R.id.btnAddBlack);
	}
	
	
	class CallSafeAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return blackList.size();
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if(convertView==null){
				convertView = View.inflate(CallSafeActivity.this, R.layout.callsafe_list_item, null);
				holder=new ViewHolder();
				holder.tvNumber=(TextView) convertView.findViewById(R.id.tvNumber);
				holder.tvMode=(TextView) convertView.findViewById(R.id.tvMode);
				holder.ibDelete=(ImageButton) convertView.findViewById(R.id.ibDelete);
				convertView.setTag(holder);
			}else{
				holder = (ViewHolder) convertView.getTag();
			}
			String mode=blackList.get(position).getMode();
			if(mode.equals("1")){
				holder.tvMode.setText("短信拦截+电话拦截");
			}else if(mode.equals("2")){
				holder.tvMode.setText("电话拦截");
			}else if(mode.equals("3")){
				holder.tvMode.setText("短信拦截");
			}
			holder.tvNumber.setText(blackList.get(position).getNumber());
			holder.ibDelete.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					String number = blackList.get(position).getNumber();
					boolean result = dao.delete(number);
					if(result==true){
						blackList.remove(blackList.get(position));
						mAdapter.notifyDataSetChanged();
						Toast.makeText(CallSafeActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(CallSafeActivity.this, "删除失败", Toast.LENGTH_LONG).show();
					}
				}
			});
			return convertView;
		}
		
	}
	static class ViewHolder{
		TextView tvNumber,tvMode;
		ImageButton ibDelete;
	}

}
