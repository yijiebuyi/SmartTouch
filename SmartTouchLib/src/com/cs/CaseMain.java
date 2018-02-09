package com.cs;

import android.os.Bundle;

import com.android.uiautomator.testrunner.UiAutomatorTestCase;
import com.cs.lua.LuaManager;
import com.cs.util.ULog;
import com.skt.ipc.SktClient;
import com.skt.ipc.rpc.RpcCallManager;

/**
 * 模拟按键实现方案：
 * 1、uiatuomation
 * 2、instrumentation(按键精灵)
 */

public class CaseMain extends UiAutomatorTestCase {
	public static String LUA_SO_PATH = "";
	public static String UTF7_IME = "";
	public static String DEFAULT_IME = "";
	
	private String mLuaLibPath;
	private String mLuaScPath;
	
	public static void main(String[] args) {
		new UiAutomatorHelper("stcase", "com.cs.CaseMain", "stcase", "1");
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		Bundle params = getParams();
		if (params != null) {
			LUA_SO_PATH = params.getString(Key.LUA_SO_PATH);
			DEFAULT_IME = params.getString(Key.DEFAULT_IME);
			UTF7_IME = params.getString(Key.UTF7_IME);
			
			mLuaScPath = params.getString(Key.LUA_SC_PATH);
			mLuaLibPath = params.getString(Key.LUA_LIB_PATH);
		}
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	/***
	 * 如果程序在底层抛异常crash了，init()会再次被执行，socket会中断，所以应用层发送消息讲无法收到
	 */
	public void init() {
		try {
			ULog.i("method:========init=========");
			LuaManager.getInstance().initLua(mLuaLibPath, mLuaScPath);
			RpcCallManager.getInstance().init();
			
			//start client
			SktClient.getInstance().start(null);
		} catch (Exception e) {
			ULog.i("case/init ex:"+e.getMessage());
			e.printStackTrace();
		} finally {
			ULog.i("============end=============");
		}
	}
	
}
