package com.liuke.mobilesafe.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.liuke.mobilesafe.db.dao.BlackNumDao;

public class CallsafeService extends Service {
	
	BroadcastReceiver mReceiver;
	private BlackNumDao dao;

	@Override
	public IBinder onBind(Intent intent) {
		
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		TelephonyManager tm=(TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		PhoneStateListener listener=new MyPhoneStateListener();
		
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		mReceiver=new BroadcastReceiver() {
			

			@Override
			public void onReceive(Context context, Intent intent) {
				Object[] object = (Object[]) intent.getExtras().get("pdus");
				for (Object temp : object) {
					SmsMessage message = SmsMessage.createFromPdu((byte[]) temp);
					String address = message.getOriginatingAddress();
					dao = new BlackNumDao(context);
					if(dao.queryMode(address).equals("1")){
						abortBroadcast();
					}else if(dao.queryMode(address).equals("3")){
						abortBroadcast();
					}
				}
			}
		};
		IntentFilter filter=new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
		filter.setPriority(Integer.MAX_VALUE);
		registerReceiver(mReceiver, filter);
	}
	
	private class MyPhoneStateListener extends PhoneStateListener{

		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:
				try {
					endCall(incomingNumber);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				break;

			default:
				break;
			}
		}

		private void endCall(String incomingNumber)
				throws IllegalAccessException, InvocationTargetException {
			dao=new BlackNumDao(CallsafeService.this);
			String mode = dao.queryMode(incomingNumber);
			if(mode.equals("1")||mode.equals("2")){
				try {
					Class<?>clazz=getClassLoader().loadClass("android.os.ServiceManager");
					Method method=clazz.getDeclaredMethod("getService", String.class);
					IBinder iBinder=(IBinder) method.invoke(null, TELEPHONY_SERVICE);
					ITelephony iTelephony=ITelephony.Stub.asInterface(iBinder);
					iTelephony.endCall();
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mReceiver);
	}
}
