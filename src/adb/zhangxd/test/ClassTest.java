package adb.zhangxd.test;

import org.junit.Test;

import adb.zhangxd.AdbDevice;
import adb.zhangxd.element.ElementParse;

public class ClassTest {
	AdbDevice device = new AdbDevice();
	ElementParse driver = new ElementParse();
//	@Test
	public void testElement(){
		driver.findElementByText("翻墙").click();
	}
	
	@Test
	public void testRestartServer(){
//		device.restartServer();
		/*int[] resolution = device.getScreenResolution();
		LogUtil.info(String.format("设备的分辨率为 %s*%s", resolution[0],resolution[1]));
		Device devDetail = device.getDeviceDetail();
		System.out.println("设备SDK版本号 " + devDetail.getSdk());
		System.out.println("设备版本信息 " + devDetail.getRelease());
		System.out.println("设备生产商 " + devDetail.getModel());
		System.out.println("设备电量 " + device.getBatteryLevel());
		System.out.println("设备电池温度 " + device.getBatteryTemp());
		System.out.println("设备电池状态" + device.getBatteryStatus());
		System.out.println("启动测试程序: " );
		device.startActivity(""
				+ "com.funshion.video.mobile/com.funshion.video.activity.StartActivity");
		System.out.println("获取当前界面conponent " + device.getFocusedPackageAndActivity());
		System.out.println("获取当前界面 包名 " + device.getCurrentPackageName());
		System.out.println("获取当前界面Activity " + device.getCurrentActivity());
		System.out.println("停止运行程序 ");
		device.stopCurrentApp();
		System.out.println("检测程序运行状态 " + device.checkProcess("com.funshion.video.mobile"));
		System.out.println("充值应用信息");
		device.resetApp();
		
		device.pullFile("/data/anr/traces.txt.bugreport", "d:/");
		device.screenShot("test", "d:/");*/
		
//		device.installApp("d:/FunshionAphone2.2.5.4_SID_2_zipalign.apk");
//		device.uninstallApp("com.funshion.video.mobile");

		
//		device.sendKeyEvent(KeyCode.HOME);
//		device.tap(800,1400);
/*		device.swipeToRight();
		device.swipeToLeft();
		device.swipeToDown();
		device.swipeToUp();
*/		
		
//		device.tap(driver.findElementByText("优惠资讯"));
		device.sendText("abcdkjld");
		}
	
	
	
	
}
