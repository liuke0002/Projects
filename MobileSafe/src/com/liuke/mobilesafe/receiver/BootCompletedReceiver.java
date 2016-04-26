package com.liuke.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		SharedPreferences mPref = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		String sim = mPref.getString("sim", "");
		boolean protect = mPref.getBoolean("protect", false);
		if (protect) {
			if (!TextUtils.isEmpty(sim)) {
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSim = tm.getSimSerialNumber();
				if(!currentSim.equals(sim)){
					SmsManager smsManager=SmsManager.getDefault();
					String phone=mPref.getString("safe_phnoe", "");
					smsManager.sendTextMessage(phone, null, "sim has changed", null, null);
				}
			}
		}
	}

}
