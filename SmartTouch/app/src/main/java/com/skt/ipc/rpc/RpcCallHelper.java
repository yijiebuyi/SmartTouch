package com.skt.ipc.rpc;

import android.content.Context;

import com.st.util.FileUtil;

public class RpcCallHelper {
	
	public static void execLua(final String luaPath, Context context) {
		String saveDir = context.getFilesDir().getAbsolutePath();
		String saveName = "running.lua";
		final String path = FileUtil.copyFromFile(luaPath, saveDir, saveName);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					RpcCallManager.getInstance().invoke(RpcConfig.REMOTE_METHOD_EXEC_LUA, path);
				} catch (RpcException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void stopLua(final String luaPath) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					RpcCallManager.getInstance().invoke(RpcConfig.REMOTE_METHOD_STOP_LUA, luaPath);
				} catch (RpcException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
