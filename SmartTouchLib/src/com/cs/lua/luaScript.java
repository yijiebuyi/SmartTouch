package com.cs.lua;

public interface luaScript {
	/**
	 * 睡眠
	 * 
	 * @param time
	 *            时间
	 */
	public void sleep(int time);

	/**
	 * 点击home键
	 */
	public void home();

	/**
	 * 返回键
	 */
	public void back();

	/**
	 * 菜单键
	 */
	public void menu();

	/**
	 * 确定键
	 */
	public void enter();

	/**
	 * 删除键
	 */
	public void delete();

	/**
	 * 模拟按键
	 */
	public void keycode(String keyCode, String uc);

	/**
	 * 模拟短按最近使用程序
	 */
	public void recentApps();

	/**
	 * 模拟短按搜索键
	 */
	public void search();

	/**
	 * 根据名称点击
	 * 
	 * @param name
	 */
	public void clickname(String name);

	/**
	 * 根据xy的坐标点击屏幕
	 */
	public void click(int x, int y);

	/**
	 * 根据屏幕的class来点击
	 * 
	 * @param name
	 */
	public void clickclass(String name);

	/**
	 * 拖动对象从一个坐标拖动到另 一个坐标
	 * @param startx
	 * @param starty
	 * @param endx
	 * @param endy
	 * @param type
	 */
	public void drag(int startx, int starty, int endx, int endy, int steps);

	/**
	 * 通过坐标滑动屏幕 (横向，纵向)
	 */
	public void swipe(int startx, int starty, int endx, int endy, int steps);

	/**
	 * 通过禁用传感器,然后模拟设备向左转,并且固定 位置
	 */
	public void orientationLeft();

	/**
	 * 通过禁用传感器,然后模拟设备转到其自然默认的 方向,并且固定位置
	 */
	public void orientationNatural();

	/**
	 * 通过禁用传感器,然后模拟设备向右转,并且固定 位置
	 */
	public void orientationRight();

	/**
	 * 模拟按电源键,如果屏幕是唤醒的没有任何作用
	 */
	public void wakeUp();

	/**
	 * 模拟按电源键,如果屏幕已经是关闭的则没有任何作用
	 */
	public void closesleep();

	/**
	 * 把当前窗口截图并将其存储为png默 认1.0f的规模(原尺寸)和90%质量, 参数为file类的文件路径
	 * @param path
	 */
	public void takeScreenshot(String path);

	/**
	 * 打开通知栏
	 */
	public void openNotification();

	/**
	 * 打开快速设置
	 */
	public void openQuickSettings();

	/**
	 * 根据edText给Edit赋值
	 * @param classname
	 */
	public void classedittext(String classname, String content);

	/**
	 * 根据text给Edit赋值
	 * @param classname
	 */
	public void textedittext(String text, String content);
	
	/**
	 * 根据资源ID给Edit赋值
	 * @param classname
	 */
	public void residedittext(String resid, String content);
	
	public int getColor(int x, int y);
}