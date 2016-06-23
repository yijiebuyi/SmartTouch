package com.st;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.st.adapter.FilelListAdapter;
import com.st.util.FileUtil;
import com.st.util.STConfig;

public class MainActivity extends Activity implements OnItemClickListener, DataManager.ScriptLibConnectListener {
	public static final String LUAPATH = FileUtil.getAndroidPath("/lua");// lua文件路径
	private ListView mListView;
	private FilelListAdapter mAdapter;
	private TextView mDesc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mDesc = (TextView)findViewById(R.id.desc);
		initListView();

		DataManager.getmInstance().registScriptLibConnectListener(this);
	}

	 /**
	    * 初始化listView
	    */
	private void initListView() {
		mListView = (ListView) findViewById(R.id.list_file);
		mListView.setOnItemClickListener(this);
		if (STConfig.mConnected) {
			mAdapter = new FilelListAdapter(this, FileUtil.getLuaFile(LUAPATH));
			mDesc.setText(R.string.script_lib_connected_txt);
		} else {
			mAdapter = new FilelListAdapter(this);
			mDesc.setText(R.string.script_lib_connecting_txt);
		}

		mListView.setAdapter(mAdapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		//execute
		String luaPath = (String)mAdapter.getItem(position);
		String name = String.format(getString(R.string.script_execute), luaPath);
		Toast.makeText(this, name, Toast.LENGTH_SHORT).show();
		Intent intent=new Intent(STConfig.ACTION_DELAY_EXEC_LUA);
		intent.setClass(this, LocalServerService.class);
		intent.putExtra(STConfig.LUA_PATH, MainActivity.LUAPATH + "/" + luaPath);
		startService(intent);
	}

	@Override
	public void onConnectChanged(boolean succ) {
		if (succ) {
			mDesc.setText(R.string.script_lib_connected_txt);
			mAdapter.setDatas(FileUtil.getLuaFile(LUAPATH));
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		DataManager.getmInstance().unregistScriptLibConnectListener();
	}
}
