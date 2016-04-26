package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.db.dao.AdressDao;

public class AddressActivity extends Activity {

	
	private TextView tvResult;
	private Button btnQuery;
	private EditText etPhoneNum;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);
		initView();
		etPhoneNum.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String adress = AdressDao.getAdress(s.toString());
				tvResult.setText(adress);
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String adress = AdressDao.getAdress(s.toString());
				tvResult.setText(adress);
			}
		});
		btnQuery.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phoneNumber = etPhoneNum.getText().toString().trim();
				if(!TextUtils.isEmpty(phoneNumber)){
					String adress = AdressDao.getAdress(phoneNumber);
					tvResult.setText(adress);
				}else{
					Animation animation = AnimationUtils.loadAnimation(AddressActivity.this, R.anim.shake);
					etPhoneNum.startAnimation(animation);
					vibirat();
				}
			}
		});
	}
	private void initView() {
		// TODO Auto-generated method stub
		tvResult=(TextView) findViewById(R.id.tvResult);
		btnQuery=(Button) findViewById(R.id.btnQuery);
		etPhoneNum=(EditText) findViewById(R.id.etPhoneNum);
	}
	private void vibirat(){
		Vibrator virbrator=(Vibrator) getSystemService(VIBRATOR_SERVICE);
		virbrator.vibrate(300);
	}

}
