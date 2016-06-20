package com.example.filemanager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.example.filemanager.config.FileConfig;
import com.example.filemanager.config.FileTheme;
import com.example.fliemanager.R;

public class MainActivity extends Activity {
	private Button tv;
	private FileConfig fileConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tv = (Button) findViewById(R.id.tv);
		fileConfig = new FileConfig();
		tv.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(MainActivity.this, FileManagerActivity.class);
				fileConfig.startPath = Environment.getExternalStorageDirectory().getPath();
		        fileConfig.rootPath = "/";
		        fileConfig.theme = FileTheme.THEME_GREY;
		        intent.putExtra(FileConfig.FILE_CONFIG, fileConfig);
				startActivityForResult(intent, 10);
			}
		});
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode == Activity.RESULT_OK && requestCode == 10) {
			Log.e("MainActivity", data.getExtras().toString());
//			tv.setText(data.getData().getPath());
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
