package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.liuke.mobilesafe.R;

public class LostFindActivity extends Activity {

	private SharedPreferences mPref;
	private TextView tvPhone;
	private ImageView ivProtect;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPref = getSharedPreferences("config", MODE_PRIVATE);
		
		boolean configed = mPref.getBoolean("configed", false);
		if(configed){
			setContentView(R.layout.activity_lost_find);
			tvPhone=(TextView) findViewById(R.id.tvPhone);
			ivProtect=(ImageView) findViewById(R.id.ivProtect);
			String number = mPref.getString("safe_phone", "");
			tvPhone.setText(number);
			boolean protect = mPref.getBoolean("protect", false);
			System.out.println(protect);
			if(protect){
				ivProtect.setImageResource(R.drawable.lock);
			}else{
				ivProtect.setImageResource(R.drawable.unlock);
			}
		}else{
			startActivity(new Intent(this, SetUp1Activity.class));
			finish();
		}
	}
	
	public void reEnter(View v){
		startActivity(new Intent(this, SetUp1Activity.class));
		finish();
	}

	

}
