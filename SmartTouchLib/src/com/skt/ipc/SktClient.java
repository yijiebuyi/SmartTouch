package com.skt.ipc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

import com.cs.lua.LuaManager;
import com.cs.util.ULog;
import com.skt.ipc.rpc.CallResponse;
import com.skt.ipc.rpc.RpcCallManager;
import com.skt.ipc.rpc.Utils;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.util.Log;

public class SktClient {
	private static final int WATCH_TIME = 2000; 
	private static final String WATCH_MSG = "watch";
	
	private static final String TAG = "scaselib";
	private static final String SOCKET_PATH = "smart_touch_skt";
    private volatile InputStream mClientInputStream;
    private volatile OutputStream mClientOutputStream;
    private volatile LocalSocket mClientSocket;
    private boolean mServerRunning = false;
    
    private static SktClient mIntance;
    CallResponse mWather;
    
    private SktClient() {
    	mWather = new CallResponse();
    }

	public static synchronized SktClient getInstance() {
    	if (mIntance == null) {
    		mIntance = new SktClient();
    	}
    	
    	return mIntance;
    }
	    
    public void start(final OnDataReceiveLisener listener) {
    	 try {
             mClientSocket = new LocalSocket();
             LocalSocketAddress address = new LocalSocketAddress(SOCKET_PATH);
             mClientSocket.connect(address);
            
             mClientInputStream = mClientSocket.getInputStream();
             mClientOutputStream = mClientSocket.getOutputStream();
             
             mServerRunning = true;
             startWatch();
             
             ULog.i("client is running..." + mServerRunning);
             for (;;) {
            	 if (!mServerRunning) {
            		 break;
            	 }
            	 
            	 RpcCallManager.getInstance().listener(mClientInputStream);
             }
         } catch (IOException e) {
         	Log.i(TAG, "io :"+e.getMessage());
         	mServerRunning = false;
         	LuaManager.getInstance().setStopLua(true);
            disconnect();
         }
    }
    
    private void disconnect() {
        try {
            if (mClientSocket != null) {
                mClientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mClientSocket = null;
        }
        
        try {
            if (mClientInputStream != null) {
                mClientInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mClientInputStream = null;
        }
        
        try {
            if (mClientOutputStream != null) {
                mClientOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mClientOutputStream = null;
        }
    }
    
    /*
     * client send message to server
     */
    public synchronized void sendData(byte[] data) {
        if (mClientOutputStream != null) {
            try {
                mClientOutputStream.write(data);
                mClientOutputStream.flush();
            } catch (Exception e) {
            	Log.i(TAG, "send data ex:"+e.getMessage());
            	mServerRunning = false;
                e.printStackTrace();
            }
        } else {
        	Log.i(TAG, "the output stream of client is null .");
        }
    }
    
    public void startWatch() {
		HandlerThread ht = new HandlerThread("client_watch");
		ht.start();
		
		Handler hander = new Handler(ht.getLooper()) {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				//send data
				mWather.setCallId(UUID.randomUUID().toString());
				mWather.setResult(WATCH_MSG);
				mWather.setState(CallResponse.SUCCESS);
	            sendData(Utils.pack(mWather));
	            
	            //send a message after 2s
				sendEmptyMessageDelayed(0, WATCH_TIME);
			}
			
		};
		
		hander.sendEmptyMessageDelayed(0, WATCH_TIME);
	}
    
    public interface OnDataReceiveLisener {
    	public void onReceive(String cmd);
    }
}
