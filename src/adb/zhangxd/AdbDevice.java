package adb.zhangxd;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import adb.zhangxd.element.UiElement;
import adb.zhangxd.entity.Device;
import adb.zhangxd.utils.Image;
import adb.zhangxd.utils.LogUtil;
import adb.zhangxd.utils.Shell;
import adb.zhangxd.utils.TestException;

public class AdbDevice {
	
	public void startServer(){
		Shell.runAdbCmd("start-server");
	}
	
	public void stopServer(){
		Shell.runAdbCmd("kill-server");
	}
	
	public void restartServer(){
		stopServer();
		startServer();
	}
	
	/**
	 * 获取屏幕分辨率
	 * @return
	 */
	public int[] getScreenResolution(){
		Pattern pattern = Pattern.compile("([0-9]+)");
		ArrayList<Integer> out =matchInteger(pattern,
				Shell.runAdbCmd("shell dumpsys display | grep PhysicalDisplayInfo"));

		int[] resolution = new int[] { out.get(0), out.get(1) };

		return resolution;
	}
	
	
	/**
	 * 获取设备SDK版本号
	 * @return
	 */
	public int getSdkVersion(){
		String out = Shell.runAdbCmd("shell getprop ro.build.version.sdk");
		return new Integer(out);
	}
	
	
	/**
	 * 获取电池电量
	 * @return
	 */
	public int getBatteryLevel(){
		String out = Shell.runAdbCmd("shell dumpsys battery | grep level");
		return new Integer(out.split(":")[1].trim());
	}
	
	/**
	 * 获取电池温度
	 * @return
	 */
	public double getBatteryTemp(){
		String out = Shell.runAdbCmd("shell dumpsys battery | grep temperature");
		return new Integer(out.split(":")[1].trim()) / 10.0;
	}
	
	/**
	 * 获取电池充电状态: 1 : BATTERY_STATUS_UNKNOWN, 未知状态 2 : BATTERY_STATUS_CHARGING,
	 * 充电状态 3 : BATTERY_STATUS_DISCHARGING, 放电状态 4 :
	 * BATTERY_STATUS_NOT_CHARGING, 未充电 5 : BATTERY_STATUS_FULL, 充电已满
	 * 
	 * @return 返回状态数值
	 */
	public int getBatteryStatus() {
		String out = Shell.runAdbCmd("shell dumpsys battery | grep status");		
		return new Integer(out.split("  status:")[1].trim());
	}

	
	/**
	 * 获取设备上当前界面的package和activity
	 * 
	 * @return 返回package/activity
	 */
	public String getFocusedPackageAndActivity() {
		Pattern pattern = Pattern.compile("([a-zA-Z0-9.]+/.[a-zA-Z0-9.]+)");
		String out = Shell.runAdbCmd("shell dumpsys window w | grep \\/ | grep name=");	

		ArrayList<String> component = matchString(pattern,out);

		return component.get(0);
	}

	
	/**
	 * 获取设备上当前界面的包名
	 * 
	 * @return 返回包名
	 */
	public String getCurrentPackageName() {
		return getFocusedPackageAndActivity().split("/")[0];
	}

	/**
	 * 获取设备上当前界面的activity
	 * 
	 * @return 返回activity名
	 */
	public String getCurrentActivity() {
		return getFocusedPackageAndActivity().split("/")[1];
	}

	
	/**
	 * @param packageName 获取pid
	 * @return pid
	 */
	public int getPid(String packageName){
		Pattern pattern = Pattern.compile("([\" \"][0-9]+)");
		ArrayList<Integer> pid = matchInteger(pattern, Shell.runAdbCmd("shell ps | grep -w " + packageName));
		
		if(pid.isEmpty()){
			throw new TestException("应用程序进程没有打开");
		}
		
		return pid.get(0);
	}
	
