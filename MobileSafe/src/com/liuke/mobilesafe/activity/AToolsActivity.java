package com.liuke.mobilesafe.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.utils.SmsUtils;
import com.liuke.mobilesafe.utils.UIUtils;

public class AToolsActivity extends Activity {

	private TextView tvPhoneAddressQuery;
	private TextView tvSmsBackup;
	private TextView tvCallShortcut;
	private ProgressDialog pd;
	private TextView tvAppLock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();

		setListener();
	}

	private void setListener() {

		tvPhoneAddressQuery.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				startActivity(new Intent(AToolsActivity.this,
						AddressActivity.class));
			}
		});
		tvAppLock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(AToolsActivity.this,
						AppLockActivity.class));
			}
		});
		tvCallShortcut.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(AToolsActivity.this,
						ContactActivity.class);
				startActivityForResult(intent, 5);
			}
		});
		tvSmsBackup.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pd = new ProgressDialog(AToolsActivity.this);
				pd.setTitle("提示");
				pd.setIcon(R.drawable.ic_launcher);
				pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				pd.setMessage("稍安勿躁。。。正在备份");
				pd.show();
				new Thread() {
					public void run() {
						boolean result = SmsUtils.backupSms(
								AToolsActivity.this, pd);
						if (result) {
							UIUtils.showToast("备份成功", AToolsActivity.this);
							pd.dismiss();

						} else {
							UIUtils.showToast("备份失败", AToolsActivity.this);
							pd.dismiss();
						}
					};
				}.start();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK && requestCode == 5) {
			String number = data.getStringExtra("number").replaceAll("-", "")
					.replaceAll(" ", "");
			Intent intent = new Intent();
			intent.putExtra("duplicate", false);
			intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "handsome");
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, BitmapFactory
					.decodeResource(getResources(), R.drawable.thumb_me));
			Intent short_cut = new Intent();
			short_cut.setAction(Intent.ACTION_CALL);
			short_cut.setData(Uri.parse("tel://" + number));
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, short_cut);
			sendBroadcast(intent);
			Toast.makeText(this, "创建成功", Toast.LENGTH_SHORT).show();
		} else {
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void initView() {
		setContentView(R.layout.activity_atools);
		tvPhoneAddressQuery = (TextView) findViewById(R.id.tvPhoneAdressQuery);
		tvSmsBackup = (TextView) findViewById(R.id.tvSmsBackup);
		tvCallShortcut = (TextView) findViewById(R.id.tvCallShortcut);
		tvAppLock = (TextView) findViewById(R.id.tvAppLock);
	}

}
