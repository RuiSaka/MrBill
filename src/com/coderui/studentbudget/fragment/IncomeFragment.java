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

public class IncomeFragment extends Fragment {
	private View view;
	private Calendar calendar;
	private MyDbHelper dbHelper;
	private Budget budget;
	private LinearLayout income_category,account_category;
	private EditText income_amount,income_remark;
	private TextView date,income_category_tv,account_category_tv;
	private ImageView income_category_imv,account_category_imv;
	private ImageButton btn_save,btn_cancel;
	private boolean scrolling = false;
	private Integer income_id=0,account_id=0;
	private String str_date,str_remark;
	private Double amount;
	private AlertDialog income_category_choose_dialog,account_category_choose_dialog;
	private String[] income_explanation,account_explanation;
	private Integer[] income_images,account_images;
	private String TAG="IncomeFragment";
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.income_fragment, container, false);
		getInstance(view);
		date.setText(StudentbudgetUntility.getCurrentDate());
		date.setOnClickListener(new IncomeOnClickListener());
		income_category.setOnClickListener(new IncomeOnClickListener());
		account_category.setOnClickListener(new IncomeOnClickListener());
		btn_save.setOnClickListener(new IncomeOnClickListener());
		btn_cancel.setOnClickListener(new IncomeOnClickListener());
		return view;
	}
	
	
	private void getInstance(View view){
		budget = new Budget();
		dbHelper = SplashScreenActivity.db;
		date = (TextView) view.findViewById(R.id.date);
		income_explanation=getActivity().getResources().getStringArray(R.array.INCOME_CATEGORY);
		account_explanation=getActivity().getResources().getStringArray(
				R.array.ACCOUNT);
		income_images=new Integer[]{R.drawable.shenghuo_income,R.drawable.jianzhi_income,R.drawable.touzi_income,R.drawable.other_income};
		account_images=new Integer[]{R.drawable.xianjing,R.drawable.yinhangka,R.drawable.otherpay};
		income_category_tv=(TextView)view.findViewById(R.id.income_category_tv);
		account_category_tv=(TextView)view.findViewById(R.id.account_category_tv);
		income_category_imv=(ImageView)view.findViewById(R.id.income_category_imv);
		account_category_imv=(ImageView)view.findViewById(R.id.account_category_imv);
		btn_save=(ImageButton)view.findViewById(R.id.btn_save);
		btn_cancel=(ImageButton)view.findViewById(R.id.btn_cancel);
		income_amount=(EditText)view.findViewById(R.id.income_amount);
		income_remark=(EditText)view.findViewById(R.id.income_remark);
		income_category=(LinearLayout)view.findViewById(R.id.income_category);
		account_category=(LinearLayout)view.findViewById(R.id.account_cgtegory);
		Intent intent=getActivity().getIntent();
		if(intent.hasExtra("amount")){
			Bundle bundle=intent.getExtras();
			int category=bundle.getInt("category");
			double amount=bundle.getDouble("amount");
			Log.v(TAG, "category: "+category+"   amount: "+amount);
			if(category==1){
				income_amount.setText(String.valueOf(amount));
			}
		}
	}
	
	class IncomeOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.income_category:
				initIncomeCategoryChooseDialog();
				income_category_choose_dialog.show();
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
	
	private void save(){
		if("".equals(income_amount.getText().toString())){
			Toast.makeText(getActivity(), "输入收入金额", Toast.LENGTH_SHORT).show();
		}else{
			amount=Double.parseDouble(income_amount.getText().toString());
			Log.d(TAG, "amount :"+amount);
			str_date=date.getText().toString();
			Log.d(TAG, "date :"+str_date);
			str_remark=income_remark.getText().toString();
			Log.d(TAG, "str_remark :"+str_remark);
			budget.setAmount(amount);
			budget.setAccount_id(account_id+1);
			budget.setCategory_id(income_id+1);
			budget.setDate(str_date);
			budget.setRemark(str_remark);
			dbHelper.insert(StudentbudgetUntility.INCOMETABLE, budget);
			StudentbudgetUntility.replaceAmount(
					StudentbudgetUntility.ACCOUNT_TABLE,
					StudentbudgetUntility.INCOME, dbHelper, budget,budget.getAccount_id().toString());
			Toast.makeText(getActivity(), "已保存", Toast.LENGTH_SHORT).show();
			cancel();
		}
	}
	private void cancel(){
		income_amount.setText("");
		income_remark.setText("");
		income_category_imv.setImageResource(income_images[0]);
		income_category_tv.setText(income_explanation[0]);
		account_category_imv.setImageResource(account_images[0]);
		account_category_tv.setText(account_explanation[0]);
		date.setText(StudentbudgetUntility.getCurrentDate());
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
	
	
	private void initIncomeCategoryChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择收入来源");
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		View view = inflater.inflate(R.layout.expense_category_wheel, null);
		final WheelView expenses_category_wheel = (WheelView) view
				.findViewById(R.id.expense_category_wheel);
		expenses_category_wheel.setVisibility(3);
		expenses_category_wheel.setViewAdapter(new MyExpensesAdapter(getActivity(),
				income_explanation, income_images));
		
		expenses_category_wheel.addChangingListener(new OnWheelChangedListener() {
			
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				income_id=newValue;
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
                income_id=expenses_category_wheel.getCurrentItem();
            }
        });
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				income_category_imv.setImageResource(income_images[income_id]);
				income_category_tv.setText(income_explanation[income_id]);
				Log.d(TAG, "expenses_id:"+income_id);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		income_category_choose_dialog = builder.create();
	}
	
	//支付账户选择对话框初始化
	private void initAccountCategoryChooseDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择存入账户");
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
