package com.liuke.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.view.SettingItemView;

public class SetUp2Activity extends BaseSetupActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up2);
		final SettingItemView sivLocked = (SettingItemView) findViewById(R.id.siv_loocked);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		String locked = mPref.getString("sim", "");
		if(!TextUtils.isEmpty(locked)){
			sivLocked.setChecked(true);
		}else{
			sivLocked.setChecked(false);
		}
		sivLocked.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sivLocked.setChecked(!sivLocked.isChecked());
				TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
				String serialNumber = tm.getSimSerialNumber();
				if(sivLocked.isChecked()){
					mPref.edit().putString("sim", serialNumber).commit();
				}else{
					mPref.edit().remove("sim").commit();
				}
			}
		});
	}

	public void showNextPage() {
		startActivity(new Intent(this, SetUp3Activity.class));
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

	public void showPreviousPage() {
		finish();
		overridePendingTransition(R.anim.trans_previous_in,
				R.anim.trans_previous_out);
	}

}
