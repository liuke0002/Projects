package com.liuke.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.liuke.mobilesafe.R;

public class SetUp4Activity extends BaseSetupActivity {


	private CheckBox cbStolean;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up4);
		cbStolean=(CheckBox) findViewById(R.id.cbStolean);
		boolean protect = mPref.getBoolean("protect", false);
		if(protect){
			cbStolean.setText("防盗保护已经开启");
		}else{
			cbStolean.setText("防盗保护没有开启");
		}
		cbStolean.setChecked(protect);
		cbStolean.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked){
					cbStolean.setText("防盗保护已经开启");
				}
				else{
					cbStolean.setText("防盗保护没有开启");
				}
				mPref.edit().putBoolean("protect", isChecked).commit();
			}
		});
	}


	@Override
	public void showNextPage() {
		mPref.edit().putBoolean("configed", true).commit();
		startActivity(new Intent(this, LostFindActivity.class));
	}

	@Override
	public void showPreviousPage() {
		finish();
		overridePendingTransition(R.anim.trans_previous_in, R.anim.trans_previous_out);
	}
}
