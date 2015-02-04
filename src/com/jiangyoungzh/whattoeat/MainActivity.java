package com.jiangyoungzh.whattoeat;


import java.util.Random;

import com.jiangyoungzh.whattoeat.service.DBService;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity {
	public static boolean needInitRandData = true;
	
	private final int RAND_STOP = 1;
	
	private TextView tv_main_default;
	private RadioGroup radioGroup;
	private Button btn_start;
	private Button btn_showList;
	
	private ProgressDialog pDialog;
	
	private String[] restauStrs = null;
	private String[] mealStrs = null;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case RAND_STOP:
				if(pDialog.isShowing())pDialog.cancel();
				MainActivity.this.showRandResault();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();

		MainActivity.this.btn_start.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				MainActivity.this.pDialog.setMessage("别着急~");
				MainActivity.this.pDialog.show();
				if(MainActivity.needInitRandData){
					MainActivity.needInitRandData = false;
					MainActivity.this.initRandData();
				}
				new Thread(){
					public void run() {
						try {
							Thread.sleep(2000);
							Message msg = Message.obtain();
							msg.what = RAND_STOP;
							MainActivity.this.handler.sendMessage(msg);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};
				}.start();
				
			}
		});
		
		MainActivity.this.btn_showList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentToList = new Intent();
				intentToList.setClass(MainActivity.this, ListActivity.class);
				startActivity(intentToList);
			}
		});
		
	}
	
	protected void showRandResault() {
		String res = getRandResault();
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
		dialogBuilder.setTitle("随机结果");
		dialogBuilder.setIcon(android.R.drawable.btn_star);
		dialogBuilder.setMessage(res);
		dialogBuilder.setPositiveButton("确定", null);
		dialogBuilder.create().show();
	}
	
	protected String getRandResault(){
		String res = "";
		int randNum = 0;
		int mode = MainActivity.this.radioGroup.getCheckedRadioButtonId();
		Random random = new Random(System.currentTimeMillis());
		switch(mode){
		case R.id.rb_randRestau:
			if(restauStrs.length<1){
				showToast("木有还木有餐厅数据，随毛线啊");
				System.exit(1);
			}
			randNum = random.nextInt(restauStrs.length);
			res = restauStrs[randNum];
			break;
		case R.id.rb_randMeal:
			if(mealStrs.length<1){
				showToast("木有还木有饭菜数据，随毛线啊");
				System.exit(1);
			}
			randNum = random.nextInt(mealStrs.length);
			res = mealStrs[randNum];
			break;
		case R.id.rb_both:
			res = "coming soon.";
			break;
		default:
		}
		return res;
	}

	private void init() {
		MainActivity.this.pDialog = new ProgressDialog(MainActivity.this);
		
		MainActivity.this.tv_main_default = (TextView)findViewById(R.id.tv_main_default);
		MainActivity.this.radioGroup = (RadioGroup)findViewById(R.id.rg_chose);
		MainActivity.this.btn_showList = (Button)findViewById(R.id.btn_showList);
		MainActivity.this.btn_start = (Button)findViewById(R.id.btn_start);
		
		//DBService dbService = new DBService(MainActivity.this, 1);
		
		initCount();

	}
	/**
	 * 取得餐厅和饭菜的数据
	 */
	private void initRandData() {
		initCount();
		DBService dbService = new DBService(MainActivity.this, 1);
		restauStrs = dbService.getAllRestauName();
		mealStrs = dbService.getAllMealName();
	}
	/**
	 * 取得餐厅和饭菜的数量
	 */
	private void initCount() {
		DBService dbService = new DBService(MainActivity.this, 1);
		int restauCount = dbService.getRestauCount();
		int mealCount = dbService.getMealCount();
		MainActivity.this.tv_main_default.setText("已录入资料\n餐厅："+restauCount+"家，饭菜："+mealCount+"种");
		dbService.close();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		//Toast.makeText(MainActivity.this,item.getTitle(),Toast.LENGTH_SHORT).show();
		switch(item.getItemId()){
		case R.id.menu_add:
			Intent intentToAdd = new Intent();
			intentToAdd.setClass(MainActivity.this, AddActivity.class);
			startActivity(intentToAdd);
			break;
		default:

		}
		return super.onMenuItemSelected(featureId, item);
	}
	protected void showToast(String str){
		Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
	}
}
