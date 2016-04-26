package com.liuke.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.liuke.mobilesafe.R;
import com.liuke.mobilesafe.db.dao.AdressDao;

/**
 * �������ѵķ���
 * 
 * @author liuke
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mPref;
	private int startX;
	private int startY;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();

		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);// ���������״̬

		receiver = new OutCallReceiver();

		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);// ��̬ע��㲥
	}

	class MyListener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// �绰��������
				String address = AdressDao.getAdress(incomingNumber);// ������������ѯ������
				showToast(address);
				break;

			case TelephonyManager.CALL_STATE_IDLE:// �绰����״̬
				if (mWM != null && view != null) {
					mWM.removeView(view);// ��window���Ƴ�view
					view = null;
				}
				break;
			default:
				break;
			}

			super.onCallStateChanged(state, incomingNumber);
		}

	}

	/**
	 * ����ȥ��Ĺ㲥������ ��ҪȨ��: android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 * @author Kevin
	 * 
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();// ��ȡȥ��绰����

			String address = AdressDao.getAdress(number);
			// Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// ֹͣ�������

		unregisterReceiver(receiver);// ע���㲥
	}

	/**
	 * �Զ�������ظ���
	 */
	private void showToast(String text) {
		mWM = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		final int winWidth=mWM.getDefaultDisplay().getWidth();
		final int winHeight=mWM.getDefaultDisplay().getHeight();
		final WindowManager.LayoutParams params = new WindowManager.LayoutParams();

		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		params.format = PixelFormat.TRANSLUCENT;
		params.gravity = Gravity.TOP + Gravity.LEFT;// Ĭ��������λ��
		params.type = WindowManager.LayoutParams.TYPE_PHONE;
		view = View.inflate(this, R.layout.toast_address, null);

		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = mPref.getInt("address_style", 1);

		view.setBackgroundResource(bgs[style]);// ���ݴ洢����ʽ���±���
		int topY = mPref.getInt("topY", 0);
		int leftX = mPref.getInt("leftX", 0);
		params.x = leftX;
		params.y = topY;
		TextView tvText = (TextView) view.findViewById(R.id.tvAddress);
		tvText.setText(text);

		mWM.addView(view, params);// ��view�������Ļ��(Window)

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					int dx = endX - startX;
					int dy = endY - startY;
					params.x += dx;
					params.y += dy;
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if(params.x>winWidth-view.getWidth()){
						params.x=winWidth-view.getWidth();
					}
					if(params.y>winHeight-view.getHeight()){
						params.y=winHeight-view.getHeight();
					}
					mWM.updateViewLayout(view, params);
					break;
				case MotionEvent.ACTION_UP:
					Editor editor = mPref.edit();
					editor.putInt("leftX", params.x);
					editor.putInt("topY", params.y);
					editor.commit();
					break;

				default:
					break;
				}
				return true;
			}
		});

	}

}
