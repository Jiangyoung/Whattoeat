package com.jiangyoungzh.whattoeat;


import android.app.Activity;
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
	private final int RAND_STOP = 1;
	
	private TextView textView1;
	private RadioGroup radioGroup;
	private Button btn_start;
	private Button btn_showList;
	
	private ProgressDialog pDialog;
	
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case RAND_STOP:
				if(pDialog.isShowing())pDialog.cancel();
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		init();

		btn_start.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				pDialog.setMessage("别着急~");
				pDialog.show();
				new Thread(){
					public void run() {
						try {
							this.sleep(3000);
							Message msg = Message.obtain();
							msg.what = RAND_STOP;
							handler.sendMessage(msg);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					};
				}.start();
				
			}
		});
		
		btn_showList.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intentToList = new Intent();
				intentToList.setClass(MainActivity.this, ListActivity.class);
				startActivity(intentToList);
				
			}
		});
		

		//actionBar = getActionBar();
		//actionBar.setDisplayHomeAsUpEnabled(true);
	}
	
	private void init() {
		pDialog = new ProgressDialog(MainActivity.this);
		
		radioGroup = (RadioGroup)findViewById(R.id.rg_chose);
		btn_showList = (Button)findViewById(R.id.btn_showList);
		btn_start = (Button)findViewById(R.id.btn_start);
		
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
}
