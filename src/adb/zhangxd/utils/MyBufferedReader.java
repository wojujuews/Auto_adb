package adb.zhangxd.utils;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MyBufferedReader extends BufferedReader {

	public MyBufferedReader(Reader reader) {
		super(reader);
	}
	
	
	String resultString = null;
	public String readLine( long timeout)
	{
		ExecutorService exec = Executors.newFixedThreadPool(1);
		Callable<String> call = new Callable<String>() {
			public String call() throws Exception {
				// 开始执行耗时操作
				resultString = readLine();
				return resultString;
			}
		};
		try {
			Future<String> future = exec.submit(call);
			future.get(timeout, TimeUnit.MILLISECONDS); // 任务处理超时时间设为
			System.out.println("任务成功返回:");
			
		} catch (TimeoutException ex) {
			return null;
		} catch (Exception e) {
			return null;
		}
		exec.shutdown();
		return resultString;
	}

	
}
