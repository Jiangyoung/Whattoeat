package com.jiangyoungzh.whattoeat.service;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.jiangyoungzh.whattoeat.util.DBHelper;

public class DBService {
	private DBHelper dbHelper;
	private SQLiteDatabase db = null;
	/**
	 * 
	 * @param context
	 * @param mode 1、读操作   2、写操作
	 */
	public DBService(Context context,int mode){
		this.dbHelper = new DBHelper(context,"whattoeat.db", null, 1);
		if(1 == mode){
			this.db = this.dbHelper.getReadableDatabase();
		}else{
			this.db = this.dbHelper.getWritableDatabase();
		}
	}
	/**
	 * 
	 * @param name 要插入的值
	 */
	public void saveRestau(String restauStr){
		if(!restauIsExist(restauStr)){
			String sql = "INSERT INTO `wte_restaurant`(`name`) VALUES('"+restauStr+"')";
			db.execSQL(sql);
		}
		
	}
	private boolean restauIsExist(String restauStr) {
		String sql = "SELECT `id` FROM `wte_restaurant` WHERE `name`='"+restauStr+"'";
		Cursor c = db.rawQuery(sql, null);
		if(c.moveToFirst())return true;		
		return false;
	}
	public void saveMeal(String restauStr,String mealStr){
		String sql = "INSERT INTO `wte_meal`(`restau_id`,`name`) VALUES((SELECT `id` FROM `wte_restaurant` WHERE `wte_restaurant`.`name`='"+restauStr+"'),'"+mealStr+"')";
		db.execSQL(sql);
	}
	public void delete(){
		//TODO
	}
	public int update(){
		//TODO
		return 0;
	}
	public void deleteRestauByName(String restauStr){
		//先删除所有餐厅下的餐
		String sql = "DELETE FROM `wte_meal` WHERE `restau_id` IN (SELECT `id` FROM `wte_restaurant` WHERE `wte_restaurant`.`name`='"+restauStr+"')" ;
		db.execSQL(sql);
		//删除餐厅
		String sql2 = "DELETE FROM `wte_restaurant` WHERE `name`='"+restauStr+"'";
		db.execSQL(sql2);
	}
	public void deleteMealFromRestauByName(String restauStr,String mealStr){
		String sql = "DELETE FROM `wte_meal` WHERE `name`='"+mealStr+"' AND `restau_id` IN (SELECT `id` FROM `wte_restaurant` WHERE `wte_restaurant`.`name`='"+restauStr+"')";
		db.execSQL(sql);
	}
	public String[] getAllRestauName(){
		String[] res = this.getAllNameByTableName("restaurant",null);
		return res;
	}
	public String[] getAllMealName(){
		String[] res = this.getAllNameByTableName("meal",null);
		return res;
	}
	private String[] getAllNameByTableName(String tbName,String[] params){
		List<String> temp = new ArrayList<String>();
		String sql = "SELECT DISTINCT `name` FROM `wte_"+tbName+"`";
		Cursor c = db.rawQuery(sql, null);
		if(params != null){
			for(int i = 0;i<params.length;i++){
				temp.add(params[i]);
			}
		}
		while(c.moveToNext()){
			temp.add(c.getString(c.getColumnIndex("name")));
		}	
		String[] res = listStringToStringArr(temp);
		return res;
	}
	public String[] getAllRestauNameAndPatchParams(String[] params){
		String[] res = this.getAllNameByTableName("restaurant",params);
		return res;
	}
	public String[] getAllMealNameAndPatchParams(String[] params){
		String[] res = this.getAllNameByTableName("meal",params);
		return res;
	}
	private String[] listStringToStringArr(List<String> listStr){
		final int listSize = listStr.size();
		String[] strs = (String[])listStr.toArray(new String[listSize]);
		return strs;
	}
	public String[] getMealsByRestauName(String restauStr){
		List<String> temp = new ArrayList<String>();
		String sql = "SELECT `name` FROM `wte_meal` "
				+ "WHERE `restau_id` IN "
				+ "(SELECT `id` FROM `wte_restaurant` "
				+ "WHERE `wte_restaurant`.`name`='"+restauStr+"')";
		Cursor c = db.rawQuery(sql, null);
		while(c.moveToNext()){
			temp.add(c.getString(c.getColumnIndex("name")));
		}
		String[] res = listStringToStringArr(temp);
		return res;
	}
	public int getRestauCount(){
		int res = this.getCountByTableName("restaurant");
		return res;
	}
	public int getMealCount(){
		int res = this.getCountByTableName("meal");
		return res;
	}
	
	private int getCountByTableName(String tbName){
		int res = 0;
		String sql = "SELECT COUNT(DISTINCT `name`) as `count` FROM `wte_"+tbName+"`";
		Cursor c = db.rawQuery(sql, null);		
		if(c.moveToFirst()){
			res = c.getInt(c.getColumnIndex("count"));
		}
		return res;
	}

	public void close(){
		db.close();
	}
}
