package com.skt.ipc.rpc;

import java.lang.reflect.Method;

/**
 * 定义RPC可调用的方法
 *
 */
public class CallDefine {
	
	//实例
	private Object mInstnce;
	
	//方法名
	private Method mMethod;
	
	public CallDefine() {
	}
	
	
	/**
	 * @param obj
	 * @param method
	 */
	public CallDefine(Object obj, Method method) {
		mInstnce = obj;
		mMethod = method;
	}

	public Object invoke(String args) throws Exception{
		return mMethod.invoke(mInstnce, args);
	}

	/**
	 * @return the obj
	 */
	public Object getObj() {
		return mInstnce;
	}

	/**
	 * @param obj the obj to set
	 */
	public void setObj(Object obj) {
		this.mInstnce = obj;
	}

	/**
	 * @return the method
	 */
	public Method getMethod() {
		return mMethod;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(Method method) {
		this.mMethod = method;
	}
	
}
