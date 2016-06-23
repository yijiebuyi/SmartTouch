package com.st.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Set;

import android.os.Bundle;
import android.text.TextUtils;

public class CaseUtil {
	private final static String SPACE = " ";
	private final static String UIAUTOMATOR_CMD = "uiautomator runtest";
	private final static String TEST_CLASS = "com.cs.CaseMain";
	private final static String EXEC_STATUS_CODE = "INSTRUMENTATION_STATUS_CODE:";
	
	public final static String JAR = "stcase.jar";
	public final static String NO_HUP = "--nohup";
	
	/*
	uiautomator runtest <jars> <-c <CLASS#method>> [-e <NAME> <VALUE>]

	runtest <class spec> [options]
 	<class spec>: <JARS> < -c <CLASSES> | -e class <CLASSES> >
   	<JARS>: a list of jar files containing test classes and dependencies. If
     	the path is relative, it's assumed to be under /data/local/tmp. Use
     	absolute path if the file is elsewhere. Multiple files can be
     	specified, separated by space.
   		<CLASSES>: a list of test class names to run, separated by comma. To
     	a single method, use TestClass#testMethod format. The -e or -c option
     	may be repeated. This option is not required and if not provided then
     	all the tests in provided jars will be run automatically.
 	options:
   		--nohup: trap SIG_HUP, so test won't terminate even if parent process
            is terminated, e.g. USB is disconnected.
   		-e debug [true|false]: wait for debugger to connect before starting.
   		-e runner [CLASS]: use specified test runner class instead. If
     		unspecified, framework default runner will be used.
   		-e <NAME> <VALUE>: other name-value pairs to be passed to test classes.
     		May be repeated.
   		-e outputFormat simple | -s: enabled less verbose JUnit style output.
	 */
	
	public synchronized static void exec(String jarPath, String method, Bundle bundle) {
		String cmd = UIAUTOMATOR_CMD + SPACE + jarPath + buildTestClassMethod(method, bundle)/* + SPACE + NO_HUP*/;
		ULog.i(cmd);
		
		try {
			Shell.sudoCache(cmd);
		} catch (Shell.ShellException e) {
			e.printStackTrace();
			ULog.i("failed to su" +  e != null ? e.getMessage() : "null msg");
		}
	}

	
	public static boolean isExecSucc(Process pc) {
		InputStream ios = pc.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(ios));
		String s;
		int code = 0;
		try {
			int start = EXEC_STATUS_CODE.length();
			while ((s = br.readLine()) != null) {
				if (s.startsWith(EXEC_STATUS_CODE)) {
					String s_c = s.substring(start);
					s_c = s_c.trim();
					code = Integer.parseInt(s_c);
				}
				ULog.i(s);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} catch (NumberFormatException e) {
			return false;
		} finally {
			try {
				br.close();
				ios.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return code == 0;
	}
	
	private static String buildTestClassMethod(String method, Bundle bundle) {
		String testMethod = buildSingleMethod(method);
		String testParams = buildTestParams(method, bundle);
		if (TextUtils.isEmpty(testMethod)) {
			return "";
		}
		
		if (TextUtils.isEmpty(testParams)) {
			return SPACE + testMethod;
		} else {
			return SPACE + testMethod + SPACE + testParams;
		}
	}
	
	/**
  	 *	to a single method, use TestClass#testMethod format.
  	 *  The -c option may be repeated. This option is not required and if not provided then
 	 *	all the tests in provided jars will be run automatically.
	 * @param method
	 * @return
	 */
	private static String buildSingleMethod(String method) {
		if (TextUtils.isEmpty(method)) {
			return "-c" + SPACE + TEST_CLASS;
		}
		
		return "-c" + SPACE + TEST_CLASS + "#" + method;
	}
	
	/**
	 *  -e <NAME> <VALUE>: other name-value pairs to be passed to test classes.	May be repeated.
	 * @param method
	 * @return
	 */
	private static String buildTestParams(String method, Bundle bundle) {
		if (bundle == null) {
			return null;
		}
		
		Set<String> keys = bundle.keySet();
		if (keys == null) {
			return null;
		}
		
		StringBuffer sb = new StringBuffer();
		for (String key : keys) {
			String val = getVal(key, bundle);
			fillParams(key, val, sb);
		}
		
		if (sb.length() > 0) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}
	
	private static void fillParams(String key, String val, StringBuffer sb) {
		sb.append(SPACE);
		sb.append("-e");
		sb.append(SPACE);
		sb.append(key);
		sb.append(SPACE);
		sb.append(val);
	}
	
	/*private static String AlphabetCovert(String val) {
			int c = val.charAt(0);
			
			int delta = 0;
			if (c < 97) {
				delta = 'a' - KeyEvent.KEYCODE_A;
			} else {
				delta = 'A' - KeyEvent.KEYCODE_A;
			}
			
			int ch = c - delta;
			return String.valueOf((char) ch);
	}*/
	
	public static boolean isTextInput(String method) {
		return "classedittext".equals(method) || "textedittext".equals(method) || "residedittext".equals(method);
	}
	
	private static String getVal(String key, Bundle bundle) {
		Object val = bundle.get(key);
		Class<?> calssType = val.getClass();
		if (calssType == String.class) {
			return (String)val;
        } else if (calssType == Boolean.class) {
        	return String.valueOf(val);
        } else if (calssType == Integer.class) {
        	return String.valueOf(val);
        } else if (calssType == Long.class) {
        	return String.valueOf(val);
        } else if (calssType == Float.class) {
        	return String.valueOf(val);
        } else {
            throw new IllegalArgumentException("Unsupported argument type for sharedPreferences: " + calssType);
        }
	}
}
