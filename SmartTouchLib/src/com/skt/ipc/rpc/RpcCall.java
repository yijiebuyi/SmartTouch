package com.skt.ipc.rpc;

import com.cs.lua.LuaManager;
import com.cs.util.ULog;

/**
 * 该类不要有重载方法
 * 若增加重载方法，需要修改 RpcCallManager类 addCall()机制，即不能以唯一的方法名作为key存放到map中
 */
public class RpcCall {
	
	public void execLua(final String luaPath) {
		if (luaPath == null) {
			ULog.i("luaPath is null, retrurn!");
			return;
		}
		
		ULog.i("rpc execlua: path:"+luaPath);
		LuaManager.getInstance().setStopLua(false);
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				LuaManager.getInstance().execLua(luaPath);
			}
		}).start();
		
	}
	
	public void stopLua(String luaPath) {
		ULog.i("stop lua!!!");
		LuaManager.getInstance().setStopLua(true);
	}
	
	public void pauseLua(String luaPath) {
		
	}
}