	/**
	 * 杀掉pid对应的进程
	 * @param pid 
	 * @return
	 */
	public boolean killProcess(int pid){
		String out = Shell.runAdbCmd("shell kill -s " + pid);
		LogUtil.info(out);
		if(out.equals("")){
			return true;
		}
			return false;
			
	}
	
	
	/**
	 * 检测运行状态
	 * @param packageName
	 * @return 正在运行返回true
	 */
	public boolean checkProcess(String packageName){
		String ps = Shell.runAdbCmd("shell ps | grep " + packageName);
		if(ps.indexOf(packageName) == -1){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * 退出当前应用
	 * 
	 */
	public void stopCurrentApp() {
		Shell.runAdbCmd("shell am force-stop " + getCurrentPackageName());
	}
	
	/**
	 * 重置当前应用，清除当前应用的数据且重启该应用
	 * 
	 */
	public void resetApp() {
		String component = getFocusedPackageAndActivity();
		clearAppDate(getCurrentPackageName());
		startActivity(component);

	}
	
	/**
	 * 获取应用程序列表
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getApkList(){
		return getAppList("shell pm list packages -s");
	}
	
	/**
	 * 获取第三方程序列表
	 * @return ArrayList<String>
	 */
	public ArrayList<String> getThirdApkList(){
		return getAppList("shell pm list packages -3");
	}
	
	
	/**
	 * 获取应用程序列表
	 */
	private ArrayList<String> getAppList(String cmd){
		ArrayList<String> appList = new ArrayList<String>();
		BufferedReader reader = Shell.runReader(Shell.adb + " " +cmd);
		
		String line;
		Pattern pattern = Pattern.compile(("[a-z]+:[\\w\\.]+"));
		Matcher mc;
		try {
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				if(line.trim().length() == 0){
					continue;
				}
				mc =pattern.matcher(line);
				while(mc.find()){
					appList.add(mc.group().trim().split(":")[1]);
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return appList;
	}
	
	/**
	 * 获取应用启动时间
	 * @param component
	 * @return
	 */
	public int getAppStartTime(String component){
		String out = Shell.runAdbCmd(String.format("shell am start -W %s | grep TotalTime", component));
		return new Integer(out.split(": ")[1]);
	}
	
	
	/**
	 * 复制设备上 文件至本地
	 * @param remotePath
	 * @param localPath
	 */
	public void pullFile(String remotePath,String localPath){
		Shell.runAdbCmd(String.format("pull %s %s", remotePath,localPath));
		if(!ifFileExist(localPath+remotePath.substring(remotePath.lastIndexOf("/")))){
			throw new RuntimeException("pull 文件失败");
		}
	}
	
	
	private boolean ifFileExist(String localPath){
		if(new File(localPath).exists()){
			return true;
		}
		return false;
	}
	
	/**
	 * 将文件传输到设备上
	 * @param localPath
	 * @param remotePath
	 */
	public void pushFile(String localPath,String remotePath){
		Shell.runAdbCmd(String.format("push %s %s", localPath,remotePath));
	}
	/**
	 * 截图
	 */
	public void screenShot(String imageName,String localPath){
		new Image().screenShot(imageName, localPath);
		if(!ifFileExist(localPath + "/" +imageName + ".png")){
			throw new RuntimeException("截取图片失败");
		}
	}
	
	
	/**
	 * 安装app
	 * @param appPath
	 */
	public void installApp(String appPath){
		Shell.runAdbCmd("install " + appPath);
		if(!isInstalled(APKDetail.apkInfo(appPath).getPackageName())){
			throw new RuntimeException("卸载安装包失败");
		}
	}
	
	/**
	 * 检测程序是否被安装
	 * @param packageName
	 * @return
	 */
	public boolean isInstalled(String packageName){
		if(getThirdApkList().contains(packageName) ||
				getApkList().contains(packageName)){
			return true;
		}
		return false;
	}
	
	
	/**
	 * 卸载app
	 * @param packageName
	 */
	public void uninstallApp(String packageName){
		Shell.runAdbCmd("uninstall " + packageName);
		if(isInstalled(packageName)){
			throw new RuntimeException("卸载安装包失败");
		}
	}
	
	/**
	 * 重启手机
	 */
	public void reboot(){
		Shell.runAdbCmd("reboot");
	}
	
	/**
	 * 判断设备是否重启成功，超时时间100s
	 * @return
	 */
	public boolean isReBooted(){
		int i =  0;
		while(true){
			try {
				if(i > 20){
					return false;
				}
				Thread.sleep(5 * 1000);
				
				if(!getDevices().isEmpty()){
					return true;
				}
				i++;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public void fastboot(){
		Shell.runAdbCmd("reboot bootloader");
	}
	
	
	/**
	 * 启动应用程序
	 * @param component
	 *    packageName/Activity
	 */
	public void startActivity(String component){
		
		Shell.runAdbCmd(String.format("shell am start -n %s",component));
		sleep(2 * 1000);
		if(!checkProcess(component.substring(0, component.indexOf("/")))){
			throw new TestException("启动应用程序失败");
		}
	}
	
	/**
	 * 打开网页
	 * @param url
	 */
	public void startWebPage(String url){
		Shell.runAdbCmd("shell am start -a android.intent.action.VIEW -d " + url);
	}
	
	/**
	 * 打电话
	 * @param phoneNum
	 */
	public void callPhone(int phoneNum){
		Shell.runAdbCmd("shell am start -a android.intent.action.Call -d tel:"+ phoneNum);
	}
	
	/**
	 * 发送一个按键
	 * @param keyCode
	 */
	public void sendKeyEvent(int keyCode){
		Shell.runAdbCmd("shell input keyevent " + keyCode);
	}
	
	
	/**
	 * 长按物理键 需要4.4以上
	 * @param keyCode
	 */
	public void longPressKey(int keyCode){
		Shell.runAdbCmd("shell input keyevent --longpress" + keyCode);
	}
	
	
	/**
	 * 在屏幕x,y位置发送一个点击事件
	 * @param x
	 * @param y
	 */
	public void tap(int x,int y){
		Shell.runAdbCmd(String.format("shell input tap %s %s", x,y));
		sleep(500);
	}
	
	/**
	 * 在屏幕x,y位置发送一个点击事件
	 * @param x
	 * @param y
	 */
	public void tap(double x,double y){
		double[] coords = ratio(x, y);
		Shell.runAdbCmd(String.format("shell input tap %s %s", coords[0],coords[1]));
		sleep(500);
	}
	
	public void tap(UiElement e){
		Shell.runAdbCmd(String.format("shell input tap %s %s", e.getX(),e.getY()));
		sleep(500);
	}
	
	/**
	 * 发送一个滑动事件
	 * @param startX
	 * @param startY
	 * @param endX
	 * @param endY
	 * @param ms
	 */
	public void swipe(int startX,int startY,int endX,int endY,long ms){
		if(getSdkVersion() < 19){
			Shell.runAdbCmd(String.format("shell input swipe %s %s %s %s %s", startX,startY,endX,endY));
		}else{
			Shell.runAdbCmd(String.format("shell input swipe %s %s %s %s %s", startX,startY,endX,endY,ms));
		}
		sleep(500);
	}
	
	
	public void swipe(double startX, double startY, double endX, double endY,
			long ms){
		double[] coords = ratio(startX, startY, endX, endY);
		if (getSdkVersion() < 19) {
			Shell.runAdbCmd(String.format("shell input swipe %s %s %s %s", coords[0],coords[1],coords[2],coords[3]));
		} else {
			Shell.runAdbCmd(String.format("shell input swipe %s %s %s %s %s", coords[0],coords[1],coords[2],coords[3],ms));
		}

		sleep(500);
	}
	
	/**
	 * 发送一个滑动事件
	 * 
	 * @param e1
	 *            起始元素
	 * @param e2
	 *            终点元素
	 * @param ms
	 *            持续时间
	 */
	public void swipe(UiElement e1, UiElement e2, long ms) {
		if (getSdkVersion() < 19) {
			Shell.runAdbCmd("shell input swipe " + e1.getX() + " " + e1.getY() + " "
					+ e2.getX() + " " + e2.getY());
		} else {
			Shell.runAdbCmd("shell input swipe " + e1.getX() + " " + e1.getY() + " "
					+ e2.getX() + " " + e2.getY() + " " + ms);
		}
		sleep(500);
	}
	
	/**
	 * 左滑屏幕
	 */
	public void swipeToLeft() {
		swipe(0.8, 0.5, 0.2, 0.5, 500);
	}

	/**
	 * 右滑屏幕
	 */
	public void swipeToRight() {
		swipe(0.2, 0.5, 0.8, 0.5, 500);
	}

	/**
	 * 上滑屏幕
	 */
	public void swipeToUp() {
		swipe(0.5, 0.7, 0.5, 0.3, 500);
	}

	/**
	 * 下滑屏幕
	 */
	public void swipeToDown() {
		swipe(0.5, 0.3, 0.5, 0.7, 500);
	}

	
	/**
	 * 发送一个长按事件
	 * 
	 * @param x
	 *            x坐标
	 * @param y
	 *            y坐标
	 */
	public void longPress(int x, int y) {
		swipe(x, y, x, y, 1500);
	}
	
	/**
	 * 发送一个长按事件
	 * 
	 * @param e
	 *            元素对象
	 */
	public void longPress(UiElement e) {
		swipe(e, e, 1500);
	}

	
	/**
	 * 发送一段文本，只支持英文，多个空格视为一个空格
	 * 
	 * @param text
	 *            英文文本
	 */
	public void sendText(String text) {
		String[] str = text.split(" ");
		ArrayList<String> out = new ArrayList<String>();
		for (String string : str) {
			if (!string.equals("")) {
				out.add(string);
			}
		}

		int length = out.size();
		for (int i = 0; i < length; i++) {
			Shell.runAdbCmd("shell input text " + out.get(i));
			sleep(100);
			if (i != length - 1) {
				sendKeyEvent(KeyCode.SPACE);
			}
		}
	}
	
	/**
	 * 清除文本
	 * 
	 * @param text
	 *            获取到的文本框中的text
	 */
	public void clearText(String text) {
		int length = text.length();
		for (int i = length; i > 0; i--) {
			sendKeyEvent(KeyCode.BACKSPACE);
		}
	}
	
	
	/**
	 * 清除应用的用户数据
	 * 
	 * @param packageName
	 *            应用的包名
	 * @return 清楚成功返回true, 否则返回false
	 */
	public boolean clearAppDate(String packageName) {
		if (Shell.runAdbCmd("shell pm clear " + packageName).equals("Success")) {
			return true;
		}
		return false;
	}
	
	
	private double[] ratio(double startX, double startY, double endX,
			double endY) {
		int[] display = getScreenResolution();
		double[] coords = new double[4];

		if (startX < 1) {
			coords[0] = display[0] * startX;
		} else {
			coords[0] = startX;
		}

		if (startY < 1) {
			coords[1] = display[1] * startY;
		} else {
			coords[1] = startY;
		}

		if (endX < 1) {
			coords[2] = display[0] * endX;
		} else {
			coords[2] = endX;
		}

		if (endY < 1) {
			coords[3] = display[1] * endY;
		} else {
			coords[3] = endY;
		}

		return coords;
	}

	
	private double[] ratio(double x,double y){
		int[] display = getScreenResolution();
		double[] coords = new double[2];
		
		if(x < 1){
			coords[0] = display[0] * x;
		}else{
			coords[0] = x;
		}
		
		if(y < 1){
			coords[1] = display[1] * y;
		}else{
			coords[1] = y;
		}
		return coords;
	}
	
	private  ArrayList<Integer> matchInteger(Pattern pattern, String str) {
		ArrayList<Integer> result = new ArrayList<Integer>();
		Matcher mc = pattern.matcher(str);
		
		while (mc.find()) {
			result.add(new Integer(mc.group().trim()));
		}
		
		return result;
	}
	
	
	private ArrayList<String> matchString(Pattern pattern, String str) {
		ArrayList<String> result = new ArrayList<String>();
		Matcher mc = pattern.matcher(str);
		
		while (mc.find()) {
			result.add(mc.group().trim());
		}
		
		return result;
	}
	
	/**
	 * 获取设备详细信息
	 * @return
	 */
	public Device getDeviceDetail(){
		Device dev = new Device();
		String brand = Shell.runCommond("adb shell cat /system/build.prop|grep ro.product.brand=");
		String model = Shell.runCommond("adb shell cat /system/build.prop|grep ro.product.model=");
		String release = Shell.runCommond("adb shell cat /system/build.prop|grep ro.build.version.release=");
		String sdk = Shell.runCommond("adb shell cat /system/build.prop|grep ro.build.version.sdk=");
	
		dev.setBrand(brand.substring(brand.indexOf("=")+1));
		dev.setModel(model.substring(model.indexOf("=")+1));
		dev.setRelease(release.substring(release.indexOf("=")+1));
		dev.setSdk(sdk.substring(sdk.indexOf("=")+1));
		return dev;
	}
	
	
	/**
	 * 获取设备列表
	 * @return
	 */
	public HashSet<String> getDevices(){
		BufferedReader reader = Shell.runReader("adb devices");
		String line = "";
		HashSet<String> devices = new HashSet<String>();
		try {
			while((line = reader.readLine()) != null){
				if(line.indexOf("List of") != -1){
					continue;
				}
				if(line.length() !=0 ){
					devices.add(line.split("\t")[0]);
					LogUtil.info("test",line.split("\t")[0] );
				}
				
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return devices;
	}
	
	public static void sleep(long millis){
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	@Test
	public void deviceTest(){
		Device dev = getDeviceDetail();
		LogUtil.info("", dev.getBrand());
		LogUtil.info("", dev.getModel());
		LogUtil.info("", dev.getRelease());
		LogUtil.info("", dev.getSdk());
	}
	
	
}
