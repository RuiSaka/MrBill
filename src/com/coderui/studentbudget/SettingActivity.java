package com.coderui.studentbudget;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.coderui.studentbudget.untilty.StudentbudgetUntility;

public class SettingActivity extends Activity implements OnClickListener {
	private Button return_btn;
	private CheckBox alarm_checkBox, voice_checkbox, shake_checkbox;
	private RelativeLayout rel_settime, rel_voicealarm, rel_shakealarm,
			set_password, local_backup, net_backup, check_version,
			send_suggest, about;
	private TextView time_tv, settime, voice_alarm, shake_alarm;
	private Calendar calendar;
	int alarm_hour = 0, alarm_minute = 0;
	private SharedPreferences sharePreferences;
	private boolean set_voice_almarm = false, set_shake_alarm = false,
			set_alarm = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_view);
		initData();
		getAlarmInfor();
	}

	private void initData() {
		return_btn = (Button) findViewById(R.id.return_btn);
		return_btn.setOnClickListener(this);

		rel_settime = (RelativeLayout) findViewById(R.id.rel_settime);
		rel_settime.setOnClickListener(this);
		rel_voicealarm = (RelativeLayout) findViewById(R.id.rel_voicealarm);
		rel_shakealarm = (RelativeLayout) findViewById(R.id.rel_shakealarm);
		time_tv = (TextView) findViewById(R.id.time_tv);
		settime = (TextView) findViewById(R.id.settime);
		alarm_checkBox = (CheckBox) findViewById(R.id.alarm_checkBox);
		voice_checkbox = (CheckBox) findViewById(R.id.voice_checkbox);
		voice_checkbox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						set_voice_almarm = isChecked;
						sharePreferences = getSharedPreferences(
								StudentbudgetUntility.PASSWORD,
								Context.MODE_PRIVATE);
						Editor edit = sharePreferences.edit();
						edit.putBoolean("set_voice_almarm", set_voice_almarm);
						edit.commit();
						if (set_alarm) {
							setAlarmManager();
						}
					}

				});
		voice_alarm = (TextView) findViewById(R.id.voice_alarm);
		shake_checkbox = (CheckBox) findViewById(R.id.shake_checkbox);
		shake_checkbox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						set_shake_alarm = isChecked;
						sharePreferences = getSharedPreferences(
								StudentbudgetUntility.PASSWORD,
								Context.MODE_PRIVATE);
						Editor edit = sharePreferences.edit();
						edit.putBoolean("set_shake_alarm", set_shake_alarm);
						edit.commit();
						if (set_alarm) {
							setAlarmManager();
						}
					}
				});
		shake_alarm = (TextView) findViewById(R.id.shake_alarm);

		set_password = (RelativeLayout) findViewById(R.id.set_password);
		set_password.setOnClickListener(this);
		local_backup = (RelativeLayout) findViewById(R.id.local_backup);
		local_backup.setOnClickListener(this);
		net_backup = (RelativeLayout) findViewById(R.id.net_backup);
		net_backup.setOnClickListener(this);

		check_version = (RelativeLayout) findViewById(R.id.check_version);
		send_suggest = (RelativeLayout) findViewById(R.id.send_suggest);
		send_suggest.setOnClickListener(this);
		about = (RelativeLayout) findViewById(R.id.about);
		about.setOnClickListener(this);

		alarm_checkBox
				.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							rel_settime.setClickable(true);
							rel_voicealarm.setClickable(true);
							rel_shakealarm.setClickable(true);
							voice_checkbox.setClickable(true);
							shake_checkbox.setClickable(true);
							settime.setTextColor(SettingActivity.this
									.getResources().getColor(R.color.black));
							time_tv.setTextColor(SettingActivity.this
									.getResources().getColor(R.color.black));
							voice_alarm.setTextColor(SettingActivity.this
									.getResources().getColor(R.color.black));
							shake_alarm.setTextColor(SettingActivity.this
									.getResources().getColor(R.color.black));
							set_alarm = true;
							sharePreferences = getSharedPreferences(
									StudentbudgetUntility.PASSWORD,
									Context.MODE_PRIVATE);
							Editor edit = sharePreferences.edit();
							edit.putBoolean("set_alarm", set_alarm);
							edit.commit();
							if (set_alarm) {
								setAlarmManager();
							}
						} else {
							rel_settime.setClickable(false);
							rel_voicealarm.setClickable(false);
							rel_shakealarm.setClickable(false);
							voice_checkbox.setClickable(false);
							shake_checkbox.setClickable(false);
							voice_checkbox.setChecked(false);
							shake_checkbox.setChecked(false);
							settime.setTextColor(SettingActivity.this
									.getResources().getColor(R.color.gray));
							time_tv.setTextColor(SettingActivity.this
									.getResources().getColor(R.color.gray));
							voice_alarm.setTextColor(SettingActivity.this
									.getResources().getColor(R.color.gray));
							shake_alarm.setTextColor(SettingActivity.this
									.getResources().getColor(R.color.gray));
							set_alarm = false;
							set_voice_almarm = false;
							set_shake_alarm = false;
							sharePreferences = getSharedPreferences(
									StudentbudgetUntility.PASSWORD,
									Context.MODE_PRIVATE);
							Editor edit = sharePreferences.edit();
							// 将是否开启提醒震动和声音保存
							edit.putBoolean("set_alarm", set_alarm);
							edit.putBoolean("set_voice_almarm",
									set_voice_almarm);
							edit.putBoolean("set_shake_alarm", set_shake_alarm);
							edit.commit();
						}
					}
				});
	}

	private void getAlarmInfor() {
		// 获取提醒时间
		sharePreferences = getSharedPreferences(StudentbudgetUntility.PASSWORD,
				Context.MODE_PRIVATE);
		alarm_hour = sharePreferences.getInt("alarm_hour", 23);
		alarm_minute = sharePreferences.getInt("alarm_minute", 00);
		String alarm_time = format(alarm_hour) + ":" + format(alarm_minute);
		time_tv.setText(alarm_time);
		// 获取是否开启提醒及提醒类型
		set_alarm = sharePreferences.getBoolean("set_alarm", false);
		set_voice_almarm = sharePreferences.getBoolean("set_voice_almarm",
				false);
		set_shake_alarm = sharePreferences.getBoolean("set_shake_alarm", false);
		voice_checkbox.setChecked(set_voice_almarm);
		shake_checkbox.setChecked(set_shake_alarm);
		alarm_checkBox.setChecked(set_alarm);
	}

	// 设置定时触发提醒
	private void setAlarmManager() {
		sharePreferences = getSharedPreferences(StudentbudgetUntility.PASSWORD,
				Context.MODE_PRIVATE);
		alarm_hour = sharePreferences.getInt("alarm_hour", 23);
		alarm_minute = sharePreferences.getInt("alarm_minute", 00);
		calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, alarm_hour);
		calendar.set(Calendar.MINUTE, alarm_minute);
		Intent intent = new Intent("Notification_Alarm");
		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent, 0);
		AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
		am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
				24 * 60 * 60 * 1000, pi);
	}

	// 设置提醒的时间
	private void setTimePicker() {
		calendar = Calendar.getInstance();
		TimePickerDialog.OnTimeSetListener timeSet = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				calendar.set(Calendar.MINUTE, minute);
				sharePreferences = getSharedPreferences(
						StudentbudgetUntility.PASSWORD, Context.MODE_PRIVATE);
				Editor edit = sharePreferences.edit();
				edit.putInt("alarm_hour", hourOfDay);
				edit.putInt("alarm_minute", minute);
				edit.commit();
				time_tv.setText(format(hourOfDay) + ":" + format(minute));
			}

		};
		sharePreferences = getSharedPreferences(StudentbudgetUntility.PASSWORD,
				Context.MODE_PRIVATE);
		alarm_hour = sharePreferences.getInt("alarm_hour", 23);
		alarm_minute = sharePreferences.getInt("alarm_minute", 00);
		new TimePickerDialog(this, timeSet, SettingActivity.this.alarm_hour,
				SettingActivity.this.alarm_minute, true).show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.return_btn:
			finish();
			break;
		case R.id.rel_settime:
			setTimePicker();
			if (set_alarm) {
				setAlarmManager();
			}
			break;
		case R.id.set_password:
			Intent intent = new Intent(SettingActivity.this,
					SetPassWordActivity.class);
			startActivity(intent);
			break;
		case R.id.net_backup:
			Intent intent_backup = new Intent(SettingActivity.this,
					StoreDateBaseToColud.class);
			startActivity(intent_backup);
			break;
		case R.id.local_backup:
			backupDatabaseToSD();
			break;
		case R.id.send_suggest:
			Intent intent_sendsuggest = new Intent(SettingActivity.this,
					SendSuggestActivity.class);
			startActivity(intent_sendsuggest);
			break;
		case R.id.about:
			Intent intent_about = new Intent(SettingActivity.this,
					AboutActivity.class);
			startActivity(intent_about);
		}
	}

	// 本地备份
	private void backupDatabaseToSD() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SettingActivity.this);
		builder.setTitle("SD卡备份");
		builder.setMessage("备份文件保存在/sdcard/MrBill");
		builder.setPositiveButton("保存", new DialogInterface.OnClickListener() {
			@SuppressLint({ "SdCardPath", "SdCardPath" })
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				if (Environment.getExternalStorageState().equals(
						Environment.MEDIA_MOUNTED)) {
					// File sdCardDir =
					// Environment.getExternalStorageDirectory();//获取SDCard目录
					// File saveFile = new File(sdCardDir, "MyBudget.db");
					File director = new File("/sdcard/MrBill");// 创建目录
					File file = new File("/sdcard/MrBill/MyBudget.db");// 创建文件
					if (!director.exists()) {
						director.mkdirs();
					}
					if (!file.exists()) {
						try {
							FileInputStream fis = new FileInputStream(
									"/data/data/com.coderui.studentbudget/databases/MyBudget.db");
							FileOutputStream fos = new FileOutputStream(file);
							byte[] buffer = new byte[1024];
							int length = 0;
							while ((length = fis.read(buffer)) > 0) {
								fos.write(buffer, 0, length);
							}
							fos.flush();
							fos.close();
							fis.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						file.delete();
						try {
							FileInputStream fis = new FileInputStream(
									"/data/data/com.coderui.studentbudget/databases/MyBudget.db");
							FileOutputStream fos = new FileOutputStream(file);
							byte[] buffer = new byte[1024];
							int length = 0;
							while ((length = fis.read(buffer)) > 0) {
								fos.write(buffer, 0, length);
							}
							fos.flush();
							fos.close();
							fis.close();
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
				Toast.makeText(SettingActivity.this, "备份成功", Toast.LENGTH_SHORT)
						.show();
			}
		});
		builder.setNegativeButton("恢复", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
			}
		});
		builder.create().show();
	}

	private String format(int x) {
		String s = "" + x;
		if (s.length() == 1)
			s = "0" + s;
		return s;
	}

}
