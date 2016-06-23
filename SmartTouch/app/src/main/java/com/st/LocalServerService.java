package com.st;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.Toast;

import com.skt.ipc.SktServer;
import com.skt.ipc.rpc.RpcCallHelper;
import com.st.util.CaseUtil;
import com.st.util.FileUtil;
import com.st.util.Key;
import com.st.util.STConfig;
import com.st.util.ULog;


public class LocalServerService extends Service {
	private SktServer mLocalServer;
	private Context mContext;
	private Bundle mParams;
	private final static String LUA_LIBS_MODULE_NAME = "libs.lua";
	private final static String LUA_LIBS_NAME = "lualibs.lua";
	private final static String LUA_SO_NAME = "libluajava.so";
	private final static String LUA_TEST_NAME = "test.lua";
	
	private final static String LOCAL_PROCESS_ENTER_METHOD = "init";
	
	private AudioManager mAudioManager;
	private VolumeReceiver mVolumeReceiver;
	private String mLuaPath;
	private long mTimeout = 0;
	private int mOldVolume = -1;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		ULog.i("===================service onCreate==================");
		mContext = this;
		mParams = new Bundle();
		mLocalServer = SktServer.getInstance();
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		registerVolumeChangeReceiver();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (intent == null) {
			return super.onStartCommand(intent, flags, startId);
		}
		
		String action = intent.getAction();
		if (STConfig.ACTION_INIT.equals(action)) {
			mLocalServer.start();
			startLocalProcess();
		} else if (STConfig.ACTION_DELAY_EXEC_LUA.equals(action)) {
			mLuaPath = intent.getExtras().getString(STConfig.LUA_PATH);
		} else if (STConfig.ACTION_EXEC_LUA.equals(action)) {
			mLuaPath = intent.getExtras().getString(STConfig.LUA_PATH);
			RpcCallHelper.execLua(mLuaPath, mContext);
		} else if (STConfig.ACTION_STOP_LUA.equals(action)) {
			mLuaPath = intent.getExtras().getString(STConfig.LUA_PATH);
			RpcCallHelper.stopLua(mLuaPath);
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mLocalServer.recyle();
		mLocalServer = null;
	}
	
	private void startLocalProcess() {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				mParams.clear();
				String path = getFilesDir().getAbsolutePath();
				String jarPath = FileUtil.copyJar(mContext);
				
				String luaLibsPath = FileUtil.copyFromAsset(mContext, LUA_LIBS_NAME, path, LUA_LIBS_MODULE_NAME);

				String luaSoPath = FileUtil.copyFromAsset(mContext, LUA_SO_NAME, path, LUA_SO_NAME);
				String luaScPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/lua";
				FileUtil.copyFromAsset(mContext, LUA_TEST_NAME, luaScPath, LUA_TEST_NAME);
				
				String defaultIme = Settings.Secure.getString(mContext.getContentResolver(), Settings.Secure.DEFAULT_INPUT_METHOD);
				String utf7Ime = STConfig.LIB_PKG_NAME + "/.ime.Utf7ImeService";
				
				mParams.putString(Key.LUA_LIB_PATH, luaLibsPath);
				mParams.putString(Key.LUA_SO_PATH, luaSoPath);
				mParams.putString(Key.LUA_SC_PATH, luaScPath);
				mParams.putString(Key.DEFAULT_IME, defaultIme);
				mParams.putString(Key.UTF7_IME, utf7Ime);
				
				CaseUtil.exec(jarPath, LOCAL_PROCESS_ENTER_METHOD, mParams);
			}
		}).start();
	}
	
	/**
	 * 注册当音量发生变化时接收的广播
	 */
	private void registerVolumeChangeReceiver() {
		mVolumeReceiver = new VolumeReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.media.VOLUME_CHANGED_ACTION");
		registerReceiver(mVolumeReceiver, filter);
	}
	
	/**
	 * 处理音量变化时的界面显示
	 * @author long
	 */
	private class VolumeReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if ("android.media.VOLUME_CHANGED_ACTION".equals(intent.getAction())) {
				if (TextUtils.isEmpty(mLuaPath)) {
					Toast.makeText(LocalServerService.this, "请选择要执行的脚本", Toast.LENGTH_SHORT).show();
					return;
				}

				if (!STConfig.mConnected) {
					Toast.makeText(LocalServerService.this, "脚本底层库还未连接成功", Toast.LENGTH_SHORT).show();
					return;
				}

				int type = intent.getIntExtra("android.media.EXTRA_VOLUME_STREAM_TYPE", AudioManager.STREAM_MUSIC);

				//EXTRA_VOLUME_STREAM_VALUE
				//EXTRA_PREV_VOLUME_STREAM_VALUE
				int currVolume = mAudioManager.getStreamVolume(type);
				int maxVolume = mAudioManager.getStreamMaxVolume(type);
				if (mOldVolume < 0) {
					mOldVolume = intent.getIntExtra("android.media.EXTRA_PREV_VOLUME_STREAM_VALUE", currVolume);
				}
				
				if (currVolume == 0) {
					if ((System.currentTimeMillis() - mTimeout) > 300) {
						RpcCallHelper.stopLua(mLuaPath);
					}
				} else if (currVolume == maxVolume) {
					if ((System.currentTimeMillis() - mTimeout) > 300) {
						RpcCallHelper.execLua(mLuaPath, mContext);
					}
				} else if (currVolume < mOldVolume) {
					RpcCallHelper.stopLua(mLuaPath);
				} else if (currVolume > mOldVolume){
					RpcCallHelper.execLua(mLuaPath, mContext);
				} else {
				}
				
				mOldVolume = currVolume;
				mTimeout = System.currentTimeMillis();
			}
		}
	}
	
}
