package com.automation.common;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import com.automation.framework.AbastractBase;
import com.automation.listener.Log4jLogger;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.touch.offset.PointOption;

/**
 * app公用方法类
 * @author 爱吃苹果的鱼
 */
public class AppCommon extends AbastractBase{

	Log4jLogger logger = new Log4jLogger();
	
	public AppCommon(AppiumDriver<MobileElement> handler) {
		this.appHandler = handler;
	}	
	
	public boolean toastChk(String toast) {
		try {
			final WebDriverWait wait = new WebDriverWait(appHandler,2);
			Assert.assertNotNull(wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(".//*[contains(@text,'"+ toast + "')]"))));
			System.out.println("找到了toast\"" + toast + "\"");
			return true;
		} catch (Exception e) {
			throw new AssertionError("找不到"+toast);
		}		
	}
	
	/**
	 * device width get
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月23日
	 */
	public int deviceXGet() {
		return appHandler.manage().window().getSize().getWidth();
	}
	
	/**
	 * device height get
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月23日
	 */
	public int deviceYGet() {
		return appHandler.manage().window().getSize().getHeight();
	}
	
	/**
	 * tap click with point x and y
	 * @param x
	 * @param y
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月23日
	 */
	@SuppressWarnings("rawtypes")
	public void tapClick(int x, int y) {
//		Thread.sleep(5000);
		TouchAction act = new TouchAction(appHandler);
		act.tap(PointOption.point(x, y)).perform();
//		act.(onElement);
	}
	
	/**
	 * 输出日志
	 * @param udid
	 * @param logPath
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public void appiumLogcat(String udid, String logPath){
		excuteAdbShell("adb -s " + udid + " >" + logPath);
	}
	
	/**
	 * 执行adb命令
	 * author:panyongjun
	 * date:2019年12月27日
	 */
	private void excuteAdbShell(String s) {
        Runtime runtime=Runtime.getRuntime();
        try{
            runtime.exec(s);
        }catch(Exception e){
            System.out.println("执行命令:"+s+"出错");
        }
    }
	
	/**
	 * 通过app输入法回车
	 * author:panyongjun
	 * date:2020年1月3日
	 */
	public void imeEnterPress(String udid) throws Throwable{
		excuteAdbShell("adb -s " + udid + " shell input keyevent KEYCODE_ENTER");
		Thread.sleep(1000);
	}
	
	/**
	 * 输入法切换
	 * author:panyongjun
	 * date:2020年1月3日
	 */
	public void imeSwith(String ime, String udid) throws Throwable{
		excuteAdbShell("adb -s " + udid + " shell ime set " + ime);
		Thread.sleep(5000);
	}
	
	/**
	 * click事件
	 * @throws Throwable
	 * @param xpath
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public void eleClickByXpath(String xpath) throws Throwable{
		try{
			String elTxt = appHandler.findElementByXPath(xpath).getAttribute("text");
			logger.info(elTxt);
		}catch(Exception e){
			e.printStackTrace();
		}
		appHandler.findElementByXPath(xpath).click();
		Thread.sleep(1000);
	}
	
	/**
	 * click事件
	 * @throws Throwable
	 * @param xpath
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public void eleClickByClassName(String className) throws Throwable{
		try{
			String elTxt = appHandler.findElementByClassName(className).getAttribute("text");
			logger.info(elTxt);
		}catch(Exception e){
			e.printStackTrace();
		}
		appHandler.findElementByClassName(className).click();
		Thread.sleep(1000);
	}
	
	/**
	 * click事件
	 * @throws Throwable
	 * @param xpath
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public void eleClickById(String id) throws Throwable{
		try{
			String elTxt = appHandler.findElementById(id).getAttribute("text");
			logger.info(elTxt);
		}catch(Exception e){
			e.printStackTrace();
		}
		appHandler.findElementById(id).click();
		Thread.sleep(1000);
	}
	
	/**
	 * click事件
	 * @throws Throwable
	 * @param xpath
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public void txtBoxSendValue(MobileElement ele, String txt) throws Throwable{
		logger.info("输入：" + txt);
		ele.sendKeys(txt);
		Thread.sleep(1000);
	}
	
	/**
	 * ele获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public MobileElement eleGetById(String id) throws Throwable{
		return appHandler.findElementById(id);
	}
	
	/**
	 * ele list 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public List<MobileElement> eleListsGetById(String id) throws Throwable{
		return appHandler.findElementsById(id);
	}
	
	/**
	 * ele 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public MobileElement eleGetByClassName(String className) throws Throwable{
		return appHandler.findElementByClassName(className);
	}
	
	/**
	 * ele list 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public List<MobileElement> eleListsGetByClassName(String className) throws Throwable{
		return appHandler.findElementsByClassName(className);
	}
	
	/**
	 * ele 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public MobileElement eleGetByLinkText(String linkTxt) throws Throwable{
		return appHandler.findElementByLinkText(linkTxt);
	}
	
	/**
	 * ele list 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public List<MobileElement> eleListsGetByLinkText(String linkTxt) throws Throwable{
		return appHandler.findElementsByLinkText(linkTxt);
	}
	
	/**
	 * ele 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public MobileElement eleGetByName(String name) throws Throwable{
		return appHandler.findElementByName(name);
	}
	
	/**
	 * ele list 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public List<MobileElement> eleListsGetByName(String name) throws Throwable{
		return appHandler.findElementsByName(name);
	}
	
	/**
	 * ele  获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public MobileElement eleGetByPartialLinkText(String partlinkTxt) throws Throwable{
		return appHandler.findElementByPartialLinkText(partlinkTxt);
	}
	
	/**
	 * ele list 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public List<MobileElement> eleListsGetByPartialLinkText(String partlinkTxt) throws Throwable{
		return appHandler.findElementsByPartialLinkText(partlinkTxt);
	}
	
	/**
	 * ele 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public MobileElement eleGetByTagName(String tagName) throws Throwable{
		return appHandler.findElementByTagName(tagName);
	}
	
	/**
	 * ele list 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public List<MobileElement> eleListsGetByTagName(String tagName) throws Throwable{
		return appHandler.findElementsByTagName(tagName);
	}
	
	/**
	 * ele 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public MobileElement eleGetByXPath(String xPath) throws Throwable{
		return appHandler.findElementByXPath(xPath);
	}
	
	/**
	 * ele list 获取
	 * @param id
	 * @return
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月12日
	 */
	public List<MobileElement> eleListsGetByXPath(String xPath) throws Throwable{
		return appHandler.findElementsByXPath(xPath);
	}
	
    /**
     * swipe to up
     * author:panyongjun
     * date:2019年8月14日
     */	
    public void swipeUp() {
        Dimension size = appHandler.manage().window().getSize();
        int height = size.height;
        int width = size.width;
        @SuppressWarnings("rawtypes")
		TouchAction act = new TouchAction(appHandler);
        act.longPress(PointOption.point(width / 2, 100))
                .moveTo(PointOption.point(width / 2, height - 100)).release()
                .perform();
    }

    /**
     * swipe to up
     * author:panyongjun
     * date:2019年8月14日
     */	
    public void swipeUp(int times) {
        for(int i=0; i<times; i++){
        	swipeUp();
        }
    }
    
    /**
     * swipe to down
     * author:panyongjun
     * date:2019年8月14日
     */
    public void swipeDown() {
        Dimension size = appHandler.manage().window().getSize();
        int height = size.height;
        int width = size.width;
        @SuppressWarnings("rawtypes")
		TouchAction act = new TouchAction(appHandler);
        act.longPress(PointOption.point(width / 2, height - 100))
                .moveTo(PointOption.point(width / 2, 100)).release().perform();
    }

    /**
     * swipe to down
     * author:panyongjun
     * date:2019年8月14日
     */
    public void swipeDown(int times) {
        for(int i=0; i<times; i++){
        	swipeDown();
        }
    }
    
    /**
     * swipe to left
     * author:panyongjun
     * date:2019年8月14日
     */
    public void swipeLeft() {
        Dimension size = appHandler.manage().window().getSize();
        int height = size.height;
        int width = size.width;
        @SuppressWarnings("rawtypes")
		TouchAction act = new TouchAction(appHandler);
        act.longPress(PointOption.point(width - 100, height / 2))
                .moveTo(PointOption.point(100, height / 2)).release().perform();
    }
    
    /**
     * swipe to left
     * author:panyongjun
     * date:2019年8月14日
     */
    public void swipeLeft(int times) {
        for(int i=0; i<times; i++){
        	swipeLeft();
        }
    }
    
    /**
     * swipe to right
     * author:panyongjun
     * date:2019年8月14日
     */  
	public void swipeRight() {
        Dimension size = appHandler.manage().window().getSize();
        int height = size.height;
        int width = size.width;
        @SuppressWarnings("rawtypes")
        TouchAction act = new TouchAction(appHandler);
        act.moveTo(PointOption.point(width - 100, height / 2)).release()
                .perform();
    }
	
    /**
     * swipe to right
     * author:panyongjun
     * date:2019年8月14日
     */ 
    public void swipeRight(int times) {
        for(int i=0; i<times; i++){
        	swipeRight();
        }
    }
}