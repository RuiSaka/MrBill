package com.coderui.studentbudget;

import java.sql.SQLException;

import com.coderui.studentbudget.db.MyDbHelper;
import com.coderui.studentbudget.untilty.StudentbudgetUntility;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class SplashScreenActivity extends Activity {
	private final int TIME_UP = 1;
	private final Double ZERO = 0.0;
	public static MyDbHelper db = null;
	public static int Height;
	public static int Width;

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == TIME_UP) {
				SharedPreferences sharePreferences=getSharedPreferences(StudentbudgetUntility.PASSWORD,Context.MODE_PRIVATE);
				boolean isSetpassword=sharePreferences.getBoolean("isSetPassWord", false);
				if(isSetpassword){
					Intent intent=new Intent(SplashScreenActivity.this,LoginActivity.class);
					startActivity(intent);
				}else{
					Intent intent = new Intent(SplashScreenActivity.this,
							HomepagerActivity.class);
					startActivity(intent);
				}
				SplashScreenActivity.this.finish();
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash_screen_view);
		new Thread() {
			@Override
			public void run() {
				try {
						initialDataBase();	
				} catch (Exception e) {
				}
				Message msg = new Message();
				msg.what = TIME_UP;
				handler.sendMessage(msg);
			}
		}.start();
	}

	private void initialDataBase() {
		StudentbudgetUntility.isFinished=false;
		db = MyDbHelper.getInstance(this.getApplicationContext());
		db.open();
		/*
		 * 查看数据库是否创建，若创建了就直接退出函数
		 */
		Cursor cursor = db.rawQuery(StudentbudgetUntility.EXPENSES_CATEGORY_TABLE);
		if (cursor.moveToNext()) {
			cursor.close();
//			db.close();
			return;
		}
		// 得到array中的字符串数组，将其布置到数据库表中
		Resources res = this.getResources();
		try {
			String expenses_category[] = res
					.getStringArray(R.array.EXPENSES_CATEGORY);
			for (String str : expenses_category) {
				String sql = "INSERT INTO EXPENSES_CATEGORY (NAME,BUDGET) VALUES(?,?)";
				db.execSQL(sql, new Object[] { str, ZERO });
			}
			String income_category[] = res
					.getStringArray(R.array.INCOME_CATEGORY);
			for (String str : income_category) {
				String sql = "INSERT INTO INCOME_CATEGORY (NAME,BUDGET) VALUES(?,?)";
				db.execSQL(sql, new Object[] { str,ZERO });
			}
			String account[] = res.getStringArray(R.array.ACCOUNT);
			for (String str : account) {
				String sql = "INSERT INTO ACCOUNT (NAME,BUDGET) VALUES(?,?)";
				db.execSQL(sql, new Object[] { str,ZERO });
			}
			String item[] = res.getStringArray(R.array.ITEM);
			for (String str : item) {
				String sql = "INSERT INTO ITEM (NAME,BUDGET) VALUES(?,?)";
				db.execSQL(sql, new Object[] { str,ZERO });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
