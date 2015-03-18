package adb.zhangxd.element;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import adb.zhangxd.AdbDevice;
import adb.zhangxd.utils.LogUtil;
import adb.zhangxd.utils.Shell;
import adb.zhangxd.utils.TestException;

public class ElementParse {

	private String tempFile = System.getProperty("java.io.tmpdir");
	private String temp = new File(tempFile + "/dumpfile").getAbsolutePath();
	private List<Element> elemList = new ArrayList<Element>(); 
	private UiElement uiElement ;
	private Element tmpElement;
	
	public ElementParse() {
		File dumpFile = new File(temp);
		if (!dumpFile.exists()) {
			dumpFile.mkdir();
		}
	}
	
	private File uidump(){
		LogUtil.info("uiautomator");
		String ui = Shell.runAdbCmd("shell uiautomator dump /data/local/tmp/uidump.xml");
		LogUtil.info(ui);
		Shell.runCommond("adb pull /data/local/tmp/uidump.xml " + temp);
		LogUtil.info("tempFile: " + temp  + "/uidump.xml" );
		return new File(temp + "/uidump.xml").getAbsoluteFile();
	}
	
	/**
	 * 根据属性和属性值 查找元素
	 * @param attr
	 * @param value
	 * @return
	 */
	public UiElement findElement(String attr,String value){
		getElementList(attr, value);
		if(elemList.size() == 0){
			throw new TestException("没有找到元素信息");
		}else{
			tmpElement = elemList.get(0);
		}
		uiElement = new UiElement();
		uiElement.setText(tmpElement.attributeValue(ElementAttribs.TEXT));
		uiElement.setResourceId(tmpElement.attributeValue(ElementAttribs.RESOURCEID));
		uiElement.setClassName(tmpElement.attributeValue(ElementAttribs.CLASS));
		uiElement.setContent(tmpElement.attributeValue(ElementAttribs.CONTENT));
		uiElement.setChecked(tmpElement.attributeValue(ElementAttribs.CHECKED));
		uiElement.setClickable(tmpElement.attributeValue(ElementAttribs.CLICKABLE));
		uiElement.setFocused(tmpElement.attributeValue(ElementAttribs.FOCUSED));
		uiElement.setBounds(tmpElement.attributeValue(ElementAttribs.BOUNDS));
		
		return uiElement;
	}
	
	/**
	 * 根据属性和属性值 查找元素列表
	 * @param attr
	 * @param value
	 * @return
	 */
	public ArrayList<UiElement> findElements(String attr,String value){
		getElementList(attr, value);
		if(elemList.size() == 0){
			return null;
		}else{
			ArrayList<UiElement> uiList = new ArrayList<UiElement>();
			for(Element tmpElement:elemList){
				uiElement = new UiElement();
				uiElement.setText(tmpElement.attributeValue(ElementAttribs.TEXT));
				uiElement.setResourceId(tmpElement.attributeValue(ElementAttribs.RESOURCEID));
				uiElement.setClassName(tmpElement.attributeValue(ElementAttribs.CLASS));
				uiElement.setContent(tmpElement.attributeValue(ElementAttribs.CONTENT));
				uiElement.setChecked(tmpElement.attributeValue(ElementAttribs.CHECKED));
				uiElement.setClickable(tmpElement.attributeValue(ElementAttribs.CLICKABLE));
				uiElement.setFocused(tmpElement.attributeValue(ElementAttribs.FOCUSED));
				uiElement.setBounds(tmpElement.attributeValue(ElementAttribs.BOUNDS));
				uiList.add(uiElement);
			}
			return uiList;
		}
	}
	
	/**
	 * 根据text查找元素
	 * @param text
	 * @return
	 */
	public UiElement findElementByText(String text){
		return findElement(ElementAttribs.TEXT, text);
	}
	
	/**
	 * 根据text值超找元素列表
	 * @param text
	 * @return
	 */
	public ArrayList<UiElement> findElementsByText(String text){
		return findElements(ElementAttribs.TEXT, text);
	}
	
	
	/**
	 * 根据resourceId查找元素
	 * @param resourceId
	 * @return
	 */
	public UiElement findElementById(String resourceId){
		return findElement(ElementAttribs.RESOURCEID, resourceId);
	}
	
