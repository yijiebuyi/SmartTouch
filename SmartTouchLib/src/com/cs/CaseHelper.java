package com.cs;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.RemoteException;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Surface;

import com.android.uiautomator.core.UiDevice;
import com.android.uiautomator.core.UiObject;
import com.android.uiautomator.core.UiObjectNotFoundException;
import com.android.uiautomator.core.UiSelector;
import com.cs.ucode.Utf7ImeHelper;
import com.cs.util.ULog;

public class CaseHelper {
	
	public static void sleep(long time) {
		ULog.i("method:============sleep");
		SystemClock.sleep(time);
	}

	public static void home() {
		ULog.i("method:============home");
		UiDevice.getInstance().pressHome();
	}

	public static void back() {
		ULog.i("method:============back");
		UiDevice.getInstance().pressBack();
	}

	public static void menu() {
		ULog.i("method:============menu");
		UiDevice.getInstance().pressMenu();
	}

	// 模拟短按回车键
	public static void enter() {
		ULog.i("method:============enter");
		UiDevice.getInstance().pressEnter();
	}

	// 模拟短按删除delete按键
	public static void delete() {
		ULog.i("method:============delete");
		UiDevice.getInstance().pressDelete();
	}

	// to simulate kinds of keycode
	public static void keycode(int keyCode) {
		ULog.i("method:============keycode");
		UiDevice.getInstance().pressKeyCode(keyCode);
	}

	// open the recent apps
	public static void recentApps() {
		ULog.i("method:============recentApps");
		try {
			UiDevice.getInstance().pressRecentApps();
		} catch (RemoteException e) {
			ULog.i("open recentApps ex:" + e.getMessage());
		}
	}

	// 模拟短按搜索键
	public static void search() {
		ULog.i("method:============search");
		UiDevice.getInstance().pressSearch();
	}

	public static void clickByText(String text) {
		ULog.i("method:============clickByname");
		try {
			new UiObject(new UiSelector().text(text)).click();
		} catch (Exception e) {
			ULog.i("click by name ex:" + e.getMessage());
		}
	}

	public static void click(int x, int y) {
		ULog.i("method:============clickxy");
		try {
			ULog.i("click:  x=" + x + "   y=" + y);
			UiDevice.getInstance().click(x, y);
		} catch (Exception e) {
			ULog.i("click ex:" + e.getMessage());
		}
	}

	// 根据屏幕的class来点击
	public static void clickByClassName(String className) {
		ULog.i("method:============clickByClassName");
		try {
			new UiObject(new UiSelector().className(className)).click();
		} catch (Exception e) {
			ULog.i("click by class name ex:" + e.getMessage());
		}
	}

	// 根据view的ResId来点击
	public static void clickByResourceId(String resId) {
		ULog.i("method:============clickByResId");
		try {
			new UiObject(new UiSelector().resourceId(resId)).click();
		} catch (Exception e) {
			ULog.i("click by Res id ex:" + e.getMessage());
		}
	}

	/**
	 * @return true if swipe is performed, false if the operation fails or the
	 *         coordinates are invalid
	 * @since API Level 18
	 */
	public static void drag(int startX, int startY, int endX, int endY, int steps) {
		ULog.i("method:============drag");
		try {
			ULog.i("startx:" + startX + " starty" + startY + " endX:" + endX
					+ " endY" + endY + " steps:" + steps);
			UiDevice.getInstance().drag(startX, startY, endX, endY, steps);
		} catch (Exception e) {
			ULog.i("drag ex:" + e.getMessage());
		}

	}

	// 通过坐标滑动屏幕 (横向，纵向)
	public static void swipe(int startX, int startY, int endX, int endY, int steps) {
		ULog.i("method:============swipe");
		try {
			ULog.i("startx:" + startX + " starty" + startY + " endX:" + endX
					+ " endY" + endY + " steps:" + steps);
			UiDevice.getInstance().swipe(startX, startY, endX, endY, steps);
		} catch (Exception e) {
			ULog.i("swipe ex:" + e.getMessage());
		}
	}

	public static void orientationLeft() {
		ULog.i("method:============orientationLeft");
		try {
			UiDevice.getInstance().setOrientationLeft();
		} catch (Exception e) {
			ULog.i("orientationLeft ex:" + e.getMessage());
		}
	}

	// 通过禁用传感器,然后模拟设备转到其自然默认的 方向,并且固定位置
	public static void orientationNatural() {
		ULog.i("method:============orientationNatural");
		try {
			UiDevice.getInstance().setOrientationNatural();
		} catch (Exception e) {
			ULog.i("orientationNatural ex:" + e.getMessage());
		}
	}

