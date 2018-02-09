package com.cs.lua;


import com.cs.CaseHelper;
import com.cs.CaseMain;
import com.cs.util.ULog;

public class luaScriptImpl implements luaScript, LuaExecListener {
	private static int EXEC_TIMEOUT = 35;
	
	public luaScriptImpl() {
		
	}
	
	@Override
	public void sleep(int time) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		CaseHelper.sleep(time);
	}

	@Override
	public void home() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.home();
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void back() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.back();
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void menu() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.menu();
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void enter() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.enter();
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void delete() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.delete();
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void keycode(String keyCode,String uc) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		//////////////////////////==============
		CaseHelper.keycode(0);
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void recentApps() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.recentApps();
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void search() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.search();
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void clickname(String name) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.clickByText(name);
		CaseHelper.sleep(EXEC_TIMEOUT);
	}

	@Override
	public void click(int x, int y) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.click(x, y);
	}

	@Override
	public void clickclass(String className) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.clickByClassName(className);
	}

	@Override
	public void drag(int startX, int startY, int endX, int endY, int steps) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.drag(startX, startY, endX, endY, steps);
	}

	@Override
	public void swipe(int startX, int startY, int endX, int endY, int steps) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.swipe(startX, startY, endX, endY, steps);
	}

	@Override
	public void orientationLeft() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.orientationLeft();
	}

	@Override
	public void orientationNatural() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.orientationNatural();
	}

	@Override
	public void orientationRight() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.orientationRight();
	}

	@Override
	public void wakeUp() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.wakeUp();
	}

	@Override
	public void closesleep() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.sleepScreen();
	}

	@Override
	public void takeScreenshot(String path) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.takeScreenshot(path);
	}

	@Override
	public void openNotification() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.openNotification();
	}

	@Override
	public void openQuickSettings() {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		CaseHelper.openQuickSettings();
	}

	@Override
	public void classedittext(String className, String content) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		String defaultIme = CaseMain.DEFAULT_IME;
		String utf7Ime = CaseMain.UTF7_IME;
		CaseHelper.inputContentByClasName(className, content, utf7Ime, defaultIme);
	}

	@Override
	public void textedittext(String text, String content) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		String defaultIme = CaseMain.DEFAULT_IME;
		String utf7Ime = CaseMain.UTF7_IME;
		CaseHelper.inputContentByText(text, content, utf7Ime, defaultIme);
	}

	@Override
	public void residedittext(String resid, String content) {
		if (LuaManager.getInstance().isStopLua()) {
			return;
		}
		
		String defaultIme = CaseMain.DEFAULT_IME;
		String utf7Ime = CaseMain.UTF7_IME;
		CaseHelper.inputContentByResourceId(resid, content, utf7Ime, defaultIme);
	}
	

	@Override
	public int getColor(int x, int y) {
		if (LuaManager.getInstance().isStopLua()) {
			return -1;
		}
		
		int color = CaseHelper.getColor(x, y);
		ULog.i("get color: (" + x + "," + y + ")=" + color);
		return color;
	}
	
	@Override
	public void onExecDone(boolean succ, int code) {
		
		if (!succ) {
			switch (code) {
			case LuaExecListener.SU_ERRROR:
				//failed to su
				
				break;
			case LuaExecListener.EXEC_ERRROR:
				
				break;
			default:
				break;
			}
		}
	}

}
