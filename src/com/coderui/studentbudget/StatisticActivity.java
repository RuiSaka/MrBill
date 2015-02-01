package com.coderui.studentbudget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.coderui.studentbudget.db.MyDbHelper;
import com.coderui.studentbudget.untilty.MyStatisticAdapter;
import com.coderui.studentbudget.untilty.StudentbudgetUntility;

public class StatisticActivity extends Activity{
	private MyDbHelper mDb;
	private int cur_month, cur_year;
	private Cursor cursor;
	private Double amountall = 0.0;
	private ListView list_of_month;
	private String[] expense_explanation;
	private Integer[] expense_images;
	private AlertDialog dlg;
	private GestureDetector gestureDetector;
	private TextView tv_no_data,tv_nodata_detail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.statistics_view);
		getInstance();
		MyStatisticAdapter adapter = new MyStatisticAdapter(this, setList(),
				amountall);
		list_of_month.setAdapter(adapter);
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (StudentbudgetUntility.isFinished) {
			finish();
		}
	}

	private void getInstance() {
		mDb = SplashScreenActivity.db;
		expense_explanation = this.getResources().getStringArray(
				R.array.EXPENSES_CATEGORY);
		expense_images = new Integer[] { R.drawable.repast,
				R.drawable.xiuxianyule, R.drawable.book, R.drawable.shsj,
				R.drawable.juhuijiaoyou, R.drawable.liwu,
				R.drawable.huafeiwangfei, R.drawable.transport,
				R.drawable.cloth, R.drawable.touzikuisun, R.drawable.yiyao,
				R.drawable.other };
		cur_month = StudentbudgetUntility.getMonth();// 获得当前的月份
		showMonthToast(cur_month);//显示toast
		cur_year = StudentbudgetUntility.getYear();// 获得当前年份
		list_of_month = (ListView) findViewById(R.id.list_of_month);
		tv_no_data=(TextView) findViewById(R.id.tv_no_data);
		tv_nodata_detail=(TextView) findViewById(R.id.tv_no_data_detal);
	}

	// 得到本月各类消费的总额
	private Map<Integer, Double> getExpensesInfo() {
		Map<Integer, Double> map = new HashMap<Integer, Double>();
		String currentMonth1 = cur_year + "/" + format(cur_month) + "/01";// 每月的月初
		String endOfMonth = cur_year + "/" + format(cur_month) + "/"
				+ format(StudentbudgetUntility.getdate());
		cursor = mDb.query("select * from EXPENSES where DATE>=? and DATE<=?",
				new String[] { currentMonth1, endOfMonth });
		while (cursor.moveToNext()) {
			double budget = cursor.getDouble(cursor.getColumnIndex("AMOUNT"));
			amountall += budget;
			Integer id = cursor.getInt(cursor
					.getColumnIndex("EXPENSES_CATEGORY_ID"));
			// 判断map里是否已有id，若已有就将budget+=value，最后put.put(id, budget)
			if (map.containsKey(id)) {
				budget += map.get(id);
			}
			map.put(id, budget);
		}
		cursor.close();
		if (amountall == 0.0) {
			amountall = 1.0;
		}
		return map;
	}

	private List<Map<String, Object>> setList() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<Integer, Double> map = getExpensesInfo();
		Set<Integer> set = map.keySet();
		for (Iterator<Integer> iter = set.iterator(); iter.hasNext();) {
			Integer id = iter.next();
			Double amount = map.get(id);
			Map<String, Object> dataMap = new HashMap<String, Object>();
			dataMap.put("id", id);
			dataMap.put("explanation", expense_explanation[id - 1]);
			dataMap.put("image", expense_images[id - 1]);
			dataMap.put("amount", amount);
			list.add(dataMap);
		}
		Collections.sort(list, new MyComparator());
		if(list.isEmpty()){
			tv_no_data.setVisibility(View.VISIBLE);
			tv_nodata_detail.setVisibility(View.VISIBLE);
		}else{
			tv_no_data.setVisibility(View.GONE);
			tv_nodata_detail.setVisibility(View.GONE);
		}
		return list;
	}
	//刚进入界面时显示月份
	private void showMonthToast(int month){
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.statistics_dialog_view,null);
		Toast toast = new Toast(this);
		TextView month_statistics=(TextView) view.findViewById(R.id.month_statistics);
		month_statistics.setText(String.valueOf(month));
        toast.setView(view);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.show();
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		return gestureDetector.onTouchEvent(event);
	}
	private String format(int x)  
    {  
      String s=""+x;  
      if(s.length()==1) s="0"+s;  
      return s;  
    } 
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
}

//按金额所占比例的降序排序
class MyComparator implements Comparator<Map<String, Object>> {

	@Override
	public int compare(Map<String, Object> lhs, Map<String, Object> rhs) {
		Double com=(Double)lhs.get("amount")-(Double)rhs.get("amount");
		if(com>=0.0){
			return -1;
		}else{
			return 1;
		}
	}

}
////手势操作
//class MyGestureListener extends SimpleOnGestureListener{
//	private Context context;
//	public MyGestureListener(Context context){
//		this.context=context;
//	}
//	@Override
//	public void onShowPress(MotionEvent e) {
//		super.onShowPress(e);
//		Log.v("MyGestureListener", "onShowPress");
//		changMonthToast();
//	}
	//切换月份的Toast
//		private void changMonthToast(){
//			Log.v("MyGestureListener", "changMonthToast");
//			Toast previousMonth=Toast.makeText(context, null, Toast.LENGTH_LONG);
//			previousMonth.setGravity(Gravity.LEFT, 0, 0);
//			LinearLayout toastView = (LinearLayout) previousMonth.getView();
//			ImageView imageCodeProject = new ImageView(context);
//			imageCodeProject.setImageResource(R.drawable.previous_month);
//			toastView.addView(imageCodeProject, 0);
//			previousMonth.show();
//			
//			Toast nextMonth=Toast.makeText(context, null, Toast.LENGTH_LONG);
//			nextMonth.setGravity(Gravity.RIGHT, 0, 0);
//			LinearLayout nextView = (LinearLayout) nextMonth.getView();
//			ImageView image_next = new ImageView(context);
//			image_next.setImageResource(R.drawable.next_month);
//			nextView.addView(image_next, 0);
//			nextMonth.show();
//		}
//}
