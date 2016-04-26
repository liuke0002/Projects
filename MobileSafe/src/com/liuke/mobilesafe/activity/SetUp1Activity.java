package com.liuke.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;

import com.liuke.mobilesafe.R;

public class SetUp1Activity extends BaseSetupActivity {


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up1);
		
	}

	@Override
	public void showNextPage() {
		startActivity(new Intent(this, SetUp2Activity.class));
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		
	}
	
}
