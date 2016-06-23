package com.skt.ipc.rpc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;

public class Utils {
	
	/**
	 * 返回clazz中根据methodName找到的第一个Method,不要应用于存在方法重载的类中
	 * @param clazz
	 * @param methodName
	 * @return
	 */
	public static Method getMethod(Class<?> clazz,String methodName){
		if(clazz == null){
			throw new NullPointerException();
		}
		
		Method[] methods = clazz.getMethods();
		for(Method method : methods){
			if(method.getName().equals(methodName)){
				return method;
			}
		}
		
		return null;
	}
	
	/**
	 * 获取可序列化对象的byte数据
	 * @param serial
	 * @return
	 */
	public static byte[] seralize(Object serial){
		ByteArrayOutputStream data = new ByteArrayOutputStream();
		ObjectOutputStream out = null;
		try {
			out = new ObjectOutputStream(data);
			out.writeObject(serial);
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out!=null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return data.toByteArray();
	}
	
	/**
	 * 反序列化
	 * @param data
	 * @return
	 * @throws ClassNotFoundException
	 */
	public static Object unseralize(byte[] data){
		ObjectInputStream in = null;
		try {
			in = new ObjectInputStream(new ByteArrayInputStream(data));
			return in.readObject();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
			if(in!=null){
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return null;
	}
	
	/**
	 * int换byte数组
	 * @param num
	 * @return
	 */
	public static byte[] int2Bytes(int num) {  
        byte[] byteNum = new byte[4];  
        for (int ix = 0; ix < 4; ++ix) {  
            int offset = 32 - (ix + 1) * 8;  
            byteNum[ix] = (byte) ((num >> offset) & 0xff);  
        }  
        return byteNum;  
    }  
  
	/**
	 * byte数组转int
	 * @param byteNum
	 * @return
	 */
    public static int bytes2Int(byte[] byteNum) {  
        int num = 0;  
        for (int ix = 0; ix < 4; ++ix) {  
            num <<= 8;  
            num |= (byteNum[ix] & 0xff);  
        }  
        return num;  
    }
    
    /**
     * 把序列化并组装,前4byte为数据区长度,后面为序列化的byte数据
     * @param obj
     * @return
     */
    public static byte[] pack(Object obj){
    	byte[] body = seralize(obj);
    	byte[] head = int2Bytes(body.length);
    	byte[] data = new byte[body.length+head.length];
    	System.arraycopy(head, 0, data, 0, head.length);
    	System.arraycopy(body, 0, data, head.length, body.length);
    	
    	return data;
    }
    
}
