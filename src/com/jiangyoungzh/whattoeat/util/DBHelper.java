package com.jiangyoungzh.whattoeat.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper{

	public DBHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String sql = "CREATE TABLE `wte_restaurant`("
					+"`id` INTEGER PRIMARY KEY AUTOINCREMENT,"
					+"`name` VARCHAR(16) NOT NULL DEFAULT '');";
		db.execSQL(sql);

		sql = "CREATE TABLE 'wte_meal'("
				+"`id` INTEGER PRIMARY KEY AUTOINCREMENT,"
				+"`restau_id` INTEGER DEFAULT 0,"
				+"`name` VARCHAR(16) NOT NULL DEFAULT '');";
				
		//String sql = "CREATE TABLE person (id INTEGER PRIMARY KEY AUTOINCREMENT,name String,sexy String,age String,height String);";
		db.execSQL(sql);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
