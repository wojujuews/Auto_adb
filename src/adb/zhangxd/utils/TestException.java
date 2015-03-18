package adb.zhangxd.utils;

public class TestException extends RuntimeException{
	private static final long serialVersionUID = 1L;
	public TestException(){
		super();
	}
	public TestException(String message){
		super(message);
		LogUtil.error("TestException", message);
	}
}
