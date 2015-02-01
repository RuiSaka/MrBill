package com.coderui.studentbudget;

import com.coderui.studentbudget.untilty.StudentbudgetUntility;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
	private EditText in_put_pass;
	private Button login;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.password_login_view);
		in_put_pass=(EditText) findViewById(R.id.in_put_pass);
		login=(Button) findViewById(R.id.log_in);
		SharedPreferences sharedPreferences=getSharedPreferences(StudentbudgetUntility.PASSWORD,Context.MODE_PRIVATE);
		final String password=sharedPreferences.getString("password", "");
		Log.v("LoginActivity", "password: "+password);
		login.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String input=in_put_pass.getText().toString();
				Log.v("LoginActivity", "input: "+input);
				if(password.equals(input)){
					Intent intent = new Intent(LoginActivity.this,
							ViewPagerActivity.class);
					startActivity(intent);
					LoginActivity.this.finish();
				}else{
					new AlertDialog.Builder(LoginActivity.this)
					 .setTitle("警告")
					 .setMessage("您输入的密码不正确，账单先生拒绝您的访问要求")
					 .setPositiveButton("退出", new OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							LoginActivity.this.finish();
						}
					})
					 .setNegativeButton("重试", new OnClickListener(){
						@Override
						public void onClick(DialogInterface dialog, int which) {
							in_put_pass.setText("");
						}
					 })
					 .create().show();
				}
			}
		});
	}
}
