package com.cs.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.os.Environment;

public class FileUtil {

	public static String getAndroidPath(String floder) {
		String path = Environment.getExternalStorageDirectory().getPath() + floder;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return path;
	}

	public static boolean fileIsExists(String path) {
		boolean isExists = false;
		try {
			File file = new File(path);
			if (file.exists()) {
				return true;
			}
		} catch (Exception e) {
			
		}
		return isExists;
	}

	public static void deleteFiles(String path) {
		try {
			File file = new File(path);
			if (file.exists()) {
				file.delete();
			}
		} catch (Exception e) {
			
		}
	}

	public static String readFromFile(String path) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(path)));
		try {
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			return sb.toString();
		} catch (IOException e) {
			return "";
		} finally {
			br.close();
		}
	}
	
	public static String getParent(String path) {
		char separatorChar = System.getProperty("file.separator", "/").charAt(0);
		int length = path.length(), firstInPath = 0;
		if (separatorChar == '\\' && length > 2 && path.charAt(1) == ':') {
			firstInPath = 2;
		}
		int index = path.lastIndexOf(separatorChar);
		if (index == -1 && firstInPath > 0) {
			index = 2;
		}
		if (index == -1 || path.charAt(length - 1) == separatorChar) {
			return null;
		}
		if (path.indexOf(separatorChar) == index
				&& path.charAt(firstInPath) == separatorChar) {
			return path.substring(0, index + 1);
		}
		return path.substring(0, index);
	}
	
}