	// 通过禁用传感器,然后模拟设备向右转,并且固定 位置
	public static void orientationRight() {
		ULog.i("method:============orientationRight");
		try {
			UiDevice.getInstance().setOrientationRight();
		} catch (Exception e) {
			ULog.i("orientationRight ex:" + e.getMessage());
		}
	}

	// 模拟按电源键,如果屏幕是唤醒的没有任何作用  
	public static void wakeUp() {
		ULog.i("method:============wakeUp");
		try {
			UiDevice.getInstance().wakeUp();
		} catch (Exception e) {
			ULog.i("wakeUp ex:" + e.getMessage());
		}
	}

	// 模拟按电源键,如果屏幕已经是关闭的则没有任何作用
	public static void sleepScreen() {
		ULog.i("method:============sleepScreen");
		try {
			UiDevice.getInstance().sleep();
		} catch (Exception e) {
			ULog.i("sleepScreen ex:" + e.getMessage());
		}
	}

	/**
     * Take a screenshot of current window and store it as PNG
     *
     * Default scale of 1.0f (original size) and 90% quality is used
     * The screenshot is adjusted per screen rotation
     *
     * @param storePath where the PNG should be written to
     * @return true if screen shot is created successfully, false otherwise
     * @since API Level 17
     */
	public static void takeScreenshot(String filePath) {
		ULog.i("method:============takeScreenshot");
		try {
			ULog.i("takeScreenshot filePath=" + filePath);
			UiDevice.getInstance().takeScreenshot(new File(filePath));
		} catch (Exception e) {
			ULog.i("takeScreenshot ex:"  + e.getMessage());
		}
	}

	// 打开通知栏
	 /**
     * Opens the notification shade.
     *
     * @return true if successful, else return false
     * @since API Level 18
     */
	public static void openNotification() {
		ULog.i("method:============openNotification");
		try {
			UiDevice.getInstance().openNotification();
		} catch (Exception e) {
			ULog.i("openNotification ex:" + e.getMessage());
		}
	}

	// 打开快速设置
	 /**
     * Opens the Quick Settings shade.
     *
     * @return true if successful, else return false
     * @since API Level 18
     */
	public static void openQuickSettings() {
		ULog.i("method:============openQuickSettings");
		try {
			UiDevice.getInstance().openQuickSettings();
		} catch (Exception e) {
			ULog.i("openQuickSettings ex=" + e.getMessage());
		}
	}

	
   /**
    * Returns the current rotation of the display, as defined in {@link Surface}
    * the value is scope in[0, 1, 2, 3]
    * 0:0
    * 1:90
    * 2:180
    * 3:270
    * @since API Level 17
   */
	public static int getDisplayRotation() {
		int sc = 0;
		ULog.i("method:============getDisplayRotation");
		try {
			sc = UiDevice.getInstance().getDisplayRotation();
		} catch (Exception e) {
			ULog.i("getDisplayRotation ex:" + e.getMessage());
		}
		return sc;
	}

	public static boolean serchByTextName(String text) throws UiObjectNotFoundException, IOException {
		ULog.i("method:============serchByTextName");
		UiObject l = new UiObject(new UiSelector().text(text));
		return l.exists();
	}

