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

public class BudgetItemAdapter extends BaseAdapter {
	private String TAG="BudgetItemAdapter";
	private Context context;
	private List<Map<String,Object>> list;
	
	public BudgetItemAdapter(Context context,List<Map<String,Object>> list){
		this.context=context;
		this.list=list;
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
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Budgetitem budgetItem;
		if(convertView==null){
			convertView=LayoutInflater.from(context).inflate(R.layout.budget_item_view, null);
			budgetItem=new Budgetitem();
			budgetItem.budget_category_imv=(ProgressBar) convertView.findViewById(R.id.budget_category_probar_imv);
			budgetItem.budget_category_probar=(ProgressBar) convertView.findViewById(R.id.budget_category_probar);
			budgetItem.budget_category_tv=(TextView) convertView.findViewById(R.id.budget_expense_category_tv);
			budgetItem.budget_amount=(TextView) convertView.findViewById(R.id.budget_amount);
			budgetItem.left_amount=(TextView) convertView.findViewById(R.id.left_amount);
			convertView.setTag(budgetItem);
		}else{
			budgetItem=(Budgetitem) convertView.getTag();
		}
		Integer image=(Integer)list.get(position).get("images");
		String tv=(String)list.get(position).get("tv");
		int percent=0;
		double budget_amount=(Double)list.get(position).get("budget_amount");//Ô¤Ëã½ð¶î
		double left_amount=(Double)list.get(position).get("left_amount");//Óà¶î
		if(budget_amount==0){
			percent=0;
		}else{
			percent=(int)((left_amount/budget_amount)*100);
		}
		budgetItem.budget_category_imv.setBackgroundResource(image);
		budgetItem.budget_category_imv.setProgress(percent);
		budgetItem.budget_category_imv.setMax(100);
		budgetItem.budget_category_tv.setText(tv);
		if(left_amount<0){
			budgetItem.left_amount.setTextColor(context.getResources().getColor(R.color.red));
		}
		budgetItem.budget_amount.setText(String.valueOf(budget_amount));
		budgetItem.budget_amount.setTextColor(context.getResources().getColor(R.color.green));
		budgetItem.left_amount.setText(String.valueOf(left_amount));
		budgetItem.budget_category_probar.setProgress(percent);
		budgetItem.budget_category_probar.setMax(100);
		return convertView;
	}

}
class Budgetitem{
	ProgressBar budget_category_imv,budget_category_probar;
	TextView budget_category_tv;
	TextView budget_amount;
	TextView left_amount;
}
