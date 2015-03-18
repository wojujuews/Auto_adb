package adb.zhangxd.entity;

public class APK {
	private String packageName;
	private String versionCode;
	private String versionName;
	private String sdkVersion;
	private String launActivity;
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getVersionCode() {
		return versionCode;
	}
	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}
	public String getVersionName() {
		return versionName;
	}
	public void setVersionName(String versionName) {
		this.versionName = versionName;
	}
	public String getSdkVersion() {
		return sdkVersion;
	}
	public void setSdkVersion(String sdkVersion) {
		this.sdkVersion = sdkVersion;
	}
	public String getLaunActivity() {
		return launActivity;
	}
	public void setLaunActivity(String launActivity) {
		this.launActivity = launActivity;
	}
}
