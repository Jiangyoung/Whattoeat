package com.jiangyoungzh.whattoeat;

import com.jiangyoungzh.whattoeat.service.DBService;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import android.widget.Toast;

public class AddActivity extends Activity {
	
	private ActionBar actionBar;
	
	private TextView tv_add_default;

	private Spinner sp_add_restau;
	private EditText et_add_restau;
	private EditText et_add_meal;
	private Button btn_add_save;
	
	private String[] restauStrs;
	private boolean restauAdded = true;

	private String restauStr;
	private String mealStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		init();
		
		String[] patchParams = new String[]{"\t--添加--"};
		final int patchParamsLength = patchParams.length;
		DBService dbService = new DBService(AddActivity.this, 1);
		
		
		AddActivity.this.restauStrs = dbService.getAllRestauNameAndPatchParams(patchParams);
		
		ArrayAdapter<String> restauAdapter = new ArrayAdapter<String>(AddActivity.this, android.R.layout.simple_spinner_item,restauStrs);
		AddActivity.this.sp_add_restau.setAdapter(restauAdapter);

		AddActivity.this.sp_add_restau.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view,
					int position, long id) {
				//Toast.makeText(AddActivity.this, "position:"+position+" id"+id, Toast.LENGTH_SHORT).show();
				
				if(position<patchParamsLength){
					AddActivity.this.restauAdded = true;
					AddActivity.this.restauStr = "";
					AddActivity.this.et_add_meal.setEnabled(false);
					AddActivity.this.et_add_restau.setVisibility(View.VISIBLE);
				}else{
					AddActivity.this.restauAdded = false;
					AddActivity.this.restauStr = restauStrs[position];
					AddActivity.this.et_add_restau.setText("");
					AddActivity.this.et_add_restau.setVisibility(View.GONE);
					AddActivity.this.et_add_meal.setEnabled(true);
				}
			}
			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});
		
		AddActivity.this.et_add_restau.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				//tv_add_default.append("\nCharSequence:"+s+"start:"+start+"before:"+before+"count:"+count);
				if(s.length()>1){
					AddActivity.this.restauStr = s.toString();
					AddActivity.this.et_add_meal.setEnabled(true);

				}else{
					AddActivity.this.et_add_meal.setText("");
					AddActivity.this.et_add_meal.setEnabled(false);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		btn_add_save.setOnClickListener(new View.OnClickListener() {		
			@Override
			public void onClick(View v) {
				AddActivity.this.mealStr = AddActivity.this.et_add_meal.getText().toString();
				DBService dbService = new DBService(AddActivity.this, 2);
				if(AddActivity.this.restauAdded && AddActivity.this.restauStr.length()>1){					
					dbService.saveRestau(AddActivity.this.restauStr);
					if(AddActivity.this.mealStr.length()>1){
						dbService.saveMeal(AddActivity.this.restauStr, AddActivity.this.mealStr);
					}
					MainActivity.needInitRandData = true;
					showToast("完成添加");
				}else if(!AddActivity.this.restauAdded && AddActivity.this.mealStr.length()>1){
					dbService.saveMeal(AddActivity.this.restauStr, AddActivity.this.mealStr);
					showToast("完成添加");
				}else{
					showToast("至少填点啥么(字符2+)");
				}
				dbService.close();
				
			}
		});

	}
	
	
	private void init() {
		AddActivity.this.actionBar = getActionBar();
		AddActivity.this.actionBar.setDisplayHomeAsUpEnabled(true);
		
		AddActivity.this.tv_add_default = (TextView) findViewById(R.id.tv_add_default);

		AddActivity.this.sp_add_restau = (Spinner) findViewById(R.id.sp_add_restau);
		AddActivity.this.et_add_restau = (EditText) findViewById(R.id.et_add_restau);
		AddActivity.this.et_add_meal = (EditText) findViewById(R.id.et_add_meal);
		AddActivity.this.btn_add_save = (Button) findViewById(R.id.btn_add_save);
		
		
	}


	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		return true;
	}
	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch(item.getItemId()){
		case android.R.id.home:
			AddActivity.this.finish();
			break;
		default:

		}
		return super.onMenuItemSelected(featureId, item);
	}
	
	protected void showToast(String str){
		Toast.makeText(AddActivity.this, str, Toast.LENGTH_SHORT).show();
	}
}
