package com.coderui.studentbudget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coderui.studentbudget.untilty.StudentbudgetUntility;

public class SetPassWordActivity extends Activity implements OnClickListener{
	private Button return_btn;
	private TextView tv_setpassword;
	private RelativeLayout choose_set_password,set_password_dialog;
	private  CheckBox choose_set_box;
	private boolean isSetPassWord=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.set_password_view);
		return_btn=(Button) findViewById(R.id.return_btn);
		return_btn.setOnClickListener(this);
		tv_setpassword=(TextView) findViewById(R.id.tv_setpassword);
		choose_set_password=(RelativeLayout) findViewById(R.id.choose_set_password);
		choose_set_password.setOnClickListener(this);
		set_password_dialog=(RelativeLayout) findViewById(R.id.set_password_dialog);
		set_password_dialog.setOnClickListener(this);
		choose_set_box=(CheckBox) findViewById(R.id.choose_set_box);
		SharedPreferences preferences=getSharedPreferences(StudentbudgetUntility.PASSWORD,Context.MODE_PRIVATE);
		boolean isset=preferences.getBoolean("isSetPassWord", false);
		choose_set_box.setChecked(isset);
		choose_set_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(choose_set_box.isChecked()){
					tv_setpassword.setTextColor(SetPassWordActivity.this.getResources().getColor(R.color.black));
					set_password_dialog.setClickable(true);
					isSetPassWord=true;
        			SharedPreferences sharePreferences=getSharedPreferences(StudentbudgetUntility.PASSWORD,Context.MODE_PRIVATE);
        			Editor edit=sharePreferences.edit();
        			edit.putBoolean("isSetPassWord", isSetPassWord);
        			edit.commit();
				}else{
					tv_setpassword.setTextColor(SetPassWordActivity.this.getResources().getColor(R.color.gray));
					set_password_dialog.setClickable(false);
					isSetPassWord=false;
					SharedPreferences sharePreferences=getSharedPreferences(StudentbudgetUntility.PASSWORD,Context.MODE_PRIVATE);
        			Editor edit=sharePreferences.edit();
        			edit.putBoolean("isSetPassWord", isSetPassWord);
        			edit.commit();
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.return_btn:
			finish();
			break;
		case R.id.set_password_dialog:
			 AlertDialog.Builder builder = new AlertDialog.Builder(SetPassWordActivity.this);        
	            LayoutInflater factory = LayoutInflater.from(this);
	            final View window = factory.inflate(R.layout.input_password_dialog, null);
	            final EditText firstPassWord=(EditText) window.findViewById(R.id.first_password);
				final EditText SecondPassWord=(EditText) window.findViewById(R.id.second_password);
	                builder.setTitle("密码设置");
	                builder.setView(window);
	                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	                    @Override
						@SuppressLint({ "ShowToast", "ShowToast" })
						public void onClick(DialogInterface dialog, int whichButton) {
	                    	if(!("".equals(firstPassWord.getText().toString()))){
	                    		if(firstPassWord.getText().toString().equals(SecondPassWord.getText().toString())){
	                    			String password=firstPassWord.getText().toString();
	                    			SharedPreferences sharePreferences=getSharedPreferences(StudentbudgetUntility.PASSWORD,Context.MODE_PRIVATE);
	                    			Editor edit=sharePreferences.edit();
	                    			edit.putString("password", password);
	                    			edit.commit();
	                    			Toast.makeText(SetPassWordActivity.this, "密码设置成功", Toast.LENGTH_SHORT).show();
	                    		}else{
	                    			Toast.makeText(SetPassWordActivity.this, "两次输入密码不一样", Toast.LENGTH_SHORT).show();
	                    		}
	                    	}else{
	                    		Toast.makeText(SetPassWordActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
	                    	}
	                    }
	                });
	                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
	                    @Override
						public void onClick(DialogInterface dialog, int whichButton) {
	                    }
	                });
	              builder.create().show();
			break;
		}
	}
}
