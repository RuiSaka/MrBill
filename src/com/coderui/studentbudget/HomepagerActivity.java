package com.coderui.studentbudget;

import android.annotation.SuppressLint;
import android.app.TabActivity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TabHost;

@SuppressLint("NewApi")
@SuppressWarnings("deprecation")
public class HomepagerActivity extends TabActivity {
	private TabHost myTabhost;
	private LinearLayout linearlaout;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tablehost);
		myTabhost=getTabHost();
		Intent intent1 = new Intent(this,ListBudgetActivity.class);
		createTab("listbill",this.getResources().getDrawable(R.drawable.list_image_selector),intent1);
		Intent intent2 = new Intent(this, ViewPagerActivity.class);
		Intent intent=getIntent();
		if(intent.hasExtra("amount")){
			Bundle bundle=intent.getExtras();
			intent2.putExtras(bundle);
		}
		createTab("viewpager",this.getResources().getDrawable(R.drawable.viewpager_image_selector),intent2);
		Intent intent3= new Intent(this, StatisticActivity.class);
		createTab("statistics",this.getResources().getDrawable(R.drawable.stastics_image_selector),intent3);
		if(intent.hasExtra("tabcategory")){
			int category=intent.getIntExtra("tabcategory", 1);
			myTabhost.setCurrentTab(category);
		}else{
			myTabhost.setCurrentTab(1);
		}
		
	}
	private void createTab(String intentName,Drawable drawable,Intent intent){ 
		myTabhost.addTab(myTabhost.newTabSpec(intentName).setIndicator(createTabView(drawable)).setContent(intent));
	}
	
	private View createTabView(Drawable drawable) {
		View view = LayoutInflater.from(this).inflate(R.layout.tab_indicator, null);
		linearlaout = (LinearLayout) view.findViewById(R.id.linearlayout);
		linearlaout.setBackgroundDrawable(drawable);
		return view;
	}
}
