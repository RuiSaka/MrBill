package com.coderui.studentbudget;

import java.util.List;
import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AccountExpandableListAdapter extends BaseExpandableListAdapter {
	private Context context;
	private List<Map<String, Object>> parentList;
	private List<List<Map<String, Object>>> childList;

	public AccountExpandableListAdapter(Context context,
			List<Map<String, Object>> parentList,
			List<List<Map<String, Object>>> childList) {
		this.context = context;
		this.parentList = parentList;
		this.childList = childList;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childList.get(groupPosition).get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		Log.v("AccountExpandableListAdapter","getChildView");	
		ChildAccount childAccount;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.child_account_list_view, null);
			childAccount = new ChildAccount();
			childAccount.account_inout_item_cat_imv = (ImageView) convertView
					.findViewById(R.id.account_inout_item_cat_imv);
			childAccount.account_inout_item_cat_tv = (TextView) convertView
					.findViewById(R.id.account_inout_item_cat_tv);
			childAccount.item_amount = (TextView) convertView
					.findViewById(R.id.item_amount);
			childAccount.item_month = (TextView) convertView
					.findViewById(R.id.item_month);
			childAccount.bill_category=(TextView)convertView.findViewById(R.id.bill_category);
			convertView.setTag(childAccount);
		} else {
			childAccount = (ChildAccount) convertView.getTag();
		}
		int bill_category = (Integer) childList.get(groupPosition).get(childPosition).get("bill_category");
		switch(bill_category){
		case 1:
			childAccount.bill_category.setText("支出");
			childAccount.bill_category.setTextColor(context.getResources().getColor(R.color.red));
			break;
		case 2:
			childAccount.bill_category.setText("收入");
			childAccount.bill_category.setTextColor(context.getResources().getColor(R.color.green));
			break;
		case 3:
			childAccount.bill_category.setText("转入");
			childAccount.bill_category.setTextColor(context.getResources().getColor(R.color.yellow));
			break;
		case 4:
			childAccount.bill_category.setText("转出");
			childAccount.bill_category.setTextColor(context.getResources().getColor(R.color.yellow));
			break;
		}
		double amount=(Double)childList.get(groupPosition).get(childPosition).get("amount");
		childAccount.item_amount.setText(String.valueOf(amount));
		String cat_tv=(String)childList.get(groupPosition).get(childPosition).get("category_tv");
		childAccount.account_inout_item_cat_tv.setText(cat_tv);
		String date=(String)childList.get(groupPosition).get(childPosition).get("date");
		childAccount.item_month.setText(date);
		childAccount.account_inout_item_cat_imv.setImageResource((Integer)childList.get(groupPosition).get(childPosition).get("category_imv"));
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
		ParentAccount parentAccount;
		Log.v("AccountExpandableListAdapter","getGroupView");
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.acconts_view, null);
			parentAccount = new ParentAccount();
			parentAccount.account_category_im = (ImageView) convertView
					.findViewById(R.id.account_category);
			parentAccount.account_category_tv = (TextView) convertView
					.findViewById(R.id.account_name);
			parentAccount.in_account = (TextView) convertView
					.findViewById(R.id.in_account);
			parentAccount.out_account = (TextView) convertView
					.findViewById(R.id.out_account);
			parentAccount.account_image_indictor = (ImageView) convertView
					.findViewById(R.id.account_image_indictor);
			parentAccount.left_account=(TextView) convertView.findViewById(R.id.left_account);
			convertView.setTag(parentAccount);
		} else {
			parentAccount = (ParentAccount) convertView.getTag();
		}
		if (isExpanded) {
			parentAccount.account_image_indictor
					.setImageResource(R.drawable.listup);
		} else {
			parentAccount.account_image_indictor
					.setImageResource(R.drawable.listdown);
		}
		double left_amount=(Double) parentList.get(groupPosition).get("left_amount");
		parentAccount.left_account.setText(String.valueOf(left_amount));
		double inaccount=(Double) parentList.get(groupPosition).get("in_account");
		parentAccount.in_account.setText(String.valueOf(inaccount));
		double outaccount=(Double) parentList.get(groupPosition).get("out_account");
		parentAccount.out_account.setText(String.valueOf(outaccount));
		String account_tv=(String)parentList.get(groupPosition).get("account_tv");
		parentAccount.account_category_tv.setText(account_tv);
		parentAccount.account_category_im.setImageResource((Integer)parentList.get(groupPosition).get("account_imv"));
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
		return false;
	}

}

class ParentAccount {
	ImageView account_category_im, account_image_indictor;
	TextView account_category_tv, in_account, out_account, left_account;
}

class ChildAccount {
	TextView item_month, item_day, account_inout_item_cat_tv, item_amount,bill_category;
	ImageView account_inout_item_cat_imv;
}
