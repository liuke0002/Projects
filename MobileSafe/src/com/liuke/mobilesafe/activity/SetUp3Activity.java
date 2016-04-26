package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.liuke.mobilesafe.R;

public class SetUp3Activity extends BaseSetupActivity {


	private Button btnContact;
	
	private EditText etNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_up3);
		initView();
		btnContact.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SetUp3Activity.this, ContactActivity.class);
				startActivityForResult(intent, 3);
			}
		});
		String safe_phone = mPref.getString("safe_phone", "");
		etNum.setText(safe_phone);
	}

	private void initView() {
		btnContact=(Button) findViewById(R.id.btnContact);
		etNum=(EditText) findViewById(R.id.etNum);
	}

	@Override
	public void showNextPage() {
		// TODO Auto-generated method stub
		startActivity(new Intent(SetUp3Activity.this, SetUp4Activity.class));
		String number = etNum.getText().toString().trim();
		
		if(TextUtils.isEmpty(number)){
			Toast.makeText(this, "安全号码不能为空", Toast.LENGTH_SHORT).show();
			return;
		}else{
			mPref.edit().putString("safe_phone", number).commit();
		}
		
		overridePendingTransition(R.anim.trans_in, R.anim.trans_out);
	}

	@Override
	public void showPreviousPage() {
		// TODO Auto-generated method stub
		finish();
		overridePendingTransition(R.anim.trans_previous_in, R.anim.trans_previous_out);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==Activity.RESULT_OK&&requestCode==3){
			String number = data.getStringExtra("number").replaceAll("-", "").replaceAll(" ", "");
			etNum.setText(number);
			mPref.edit().putString("safe_phone", number).commit();
		}else{
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
