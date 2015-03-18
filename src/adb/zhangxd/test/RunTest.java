package adb.zhangxd.test;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RunTest {

	private static int time = -1;

	public static int getStartTime(final String cmd, long timeout)

	{
		ExecutorService exec = Executors.newFixedThreadPool(1);
		Callable<String> call = new Callable<String>() {
			public String call() throws Exception {
				// 开始执行耗时操作
//				String result = RunTest.runCMD1(cmd, "ThisTime:");
				return "";
			}
		};
		try {
			Future<String> future = exec.submit(call);
			String obj = future.get(timeout, TimeUnit.MILLISECONDS); // 任务处理超时时间设为
			System.out.println("任务成功返回:" + obj);
			time = obj != null ? Integer.parseInt(obj.split("\\:")[1].trim())
					: -1;
		} catch (TimeoutException ex) {
			System.out.println("处理超时啦....");
		} catch (Exception e) {
			System.out.println("处理失败.");
			e.printStackTrace();
		}
		// 关闭线程池
		exec.shutdown();
		return time;
	}

	public static void main(String[] args) {
//		int startTime = RunTest
//				.getStartTime(
//						"adb shell am start -W com.mydream.wifi/com.wifibanlv.www.activity.LogoActivity",
//						1000 * 60);
//		System.out.println(startTime);
		
		Pattern pattern = Pattern.compile("[a-z]{3}");
		Matcher matcher = pattern.matcher("abcd  ad adjfl faljf");
		while(matcher.find()){
			System.out.println(matcher.group());
		}
	}
}
