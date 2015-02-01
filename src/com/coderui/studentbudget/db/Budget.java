package com.coderui.studentbudget.db;

public class Budget {
	private Double amount;
	private Double old_amount;
	private Integer category_id;
	private Integer account_id_in;
	private Integer account_id_out;
	private Integer account_id;
	private String remark;
	private Integer item;
	private String date;

	public Double getOld_amount() {
		return old_amount;
	}

	public void setOld_amount(Double old_amount) {
		this.old_amount = old_amount;
	}

	public Integer getAccount_id_in() {
		return account_id_in;
	}

	public Integer getAccount_id_out() {
		return account_id_out;
	}

	public void setAccount_id_in(int account_id_in) {
		this.account_id_in = account_id_in;
	}

	public void setAccount_id_out(int account_id_out) {
		this.account_id_out = account_id_out;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Integer getCategory_id() {
		return category_id;
	}

	public void setCategory_id(int category_id) {
		this.category_id = category_id;
	}

	public Integer getAccount_id() {
		return account_id;
	}

	public void setAccount_id(int account_id) {
		this.account_id = account_id;
	}

	public Integer getItem() {
		return item;
	}

	public void setItem(int item) {
		this.item = item;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
}
