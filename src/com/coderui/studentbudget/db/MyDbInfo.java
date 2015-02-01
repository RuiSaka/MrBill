package com.coderui.studentbudget.db;

public class MyDbInfo {
	public static final String MY_DB_NAME="MyBudget.db";
	public static final int DB_VERSION=2;
	
	public MyDbInfo() {
		// TODO Auto-generated constructor stub
	}
	
	private static String TableNames[]={
		"EXPENSES",
		"INCOME",
		"ACCOUNT",
		"EXPENSES_CATEGORY",
		"INCOME_CATEGORY",
		"TRANSFER",
		"BACKUP"
	};
	
	private static String FieldNames[][]={
		{"ID","AMOUNT","EXPENSES_CATEGORY_ID","ACCOUNT_ID","DATE","ITEM_ID","REMARK"},
		{"ID","AMOUNT","ICNOME_CATEGORY_ID","ACCOUNT_ID","DATE","REMARK"},
		{"ID","NAME","BUDGET"},
		{"ID","NAME","BUDGET"},
		{"ID","NAME","BUDGET"},
		{"ID","AMOUNT","ACCOUNT_ID_OUT","ACCOUNT_ID_IN","DATE","REMARK"},
		{"ID","NAME"}
	};
	
	private static String FieldTypes[][]={
		{"INTEGER PRIMARY KEY AUTOINCREMENT","DOUBLE","INTEGER","INTEGER","TEXT","INTEGER","TEXT"},
		{"INTEGER PRIMARY KEY AUTOINCREMENT","DOUBLE","INTEGER","INTEGER","TEXT","TEXT"},
		{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","DOUBLE"},
		{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","DOUBLE"},
		{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT","DOUBLE"},
		{"INTEGER PRIMARY KEY AUTOINCREMENT","DOUBLE","INTEGER","INTRGER","TEXT","TEXT"},
		{"INTEGER PRIMARY KEY AUTOINCREMENT","TEXT"}
	};

	public static String[][] getFieldNames() {
		return FieldNames;
	}

	public static String[][] getFieldTypes() {
		return FieldTypes;
	}

	public static String[] getTableNames() {
		return TableNames;
	}
}
