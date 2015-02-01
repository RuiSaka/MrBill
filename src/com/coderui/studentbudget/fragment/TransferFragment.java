package com.coderui.studentbudget.fragment;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

public class TransferFragment extends Fragment {
	private View view;
	private Calendar calendar;
	private MyDbHelper dbHelper;
	private Budget budget;
	private EditText transfer_amount,transfer_remark;
	private LinearLayout out_category,in_category;
	private TextView date,out_category_tv,in_category_tv;
	private ImageView out_category_imv,in_category_imv;
	private ImageButton btn_save,btn_cancel;
	private boolean scrolling = false;
	private AlertDialog account_out_choose_dialog,account_in_choose_dialog;
	private Integer account_out_id=1,account_in_id=0;
	private String str_date,str_remark;
	private Double amount;
	private String[] account_explanation;
	private Integer[] account_images;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.transfer_fragment, container, false);
		getInstance(view);
		date.setText(StudentbudgetUntility.getCurrentDate());
		date.setOnClickListener(new myOnClickListener());
		out_category.setOnClickListener(new TransferOnClickListener());
		in_category.setOnClickListener(new TransferOnClickListener());
		btn_save.setOnClickListener(new TransferOnClickListener());
		btn_cancel.setOnClickListener(new TransferOnClickListener());
		return view;
	}
	
	private void getInstance(View view){
		budget = new Budget();
		dbHelper = SplashScreenActivity.db;
		date = (TextView) view.findViewById(R.id.date);
		account_explanation=getActivity().getResources().getStringArray(
				R.array.ACCOUNT);
		account_images=new Integer[]{R.drawable.xianjing,R.drawable.yinhangka,R.drawable.otherpay};
		transfer_amount=(EditText) view.findViewById(R.id.transfer_amount);
		transfer_remark=(EditText) view.findViewById(R.id.transfer_remark);
		out_category=(LinearLayout) view.findViewById(R.id.out_category);
		in_category=(LinearLayout) view.findViewById(R.id.in_category);
		out_category_tv=(TextView) view.findViewById(R.id.out_category_tv);
		in_category_tv=(TextView) view.findViewById(R.id.in_category_tv);
		out_category_imv=(ImageView) view.findViewById(R.id.out_category_imv);
		in_category_imv=(ImageView) view.findViewById(R.id.in_category_imv);
		btn_save=(ImageButton) view.findViewById(R.id.btn_save);
		btn_cancel=(ImageButton) view.findViewById(R.id.btn_cancel);
		Intent intent=getActivity().getIntent();
		if(intent.hasExtra("amount")){
			Bundle bundle=intent.getExtras();
			int category=bundle.getInt("category");
			double amount=bundle.getDouble("amount");
			if(category==3){
				transfer_amount.setText(String.valueOf(amount));
			}
		}
	}
	
	class myOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
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
	
	class TransferOnClickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.out_category:
				initAccounrOutChooseDialog();
				account_out_choose_dialog.show();
				break;
			case R.id.in_category:
				initAccountInChooseDialog();
				account_in_choose_dialog.show();
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
		if("".equals(transfer_amount.getText().toString())){
			Toast.makeText(getActivity(), "您还未输入转账金额!", Toast.LENGTH_SHORT).show();
		}else{
			amount=Double.parseDouble(transfer_amount.getText().toString());
			str_date=date.getText().toString();
			str_remark=transfer_remark.getText().toString();
			budget.setAmount(amount);
			budget.setAccount_id_out(account_out_id+1);
			budget.setAccount_id_in(account_in_id+1);
			budget.setDate(str_date);
			budget.setRemark(str_remark);
			dbHelper.insert(StudentbudgetUntility.TRANSFERTABLE, budget);
			budget.setAccount_id(budget.getAccount_id_in());
			StudentbudgetUntility.replaceAmount(
					StudentbudgetUntility.ACCOUNT_TABLE,
					StudentbudgetUntility.INCOME, dbHelper, budget,budget.getAccount_id().toString());
			budget.setAccount_id(budget.getAccount_id_out());
			StudentbudgetUntility.replaceAmount(
					StudentbudgetUntility.ACCOUNT_TABLE,
					StudentbudgetUntility.EXPENSES, dbHelper, budget,budget.getAccount_id().toString());
			Toast.makeText(getActivity(), "已保存", Toast.LENGTH_SHORT).show();
			cancel();
		}
	}
	private void cancel(){
		transfer_amount.setText("");
		transfer_remark.setText("");
		out_category_imv.setImageResource(account_images[1]);
		out_category_tv.setText(account_explanation[1]);
		in_category_imv.setImageResource(account_images[0]);
		in_category_tv.setText(account_explanation[0]);
		date.setText(StudentbudgetUntility.getCurrentDate());
	}
	
	private void initAccounrOutChooseDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择转出账户");
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
				account_out_id=newValue;
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
                account_out_id=account_category_wheel.getCurrentItem();
            }
        });
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				out_category_imv.setImageResource(account_images[account_out_id]);
				out_category_tv.setText(account_explanation[account_out_id]);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		account_out_choose_dialog=builder.create();
	}
	
	private void initAccountInChooseDialog(){
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("选择转出账户");
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
				account_in_id=newValue;
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
                account_in_id=account_category_wheel.getCurrentItem();
            }
        });
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				in_category_imv.setImageResource(account_images[account_in_id]);
				in_category_tv.setText(account_explanation[account_in_id]);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		});
		account_in_choose_dialog=builder.create();
	}
	private String format(int x)  
    {  
      String s=""+x;  
      if(s.length()==1) s="0"+s;  
      return s;  
    }  
}
