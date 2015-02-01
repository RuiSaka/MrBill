package com.coderui.studentbudget;

import java.util.List;
import java.util.Map;

import com.coderui.studentbudget.untilty.StudentbudgetUntility;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<Map<String, Object>> parentList;
	private List<List<Map<String, Object>>> childList;

	public MyExpandableListAdapter(Context context,
			List<Map<String, Object>> parentList,
			List<List<Map<String, Object>>> childList) {
		this.context = context;
		this.parentList = parentList;
		this.childList = childList;
	}

	@Override
	public Object getChild(int arg0, int arg1) {
		return childList.get(arg0).get(arg1);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildListView childListView;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.child_day_budget_view, null);
			childListView = new ChildListView();
			childListView.im_list_category = (ImageView) convertView
					.findViewById(R.id.im_list_category);
			childListView.tv_list_category = (TextView) convertView
					.findViewById(R.id.tv_list_category);
			childListView.tv_list_amount = (TextView) convertView
					.findViewById(R.id.tv_list_amount);
			convertView.setTag(childListView);
		} else {
			childListView = (ChildListView) convertView.getTag();
		}
		childListView.im_list_category.setImageResource((Integer) childList
				.get(groupPosition).get(childPosition).get("image"));
		childListView.tv_list_category.setText(String.valueOf(childList
				.get(groupPosition).get(childPosition).get("explanation")));
		childListView.tv_list_amount.setText(String.valueOf(childList
				.get(groupPosition).get(childPosition).get("amount")));
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return childList.get(groupPosition).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return parentList.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return parentList.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ParentListItemView parentListView;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.parent_day_budget_view, null);
			parentListView = new ParentListItemView();
			parentListView.tv_day = (TextView) convertView
					.findViewById(R.id.tv_day);
			parentListView.tv_month = (TextView) convertView
					.findViewById(R.id.tv_weekday);
			parentListView.tv_amount = (TextView) convertView
					.findViewById(R.id.tv_amount);
			parentListView.image_indictor = (ImageView) convertView
					.findViewById(R.id.image_indictor);
			convertView.setTag(parentListView);
		} else {
			parentListView = (ParentListItemView) convertView.getTag();
		}
		if (isExpanded) {
			parentListView.image_indictor.setImageResource(R.drawable.listup);
		} else {
			parentListView.image_indictor.setImageResource(R.drawable.listdown);
		}
		String day = String.valueOf(parentList.get(groupPosition).get("day"));
		parentListView.tv_day.setText(day);
		String strDate = (String) parentList.get(groupPosition).get("strDate");
		try {
			parentListView.tv_month.setText(StudentbudgetUntility
					.getWeekdays(strDate));
		} catch (Exception e) {
			e.printStackTrace();
		}
		parentListView.tv_amount.setText(parentList.get(groupPosition).get(
				"amountSum")
				+ "¥");
		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		// TODO Auto-generated method stub
		return true;
	}

}

class ParentListItemView {
	TextView tv_day;
	TextView tv_month;
	TextView tv_amount;
	ImageView image_indictor;

}

class ChildListView {
	ImageView im_list_category;
	TextView tv_list_category;
	TextView tv_list_amount;
}
