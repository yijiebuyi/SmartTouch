package com.st;

import com.st.util.ULog;

public class DataManager {

    private static DataManager mInstance;
    private ScriptLibConnectListener mScriptLibConnectListener;

    private DataManager() {

    }

    public static synchronized DataManager getmInstance() {
        if (mInstance == null) {
            mInstance = new DataManager();
        }

        return mInstance;
    }

    public void registScriptLibConnectListener(ScriptLibConnectListener listener) {
        mScriptLibConnectListener = listener;
    }

    public void unregistScriptLibConnectListener() {
        mScriptLibConnectListener = null;
    }

    public void notifyScriptLibConnectChanged(boolean succ) {
        if (mScriptLibConnectListener != null) {
            mScriptLibConnectListener.onConnectChanged(succ);
        }
    }

    public interface ScriptLibConnectListener {
        public void onConnectChanged(boolean succ);
    }
}
