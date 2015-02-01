package com.coderui.studentbudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CalculatorActivity extends Activity implements
		android.view.View.OnClickListener {
	private Button[] btn = new Button[20];
	private Button calculator_return_btn;
	private EditText edittext;
	private String number = "";
	private double temp1, temp2;// 计算的第一个数和第二个数
	private int index;// + - * /的索引
	private double result = 0;// 运算的结果

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.calculator_view);
		getInstance();
	}

	private void getInstance() {
		btn[0] = (Button) findViewById(R.id.zero);
		btn[1] = (Button) findViewById(R.id.one);
		btn[2] = (Button) findViewById(R.id.two);
		btn[3] = (Button) findViewById(R.id.three);
		btn[4] = (Button) findViewById(R.id.four);
		btn[5] = (Button) findViewById(R.id.five);
		btn[6] = (Button) findViewById(R.id.six);
		btn[7] = (Button) findViewById(R.id.seven);
		btn[8] = (Button) findViewById(R.id.eight);
		btn[9] = (Button) findViewById(R.id.nine);
		btn[10] = (Button) findViewById(R.id.clean);
		btn[11] = (Button) findViewById(R.id.dot);
		btn[12] = (Button) findViewById(R.id.equal);
		btn[13] = (Button) findViewById(R.id.add);
		btn[14] = (Button) findViewById(R.id.subtraction);
		btn[15] = (Button) findViewById(R.id.multiply);
		btn[16] = (Button) findViewById(R.id.divide);
		btn[17] = (Button) findViewById(R.id.income);
		btn[18] = (Button) findViewById(R.id.expense);
		btn[19] = (Button) findViewById(R.id.transfer);
		calculator_return_btn=(Button) findViewById(R.id.calculator_return_btn);
		calculator_return_btn.setOnClickListener(this);
		edittext = (EditText) findViewById(R.id.screen);
		edittext.setInputType(InputType.TYPE_NULL);
		edittext.setText(number);
		for (int i = 0; i < 20; i++) {
			btn[i].setOnClickListener(this);
		}
	}
	
	private boolean idDouble(String str){
		try 
	      { 
	            Double.parseDouble(str); 
	            return   true; 
	      } 
	      catch(NumberFormatException   ex){} 
	      return   false; 
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.clean:
			if ("".equals(number)) {
				edittext.setText(number);
				edittext.setSelection(number.length());
			} else {
				number = number.substring(0, number.length() - 1);
				edittext.setText(number);
				edittext.setSelection(number.length());
				if (("".equals(number)) || ("-".equals(number))) {
					edittext.setText(number);
				} else {
					temp2 = Double.parseDouble(number);
				}
			}
			break;
		case R.id.add:
			if (!("".equals(number))) {
				edittext.setText(number);
				index = 0;
				temp1 = Double.parseDouble(number);
				number = "";
				temp2 = 0;
			}
			break;
		case R.id.subtraction:
			if (!("".equals(number))) {
				edittext.setText(number);
				index = 1;
				temp1 = Double.parseDouble(number);
				number = "";
				temp2 = 0;
			}
			break;
		case R.id.multiply:
			if (!("".equals(number))) {
				edittext.setText(number);
				index = 2;
				temp1 = Double.parseDouble(number);
				number = "";
				temp2 = 0;
				break;
			}
		case R.id.divide:
			if (!("".equals(number))) {
				edittext.setText(number);
				index = 3;
				temp1 = Double.parseDouble(number);
				number = "";
				temp2 = 0;
			}
			break;
		case R.id.dot:
			if(!(number.contains("."))){
				number += "0.";
				edittext.setText(number);
				break;
			}
		case R.id.income:
			String income = edittext.getText().toString();
			if(idDouble(income)){
				Bundle bundle=new Bundle();
				bundle.putInt("category", 1);//1表示收入,2表示支出,3表示转账
				bundle.putDouble("amount", Double.parseDouble(income));
				Intent intent=new Intent(CalculatorActivity.this,HomepagerActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(this, "不能记入，格式不正确", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.expense:
			String expense = edittext.getText().toString();
			if(idDouble(expense)){
				Bundle bundle=new Bundle();
				bundle.putInt("category", 2);
				bundle.putDouble("amount", Double.parseDouble(expense));
				Intent intent=new Intent(CalculatorActivity.this,HomepagerActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(this, "不能记入，格式不正确", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.transfer:
			String transfer = edittext.getText().toString();
			if(idDouble(transfer)){
				Bundle bundle=new Bundle();
				bundle.putInt("category", 3);
				bundle.putDouble("amount", Double.parseDouble(transfer));
				Intent intent=new Intent(CalculatorActivity.this,HomepagerActivity.class);
				intent.putExtras(bundle);
				startActivity(intent);
				finish();
			}else{
				Toast.makeText(this, "不能记入，格式不正确", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.equal:
			switch (index) {
			case 0:
				result = temp1 + temp2;
				edittext.setText(String.valueOf(result));
				number = String.valueOf(result);
				result = 0;
				break;
			case 1:
				result = temp1 - temp2;
				edittext.setText(String.valueOf(result));
				number = String.valueOf(result);
				result = 0;
				break;
			case 2:
				result = temp1 * temp2;
				edittext.setText(String.valueOf(result));
				number = String.valueOf(result);
				result = 0;
				break;
			case 3:
				if (temp2 == 0) {
					edittext.setText("∞");
					number = "";
					result = 0;
				} else {
					result = temp1 / temp2;
					edittext.setText(String.valueOf(result));
					number = String.valueOf(result);
					result = 0;
				}
				break;
			default:
				break;
			}
			break;
		case R.id.calculator_return_btn:
			CalculatorActivity.this.finish();
			break;
		default:
			Button btn = (Button) v;
			String temp = btn.getText().toString();
			number += temp;
			edittext.setText(number);
			temp2 = Double.parseDouble(number);
		}
	}

}
