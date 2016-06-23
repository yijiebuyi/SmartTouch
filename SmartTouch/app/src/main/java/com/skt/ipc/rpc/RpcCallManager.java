package com.skt.ipc.rpc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

import android.os.Handler;

import com.skt.ipc.SktServer;
import com.st.util.STConfig;
import com.st.util.ULog;

public class RpcCallManager {
	private static RpcCallManager mInstance;
	private Map<String, Call> mCalls = new ConcurrentHashMap<String, Call>();
	private Handler mHandler;
	private boolean mIsContented = false;
	
	private RpcCallManager() {
		
	}
	
	public static synchronized RpcCallManager getInstance() {
		if (mInstance == null) {
			mInstance = new RpcCallManager();
		}
		
		return mInstance;
	}
	
	public void setHandler(Handler mHandler) {
		this.mHandler = mHandler;
	}

	public Object invoke(String method, String luaPath) throws RpcException {
		CallInfo info = new CallInfo(method, luaPath);
		Call call = new Call();
		mCalls.put(info.getCallId(), call);
		try {
			ULog.d("----------------------server send data----------------------");
			SktServer.getInstance().sendByteData(Utils.pack(info));
		} catch (IOException e) {
			mCalls.remove(info.getCallId());
			throw new RpcException("rpc exception:" + e.getLocalizedMessage());
		}
		
		CallResponse response = call.getResponse();
		mCalls.remove(info.getCallId());
		
		ULog.i("state="+response.getState()+"   res:"+response.getResult());
		switch(response.getState()){
			case CallResponse.EXCEPTION:
				throw new RpcException("rpc call exception");
			case CallResponse.METHOD_NOT_FOUND:
				throw new RpcException("method not found");
			case CallResponse.SUCCESS:
				return response.getResult();
			default:
				return null;
		}
	}
	
	public void listener(InputStream is) throws IOException{
		byte[] byteNum = new byte[4];
		int num = is.read(byteNum);
		if(num < 1) {
            return;
    	} else if (num != 4){
			return;
		}
		
		int len = Utils.bytes2Int(byteNum);
		byte[] content = new byte[len];
		//读取数据区并反序列化
		is.read(content);
        Object obj = Utils.unseralize(content);
        if(obj instanceof CallResponse){
        	CallResponse response = (CallResponse)obj;
        	Call call = mCalls.get(response.getCallId());
        	if (call != null) {
        		//通知获取结果
        		call.setResponse(response);
        		if (mHandler != null) {
    				mHandler.sendEmptyMessage(STConfig.RPC_CALL_RESPONSE);
    			}
        	} else {
        		ULog.i("watch from server");
        		if (!mIsContented) {
        			if (mHandler != null) {
        				mHandler.sendEmptyMessage(STConfig.RPC_CONNECTED);
        			}
        		}
        	}
        	
    		mIsContented = true;
        }
        
	}
	
	private class Call{
		//用阻塞队列来通知
		ArrayBlockingQueue<CallResponse> mQueue = new ArrayBlockingQueue<CallResponse>(1);
		
		public void setResponse(CallResponse response){
			mQueue.add(response);
		}
		
		public CallResponse getResponse(){
			try {
				return mQueue.take();
			} catch (InterruptedException e) {
				return null;
			}
		}
	}
	
}
