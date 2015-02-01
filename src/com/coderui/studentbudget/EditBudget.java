package com.coderui.studentbudget;

import java.sql.SQLException;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coderui.studentbudget.db.MyDbHelper;
import com.coderui.studentbudget.untilty.MyExpensesAdapter;
import com.coderui.studentbudget.wheel.OnWheelChangedListener;
import com.coderui.studentbudget.wheel.OnWheelScrollListener;
import com.coderui.studentbudget.wheel.WheelView;

public class EditBudget extends Activity implements OnClickListener {
	private TextView date, edit_expense_category_tv, edit_account_category_tv;
	private EditText edit_expense_amount, edit_expense_remark;
	private ImageView edit_expense_category_imv, edit_account_category_imv,edit_budget_list,edit_budget_budget,edit_budget_stastics;
	private int category_id, old_account_id;
	private String bundle_date;
	private Double amount, newAmount;
	private boolean scrolling = false, isEdit = false;
	private LinearLayout edit_expense_category, edit_account_cgtegory;
	private ImageButton edit_btn_save, edit_btn_cancel, back_imb, calaulator;
	private String[] expense_explanation, account_explanation;
	private Integer[] expense_images, account_images;
	private MyDbHelper mDb;
	private Cursor cursor;
	private Calendar calendar;
	private Integer expenses_id, account_id;
	private AlertDialog expense_category_choose_dialog,
			account_category_choose_dialog;
	private String old_remark, str_date, str_remark;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_budget_view);
		getInstance();
	}

	private void getInstance() {
		mDb = SplashScreenActivity.db;
		/* 得到intent中的数据 */
		Intent intent = getIntent();
		Bundle bundle = intent.getExtras();
		bundle_date = bundle.getString("date");
		category_id = bundle.getInt("category");
		expenses_id = category_id - 1;// 将原来的支出种类复制给现在的
		amount = bundle.getDouble("amount");
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

		date = (TextView) findViewById(R.id.edit_date);
		date.setText(String.valueOf(bundle_date));
		date.setOnClickListener(this);
		edit_expense_amount = (EditText) findViewById(R.id.edit_expense_amount);
		edit_expense_amount.setText(String.valueOf(amount));
		edit_expense_category_imv = (ImageView) findViewById(R.id.edit_expense_category_imv);
		edit_expense_category_imv
				.setImageResource(expense_images[category_id - 1]);
		edit_expense_category_tv = (TextView) findViewById(R.id.edit_expense_category_tv);
		edit_expense_category_tv.setText(expense_explanation[category_id - 1]);
		edit_account_category_imv = (ImageView) findViewById(R.id.edit_account_category_imv);
		edit_account_category_tv = (TextView) findViewById(R.id.edit_account_category_tv);
		edit_expense_remark = (EditText) findViewById(R.id.edit_expense_remark);

		cursor = mDb
				.query("SELECT * FROM EXPENSES WHERE AMOUNT=? and EXPENSES_CATEGORY_ID=? and DATE=?",
						new String[] { String.valueOf(amount),
								String.valueOf(category_id), bundle_date });
		if (cursor.moveToNext()) {
			old_account_id = cursor.getInt(cursor.getColumnIndex("ACCOUNT_ID"));
			account_id = old_account_id - 1;
			old_remark = cursor.getString(cursor.getColumnIndex("REMARK"));
			edit_account_category_imv
					.setImageResource(account_images[old_account_id - 1]);
			edit_account_category_tv
					.setText(account_explanation[old_account_id - 1]);
			edit_expense_remark.setText(old_remark);
		}
		back_imb = (ImageButton) findViewById(R.id.back_imb);
		back_imb.setOnClickListener(this);
		calaulator = (ImageButton) findViewById(R.id.calaulator);
		calaulator.setOnClickListener(this);
		edit_btn_save = (ImageButton) findViewById(R.id.edit_btn_save);
		edit_btn_save.setOnClickListener(this);
		edit_btn_cancel = (ImageButton) findViewById(R.id.edit_btn_cancel);
		edit_btn_cancel.setOnClickListener(this);
		edit_expense_category = (LinearLayout) findViewById(R.id.edit_expense_category);
		edit_expense_category.setOnClickListener(this);
		edit_account_cgtegory = (LinearLayout) findViewById(R.id.edit_account_cgtegory);
		edit_account_cgtegory.setOnClickListener(this);
		edit_budget_list=(ImageView) findViewById(R.id.edit_budget_list);
		edit_budget_list.setOnClickListener(this);
		edit_budget_budget=(ImageView) findViewById(R.id.edit_budget_budget);
		edit_budget_budget.setOnClickListener(this);
		edit_budget_stastics=(ImageView)findViewById(R.id.edit_budget_stastics);
		edit_budget_stastics.setOnClickListener(this);
	}

	// 时间编辑器
	private void setDatePicker() {
		calendar = Calendar.getInstance();
		DatePickerDialog.OnDateSetListener dateSet = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DATE, dayOfMonth);
				date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
			}
		};
		new DatePickerDialog(this, dateSet, calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE))
				.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.edit_date:
			setDatePicker();
			break;
		case R.id.edit_expense_category:
			initExpenseCategoryChooseDialog();
			expense_category_choose_dialog.show();
			break;
		case R.id.edit_account_cgtegory:
			initAccountCategoryChooseDialog();
			account_category_choose_dialog.show();
			break;
		case R.id.edit_btn_save:
			save();
			break;
		case R.id.edit_btn_cancel:
			cancel();
			break;
		case R.id.calaulator:
			Intent intent = new Intent(EditBudget.this,
					CalculatorActivity.class);
			EditBudget.this.startActivity(intent);
			break;
		case R.id.back_imb:
			isEdit();
			if (isEdit) {
				new AlertDialog.Builder(EditBudget.this)
						.setTitle("确认返回")
						.setMessage("你编辑的内容还未保存，是否继续离开?")
						.setPositiveButton("返回",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										finish();
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).create().show();

			} else {
				finish();
			}
			break;
		case R.id.edit_budget_list:
			Intent list_intent=new Intent(EditBudget.this,HomepagerActivity.class);
			list_intent.putExtra("tabcategory", 0);
			startActivity(list_intent);
			break;
		case R.id.edit_budget_budget:
			Intent budget_intent=new Intent(EditBudget.this,HomepagerActivity.class);
			budget_intent.putExtra("tabcategory", 1);
			startActivity(budget_intent);
			break;
		case R.id.edit_budget_stastics:
			Intent list_stastics=new Intent(EditBudget.this,HomepagerActivity.class);
			list_stastics.putExtra("tabcategory", 2);
			startActivity(list_stastics);
			break;
		}
	}

	private void isEdit() {
		if (!(amount == Double.parseDouble(edit_expense_amount.getText()
				.toString())
				&& bundle_date.equals(date.getText().toString())
				&& old_remark.equals(edit_expense_remark.getText().toString())
				&& old_account_id == (account_id + 1) && category_id == (expenses_id + 1))) {
			isEdit = true;
		}
	}

	private void save() {
		if ("".equals(edit_expense_amount.getText().toString())) {
			Toast.makeText(this, "消费金额未编辑", Toast.LENGTH_SHORT).show();
		} else {
			newAmount = Double.parseDouble(edit_expense_amount.getText()
					.toString());
			str_date = date.getText().toString();
			str_remark = edit_expense_remark.getText().toString();
			// 更新数据库
			try {
				mDb.execSQL(
						"UPDATE EXPENSES SET DATE=?,AMOUNT=?,EXPENSES_CATEGORY_ID=?,ACCOUNT_ID=?,REMARK=? WHERE AMOUNT=? and EXPENSES_CATEGORY_ID=? and DATE=?",
						new Object[] { str_date, newAmount, expenses_id + 1,
								account_id + 1, str_remark, amount,
								category_id, bundle_date });
			} catch (SQLException e) {
				e.printStackTrace();
			}
			Toast.makeText(this, "已修改", Toast.LENGTH_SHORT).show();
		}
		finish();
	}

	private void cancel() {
		date.setText(String.valueOf(bundle_date));
		edit_expense_amount.setText(String.valueOf(amount));
		edit_expense_category_imv
				.setImageResource(expense_images[category_id - 1]);
		edit_expense_category_tv.setText(expense_explanation[category_id - 1]);
		edit_account_category_imv
				.setImageResource(account_images[old_account_id - 1]);
		edit_account_category_tv
				.setText(account_explanation[old_account_id - 1]);
		edit_expense_remark.setText(old_remark);
		isEdit = true;
	}

	// 初始化支出种类选择对话框
	private void initExpenseCategoryChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择支出种类");
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.expense_category_wheel, null);
		final WheelView expenses_category_wheel = (WheelView) view
				.findViewById(R.id.expense_category_wheel);
		expenses_category_wheel.setVisibility(3);
		expenses_category_wheel.setViewAdapter(new MyExpensesAdapter(this,
				expense_explanation, expense_images));

		expenses_category_wheel
				.addChangingListener(new OnWheelChangedListener() {

					@Override
					public void onChanged(WheelView wheel, int oldValue,
							int newValue) {
						expenses_id = newValue;
					}
				});
		expenses_category_wheel
				.addScrollingListener(new OnWheelScrollListener() {
					@Override
					public void onScrollingStarted(WheelView wheel) {
						scrolling = true;
					}

					@Override
					public void onScrollingFinished(WheelView wheel) {
						scrolling = false;
						expenses_id = expenses_category_wheel.getCurrentItem();
					}
				});
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				edit_expense_category_imv
						.setImageResource(expense_images[expenses_id]);
				edit_expense_category_tv
						.setText(expense_explanation[expenses_id]);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		expense_category_choose_dialog = builder.create();
	}

	// 支付账户选择对话框初始化
	private void initAccountCategoryChooseDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("选择支付种类");
		LayoutInflater inflater = LayoutInflater.from(this);
		View view = inflater.inflate(R.layout.account_category_wheel, null);
		final WheelView account_category_wheel = (WheelView) view
				.findViewById(R.id.account_category_wheel);
		account_category_wheel.setVisibility(3);
		account_category_wheel.setViewAdapter(new MyExpensesAdapter(this,
				account_explanation, account_images));
		account_category_wheel
				.addChangingListener(new OnWheelChangedListener() {

					@Override
					public void onChanged(WheelView wheel, int oldValue,
							int newValue) {
						account_id = newValue;
					}
				});
		account_category_wheel
				.addScrollingListener(new OnWheelScrollListener() {
					@Override
					public void onScrollingStarted(WheelView wheel) {
						scrolling = true;
					}

					@Override
					public void onScrollingFinished(WheelView wheel) {
						scrolling = false;
						account_id = account_category_wheel.getCurrentItem();
					}
				});
		builder.setView(view);
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				edit_account_category_imv
						.setImageResource(account_images[account_id]);
				edit_account_category_tv
						.setText(account_explanation[account_id]);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

			}
		});
		account_category_choose_dialog = builder.create();
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
