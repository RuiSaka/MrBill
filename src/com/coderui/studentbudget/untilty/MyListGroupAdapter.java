package com.coderui.studentbudget.untilty;

import java.util.List;
import java.util.Map;

import com.coderui.studentbudget.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyListGroupAdapter extends BaseAdapter {
	private Context context;
	private List<Map<String, Object>> list;

	public MyListGroupAdapter(Context context, List list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ListItemView listItemView;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.list_item_of_month, null);
			listItemView = new ListItemView();
			listItemView.date=(TextView) convertView.findViewById(R.id.list_month);
			listItemView.text_all=(TextView) convertView.findViewById(R.id.data_all);
			listItemView.text_average=(TextView) convertView.findViewById(R.id.data_average);
			listItemView.text_date_of_range=(TextView) convertView.findViewById(R.id.date_range);
			convertView.setTag(listItemView);
		}else{
			listItemView=(ListItemView) convertView.getTag();
		}
		int cur_month=(Integer)list.get(position).get("date");
		listItemView.date.setText(String.valueOf(cur_month));
		listItemView.text_all.setText(list.get(position).get("all_amount")+"¥");
		listItemView.text_average.setText(list.get(position).get("average_amount")+"¥");
		int cur_year=(Integer)list.get(position).get("cur_year");
		String dateRange=cur_month+"/01"+"-"+cur_month+"/"+StudentbudgetUntility.getDays(cur_month, 13, cur_year);
		listItemView.text_date_of_range.setText(dateRange);
		return convertView;
	}
	
	public final class ListItemView{
		TextView date;
		TextView text_all;
		TextView text_average;
		TextView text_date_of_range;
	}

}
