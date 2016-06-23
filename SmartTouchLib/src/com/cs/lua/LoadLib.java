package com.cs.lua;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;

import com.cs.util.ULog;

public class LoadLib {
	
	public static final void load(LuaState L, final String lualibPath, String luaPath) {
		try {
			//L.pushJavaObject(context);
			L.setGlobal("script");
			//registerMethod(L);
			JavaFunction assetLoader = new JavaFunction(L) {
				@Override
				public int execute() throws LuaException {
					return 1;
				}
			};

			L.getGlobal("package"); // package
			L.getField(-1, "loaders"); // package loaders
			int nLoaders = L.objLen(-1); // package loaders

			L.pushJavaFunction(assetLoader);
			// package loaders loader
			L.rawSetI(-2, nLoaders + 1); // package loaders
			L.pop(1); // package

			L.getField(-1, "path"); // package path
			String libPath = lualibPath + "/?.lua";
			L.pushString(";" + libPath + ";" + luaPath + "/?.lua"); // package path custom
			L.concat(2); // package pathCustom
			L.setField(-2, "path"); // package
			L.pop(1);
		} catch (LuaException e) {
			ULog.i("LuaException:"+e.getLocalizedMessage());
			e.printStackTrace();
		}
	}
	
	private static byte[] readAll(InputStream input) throws Exception {
		ByteArrayOutputStream output = new ByteArrayOutputStream(4096);
		byte[] buffer = new byte[4096];
		int n = 0;
		while (-1 != (n = input.read(buffer))) {
			output.write(buffer, 0, n);
		}
		return output.toByteArray();
	}
	
	public static String evalLua(LuaState L, String src) throws LuaException {
		L.setTop(0);
		int ok = L.LloadString(src);
		if (ok == 0) {
			L.getGlobal("debug");
			L.getField(-1, "traceback");
			L.remove(-2);
			L.insert(-2);
			ok = L.pcall(0, 0, -2);
			if (ok == 0) {				
				return "success";
			}
		}
		throw new LuaException(errorReason(ok) + ": " + L.toString(-1));
		//return null;		
		
	}
	private static String errorReason(int error) {
		switch (error) {
		case 4:
			return "Out of memory";
		case 3:
			return "Syntax error";
		case 2:
			return "Runtime error";
		case 1:
			return "Yield error";
		}
		return "Unknown error " + error;
	}
}
