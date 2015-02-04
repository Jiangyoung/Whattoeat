package com.jiangyoungzh.whattoeat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.jiangyoungzh.whattoeat.service.DBService;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ListActivity extends Activity {
	
	private ActionBar actionBar;
	
	private TextView tv_list_default;
	private Spinner sp_list_restau;
	private ListView lv_list_meal;
	
	private List<Map<String,Object>> mealList;
	
	private SimpleAdapter simpleAdapter;
	
	private String[] restauStrs;
	private String[] mealStrs;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list);
		
		init();
		
		sp_list_restau.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				ListActivity.this.initMealListView();
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		lv_list_meal.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				final int itemClickPosition = position;
				AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ListActivity.this);		
				String[] items = new String[]{"删除","点了也没用"};
				DialogInterface.OnClickListener itemClickListener = new DialogInterface.OnClickListener() {	
					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch(which){
						case 0:
							AlertDialog.Builder confirmDialogBuilder = new AlertDialog.Builder(ListActivity.this);
							confirmDialogBuilder.setTitle("提示");
							confirmDialogBuilder.setMessage("确定要删除吗？");
							
							DialogInterface.OnClickListener positiveClickListener = new DialogInterface.OnClickListener() {	
								@Override
								public void onClick(DialogInterface dialog, int which) {
									String mealStr = "";
									String restauStr = "";
									int spinnerPosition = sp_list_restau.getSelectedItemPosition();
									restauStr = ListActivity.this.restauStrs[spinnerPosition];
									mealStr = ListActivity.this.mealStrs[itemClickPosition];
									DBService dbService = new DBService(ListActivity.this, 2);
									dbService.deleteMealFromRestauByName(restauStr, mealStr);
									dbService.close();
									Toast.makeText(ListActivity.this, "删除成功",Toast.LENGTH_SHORT).show();
									ListActivity.this.initMealListView();
								}
							};
							confirmDialogBuilder.setPositiveButton("确认",positiveClickListener);
							break;
						case 1:
							break;
						default:
						}
						
					}
				};
				dialogBuilder.setItems(items, itemClickListener);
				dialogBuilder.setNegativeButton("取消", null);
				dialogBuilder.create();
			}
		});
	}
	
	protected void initMealListView() {
		DBService dbService = new DBService(ListActivity.this, 1);
		ListActivity.this.mealStrs = dbService.getAllMealName();
		dbService.close();
		mealList = new ArrayList<Map<String,Object>>();
		Map<String,Object> map;
		for(int i=0;i<mealStrs.length;i++){
			map = new HashMap<String,Object>();
			map.put("mealName", mealStrs[i]);
			mealList.add(map);
		}
		simpleAdapter = new SimpleAdapter(ListActivity.this, mealList,
				android.R.layout.simple_list_item_1,
				new String[]{"name"},new int[]{android.R.id.text1});
		lv_list_meal.setAdapter(simpleAdapter);		
	}

	private void init() {
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		
		tv_list_default = (TextView) findViewById(R.id.tv_list_default);
		sp_list_restau = (Spinner) findViewById(R.id.sp_list_restau);
		lv_list_meal = (ListView) findViewById(R.id.lv_list_meal);
		
		initRestauSpinner();
		
	}

	private void initRestauSpinner() {		
		DBService dbService = new DBService(ListActivity.this, 1);
		ListActivity.this.restauStrs = dbService.getAllRestauName();
		dbService.close();
		ArrayAdapter<String> restauAdapter = new ArrayAdapter<String>(ListActivity.this, android.R.layout.simple_spinner_item,restauStrs);
		sp_list_restau.setAdapter(restauAdapter);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			ListActivity.this.finish();
			break;
		default:

		}
		return super.onMenuItemSelected(featureId, item);
	}
	
}
