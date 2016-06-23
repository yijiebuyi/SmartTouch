package com.skt.ipc.rpc;

import java.io.Serializable;
import java.util.UUID;

/**
 * RPC调用描述信息
 *
 */
public class CallInfo implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String mCallId = UUID.randomUUID().toString();

	private String mMethod;
	
	private String mLuaPath;
	
	private Object[] mParameters;
	
	public CallInfo() {
	}

	/**
	 * @param method name
	 * @param parameters
	 */
	public CallInfo(String method, Object[] parameters, String luaPath) {
		mMethod = method;
		mParameters = parameters;
		mLuaPath = luaPath;
	}

	
	public CallInfo(String method, String luaPath) {
		mMethod = method;
		mLuaPath = luaPath;
	}

	/**
	 * @return the method name
	 */
	public String getMethod() {
		return mMethod;
	}

	/**
	 * @param to set the method name
	 */
	public void setMethod(String method) {
		this.mMethod = method;
	}

	/**
	 * @return the parameters
	 */
	public Object[] getParameters() {
		return mParameters;
	}

	/**
	 * @param parameters the parameters to set
	 */
	public void setParameters(Serializable[] parameters) {
		this.mParameters = parameters;
	}
	
	public String getCallId() {
		return mCallId;
	}
	
	
	public String getLuaPath() {
		return mLuaPath;
	}

	public void setLuaPath(String path) {
		mLuaPath = path;
	}

	@Override
	public String toString() {
		return "callId="+mCallId+",name="+mMethod;
	}
}