	public static void inputContentByClasName(String className, String text, String utf7Ime, String defaultIme) {
		ULog.i("method:============inputTextByClasName");
		try {
			Process p = Runtime.getRuntime().exec("settings put secure default_input_method " + utf7Ime);
			p.waitFor();
			
			UiObject ui = new UiObject(new UiSelector().className(className));
			ui.setText(Utf7ImeHelper.e(text));

		} catch (Exception e) {
			ULog.i("inputTextByClasName ex:" + e.getMessage());
		} finally {
			try {
				if (!TextUtils.isEmpty(defaultIme)) {
					Process p1 = Runtime.getRuntime().exec("settings put secure default_input_method " + defaultIme);
					try {
						p1.waitFor();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void inputContentByText(String text, String content, String utf7Ime, String defaultIme) {
		ULog.i("method:============inputTextByName");
		try {
			Process p = Runtime.getRuntime().exec("settings put secure default_input_method " + utf7Ime);
			p.waitFor();
			
			UiObject ui = new UiObject(new UiSelector().textMatches(text));
			ui.setText(Utf7ImeHelper.e(content));
		} catch (Exception e) {
			ULog.i("inputTextByName ex:" + e.getMessage());
		} finally {
			try {
				if (!TextUtils.isEmpty(defaultIme)) {
					Process p1 = Runtime.getRuntime().exec("settings put secure default_input_method " + defaultIme);
					try {
						p1.waitFor();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public static void inputContentByResourceId(String resId, String text, String utf7Ime, String defaultIme) {
		ULog.i("method:============inputTextByResId");
		try {
			Process p = Runtime.getRuntime().exec("settings put secure default_input_method " + utf7Ime);
			p.waitFor();

			UiObject ui = new UiObject(new UiSelector().resourceId(resId));
			ui.setText(Utf7ImeHelper.e(text));
		} catch (Exception e) {
			ULog.i("inputTextByResId ex:" + e.getMessage());
		} finally {
			try {
				if (!TextUtils.isEmpty(defaultIme)) {
					Process p1 = Runtime.getRuntime().exec("settings put secure default_input_method " + defaultIme);
					try {
						p1.waitFor();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static int getColor(int x, int y) {
		ULog.i("method:============getColor");
		
		int color = 0;
		Bitmap bmp = takeScreenshot();
		if (bmp == null) {
			return -1;
		}
		
		if(x >= bmp.getWidth() || y >= bmp.getHeight()) {
			ULog.i("x, y is out of bounds");
			return -1;
		}
		
		color = bmp.getPixel(x, y);
		bmp.recycle();
		
		int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);
		ULog.i("color="+color + "  r="+r+"  g="+g+"   b="+b);
		return color;
	}
	
	public static int[] getColor(int x, int y, int w, int h) {
		ULog.i("method:============getColor");
		int pixels[] = new int[w * h];
		Bitmap bmp = takeScreenshot();
		if (bmp == null) {
			return null;
		}
		
		if(x >= bmp.getWidth() || y >= bmp.getHeight()) {
			ULog.i("x, y is out of bounds");
			return null;
		}
		
		Point size = adjustScreenshotSize(x, y, w, h, bmp);
		bmp.getPixels(pixels, 0, bmp.getWidth(), x, y, size.x, size.y);
		bmp.recycle();
		
		return pixels;
	}
	
	private static Point adjustScreenshotSize(int x, int y, int w, int h, Bitmap bmp) {
		Point p = new Point(w, h);
		if (x + w > bmp.getWidth()) {
			w = bmp.getWidth() - x;
		}
		
		if (y + h > bmp.getHeight()) {
			h = bmp.getHeight() - y;
		}
		p.set(w, h);
		
		return p;
	}
	
	/**
	 * Take a screenshot of current window and store it as PNG
	 *
	 * @return bitmap if screen shot is created successfully, null otherwise
	 * @since API Level 17
	 */
	private static Bitmap takeScreenshot() {
		if (Build.VERSION.SDK_INT < 17) {
			ULog.i("sdk must be >= 17 for takeScreenshot.");
			return null;
		}
		
		long s = System.currentTimeMillis();
		ULog.i("method:============takeScreenshot");
		try {
			Class<?> uiDeviceCls = UiDevice.getInstance().getClass();
			Method method = uiDeviceCls.getDeclaredMethod("getAutomatorBridge");
			method.setAccessible(true);
			Object uiAtuomatorBridge = method.invoke(UiDevice.getInstance());
			if (uiAtuomatorBridge != null) {
				Class<?> uiAutomatorBridgeCls = uiAtuomatorBridge.getClass().getSuperclass();
				Field field = uiAutomatorBridgeCls.getDeclaredField("mUiAutomation");
				field.setAccessible(true);
				Object uiAutomation = field.get(uiAtuomatorBridge);
				if (uiAutomation != null) {
					Class<?> UiAutomationCls = uiAutomation.getClass();
					Method takeScreenshot =UiAutomationCls.getDeclaredMethod("takeScreenshot");
					takeScreenshot.setAccessible(true);
					Object obj = takeScreenshot.invoke(uiAutomation);
					if (obj instanceof Bitmap) {
						Bitmap bmp = (Bitmap)obj;
						return bmp;
					}
				}
			}
		} catch (NoSuchFieldException e) {
			ULog.i("NoSuchFieldException:"+e.getMessage());
			e.printStackTrace();
		} catch (SecurityException e) {
			ULog.i("SecurityException:"+e.getMessage());
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			ULog.i("NoSuchMethodException:"+e.getMessage());
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			ULog.i("IllegalAccessException:"+e.getMessage());
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			ULog.i("IllegalArgumentException:"+e.getMessage());
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			ULog.i("InvocationTargetException:"+e.getMessage());
			e.printStackTrace();
		}
		ULog.i("take screenshot: takes="+ (System.currentTimeMillis() - s));
		return null;
	}
}
