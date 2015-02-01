package com.coderui.studentbudget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;

import com.coderui.studentbudget.db.MyDbHelper;
import com.coderui.studentbudget.untilty.StudentbudgetUntility;

public class AllAccountActivity extends Activity {
	private MyDbHelper mDb;
	private Button back;
	private ExpandableListView accounts_list;
	private List<Map<String, Object>> parentList;
	private List<List<Map<String, Object>>> childList;
	private String[] expense_explanation, account_explanation,
			income_explanation;
	private Integer[] expense_images, account_images, income_images;
	private Cursor cursor;
	private int cur_month, cur_year;
	private AccountExpandableListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.all_accounts);
		getInstance();
		getAccountData();
		adapter=new AccountExpandableListAdapter(this, parentList, childList);
		accounts_list.setAdapter(adapter);
	}

	private void getInstance() {
		mDb = SplashScreenActivity.db;
		cur_year = StudentbudgetUntility.getYear();
		cur_month = StudentbudgetUntility.getMonth();
		expense_explanation = this.getResources().getStringArray(
				R.array.EXPENSES_CATEGORY);
		account_explanation = this.getResources().getStringArray(
				R.array.ACCOUNT);
		expense_images = new Integer[] { R.drawable.repast,
				R.drawable.xiuxianyule, R.drawable.book, R.drawable.shsj,
				R.drawable.juhuijiaoyou, R.drawable.liwu,
				R.drawable.huafeiwangfei, R.drawable.transport,
				R.drawable.cloth, R.drawable.touzikuisun, R.drawable.yiyao,
				R.drawable.other };
		account_images = new Integer[] { R.drawable.xianjing,
				R.drawable.yinhangka, R.drawable.otherpay };
		income_explanation = this.getResources().getStringArray(
				R.array.INCOME_CATEGORY);
		income_images = new Integer[] { R.drawable.shenghuo_income,
				R.drawable.jianzhi_income, R.drawable.touzi_income,
				R.drawable.other_income };
		parentList = new ArrayList<Map<String, Object>>();
		childList = new ArrayList<List<Map<String, Object>>>();
		back = (Button) findViewById(R.id.account_return_btn);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		accounts_list = (ExpandableListView) findViewById(R.id.accounts_list);
	}

	private void getAccountData() {
		parentList.clear();
		childList.clear();
//		double leftamount=0;
		double[] in_account=new double[3], out_account=new double[3];
		String currentMonth1 = cur_year + "/" + cur_month + "/01";// 每月的月初
		String endOfMonth = cur_year + "/" + cur_month + "/"
				+ StudentbudgetUntility.getdate();
		for(int i=1;i<account_explanation.length+1;i++){
			Map<String, Object> parentMap = new HashMap<String, Object>();
			List<Map<String,Object>> child=new ArrayList<Map<String,Object>>();
			//获得支出表中的本月支出
			cursor = mDb.query("select * from EXPENSES where DATE>=? and DATE<=?",
					new String[] { currentMonth1, endOfMonth });
			while (cursor.moveToNext()) {
				int account_id=cursor.getInt(cursor.getColumnIndex("ACCOUNT_ID"));
				if(account_id==i){
					Map<String,Object> childMap=new HashMap<String,Object>();
					double amount=cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
					int expense_category_id=cursor.getInt(cursor.getColumnIndex("EXPENSES_CATEGORY_ID"));
					String date=cursor.getString(cursor.getColumnIndex("DATE"));
					childMap.put("amount", amount);
					childMap.put("bill_category", 1);//账单类型，1支出、2收入、3转账
					childMap.put("category_imv", expense_images[expense_category_id-1]);
					childMap.put("category_tv", expense_explanation[expense_category_id-1]);
					childMap.put("date", date);
					child.add(childMap);
					out_account[i-1]+=amount;//算出出自同一账户的支出总额
				}
			}
			//获得本月收入表中的收入
			cursor=mDb.query("select * from INCOME where DATE>=? and DATE<=?",
					new String[] { currentMonth1, endOfMonth });
			while (cursor.moveToNext()) {
				int account_id=cursor.getInt(cursor.getColumnIndex("ACCOUNT_ID"));
				if(account_id==i){
					Map<String,Object> childMap=new HashMap<String,Object>();
					double amount=cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
					int expense_category_id=cursor.getInt(cursor.getColumnIndex("ICNOME_CATEGORY_ID"));
					String date=cursor.getString(cursor.getColumnIndex("DATE"));
					childMap.put("amount", amount);
					childMap.put("bill_category", 2);//账单类型，1支出、2收入、3转账、4转出
					childMap.put("category_imv", income_images[expense_category_id-1]);
					childMap.put("category_tv", income_explanation[expense_category_id-1]);
					childMap.put("date", date);
					child.add(childMap);
					in_account[i-1]+=amount;//算出出自同一账户的收入总额
				}
			}
			//获得本月转账表中的
			cursor=mDb.query("select * from TRANSFER where DATE>=? and DATE<=?",
					new String[] { currentMonth1, endOfMonth });
			while (cursor.moveToNext()) {
				int account_id_out=cursor.getInt(cursor.getColumnIndex("ACCOUNT_ID_OUT"));
				int account_id_in=cursor.getInt(cursor.getColumnIndex("ACCOUNT_ID_IN"));
				if(account_id_in==i){
					Map<String,Object> childMap=new HashMap<String,Object>();
					double amount=cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
					String date=cursor.getString(cursor.getColumnIndex("DATE"));
					childMap.put("amount", amount);
					childMap.put("bill_category", 3);//账单类型，1支出、2收入、3转入、4转出
					childMap.put("category_imv", account_images[account_id_out-1]);
					childMap.put("category_tv", account_explanation[account_id_out-1]);
					childMap.put("date", date);
					child.add(childMap);
					in_account[i-1]+=amount;//算出出自同一账户的收入总额
				}
				if(account_id_out==i){
					Map<String,Object> childMap=new HashMap<String,Object>();
					double amount=cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
					String date=cursor.getString(cursor.getColumnIndex("DATE"));
					childMap.put("amount", amount);
					childMap.put("bill_category",4);//账单类型，1支出、2收入、3转账、4转出
					childMap.put("category_imv", account_images[account_id_in-1]);
					childMap.put("category_tv", account_explanation[account_id_in-1]);
					childMap.put("date", date);
					child.add(childMap);
					out_account[i-1]+=amount;//算出出自同一账户的收入总额
				}
			}
			childList.add(child);
//			cursor=mDb.query("select * from ACCOUNT where ID=?",new String[]{String.valueOf(i)});
//			if(cursor.moveToFirst()){
//				leftamount=cursor.getDouble(cursor.getColumnIndex("BUDGET"));
//				Log.v(TAG, "leftamount: "+leftamount);
//			}
			parentMap.put("left_amount",in_account[i-1]-out_account[i-1]);
			parentMap.put("account_imv", account_images[i-1]);
			parentMap.put("account_tv", account_explanation[i-1]);
			parentMap.put("in_account",in_account[i-1]);
			parentMap.put("out_account",out_account[i-1]);
			parentList.add(parentMap);
		}
		cursor.close();
		
	}
}
