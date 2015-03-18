package adb.zhangxd;

import java.io.BufferedReader;
import java.io.IOException;

import adb.zhangxd.entity.APK;
import adb.zhangxd.utils.Shell;

import com.google.common.base.CharMatcher;

public class APKDetail {
	
	private static APK apk = new APK();
	
	public static APK apkInfo(String apkPath){
		BufferedReader reader = Shell.runReader(String.format("%s d badging %s", Shell.aapt,apkPath));
		String line = "";
		try {
			while((line = reader.readLine()) != null){
				if(line.indexOf("versionCode") != -1){
					String pName = line.substring(line.indexOf("name='")+6);
					String vCode = line.substring(line.indexOf("Code='")+6);
					String vName = line.substring(line.indexOf("versionName='")+13);
					apk.setPackageName(pName.substring(0, pName.indexOf("'")));
					apk.setVersionCode(vCode.substring(0, vCode.indexOf("'")));
					apk.setVersionName(vName.substring(0, vName.indexOf("'")));
				}
				if((line.indexOf("sdkVersion")) != -1){
					apk.setSdkVersion(CharMatcher.DIGIT.retainFrom(line));
				}
				if((line.indexOf("launchable-activity")) != -1){
					apk.setLaunActivity(line.substring(line.indexOf("name='")+6, line.indexOf("' ")));
				}
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return apk;
	}
	
	
}
