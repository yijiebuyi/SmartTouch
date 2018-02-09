package com.st.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class FileUtil {
    private final static String DEFAULT_DIR = "/data/data";

    /**
     * 获取SD卡
     */
    public static String getAndroidPath(String SDpath) {
        String path = Environment.getExternalStorageDirectory().getPath() + SDpath;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * 判断文件是否存在
     *
     * @param path
     * @return
     */
    public static boolean fileIsExists(String path) {
        boolean isExists = false;
        try {
            File file = new File(path);
            if (file.exists()) {
                return true;
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
        return isExists;
    }

    /**
     * 删除文件
     *
     * @param path
     * @return
     */
    public static void deleteFiles(String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                file.delete();
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 获取跟目�?
     */
    public static String getSd() {
        String sdPath = "";
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            sdPath = Environment.getDataDirectory().getAbsolutePath() + "/";
        }
        return sdPath;
    }

    /**
     * 从assets复制文件到指定位
     *
     * @param myContext
     * @param assetName
     * @param saveDir
     * @param saveName
     * @return
     */
    public static String copyFromAsset(Context myContext, String assetName, String saveDir, String saveName) {
        String filePath = saveDir + "/" + saveName;
        if (fileIsExists(filePath)) {
            return filePath;
        } else {
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = null;
            try {
                if (!(file = new File(filePath)).exists()) {
                    file.createNewFile();
                }
                String[] ss = myContext.getResources().getAssets().list("");

                //InputStream is = myContext.getResources().getAssets().open(assetName); use in eclipse
                InputStream is = myContext.getClass().getClassLoader().getResourceAsStream("assets/" + assetName);

                FileOutputStream fos = new FileOutputStream(filePath);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
                if (file != null) {
                    file.delete();
                }
            }
            return filePath;
        }
    }

    public static String copyFromFile(String srcPath, String saveDir, String saveName) {
        String filePath = saveDir + "/" + saveName;
        if (fileIsExists(filePath)) {
            return filePath;
        } else {
            File dir = new File(saveDir);
            if (!dir.exists()) {
                dir.mkdir();
            }
            try {
                File file = null;
                if (!(file = new File(filePath)).exists()) {
                    file.createNewFile();
                }

                InputStream is = new FileInputStream(srcPath);
                ULog.i("aaa srcPath:" + srcPath);
                FileOutputStream fos = new FileOutputStream(filePath);
                byte[] buffer = new byte[7168];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return filePath;
        }
    }


    /**
     * @param is
     * @return
     * @throws IOException
     */
    public static String readFromFile(Context context, String path) throws IOException {
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
            Log.e("ReadStream", "读取异常");
            return "";
        } finally {
            br.close();
        }
    }

    /**
     * 流读取文�?拿到
     *
     * @param is
     * @return
     * @throws IOException
     */
    public static String readFromAssets(Context context, String name) throws IOException {
        InputStream openRawResource = context.getAssets().open(name);
        BufferedReader br = new BufferedReader(new InputStreamReader(openRawResource));
        try {
            StringBuffer sb = new StringBuffer();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
            return sb.toString();
        } catch (IOException e) {
            Log.e("ReadStream", "读取文件流失");
            return "";
        } finally {
            br.close();
        }
    }

    /**
     * 复制jar包到指定路径
     *
     * @param context
     * @return
     */
    public static String copyJar(Context context) {
        File cache;
        String dir = null;
        if ((cache = context.getCacheDir()) != null) {
            dir = cache.getAbsolutePath();
        } else {
            dir = DEFAULT_DIR + "/" + context.getPackageName();
        }
        if (fileIsExists(dir + "/" + CaseUtil.JAR)) {
            return dir + "/" + CaseUtil.JAR;
        } else {
            return FileUtil.copyFromAsset(context, CaseUtil.JAR, dir, CaseUtil.JAR);
        }
    }

    public static ArrayList<String> getLuaFile(String SDpath) {
        ArrayList<String> filelist = null;
        File file = new File(SDpath);
        if (!file.exists()) {
            file.mkdirs();
        }
        filelist = new ArrayList<String>();
        File[] listFiles = file.listFiles();
        for (File fileitem : listFiles) {
            if (fileitem.isFile()) {
                String name = fileitem.getName();
                int length = name.length();
                if (".lua".equals(name.substring(length - 4, length))) {
                    filelist.add(name);
                }
            }
        }
        return filelist;
    }

}
