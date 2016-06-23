package com.cs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class UiAutomatorHelper {

	// 以下参数需要配置，用例集id，用例id，安卓id
	private static String android_id = "3";
	private static String jar_name = "";

	// 工作空间不需要配置，自动获取工作空间目录
	private static String workspace_path;

	public static void main(String[] args) {
		String jarName = "", testClass = "", testName = "", androidId = "";
		for (int i = 0; i < args.length; i++) {
			if (args[i].equals("--jar_name")) {
				jarName = args[i + 1];
			}
			if (args[i].equals("--test_class")) {
				testClass = args[i + 1];
			}
			if (args[i].equals("--test_name")) {
				testName = args[i + 1];
			}
			if (args[i].equals("--android_id")) {
				androidId = args[i + 1];
			}
		}
		new UiAutomatorHelper(jarName, testClass, testName, androidId);

	}

	public UiAutomatorHelper() {
		workspace_path = getWorkSpase();
		System.out.println("---工作空间：\t\n" + getWorkSpase());
	}

	/**
	 * 需求：UI工程调试构造器，输入jar包名，包名，类名，用例名
	 * 
	 * @param jarName
	 * @param testClass
	 * @param testName
	 * @param androidId
	 */
	public UiAutomatorHelper(String jarName, String testClass, String testName,
			String androidId) {
		System.out.println("-----------start--uiautomator--debug-------------");
		workspace_path = getWorkSpase();
		System.out.println("----工作空间：\t\n" + getWorkSpase());

		jar_name = jarName;
		android_id = androidId;
		runUiautomator();
		System.out.println("*******************");
		System.out.println("---FINISH DEBUG----");
		System.out.println("*******************");
	}

	// 运行步骤
	private void runUiautomator() {
		creatBuildXml();
		modfileBuild();
		buildWithAnt();
		if (System.getProperty("os.name").equals("Linux")) {
			pushTestJar(workspace_path + "/bin/" + jar_name + ".jar");
		} else {
			pushTestJar(workspace_path + "\\bin\\" + jar_name + ".jar");
		}

		/*
		 * if (test_name.equals("")) { runTest(jar_name, test_class); return; }
		 * runTest(jar_name, test_class + "#" + test_name);
		 */
	}

	// 1--判断是否有build
	public boolean isBuild() {
		File buildFile = new File("build.xml");
		if (buildFile.exists()) {
			return true;
		}
		// 创建build.xml
		execCmd("cmd /c android create uitest-project -n " + jar_name + " -t "
				+ android_id + " -p " + workspace_path);
		return false;
	}

	// 创建build.xml
	public void creatBuildXml() {
		execCmd("cmd /c android create uitest-project -n " + jar_name + " -t "
				+ android_id + " -p " + "\"" + workspace_path + "\"");
	}

	// 2---修改build
	public void modfileBuild() {
		StringBuffer stringBuffer = new StringBuffer();
		try {
			File file = new File("build.xml");
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file));
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = null;
				while ((lineTxt = bufferedReader.readLine()) != null) {
					if (lineTxt.matches(".*help.*")) {
						lineTxt = lineTxt.replaceAll("help", "build");
						// System.out.println("修改后： " + lineTxt);
					}
					stringBuffer = stringBuffer.append(lineTxt + "\t\n");
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}

		System.out.println("-----------------------");

		// 修改后写回去
		writerText("build.xml", new String(stringBuffer));
		System.out.println("--------修改build完成---------");
	}

	// 3---ant 执行build
	public void buildWithAnt() {
		if (System.getProperty("os.name").equals("Linux")) {
			execCmd("ant");
			return;
		}
		execCmd("cmd /c ant");
	}

	// 4---push jar
	public void pushTestJar(String localPath) {
		localPath = "\"" + localPath + "\"";
		System.out.println("----jar包路径： " + localPath);
		String pushCmd = "adb push " + localPath + " /data/local/tmp/";
		System.out.println("----" + pushCmd);
		execCmd(pushCmd);
	}

	// 5---运行测试
	public void runTest(String jarName, String testName) {
		String runCmd = "adb shell uiautomator runtest ";
		String testCmd = jarName + ".jar " + "--nohup -c " + testName;
		System.out.println("----runTest:  " + runCmd + testCmd);
		execCmd(runCmd + testCmd);
	}

	public String getWorkSpase() {
		File directory = new File("");
		String abPath = directory.getAbsolutePath();
		return abPath;
	}

	/**
	 * 需求：执行cmd命令，且输出信息到控制台
	 * 
	 * @param cmd
	 */
	public void execCmd(String cmd) {
		System.out.println("----execCmd:  " + cmd);
		try {
			Process p = Runtime.getRuntime().exec(cmd);
			// 正确输出流
			InputStream input = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					input));
			String line = "";
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				saveToFile(line, "runlog.log", false);
			}
			// 错误输出流
			InputStream errorInput = p.getErrorStream();
			BufferedReader errorReader = new BufferedReader(
					new InputStreamReader(errorInput));
			String eline = "";
			while ((eline = errorReader.readLine()) != null) {
				System.out.println(eline);
				saveToFile(eline, "runlog.log", false);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 需求：写如内容到指定的文件中
	 * 
	 * @param path
	 *            文件的路径
	 * @param content
	 *            写入文件的内容
	 */
	public void writerText(String path, String content) {

		File dirFile = new File(path);

		if (!dirFile.exists()) {
			dirFile.mkdir();
		}

		try {
			// new FileWriter(path + "t.txt", true) 这里加入true 可以不覆盖原有TXT文件内容 续写
			BufferedWriter bw1 = new BufferedWriter(new FileWriter(path));
			bw1.write(content);
			bw1.flush();
			bw1.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void saveToFile(String text, String path, boolean isClose) {
		File file = new File("runlog.log");
		BufferedWriter bf = null;
		try {
			FileOutputStream outputStream = new FileOutputStream(file, true);
			OutputStreamWriter outWriter = new OutputStreamWriter(outputStream);
			bf = new BufferedWriter(outWriter);
			bf.append(text);
			bf.newLine();
			bf.flush();

			if (isClose) {
				bf.close();
			}
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}