package com.coderui.studentbudget.untilty;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.coderui.studentbudget.R;
import com.coderui.studentbudget.wheeladapter.AbstractWheelTextAdapter;

public class MyAccountAdapter extends AbstractWheelTextAdapter {
	private String[] explanation=null;
	private Integer[] images=null;

	public MyAccountAdapter(Context context,String[] explanation,Integer[] images) {
		super(context,R.layout.account_category_wheel_item);
		this.explanation=explanation;
		this.images=images;
	}

	@Override
	public View getItem(int index, View convertView, ViewGroup parent) {
		 View view = super.getItem(index, convertView, parent);
         ImageView img = (ImageView) view.findViewById(R.id.account);
         img.setImageResource(images[index]);
         TextView tv=(TextView) view.findViewById(R.id.account_name);
         tv.setText(explanation[index]);
		return super.getItem(index, convertView, parent);
	}
	@Override
	public int getItemsCount() {
		return images.length;
	}

	@Override
	protected CharSequence getItemText(int index) {
		return explanation[index];
	}

}
