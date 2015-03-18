package adb.zhangxd.element;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import adb.zhangxd.AdbDevice;
import adb.zhangxd.utils.Shell;

public class UiElement  {
	
	private Pattern pattern = Pattern.compile("([0-9]+)");
	
	private int x;
	private int y;
	
	private String text;
	private String resourceId;
	private String className;
//	private String packageName;
	private String content;
	private boolean checkable;
	private boolean checked;
	private boolean clickable;
//	private boolean enabled;
//	private boolean focusable ;
	private boolean focused ;
//	private boolean scrollable;
//	private String longClickable;
//	private String password;
//	private String selected;
	private String bounds;
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getResourceId() {
		return resourceId;
	}

	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCheckable() {
		return checkable;
	}

	public void setCheckable(String checkable) {
		if(checkable.equals("true")){
			this.checkable = true;
		}else{
			this.checkable = false;
		}
	}

	public boolean isChecked() {
		return checked;
	}

	public void setChecked(String checked) {
		if(checked.equals("true")){
			this.checked = true;
		}else{
			this.checked = false;
		}
	}

	public boolean isClickable() {
		return clickable;
	}

	public void setClickable(String clickable) {
		if(clickable.equals("true")){
			this.clickable = true;
		}else{
			this.clickable = false;
		}
	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(String focused) {
		if(focused.equals("true")){
			this.focused = true;
		}else{
			this.focused = false;
		}
	}

	public String getBounds() {
		return bounds;
	}

	public void setBounds(String bounds) {
		this.bounds = bounds;
		Matcher mat = pattern.matcher(bounds);
		ArrayList<Integer> coords = new ArrayList<Integer>();
		while (mat.find()) {
			coords.add(new Integer(mat.group()));
		}
		int startX = coords.get(0);
		int startY = coords.get(1);
		int endX = coords.get(2);
		int endY = coords.get(3);

		setX((endX - startX) / 2 + startX);
		setY((endY - startY) / 2 + startY);
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public void click(){
		Shell.runAdbCmd(String.format("input tap %s %s",this.x,this.y));
		AdbDevice.sleep(500);
	}
	
}
