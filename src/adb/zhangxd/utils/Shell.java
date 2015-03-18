package adb.zhangxd.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Shell {
	private static Process process;
	private static BufferedReader reader;

	public static String adb = "E:\\adt-bundle-windows\\platform-tools\\adb.exe";
	public static String aapt = "E:\\adt-bundle-windows\\build-tools\\22.0.0\\aapt.exe";

//	private static Shell shell;
//	
//	private Shell(){
//	}
//	
//	public static Shell getInstance(){
//		if(shell == null){
//			shell = new Shell();
//		}
//		return shell;
//	}
	
	private static String cmdCommond;
	
	public static String runAdbCmd(String cmd){
		cmdCommond = String.format("%s %s",adb,cmd);
		LogUtil.debug("commond is: " + cmdCommond);
		return runCommond(cmdCommond);
	}
	
	public static String runAAPTCmd(String cmd){
		cmdCommond =  String.format("%s %s",aapt,cmd);
		return runCommond(cmdCommond);
	}
	
	// 需要逐行处理输出信息
	public static BufferedReader runReader(String cmd) {
		try {
			process = Runtime.getRuntime().exec(cmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
		
		return reader;
	}
	
	
	public static String runCommond(String cmd){
		StringBuilder sb = new StringBuilder();
		try {
			process = Runtime.getRuntime().exec(cmd);
			reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line;
			while((line = reader.readLine()) != null ){
				sb.append(line);
			}
			
			process.waitFor();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} 
		
		if(sb.indexOf("not found") != -1){
			try {
				throw new Exception("commond not found");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		LogUtil.debug("commond result: ", sb.toString());
		
		return sb.toString();
	}
	// 设置执行CMD命令超时时间
	public static String runUtil(String cmd){
		final ExecutorService exec = Executors.newFixedThreadPool(1);
		Callable<String> call = new Callable<String>() {
			@Override
			public String call() throws Exception {
				return runCommond(cmd);
			}
		};
		String obj = "";
		try {
			Future<String> future = exec.submit(call);
			obj = future.get(120, TimeUnit.SECONDS);
		} catch (TimeoutException ex) {
			System.out.println("处理任务超时");
			ex.printStackTrace();
		}catch (Exception e) {
			System.out.println("任务执行失败");
			e.printStackTrace();
		}
		exec.shutdown();
		return obj;
	}
	
	
	public void timeOut(){
		
	}
	
	
//	public void setAaptPath(String aaptPath){
//		this.aapt= aaptPath;
//	}
//	
//	public void setAdbPaht(String adbPath){
//		this.adb = adbPath;
//	}
	
}
