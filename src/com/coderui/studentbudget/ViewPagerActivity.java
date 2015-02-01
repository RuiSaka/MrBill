package com.coderui.studentbudget;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.coderui.studentbudget.fragment.ExpenseFragment;
import com.coderui.studentbudget.fragment.IncomeFragment;
import com.coderui.studentbudget.fragment.TransferFragment;
import com.coderui.studentbudget.untilty.MyPagerAdapter;
import com.coderui.studentbudget.untilty.StudentbudgetUntility;

public class ViewPagerActivity extends FragmentActivity {
	private ViewPager myViewPager;
	
	private TextView tv_title;
	private ImageButton allaccount_imb,setting_imb,calculator_imb,budget_expense_imb;

	private MyPagerAdapter myAdapter;

	private List<Fragment> mListFragments;
	private Fragment expenseFragment = null;
	private Fragment incomeFragment = null;
	private Fragment transferFragment = null;
	private int category=0;
	private double amount=0;
	private boolean editable=false;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager_view);
		getIntances();
		
		mListFragments.add(incomeFragment);
		mListFragments.add(expenseFragment);
		mListFragments.add(transferFragment);
		FragmentManager fragmentManager =getSupportFragmentManager();
		myAdapter = new MyPagerAdapter(fragmentManager,mListFragments);
		myViewPager.setAdapter(myAdapter);
		
		fromCalculator();
		// 初始化当前显示的view
		if(!editable){
			myViewPager.setCurrentItem(1);
		}
		Log.d("ViewPagerActivity", "editable"+editable);
		// 初始化第二个view的信息
		myViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int arg0) {
				//final Fragment showView = mListFragments.get(arg0);
				switch (arg0) {
				case 0:
					tv_title.setText(R.string.title_income_fragment);
					break;
				case 1:
					tv_title.setText(R.string.title_expense_fragment);
					break;
				case 2:
					tv_title.setText(R.string.title_transfer_fragment);
					break;
				default:
					break;
				}
			}
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				Log.d("k", "onPageScrolled - " + arg0);
				// 从1到2滑动，在1滑动前调用
			}
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		if (StudentbudgetUntility.isFinished) {
			finish();
		}
	}
	//判断来自计算器的数据
	private void fromCalculator(){
		Intent intent=getIntent();
		if(intent.hasExtra("amount")){
			editable=true;
			Bundle bundle=intent.getExtras();
			category=bundle.getInt("category");
			amount=bundle.getDouble("amount");
			switch(category){
			case 1://收入
				myViewPager.setCurrentItem(0);
				tv_title.setText(R.string.title_income_fragment);
				
				break;
			case 2://支出
				myViewPager.setCurrentItem(1);
				tv_title.setText(R.string.title_expense_fragment);
				break;
			case 3://转账
				myViewPager.setCurrentItem(2);
				tv_title.setText(R.string.title_transfer_fragment);
				break;
			}
		}
	}
	private void getIntances(){
		myViewPager = (ViewPager) findViewById(R.id.viewPaper);
		tv_title=(TextView) findViewById(R.id.title);
		allaccount_imb=(ImageButton)findViewById(R.id.allaccount_imb);
		allaccount_imb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ViewPagerActivity.this,AllAccountActivity.class);
				startActivity(intent);
				
			}
		});
		setting_imb=(ImageButton)findViewById(R.id.setting_imb);
		setting_imb.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ViewPagerActivity.this,SettingActivity.class);
				startActivity(intent);
			}
		});
		calculator_imb=(ImageButton)findViewById(R.id.calculator_imb);
		calculator_imb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ViewPagerActivity.this,CalculatorActivity.class);
				startActivity(intent);
			}
		});
		budget_expense_imb=(ImageButton)findViewById(R.id.budget_expense_imb);
		budget_expense_imb.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent=new Intent(ViewPagerActivity.this,BudgetActivity.class);
				startActivity(intent);
			}
		});
		mListFragments = new ArrayList<Fragment>();
		expenseFragment=new ExpenseFragment();
		incomeFragment=new IncomeFragment();
		transferFragment=new TransferFragment();

	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			 new AlertDialog.Builder(this)
			 .setTitle("确认退出")
			 .setMessage("确定要退出应用?")
			 .setPositiveButton("是", new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					StudentbudgetUntility.isFinished=true;
					finish();
				}
			})
			 .setNegativeButton("否", new OnClickListener(){
				@Override
				public void onClick(DialogInterface dialog, int which) {
				}
			 })
			 .create().show();
		}
		return super.onKeyDown(keyCode, event);
	}
	

}