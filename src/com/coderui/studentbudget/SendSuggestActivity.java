package com.coderui.studentbudget;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SendSuggestActivity extends Activity {
	private EditText content;
	private Button send,return_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.sendsuggest_view);
		content = (EditText) findViewById(R.id.suggest_et);
		send = (Button) findViewById(R.id.sendsuggest);
		return_btn=(Button) findViewById(R.id.send_return_btn);
		send.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String strEmailBody = content.getText().toString();
				Intent intent = new Intent(android.content.Intent.ACTION_SEND);
				intent.setType("plain/text");
				String[] strEmailReciver = new String[]{"coderui@outlook.com"};
				intent.putExtra(android.content.Intent.EXTRA_EMAIL,strEmailReciver);
				intent.putExtra(android.content.Intent.EXTRA_TEXT, strEmailBody);
				startActivity(Intent.createChooser(intent,"·¢ËÍ"));
			}
		});
	}
}
