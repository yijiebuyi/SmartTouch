package com.cs.lua;

public interface LuaExecListener {
	public final static int NO_ERRROR = 0;
	public final static int SU_ERRROR = 1 << 0;
	public final static int EXEC_ERRROR = 1 << 1;
	
	public void onExecDone(boolean succ, int code);
}
