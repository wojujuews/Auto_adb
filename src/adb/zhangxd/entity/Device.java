package adb.zhangxd.entity;

public class Device {
	//手机品牌
	private String brand;
	//手机型号 有为空的现象
	private String model;
	//手机系统版本号
	private String release;
	//手机rom版本
	private String sdk;
	public String getBrand() {
		return brand;
	}
	public void setBrand(String brand) {
		this.brand = brand;
	}
	public String getModel() {
		return model;
	}
	public void setModel(String model) {
		this.model = model;
	}
	public String getRelease() {
		return release;
	}
	public void setRelease(String release) {
		this.release = release;
	}
	public String getSdk() {
		return sdk;
	}
	public void setSdk(String sdk) {
		this.sdk = sdk;
	}
	
}
