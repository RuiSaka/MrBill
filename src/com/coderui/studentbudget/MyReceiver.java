package com.coderui.studentbudget;

import com.coderui.studentbudget.untilty.StudentbudgetUntility;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class MyReceiver extends BroadcastReceiver {

	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		SharedPreferences sharePreferences=context.getSharedPreferences(StudentbudgetUntility.PASSWORD,Context.MODE_PRIVATE);
		boolean set_voice_almarm=sharePreferences.getBoolean("set_voice_almarm", false);
		boolean set_shake_alarm=sharePreferences.getBoolean("set_shake_alarm", false);
		NotificationManager manager=(NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
		Notification notification = new Notification();
		notification.icon=R.drawable.actionbar_icon;
		notification.tickerText="账单先生";
		notification.when=System.currentTimeMillis();
		if(set_voice_almarm){
			notification.defaults=Notification.DEFAULT_SOUND;
			notification.audioStreamType= android.media.AudioManager.ADJUST_LOWER;
		}
		if(set_shake_alarm){
			notification.defaults = Notification.DEFAULT_VIBRATE;
		}
		notification.flags=Notification.FLAG_AUTO_CANCEL;//自动清除
		Intent notificationIntent = new Intent(context, HomepagerActivity.class);
		notificationIntent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
		notification.setLatestEventInfo(context, "温馨提示", "账单先生提醒您，到了该记账的时间了。", contentIntent);
		manager.notify(1993, notification);
	}

}
