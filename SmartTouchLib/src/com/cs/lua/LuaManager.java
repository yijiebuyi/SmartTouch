package com.cs.lua;

import java.io.File;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

import com.cs.util.FileUtil;
import com.cs.util.ULog;

public class LuaManager {
	private static LuaManager mInstance;
	
	private LuaState mLuaState;
	private String mLuaLibPath;
	private String mLuaScPath;
	private boolean mStopLua = false;
	
	/**
	 * load the model lib
	 */
	private static final String REQUIRE_PATH = "require 'libs'" + "\n"; 
	
	/**
	 * the first method
	 */
	private static final String INIT_METHOD = "main";
	
	private LuaManager() {
		
	}
	
	public static synchronized LuaManager getInstance() {
		if (mInstance == null) {
			mInstance = new LuaManager();
		}
		
		return mInstance;
	}
	
	public void initLua(String luaLibPath, String luaScPath) {
		try {
			mLuaLibPath = luaLibPath;
			mLuaScPath = luaScPath;
			
			mLuaState = LuaStateFactory.newLuaState();
			mLuaState.openLibs();
			buildLua(luaLibPath, luaScPath);
		} catch (Exception e) {
			ULog.i("init lua ex:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	private void buildLua(String libsPath, String luaScPath) {
		try {
			ULog.i("method:=========LdoFile===="+libsPath);
			int result = mLuaState.LdoFile(libsPath);
			if (result == 0) {
				//create a luaScriptImpl instance
				mLuaState.getField(LuaState.LUA_GLOBALSINDEX, "newObject");
				mLuaState.call(0, 0);

				//load lua lib
				String luaLibDir = FileUtil.getParent(libsPath);
				String luaScDir = FileUtil.getParent(luaScPath);
				LoadLib.load(mLuaState, luaLibDir, luaScDir);
			} else {
				ULog.i("load lua lib is failed");
			}
		} catch (Exception e) {
			ULog.i("load ex:"+e.getMessage());
			e.printStackTrace();
		}
	}
	
	public void execLua(String FilePath) {
		try {
			if (!(new File(FilePath)).exists()) {
				ULog.i("lua file is not exist! path="+FilePath);
				return;
			}
			
			String luaStr = REQUIRE_PATH + FileUtil.readFromFile(FilePath);
			int num = mLuaState.LdoString(luaStr);

			if (num == 0) {
				ULog.i("==========exec start===="+FilePath);
				// 任何函数从main方法开始
				mLuaState.getField(LuaState.LUA_GLOBALSINDEX, INIT_METHOD);
				mLuaState.call(0, 0); 
			} else {
				ULog.i("lua load failed! path="+FilePath);
			}
		} catch (Exception e) {
			ULog.i("lua exec failed! path:"+FilePath);
			e.printStackTrace();
		}
	}

	
	public String getLuaLibPath() {
		return mLuaLibPath;
	}

	public LuaState getLuaState() {
		return mLuaState;
	}

	public String getmuaScPath() {
		return mLuaScPath;
	}

	public boolean isStopLua() {
		return mStopLua;
	}

	public void setStopLua(boolean stopLua) {
		mStopLua = stopLua;
	}
	
	
	
}
