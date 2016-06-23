package com.skt.ipc.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.cs.util.ULog;
import com.skt.ipc.SktClient;

public class RpcCallManager {
	
	private static RpcCallManager mInstance;
	private Map<String, CallDefine> mCalls = new ConcurrentHashMap<String, CallDefine>();
	
	private RpcCallManager() {
		
	}
	
	public static synchronized RpcCallManager getInstance() {
		if (mInstance == null) {
			mInstance = new RpcCallManager();
		}
		
		return mInstance;
	}
	
	public void init () {
		RpcCall obj = new RpcCall();
		addCall(RpcConfig.METHOD_EXEC_LUA, obj);
		addCall(RpcConfig.METHOD_STOP_LUA, obj);
		addCall(RpcConfig.METHOD_PAUSE_LUA, obj);
	}
   
	private void addCall(String method, RpcCall obj) {
		mCalls.put(method, new CallDefine(obj, Utils.getMethod(RpcCall.class, method)));
	}
	
    public CallResponse invoke(CallInfo info){
		CallResponse response = new CallResponse();
		response.setCallId(info.getCallId());
		
		//method not found
		if (info.getMethod() == null) {
			response.setState(CallResponse.METHOD_NOT_FOUND);
			return response;
		}
		
		CallDefine callDefine = mCalls.get(info.getMethod());
		if (callDefine==null) {
			response.setState(CallResponse.METHOD_NOT_FOUND);
		} else{
			try {
				//call the method
				Object rs = callDefine.invoke(info.getLuaPath());
				response.setResult(rs);
				response.setState(CallResponse.SUCCESS);
			} catch (Exception e) {
				ULog.i("call ex:"+e.getMessage());
				response.setState(CallResponse.EXCEPTION);
			}
		}
		return response;
	  }
	  
	  public void listener(InputStream is) throws IOException{
			byte[] byteNum = new byte[4];
			int num = is.read(byteNum);
			if(num < 1) {
                return;
        	} else if (num != 4){
				return;
			}
			
			//得到数据区的长度，这里简单处理直接读取全部数据区.
			int len = Utils.bytes2Int(byteNum);
			byte[] content = new byte[len];
			
			//读取数据区并反序列化
			is.read(content);
            Object info = Utils.unseralize(content);
            if(info instanceof CallInfo){
            	CallResponse reInfo = invoke((CallInfo)info);
            	byte[] rs = Utils.pack(reInfo);
            	//把数据写回客户端完成本次调用
            	SktClient.getInstance().sendData(rs);
            }
		}
}
