package com.coderui.studentbudget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.coderui.studentbudget.db.MyDbHelper;
import com.coderui.studentbudget.untilty.MyListGroupAdapter;
import com.coderui.studentbudget.untilty.StudentbudgetUntility;

public class ListBudgetActivity extends Activity {
	private MyDbHelper mDb;
	private int cur_month, cur_year;
	private Cursor cursor;
	private List<Map<String,Object>> list;
	private ListView listView;
	private MyListGroupAdapter adapter;
	private final int REQUESTCODE=0;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list_view);
		getInstance();
		getCurrentMonthExpenseAmount();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if(StudentbudgetUntility.isFinished){
			finish();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	private void getInstance(){
		mDb = SplashScreenActivity.db;
		cur_month = StudentbudgetUntility.getMonth();// 获得当前的月份
		cur_year = StudentbudgetUntility.getYear();// 获得当前年份
		listView=(ListView) findViewById(R.id.list_of_month);
		//为listView的Item点击设置触发事件
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int arg2,
					long arg3) {
				
				TextView tv_month=(TextView)view.findViewById(R.id.list_month);
				int month=Integer.valueOf(tv_month.getText().toString());
				Bundle bundle=new Bundle();
				bundle.putInt("year",cur_year);
				bundle.putInt("month", month);
				Intent intent=new Intent(ListBudgetActivity.this,ListDayBudgetActivity.class);
				intent.putExtras(bundle);
				ListBudgetActivity.this.startActivityForResult(intent, REQUESTCODE);
			}
		});
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(requestCode==REQUESTCODE){
			getCurrentMonthExpenseAmount();
		}
	}
	
	 //获得当年每月的消费
	private void getCurrentMonthExpenseAmount(){
		list=new ArrayList<Map<String,Object>>();
		java.text.DecimalFormat df =new java.text.DecimalFormat("######0.0");//double保留2位数
		for(int i=cur_month;i>0;i--){
			double amountOfMonth=0;
			String currentMonth1=cur_year+"/"+format(i)+"/01";//每月的月初
			String endOfMonth=cur_year+"/"+format(i)+"/"+format(StudentbudgetUntility.getDays(i, cur_month, cur_year));
			cursor = mDb.query(
					"select sum(AMOUNT) from EXPENSES where DATE>=? and DATE<=?",
					new String[] { currentMonth1,
							endOfMonth });
			if (cursor.moveToNext()) {
				amountOfMonth = cursor.getDouble(0);
			}
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("date", i);
			map.put("all_amount", String.valueOf(amountOfMonth));
			map.put("cur_year", cur_year);
			map.put("average_amount", String.valueOf(df.format(amountOfMonth/StudentbudgetUntility.getDays(i, cur_month, cur_year))));
			list.add(map);
		}
		adapter=new MyListGroupAdapter(this, list);
		listView.setAdapter(adapter);
		cursor.close();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	private String format(int x)  
    {  
      String s=""+x;  
      if(s.length()==1) s="0"+s;  
      return s;  
    }  
}
