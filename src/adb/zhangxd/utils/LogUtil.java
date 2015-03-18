package adb.zhangxd.utils;


public class LogUtil {
	
	public static void debug(String message){
		System.out.println(String.format("debug %s",message));
	}
	
	public static void debug(String TAG,String message){
		System.out.println(String.format("debug %s %s",TAG,message));
	}
	
	public static void info(String message){
		System.out.println(String.format("info %s",message));
	}
	
	public static void info(String TAG,String message){
		System.out.println(String.format("info %s %s",TAG,message));
	}
	
	public static void error(String message){
		System.out.println(String.format("error %s",message));
	}
	
	public static void error(String TAG,String message){
		System.out.println(String.format("error %s %s",TAG,message));
	}
	
	
}
