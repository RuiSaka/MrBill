package com.coderui.studentbudget.db;

import com.coderui.studentbudget.untilty.StudentbudgetUntility;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDbHelper {
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb; // 数据库实例对象
	private static MyDbHelper openHelper = null;// 数据库调用实例

	private static String TableNames[];// 表名
	private static String FieldNames[][];// 字段名
	private static String FieldTypes[][];// 字段类型
	private static String NO_CREATE_TABLES = "no tables";
	private static String message = "";

	private final Context mCtx; // 上下文实例

	private MyDbHelper(Context ctx) {
		this.mCtx = ctx;
	}

	public static MyDbHelper getInstance(Context context) {
		if (openHelper == null) {
			openHelper = new MyDbHelper(context);
			TableNames = MyDbInfo.getTableNames();
			FieldNames = MyDbInfo.getFieldNames();
			FieldTypes = MyDbInfo.getFieldTypes();
		}
		return openHelper;
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {

		public DatabaseHelper(Context context) {
			super(context, MyDbInfo.MY_DB_NAME, null, MyDbInfo.DB_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			if (TableNames == null) {
				message = NO_CREATE_TABLES;
				return;
			}
			for (int i = 0; i < TableNames.length; i++) {
				String sql = "CREATE TABLE " + TableNames[i] + " (";
				for (int j = 0; j < FieldNames[i].length; j++) {
					sql += FieldNames[i][j] + " " + FieldTypes[i][j] + ",";
				}
				sql = sql.substring(0, sql.length() - 1);
				sql += ")";
				db.execSQL(sql);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			for (int i = 0; i < TableNames[i].length(); i++) {
				String sql = "DROP TABLE IF EXISTS " + TableNames[i];
				db.execSQL(sql);
			}
			onCreate(db);
		}
	}

	/** 添加数据库相关信息 */
	public void insertTables(String[] tableNames, String[][] fieldNames,
			String[][] fieldTypes) {
		TableNames = tableNames;
		FieldNames = fieldNames;
		FieldTypes = fieldTypes;
	}

	/** 打开数据库 */
	public MyDbHelper open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/** 关闭数据库 */
	public void close() {
		mDbHelper.close();
	}

	public void execSQL(String sql, Object[] bindArgs)
			throws java.sql.SQLException {
		mDb.execSQL(sql, bindArgs);
	}
	/**sql查询语句*/
	public Cursor query(String sql,String[] obj){
		Cursor cursor=mDb.rawQuery(sql, obj);
		return cursor;
	}

	/** sql语句查询数据 */
	public Cursor rawQuery(String table) {
		Cursor cursor = mDb.rawQuery("SELECT * FROM " + table, null);
		return cursor;
	}

	/** 查询数据 */
	public Cursor select(String table, String budgetId) {
			Cursor cursor = mDb.rawQuery("SELECT BUDGET FROM " + table
					+ " WHERE ID=?", new String[] { budgetId });
			return cursor;
	}

	/** 添加数据 */
	public void insert(String table, Budget budget) {
		if (table.equals(StudentbudgetUntility.EXPENSESTABLE)) {
			mDb.execSQL(
					"INSERT INTO "
							+ table
							+ "(AMOUNT,EXPENSES_CATEGORY_ID,ACCOUNT_ID,DATE,ITEM_ID,REMARK) VALUES(?,?,?,?,?,?)",
					new Object[] { budget.getAmount(), budget.getCategory_id(),
							budget.getAccount_id(), budget.getDate(),
							budget.getItem(), budget.getRemark() });
		}
		if (table.equals(StudentbudgetUntility.INCOMETABLE)) {
			mDb.execSQL(
					"INSERT INTO "
							+ table
							+ "(AMOUNT,ICNOME_CATEGORY_ID,ACCOUNT_ID,DATE,REMARK) VALUES(?,?,?,?,?)",
					new Object[] { budget.getAmount(), budget.getCategory_id(),
							budget.getAccount_id(), budget.getDate(),
							budget.getRemark() });
		}
		if (table.equals(StudentbudgetUntility.TRANSFERTABLE)) {
			mDb.execSQL(
					"INSERT INTO "
							+ table
							+ "(AMOUNT,ACCOUNT_ID_OUT,ACCOUNT_ID_IN,DATE,REMARK) VALUES(?,?,?,?,?)",
					new Object[] { budget.getAmount(),
							budget.getAccount_id_out(),
							budget.getAccount_id_in(), budget.getDate(),
							budget.getRemark() });
		}
	}

	/** 删除数据 */
	public void delete(String sql) {
		mDb.execSQL(sql);
	}

	/** 更新数据 */
	public void update(String table, Budget budget,int category) {
		switch(category){
		case 0://支出
			if (table.equals(StudentbudgetUntility.ACCOUNT_TABLE)) {
				mDb.execSQL("UPDATE " + table + " SET BUDGET=? WHERE ID=?",
						new Object[] { budget.getOld_amount() - budget.getAmount(),
								budget.getAccount_id() });
			}
			if(table.equals(StudentbudgetUntility.EXPENSES_CATEGORY_TABLE)){
				mDb.execSQL("UPDATE " + table + " SET BUDGET=? WHERE ID=?",
						new Object[] { budget.getOld_amount() + budget.getAmount(),
								budget.getCategory_id() });
			}
			if(table.equals(StudentbudgetUntility.ITEM_TABLE)){
				mDb.execSQL("UPDATE " + table + " SET BUDGET=? WHERE ID=?",
						new Object[] { budget.getOld_amount() + budget.getAmount(),
								budget.getItem() });
			}
			break;
		case 1://收入
			if (table.equals(StudentbudgetUntility.ACCOUNT_TABLE)) {
				mDb.execSQL("UPDATE " + table + " SET BUDGET=? WHERE ID=?",
						new Object[] { budget.getOld_amount() + budget.getAmount(),
								budget.getAccount_id() });
			}
			if(table.equals(StudentbudgetUntility.INCOME_CATEGORY_TABLE)){
				mDb.execSQL("UPDATE " + table + " SET BUDGET=? WHERE ID=?",
						new Object[] { budget.getOld_amount() + budget.getAmount(),
								budget.getCategory_id() });
			}
			break;
		}
	}

	/** 错误信息： 不为null数据库未建立 */
	public String getMessage() {
		return message;
	}

}