	/**
	 * 根据resourceId查找元素列表
	 * @param resourceId
	 * @return
	 */
	public ArrayList<UiElement> findElementsById(String resourceId){
		return findElements(ElementAttribs.RESOURCEID, resourceId);
	}
	
	
	/**
	 * 根据class查找元素
	 * @param className
	 * @return
	 */
	public UiElement findElementByClass(String className){
		return findElement(ElementAttribs.CLASS, className);
	}
	
	
	/**
	 * 根据class查找元素列表
	 * @param className
	 * @return
	 */
	public ArrayList<UiElement> findElementsByClass(String className){
		return findElements(ElementAttribs.CLASS, className);
	}
	
	/**
	 * 通过checked定位单个元素
	 * 
	 * @param checked
	 * @return 返回元素位置坐标
	 */
	public UiElement findElementByChecked(String checked) {
		return findElement(ElementAttribs.CHECKED, checked);
	}

	/**
	 * 通过checked定位多个同属性的相同元素
	 * 
	 * @param checked
	 * @return 返回元素位置坐标集合
	 */
	public ArrayList<UiElement> findElementsByChecked(String checked) {
		return findElements(ElementAttribs.CHECKED, checked);
	}
	
	
	
	/**
	 * 通过checkable定位单个元素
	 * 
	 * @param checkable
	 * @return 返回元素位置坐标
	 */
	public UiElement findElementByCheckable(String checkable) {
		return this.findElement(ElementAttribs.CHECKABLE, checkable);
	}

	/**
	 * 通过checkable定位多个同属性的相同元素
	 * 
	 * @param checkable
	 * @return 返回元素位置坐标集合
	 */
	public ArrayList<UiElement> findElementsByCheckable(String checkable) {
		return this.findElements(ElementAttribs.CHECKABLE, checkable);
	}
	
	/**
	 * 通过content-desc定位单个元素
	 * 
	 * @param contentdesc
	 * @return 返回元素位置坐标
	 */
	public UiElement findElementByContentdesc(String contentdesc) {
		return this.findElement(ElementAttribs.CONTENT, contentdesc);
	}

	/**
	 * 通过content-desc定位多个同属性的相同元素
	 * 
	 * @param contentdesc
	 * @return 返回元素位置坐标集合
	 */
	public ArrayList<UiElement> findElementsByContentdesc(String contentdesc) {
		return this.findElements(ElementAttribs.CONTENT, contentdesc);
	}
	
	/**
	 * 通过clickable定位单个元素
	 * 
	 * @param clickable
	 * @return 返回元素位置坐标
	 */
	public UiElement findElementByClickable(String clickable) {
		return this.findElement(ElementAttribs.CLICKABLE, clickable);
	}

	/**
	 * 通过clickable定位多个同属性的相同元素
	 * 
	 * @param clickable
	 * @return 返回元素位置坐标集合
	 */
	public ArrayList<UiElement> findElementsByClickable(String clickable) {
		return this.findElements(ElementAttribs.CLICKABLE, clickable);
	}
	
	
	/**
	 * 等待界面元素出现
	 * @param name
	 * @return
	 */
	public boolean waitForElement(String attri,String name){
		for(int i =1 ;i <=10; i++){
			UiElement el = findElement(attri, name);
			if(el == null){
				sleep(1);
			}else{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 等待目标activity出现
	 * @param activity
	 * @return
	 */
	public boolean waitForActivity(String activity){
		
		for(int i = 1; i<=20; i++){
			String currentActivity = new AdbDevice().getCurrentActivity();
			if(activity.equalsIgnoreCase(currentActivity)){
				return true;
			}
			sleep(1);
		}
		return false;
	}
	
	
	
	private void getElementList(String attr,String value){
		SAXReader reader = new SAXReader();
		Document doc;
		try {
			doc = reader.read(uidump());
			Element root = doc.getRootElement();
			getElementList(root,attr,value);
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		
	}
	
	private void getElementList(Element element,String attr,String value){
		List<Element> elements = element.elements();
		if(elements.size() == 0 && element.attributeValue(attr).equals(value)){
			elemList.add(element);
		}else{
			for(Iterator<Element> it = element.nodeIterator(); it.hasNext();){
				Element elem = it.next();
				getElementList(elem,attr,value);
			}
		}
	}
	
	private void sleep(int second){
		try {
			Thread.sleep(second * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
}
