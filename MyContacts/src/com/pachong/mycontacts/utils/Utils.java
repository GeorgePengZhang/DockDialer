package com.pachong.mycontacts.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.View;

public class Utils {

	public static final boolean DEBUG = true;
	
	public interface IChildViewOnClick {
		public void onChildViewClick(View v);
	}
	
	/**
	 * Returns whether the SDK is KitKat or later
	 */
    public static boolean isKitKatOrLater() {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2;
    }
    
	public static void writeLogToSdcard(String msg) {
		if (!Utils.DEBUG) {
			return;
		}
		
		Log.d("TAG", msg);
		msg = msg+"\n";
		try {
			File file = new File(Environment.getExternalStorageDirectory().getPath(), "alarmlog.txt");
			FileOutputStream fos = new FileOutputStream(file, true);
			fos.write(msg.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeLogToSdcard(String fileName, String msg) {
		if (!Utils.DEBUG) {
			return;
		}
		
		Log.d("TAG", msg);
		msg = msg+"\n";
		try {
			File file = new File(Environment.getExternalStorageDirectory().getPath(), fileName);
			FileOutputStream fos = new FileOutputStream(file, true);
			fos.write(msg.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	* 检测app是否启动
	* 
	*/
	public static boolean isAppRunning(Context context, String pkg) {
		ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(100);
		boolean isAppRunning = false;
		for (RunningTaskInfo info : list) {
			String packageName = info.baseActivity.getPackageName();
			Log.d("TAG", "isAppRunning:"+packageName+",pkg:"+pkg+",top:"+info.topActivity.getPackageName());
		    if (info.topActivity.getPackageName().equals(pkg) || info.baseActivity.getPackageName().equals(pkg)) {
		        isAppRunning = true;
		        break;
		    }
		}
			     
        return isAppRunning;
    }
	
	/**
	 * 判断当前正在运行的app
	 * @param context
	 * @param pkg
	 * @return
	 */
	public static boolean isTopApp(Context context, String pkg) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE) ;
		List<RunningTaskInfo> runningTaskInfos = am.getRunningTasks(1) ;
		boolean isAppTop = false;
		if(runningTaskInfos != null && runningTaskInfos.size() > 0) {
			ComponentName componentName = runningTaskInfos.get(0).topActivity;
			if (componentName != null && componentName.getPackageName().equals(pkg)) {
				isAppTop = true;
			}
		}
		
		return isAppTop;
	}
	
	
	/**
	 * 获取首字母 第一个字符非字母的都设置为#
	 * @param name
	 * @return
	 */
	public static String getFirstLetter(String name) {
		String firstLetter = name.substring(0, 1).toUpperCase(Locale.getDefault());
		if (!firstLetter.matches("[A-Z]")) {
			firstLetter = "#";
		}
		return firstLetter;
	}
	
}
