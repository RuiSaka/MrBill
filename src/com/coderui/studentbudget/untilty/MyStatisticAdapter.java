package com.coderui.studentbudget.untilty;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.coderui.studentbudget.R;

public class MyStatisticAdapter extends BaseAdapter {
	private String TAG="MyStatisticAdapter";
	private Context context;
	private List<Map<String,Object>> list;
	private Double amountAll;
	
	
	public MyStatisticAdapter(Context context,List<Map<String,Object>> list,Double amountAll){
		this.context=context;
		this.list=list;
		this.amountAll=amountAll;
		
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		StatisticListItem statisticListItem;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.category_list_statistic, null);
			statisticListItem = new StatisticListItem();
			statisticListItem.imv_bar=(ProgressBar) convertView.findViewById(R.id.expense_category_imv);
			statisticListItem.amount=(TextView) convertView.findViewById(R.id.amount);
			statisticListItem.tv_category=(TextView)convertView.findViewById(R.id.tv_category);
			statisticListItem.percent_tv=(TextView)convertView.findViewById(R.id.percent_tv);
			statisticListItem.bar=(ProgressBar) convertView.findViewById(R.id.expense_category);
			convertView.setTag(statisticListItem);
		}else{
			statisticListItem=(StatisticListItem) convertView.getTag();
		}
		int percent=(int) ((((Double)list.get(position).get("amount"))/amountAll)*100);
		statisticListItem.imv_bar.setBackgroundResource((Integer)list.get(position).get("image"));
		statisticListItem.imv_bar.setProgress(percent);
		Log.v(TAG, "Percent"+String.valueOf(percent));
		statisticListItem.imv_bar.setMax(100);
		statisticListItem.tv_category.setText((String)list.get(position).get("explanation"));
		statisticListItem.amount.setText(String.valueOf(list.get(position).get("amount"))+"¥");
		statisticListItem.percent_tv.setText(String.valueOf(percent)+"%");
		statisticListItem.bar.setProgress(percent);
		statisticListItem.bar.setMax(100);
		return convertView;
	}
	
	public final class StatisticListItem{
		ProgressBar imv_bar;
		TextView tv_category;
		TextView amount;
		TextView percent_tv;
		ProgressBar bar;
	}
	

}
