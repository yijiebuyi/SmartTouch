package com.st.util;

public class STConfig {
	public static boolean mConnected = false; //是否底层case库连接成功

	public static final String ACTION_INIT = "com.sc.lua.INIT";
	public static final String ACTION_EXEC_LUA = "com.sc.lua.EXEC";
	public static final String ACTION_DELAY_EXEC_LUA = "com.sc.lua.DELAY_EXEC";
	public static final String ACTION_PAUSE_LUA = "com.sc.lua.PAUSE";
	public static final String ACTION_STOP_LUA = "com.sc.lua.STOP";
	
	public static final String LUA_PATH = "lua-path";
	
	public static final int RPC_CONNECTED = 1 << 0;
	public static final int RPC_CALL_RESPONSE = 1 << 1;
	public static final int RPC_WATCHER = 1 << 2;

	public static final String LIB_PKG_NAME = "com.sc";
}
