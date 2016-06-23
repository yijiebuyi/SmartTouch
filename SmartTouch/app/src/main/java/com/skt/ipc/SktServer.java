package com.skt.ipc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.net.LocalServerSocket;
import android.net.LocalSocket;

import com.skt.ipc.rpc.RpcCallManager;
import com.st.util.ULog;

public class SktServer {
	private static final String SOCKET_PATH = "smart_touch_skt";
	
	private volatile LocalServerSocket mLocalServerSocket;
	private volatile InputStream mServerInputStream;
	private volatile OutputStream mServerOutputStream;
	    
    private static SktServer mInstance;
    private boolean mIsRunning = false;
    
    private SktServer() {
    	
    }
    
    public synchronized static SktServer getInstance() {
    	if (mInstance == null) {
    		mInstance = new SktServer();
    	}
    	
    	return mInstance;
    }
    
	public void start() {
		ULog.i("server --------------start--");
		if (!mIsRunning) {
			new ServerThread().start();
		}
	}
	    
	//server******************SocketLocalService****************
    private class ServerThread extends Thread {

        @Override
        public void run() {
            try {
            	ULog.i("server run----------------");
                mLocalServerSocket = new LocalServerSocket(SOCKET_PATH);
                mIsRunning = true;
                LocalSocket socket = mLocalServerSocket.accept();
                if (socket == null) {
                	ULog.i("local socket connted local socket service failed!!!!!!!");
                } else {
                	ULog.i("local socket connted local socket service successful!");
                    mServerInputStream = socket.getInputStream();
                    mServerOutputStream = socket.getOutputStream();
                    for (;;) {
                        RpcCallManager.getInstance().listener(mServerInputStream);
                    }
                }
            } catch (IOException e) {
            	ULog.i("server io ex:"+ e.getMessage());
                e.printStackTrace();
                closeLockSocketService();
            }
        }
    }
    
    /*
     * server send message to client
     */
    public void sendByteData(byte[] data) throws IOException{
        if (mServerOutputStream != null) {
            mServerOutputStream.write(data);
            mServerOutputStream.flush();
        }
    }
    
    private synchronized void closeLockSocketService() {
        //close server socket
    	try {
            if (mLocalServerSocket != null) {
                mLocalServerSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mLocalServerSocket = null;
        }
        
        //close input stream
        try {
            if (mServerInputStream != null) {
                mServerInputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mServerInputStream = null;
        }
        
        //close output stream
        try {
            if (mServerOutputStream != null) {
                mServerOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mServerOutputStream = null;
        }
        
        mIsRunning = false;
    }
    
    public void recyle() {
    	closeLockSocketService();
    }
    
}
