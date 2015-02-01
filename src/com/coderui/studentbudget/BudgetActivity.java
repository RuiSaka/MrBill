package com.coderui.studentbudget;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coderui.studentbudget.db.MyDbHelper;
import com.coderui.studentbudget.untilty.BudgetItemAdapter;
import com.coderui.studentbudget.untilty.StudentbudgetUntility;

public class BudgetActivity extends Activity {
	private Button budget_return_btn;
	private ListView listview;
	private List<Map<String,Object>> list;
	private String[] expense_explanation;
	private Integer[] expense_images;
	private BudgetItemAdapter adapter;
	private MyDbHelper mDb;
	private Cursor cursor;
	private int cur_month, cur_year,cur_day;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.budget);
		mDb = SplashScreenActivity.db;
		expense_explanation = this.getResources().getStringArray(
				R.array.EXPENSES_CATEGORY);
		expense_images = new Integer[] { R.drawable.repast,
				R.drawable.xiuxianyule, R.drawable.book, R.drawable.shsj,
				R.drawable.juhuijiaoyou, R.drawable.liwu,
				R.drawable.huafeiwangfei, R.drawable.transport,
				R.drawable.cloth, R.drawable.touzikuisun, R.drawable.yiyao,
				R.drawable.other };
		list=new ArrayList<Map<String,Object>>();
		cur_year=StudentbudgetUntility.getYear();
		cur_month = StudentbudgetUntility.getMonth();// 获得当前的月份
		cur_day=StudentbudgetUntility.getdate();
		budget_return_btn=(Button) findViewById(R.id.budget_return_btn);
		getData();
		budget_return_btn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				BudgetActivity.this.finish();
			}
		});
		listview=(ListView) findViewById(R.id.list_of_budget);
		adapter=new BudgetItemAdapter(this, list);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, final View view, final int id,
					long arg3) {
				AlertDialog.Builder builder = new AlertDialog.Builder(BudgetActivity.this);        
	            LayoutInflater factory = LayoutInflater.from(BudgetActivity.this);
	            final View window = factory.inflate(R.layout.set_budget_dialog, null);
	            final EditText set_budget_dt=(EditText) window.findViewById(R.id.set_budget_dt);
	            builder.setTitle("设置预算");
	            builder.setView(window);
	            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						double set_budget_amount=0;
						if("".equals(set_budget_dt.getText().toString())){
							set_budget_amount=0;
		            	}else{
		            		set_budget_amount=Double.parseDouble(set_budget_dt.getText().toString());
		            	}
						try {
							mDb.execSQL("UPDATE EXPENSES_CATEGORY SET BUDGET=? WHERE ID=?", new Object[]{set_budget_amount,id+1});
						} catch (SQLException e) {
							e.printStackTrace();
							Toast.makeText(BudgetActivity.this, "设置失败！", Toast.LENGTH_SHORT).show();
						}
						BudgetActivity.this.getData();
						Toast.makeText(BudgetActivity.this, "设置成功！", Toast.LENGTH_SHORT).show();
					}
				});
	            builder.setNegativeButton("取消", new DialogInterface.OnClickListener(){
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
	            });
	            builder.create().show();
			}
		});
	}
	//得到数据库中的预算
	private void getData(){
		list.clear();
		cursor=mDb.rawQuery(StudentbudgetUntility.EXPENSES_CATEGORY_TABLE);
		while(cursor.moveToNext()){
			Map<String,Object> map=new HashMap<String,Object>();
			int id=cursor.getInt(cursor.getColumnIndex("ID"));
			map.put("images", expense_images[id-1]);
			map.put("tv", expense_explanation[id-1]);
			double budget_amount=cursor.getDouble(cursor.getColumnIndex("BUDGET"));
			map.put("budget_amount", budget_amount);
			double sumamount=0;
			String currentMonth1 = cur_year + "/" + cur_month + "/01";// 每月的月初
			String endOfMonth = cur_year + "/" + cur_month + "/"
					+ StudentbudgetUntility.getdate();
			Cursor cur = mDb.query("select * from EXPENSES where DATE>=? and DATE<=?",
					new String[] { currentMonth1, endOfMonth });
			while(cur.moveToNext()){
				Integer expense_id = cur.getInt(cur
						.getColumnIndex("EXPENSES_CATEGORY_ID"));
				if(expense_id==id){
					sumamount+=cur.getDouble(cur.getColumnIndex("AMOUNT"));
				}
			}
			cur.close();
			map.put("left_amount", budget_amount-sumamount);
			list.add(map);
		}
		cursor.close();
	}
}
