package com.coderui.studentbudget.fragment;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coderui.studentbudget.R;
import com.coderui.studentbudget.SplashScreenActivity;
import com.coderui.studentbudget.db.Budget;
import com.coderui.studentbudget.db.MyDbHelper;
import com.coderui.studentbudget.untilty.MyExpensesAdapter;
import com.coderui.studentbudget.untilty.StudentbudgetUntility;
import com.coderui.studentbudget.wheel.OnWheelChangedListener;
import com.coderui.studentbudget.wheel.OnWheelScrollListener;
import com.coderui.studentbudget.wheel.WheelView;

public class ExpenseFragment extends Fragment {
	private View view;
	private Calendar calendar;
	private MyDbHelper dbHelper;
	private Budget budget;
	private EditText expense_amount,expense_remark;
	private TextView date,expense_category_tv,account_category_tv;
	private LinearLayout expenses_category_choose,account_category_choose;
	private AlertDialog expense_category_choose_dialog,account_category_choose_dialog;
	private ImageView expense_category_imv,account_category_imv;
	private ImageButton btn_save,btn_cancel;
	private boolean scrolling = false;
	private Integer expenses_id=0,account_id=0;
	private String str_date,str_remark;
	private Double amount;
	private String[] expense_explanation,account_explanation;
	private Integer[] expense_images,account_images;
	private String TAG="ExpenseFragment";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.expense_fragment, container, false);
		getInstance(view);
		date.setText(StudentbudgetUntility.getCurrentDate());
		date.setOnClickListener(new myOnClickListener());
		expenses_category_choose.setOnClickListener(new myOnClickListener());
		account_category_choose.setOnClickListener(new myOnClickListener());
		btn_save.setOnClickListener(new myOnClickListener());
		btn_cancel.setOnClickListener(new myOnClickListener());
		return view;
	}
	//映射出布局文件中的控件
	private void getInstance(View view) {
		budget = new Budget();
		dbHelper = SplashScreenActivity.db;
		expense_explanation = getActivity().getResources().getStringArray(
				R.array.EXPENSES_CATEGORY);
		account_explanation=getActivity().getResources().getStringArray(
				R.array.ACCOUNT);
		expense_images = new Integer[]{ R.drawable.repast, R.drawable.xiuxianyule,
				R.drawable.book, R.drawable.shsj, R.drawable.juhuijiaoyou,
				R.drawable.liwu, R.drawable.huafeiwangfei, R.drawable.transport,
				R.drawable.cloth, R.drawable.touzikuisun, R.drawable.yiyao,
				R.drawable.other };
		account_images=new Integer[]{R.drawable.xianjing,R.drawable.yinhangka,R.drawable.otherpay};
		date = (TextView) view.findViewById(R.id.date);
		expenses_category_choose = (LinearLayout) view
				.findViewById(R.id.expense_category);
		account_category_choose=(LinearLayout) view.findViewById(R.id.account_cgtegory);
		expense_category_tv=(TextView) view.findViewById(R.id.expense_category_tv);
		account_category_tv=(TextView) view.findViewById(R.id.account_category_tv);
		expense_category_imv=(ImageView) view.findViewById(R.id.expense_category_imv);
		account_category_imv=(ImageView) view.findViewById(R.id.account_category_imv);
		expense_amount=(EditText)view.findViewById(R.id.expense_amount);
		btn_save=(ImageButton)view.findViewById(R.id.btn_save);
		btn_cancel=(ImageButton)view.findViewById(R.id.btn_cancel);
		expense_remark=(EditText)view.findViewById(R.id.expense_remark);
		Intent intent=getActivity().getIntent();
		if(intent.hasExtra("amount")){
			Bundle bundle=intent.getExtras();
			int category=bundle.getInt("category");
			double amount=bundle.getDouble("amount");
			Log.v(TAG, "category: "+category+"   amount: "+amount);
			if(category==2){
				expense_amount.setText(String.valueOf(amount));
			}
		}
	}

	class myOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.expense_category:
				initExpenseCategoryChooseDialog();
				expense_category_choose_dialog.show();
				break;
			case R.id.account_cgtegory:
				initAccountCategoryChooseDialog();
				account_category_choose_dialog.show();
				break;
			case R.id.btn_save:
				save();
				break;
			case R.id.btn_cancel:
				cancel();
				break;
			case R.id.date:
				setDatePicker();
				break;
			}
		}
	}
	
	private void setDatePicker(){
		calendar = Calendar.getInstance();
		DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DATE, dayOfMonth);
				date.setText(year+"/"+format(monthOfYear+1)+"/"+format(dayOfMonth));
			}
		};
		new DatePickerDialog(getActivity(), dateSet,
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DATE)).show();
	}
	
	
	private void save(){
		if("".equals(expense_amount.getText().toString())){
			Toast.makeText(getActivity(), "花了多少啊？", Toast.LENGTH_SHORT).show();
		}else{
			amount=Double.parseDouble(expense_amount.getText().toString());
			Log.d(TAG, "amount :"+amount);
			str_date=date.getText().toString();
			Log.d(TAG, "date :"+str_date);
			str_remark=expense_remark.getText().toString();
			Log.d(TAG, "str_remark :"+str_remark);
			budget.setAmount(amount);
			budget.setAccount_id(account_id+1);
			budget.setCategory_id(expenses_id+1);
			budget.setDate(str_date);
			budget.setRemark(str_remark);
			dbHelper.insert(StudentbudgetUntility.EXPENSESTABLE, budget);
			//将支出记录到account中
			StudentbudgetUntility.replaceAmount(
					StudentbudgetUntility.ACCOUNT_TABLE,
					StudentbudgetUntility.EXPENSES, dbHelper, budget,budget.getAccount_id().toString());
			Toast.makeText(getActivity(), "已保存", Toast.LENGTH_SHORT).show();
			cancel();
		}
		
	}
	private void cancel(){
		expense_amount.setText("");
		expense_remark.setText("");
		expense_category_imv.setImageResource(expense_images[0]);
		expense_category_tv.setText(expense_explanation[0]);
		account_category_imv.setImageResource(account_images[0]);
		account_category_tv.setText(account_explanation[0]);
		date.setText(StudentbudgetUntility.getCurrentDate());
	}
	
	//初始化支出种类选择对话框
	private void initExpenseCategoryChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择支出种类");
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.expense_category_wheel, null);
		final WheelView expenses_category_wheel = (WheelView) view
				.findViewById(R.id.expense_category_wheel);
		expenses_category_wheel.setVisibility(3);
		expenses_category_wheel.setViewAdapter(new MyExpensesAdapter(getActivity(),
				expense_explanation, expense_images));
		
		expenses_category_wheel.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				expenses_id=newValue;
			}
		});
		expenses_category_wheel.addScrollingListener( new OnWheelScrollListener() {
            @Override
			public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }
            @Override
			public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                expenses_id=expenses_category_wheel.getCurrentItem();
            }
        });
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				expense_category_imv.setImageResource(expense_images[expenses_id]);
				expense_category_tv.setText(expense_explanation[expenses_id]);
				Log.d(TAG, "expenses_id:"+expenses_id);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		expense_category_choose_dialog = builder.create();
	}
	
	//支付账户选择对话框初始化
	private void initAccountCategoryChooseDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择支付种类");
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.account_category_wheel, null);
		final WheelView account_category_wheel = (WheelView) view
				.findViewById(R.id.account_category_wheel);
		account_category_wheel.setVisibility(3);
		account_category_wheel.setViewAdapter(new MyExpensesAdapter(getActivity(),
				account_explanation, account_images));
		account_category_wheel.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				account_id=newValue;
			}
		});
		account_category_wheel.addScrollingListener( new OnWheelScrollListener() {
            @Override
			public void onScrollingStarted(WheelView wheel) {
                scrolling = true;
            }
            @Override
			public void onScrollingFinished(WheelView wheel) {
                scrolling = false;
                account_id=account_category_wheel.getCurrentItem();
            }
        });
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				account_category_imv.setImageResource(account_images[account_id]);
				account_category_tv.setText(account_explanation[account_id]);
				Log.d(TAG, "account_id:"+account_id);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		account_category_choose_dialog=builder.create();
	}
	private String format(int x)  
    {  
      String s=""+x;  
      if(s.length()==1) s="0"+s;  
      return s;  
    }  
	
}
