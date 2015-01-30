package com.jiangyoungzh.whattoeat;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class AddActivity extends Activity {
	
	private ActionBar actionBar;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		
		actionBar = getActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
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
}
