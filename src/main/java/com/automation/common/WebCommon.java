package com.automation.common;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.exception.FrameworkException;
import com.automation.framework.AbastractBase;
import com.automation.framework.WebDriverManager;
import com.automation.listener.Log4jLogger;

/**
 * web公共方法类
 * @author 爱吃苹果的鱼
 */
public class WebCommon extends AbastractBase{
	
	Log4jLogger logger = new Log4jLogger();
	
	public WebCommon(RemoteWebDriver handler){
		driver = handler;
	}
	
	/**
	 * 登录后跳转至内部往来默认页
	 * @author panyongjun
	 * 2019年6月14日 下午3:41:00
	 * @throws Throwable
	 */
	public void toURL(String testUrl) throws Throwable{
		driver.navigate().to(testUrl);
		Thread.sleep(3000);
	}
	
	
	/**
	 * 检查checkbox是否已勾选
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public boolean isCheckedChk(By locator) throws Throwable {
		boolean isChecked = false;
		try{
			isChecked = eleAttrValueGet(locator, "checked").equalsIgnoreCase("true");
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return isChecked;
	}
	
	public void eleClickByJS(WebElement ele) throws Throwable {
		WebDriver wd = driver;
		((JavascriptExecutor)wd).executeScript("arguments[0].click();", ele);	
    }
	
	/**
	 * 在元素List中，随便选择一个click
	 * @param eleLists
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public int eleClickRandom(List<WebElement> eleLists) throws Throwable {
		int eleIndex = 0;
		try{
			Random rand = new Random();
			if(eleLists.size() > 1){
				eleIndex = rand.nextInt(eleLists.size());
				eleLists.get(eleIndex).click();
				
			}
			if(eleLists.size() == 1){
				eleLists.get(eleIndex).click();
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return eleIndex;
	}
	
	/**
	 * 在元素List中，随便选择一个click
	 * @param locator
	 * @author panyongjun
	 * @throws Throwable
	 */
	public int eleClickRandom(By locator) throws Throwable {
		int eleIndex = 0;
		try{
			Random rand = new Random();
			List<WebElement> eleLists = eleListsGet(locator);
			if(eleLists.size() > 1){
				eleIndex = rand.nextInt(eleLists.size());
				String eleTxt = eleLists.get(eleIndex).getText();
				if(!eleTxt.equalsIgnoreCase("")){
					logger.info("点击" + eleTxt);
				}else{
					String eleValue = eleLists.get(eleIndex).getAttribute("value");
					logger.info("点击" + eleValue);
				}
				eleLists.get(eleIndex).click();
				Thread.sleep(1000);
			}
			if(eleLists.size() == 1){
				eleLists.get(eleIndex).click();
				Thread.sleep(1000);
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return eleIndex;
	}
	
	/**
	 * 常用于页面跳转中，保留指定的页面
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void keepOnePageOpen(String expectedHandle) throws Throwable {
		try{
			Object[] handleArray = driver.getWindowHandles().toArray();
			for(Object handle:handleArray){
				driver.switchTo().window(handle.toString());
				if(handle.toString().equalsIgnoreCase(expectedHandle)){
					continue;
				}
				else{
					driver.close();
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}

	/**
	 * 常用于页面跳转中，保留一个页面
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void keepOnePageOpen() throws Throwable {
		int i = 1;
		Object[] handleArray = driver.getWindowHandles().toArray();
		int totalHandle = handleArray.length;
		for(Object handle:handleArray){
			driver.switchTo().window(handle.toString());
			if(i==totalHandle){
				break;
			}
			else{
				driver.close();
			}
			i++;
		}
	}
	
	/**
	 * 重新打开浏览器
	 * @param newUrl
	 * @param browserType
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public RemoteWebDriver newBrowserOpen(String newUrl, String browserType) throws Throwable {
		RemoteWebDriver theHandler = null;
		try{
			WebDriverManager webDriverManager = new WebDriverManager();
			theHandler = webDriverManager.getCurrentWebDriver(browserType);
			driver = theHandler;
			navigateToUrl(newUrl);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return theHandler;
	}
	
	/**
	 * 在浏览器中新打开一个browser tab
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void newBrowserTab() throws Throwable {
		try{
			Robot robot = new Robot();
			robot.keyPress(KeyEvent.VK_CONTROL);
			robot.keyPress(KeyEvent.VK_T);
			robot.keyRelease(KeyEvent.VK_CONTROL);
			robot.delay(100);
			robot.keyRelease(KeyEvent.VK_T);
			robot.delay(100);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 验证页面元素是否已显示
	 * @param by
	 * @param int 秒数， 如10
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean eleDisplayChk(final By by, int maxTime) throws Throwable {	
		try{
			WebDriver wd = driver;
			(new WebDriverWait(wd, maxTime, 1)).until
					(new ExpectedCondition<WebElement>(){
						public WebElement apply(WebDriver d){
							return d.findElement(by);
						}
					});
			return true;
		}catch(TimeoutException ex){
			logger.error("element is not visible");
			return false;
		}
	}
		
	/**
	 * 验证页面元素是否已显示
	 * @param by
	 * @return
	 * @throws Throwable
	 */
	public boolean eleDisplayChk(final By locator) throws Throwable {	
		try
		{
			WebDriver wd = driver;
			WebElement el = (WebElement) (new WebDriverWait(wd, MAX_TIME, 1)).until
					(new ExpectedCondition<WebElement>(){
						
						public WebElement apply(WebDriver d){
							return d.findElement(locator);
						}
					});
			return el.isDisplayed();
		}
		catch(TimeoutException ex)
		{
			logger.error("element:\\" + locator + " \\is not visible");
			return false;
		}
	}
	
	/**
	 * 页面元素hover
	 * @param locator
	 * @throws Throwable
	 */
	public void eleHoverAndWait(By locator) throws Throwable {
		Actions act = new Actions(driver);
		act.moveToElement(driver.findElement(locator)).perform();
//		handler.findElement(locator).hover();
		Thread.sleep(3000);
		//waitPageLoad(10);
	}
	
	/**
	 * 页面元素hover
	 * @param locator
	 * @throws Throwable
	 */
	public void englisheleHover(By locator) throws Throwable {
		Actions act = new Actions(driver);
		act.moveToElement(driver.findElement(locator)).perform();
		Thread.sleep(2000);
	}
	
	
	/**
	 * 页面元素属性值获取
	 * @param locator
	 * @param attribute
	 * @return
	 * @throws Throwable
	 */
	public List<String> eleAttrValueListGet(By locator, String attribute) throws Throwable {
		List<String> eleAttrValueList = new ArrayList<String>();
		List<WebElement> eleLists = eleListsGet(locator);
		for(WebElement ele:eleLists){
			String eleValue = ele.getAttribute(attribute);
			eleAttrValueList.add(eleValue);
		}
		return eleAttrValueList;
	}
	
	/**
	 * 页面元素属性值获取
	 * @param locator
	 * @param attribute
	 * @return
	 * @throws Throwable
	 */
	public String eleAttrValueGet(By locator, String attribute) throws Throwable {
		String eleAttrValue = "";
		WebElement webElement = driver.findElement(locator);
		eleAttrValue = StringUtils.trim(webElement.getAttribute(attribute));
		logger.info("元素的value值为" + eleAttrValue);
		return eleAttrValue;
	}
	
	/**
	 *  获取combo当前的文本值
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public String comboDefaultTextGet(By locator) throws Throwable {
		List<String> comboSelectedOptionTxtList = new ArrayList<String>();
		comboSelectedOptionTxtList = comboOptionTxtListGet(locator);
		for(String optionTxt:comboSelectedOptionTxtList){
			logger.info("当前combo选择的option为" + optionTxt);
		}
		return comboSelectedOptionTxtList.get(0);
	}
	
	/**
	 *  获取combo当前的文本值
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<String> comboDefaultTxtGet(By locator) throws Throwable {
		List<String> comboSelectedOptionTxtList = new ArrayList<String>();
		comboSelectedOptionTxtList = comboOptionTxtListGet(locator);
		for(String optionTxt:comboSelectedOptionTxtList){
			logger.info("当前combo选择的option为" + optionTxt);
		}
		return comboSelectedOptionTxtList;
	}
	
	/**
	 * 获取combo所有option的文本值
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<String> comboOptionTxtListGet(By locator) throws Throwable {
		List<String> comboOptionTxtList = new ArrayList<String>();
		WebElement select = driver.findElement(locator); 
		List<WebElement> optionList = select.findElements(By.tagName("option"));
		for(WebElement option:optionList){
			String optionTxt = option.getText().replace(" ", "");
			comboOptionTxtList.add(optionTxt);
		}
		return comboOptionTxtList;
	}

	/**
	 * 文本输入框清空
	 * @param ele
	 * @throws Throwable
	 */
	public void txtBoxClear(WebElement ele) throws Throwable {
		ele.clear();
	}
	
	/**
	 * 文本输入框清空
	 * @param locator
	 * @throws Throwable
	 */
	public void txtBoxClear(By locator) throws Throwable {
		driver.findElement(locator).clear();
	}

	/**
	 * 元素文本列表获取，保持文本的原始样式，不删除多去空格
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<String> eleTxtListsGetNotTrim(By locator)  throws Throwable {
		List<String> eleTxtLists = new ArrayList<String>();
		List<WebElement> eleLists = driver.findElements(locator);
		for(WebElement ele:eleLists)
		{
			String eleTxt = ele.getText();
			if(!eleTxt.replace(" ", "").equalsIgnoreCase(""))
			{
				eleTxtLists.add(eleTxt);
			}
		}
		return eleTxtLists;
	}
	
	/**
	 * 某元素下，元素文本列表获取，删除多去空格
	 * @param ele
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<String> eleTxtListsGet(WebElement topEle, By locator)  throws Throwable {
		List<String> eleTxtLists = new ArrayList<String>();
		List<WebElement> eleLists = topEle.findElements(locator);
		for(WebElement ele:eleLists)
		{
			String eleTxt = ele.getText().replace(" ", "");
			if(!eleTxt.equalsIgnoreCase(""))
			{
				eleTxtLists.add(eleTxt);
			}
		}
		return eleTxtLists;
	}
	
	/**
	 * 在文本列表中，随机选个文本值
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public String strGetFromArray(String[] strArray)  throws Throwable {
		String eleTxt = "";
		Random rand = new Random();
		if(strArray.length > 1){
			eleTxt = strArray[rand.nextInt(strArray.length)].replace(" ", "");
		}
		if(strArray.length == 1){
			eleTxt = strArray[0].replace(" ", "");
		}
		return eleTxt;
	}
	
	/**
	 * 在文本列表中，随机选个文本值
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public String strGetFromList(List<String> strList)  throws Throwable {
		String eleTxt = "";
		Random rand = new Random();
		if(strList.size() > 1){
			eleTxt = strList.get(rand.nextInt(strList.size()));
		}
		if(strList.size() == 1){
			eleTxt = strList.get(0);
		}
		return eleTxt;
	}
	
	/**
	 * 元素文本列表获取，不删除多去空格，允许添加空字符串到list
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<String> eleTxtListsGetAll(By locator)  throws Throwable {
		List<String> eleTxtLists = new ArrayList<String>();
		List<WebElement> eleLists = driver.findElements(locator);
		for(WebElement ele:eleLists)
		{
			String eleTxt = ele.getText();
			eleTxtLists.add(eleTxt);
		}
		return eleTxtLists;
	}
	
	/**
	 * 元素文本列表获取，删除多去空格
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<String> eleTxtListsGet(By locator)  throws Throwable {
		List<String> eleTxtLists = new ArrayList<String>();
		List<WebElement> eleLists = driver.findElements(locator);
		for(WebElement ele:eleLists)
		{
			String eleTxt = ele.getText().replace(" ", "").replaceAll("\r|\n", "");
			if(!eleTxt.equalsIgnoreCase(""))
			{
				eleTxtLists.add(eleTxt);
			}
		}
		return eleTxtLists;
	}
	
	/**
	 * css元素列表获取
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<WebElement> eleListsGetCss(By locator)  throws Throwable {
		return driver.findElements(locator);
	}
	
	/**
	 * 某元素下，元素列表获取
	 * @param ele
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<WebElement> eleListsGet(WebElement ele, By locator)  throws Throwable {
		List<WebElement> eleLists = null;
		if(eleDisplayChk(locator))
		{
			eleLists = ele.findElements(locator);
		}
		return eleLists;
	}
	
	/**
	 * 元素列表获取
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public List<WebElement> eleListsGet(By locator)  throws Throwable {
		return driver.findElements(locator);
	}

	/**
	 * 根据文本输入框属性及其值，获取文本输入框，并传值 
	 * @param locator
	 * @param attriName, it is unique
	 * @param attriValue, it is unique
	 * @param sendValue
	 * @throws Throwable
	 */
	public void txtBoxSendValue(By locator, String attriName, String attriValue, String sendValue) throws Throwable {
		List<WebElement> eleLists = driver.findElements(locator);
		for(WebElement ele:eleLists)
		{
			String eleAttriValue = ele.getAttribute(attriName);
			if(eleAttriValue.equalsIgnoreCase(attriValue))
			{
				txtBoxClear(ele);
				ele.sendKeys(sendValue);
				break;
			}
		}
	}
	
	/**
	 * 文本输入框传值
	 * @param locator, it is unique
	 * @param sendValue
	 * @throws Throwable
	 */
	public void txtBoxSendValue(By locator, String sendValue) throws Throwable {
		txtBoxClear(locator);
		logger.info("输入：" + sendValue);
		driver.findElement(locator).sendKeys(sendValue);
	}

	/**
	 * 文本输入框传值
	 * @param locator, it is unique
	 * @param sendValue
	 * @throws Throwable
	 */
	public void txtBoxValueSet(By locator, String sendValue) throws Throwable {
		WebElement input = driver.findElement(locator);
		((JavascriptExecutor)driver).executeScript("arguments[0].value=arguments[1]",input, sendValue);
	}
	
	/**
	 * 文本输入框传值
	 * @param locator, it is unique
	 * @param sendValue
	 * @throws Throwable
	 */
	public void txtBoxSendValueAction(By locator, String sendValue) throws Throwable {
		logger.info("输入：" + sendValue);
		Actions act = new Actions(driver);
		
		act.moveToElement(driver.findElement(locator)).sendKeys("");
		act.moveToElement(driver.findElement(locator)).sendKeys(sendValue);
	}
	
	/**
	 * 元素文本值获取
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public String eleTxtGetNotTrim(By locator) throws Throwable {
		String eleTxt = "";
		eleTxt = driver.findElement(locator).getText();
		return eleTxt;
	}
	
	/**
	 * 根据元素属性及值，获取元素，并获取文本值获取
	 * @param locator
	 * @param attrName, it is unique
	 * @param attrValue, it is unique
	 * @return
	 * @throws Throwable
	 */
	public String eleTxtGet(By locator, String attrName, String attrValue) throws Throwable {
		String eleTxt = "";
		List<WebElement> eleList = eleListsGet(locator);
		for(WebElement ele:eleList)
		{
			String eleValue = ele.getAttribute(attrName);
			if(eleValue.equalsIgnoreCase(attrValue))
			{
				eleTxt = ele.getText();
				break;
			}
		}
		return eleTxt;
	}
	
	/**
	 * 元素文本值获取
	 * @param locator
	 * @param byValue, it is unique
	 * @return
	 * @throws Throwable
	 */
	public String eleTxtGet(By locator, String byValue) throws Throwable {
		String eleTxt = "";
		List<WebElement> eleList = eleListsGet(locator);
		for(WebElement ele:eleList)
		{
			String eleValue = ele.getAttribute("value");
			if(eleValue.equalsIgnoreCase(byValue))
			{
				eleTxt = ele.getText();
				break;
			}
		}
		return eleTxt;
	}
	
	/**
	 * 元素tagName获取
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public String eleTagNameGet(By locator) throws Throwable {
		String tagName = "";
		if(eleDisplayChk(locator)){
			tagName = driver.findElement(locator).getTagName();
		}
		return tagName;
	}
	
	/**
	 * 元素文本值获取
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public String eleTxtGet(By locator) throws Throwable {
		String eleTxt = "";
		if(eleDisplayChk(locator)){
			eleTxt = driver.findElement(locator).getText().replace(" ", "");
		}
		return eleTxt;
	}
	
	/**
	 * 根据元素属性及值，获取元素，并点击
	 * @param locator
	 * @param attrName
	 * @param attrValue
	 * @throws Throwable
	 */
	public void eleClickBy(By locator, String attrName, String attrValue) throws Throwable {
		List<WebElement> eleLists = eleListsGet(locator);
		for(WebElement ele:eleLists)
		{
			String eleValue = ele.getAttribute(attrName);
			if(eleValue.equalsIgnoreCase(attrValue))
			{
				ele.click();
				logger.info("点击" + eleValue);
				break;
			}
		}
    }
	
	/**
	 * 根据元素文本，获取元素，并点击
	 * @param locator
	 * @param byValue
	 * @throws Throwable
	 */
	public void eleClickByText(By locator, String byText) throws Throwable {
		List<WebElement> eleLists = eleListsGet(locator);
		for(WebElement ele:eleLists)
		{
			String eleValue = ele.getText().replace(" ", "");
			if(eleValue.contains(byText))
			{
				ele.click();
				Thread.sleep(1000);
				logger.info("点击" + eleValue);
				break;
			}
		}
    }
	
	/**
	 * 根据元素文本，获取元素，并点击
	 * @param locator
	 * @param byValue
	 * @throws Throwable
	 */
	public void eleClickByText(List<WebElement> eleList, String byText) throws Throwable {
		for(WebElement ele:eleList)
		{
			String eleValue = ele.getText().replace(" ", "");
			if(eleValue.contains(byText))
			{
				ele.click();
				Thread.sleep(1000);
				logger.info("点击" + eleValue);
				break;
			}
		}
    }
	
	/**
	 * 根据元素value，获取元素，并点击
	 * @param locator
	 * @param byValue
	 * @throws Throwable
	 */
	public void eleClickByValue(List<WebElement> eleList, String byValue) throws Throwable {
		for(WebElement ele:eleList)
		{
			String eleValue = ele.getAttribute("value").replace(" ", "");
			if(eleValue.contains(byValue))
			{
				ele.click();
				Thread.sleep(1000);
				logger.info("点击" + eleValue);
				break;
			}
		}
    }
	
	/**
	 * 根据元素显示的文本，获取元素，并点击. (不去空格)
	 * @param locator
	 * @param byValue
	 * @throws Throwable
	 */
	public void eleClickByTrueText(By locator, String byText) throws Throwable {
		List<WebElement> eleLists = eleListsGet(locator);
		for(WebElement ele:eleLists)
		{
			String eleTxt = ele.getText();
			if(eleTxt.contains(byText))
			{
				ele.click();
				logger.info("点击" + eleTxt);
				break;
			}
		}
    }
	
	/**
	 * 根据元素value值，获取元素，并点击
	 * @param locator
	 * @param byValue
	 * @throws Throwable
	 */
	public void eleClickByValue(By locator, String byValue) throws Throwable {
		List<WebElement> eleLists = eleListsGet(locator);
		for(WebElement ele:eleLists)
		{
			String eleValue = ele.getAttribute("value");
			if(eleValue.equalsIgnoreCase(byValue))
			{
				ele.click();
				logger.info("点击" + eleValue);
				break;
			}
		}
    }
	
	/**
	 * 元素点击
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void eleClickByAction(WebElement ele) throws Throwable {
		Actions action = new Actions(driver);
		action.moveToElement(ele).build().perform();
		action.moveToElement(ele).click().perform();
	}
	
	/**
	 * 通过seleniumAction方法点击元素
	 * @param locator
	 * @throws Throwable
	 */
	public void eleClickByAction(By locator) throws Throwable {
		Actions action = new Actions(driver);
		action.moveToElement(driver.findElement(locator)).click().perform();
	}
	
	/**
	 * 元素点击
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void eleClickByAction(int x, int y) throws Throwable {
		Actions action = new Actions(driver);
		action.moveByOffset(x, y).click().perform();
	}
	
	/**
	 * 元素点击
	 * @param locator, it is the unique
	 * @throws Throwable
	 */
	public void eleClickByCss(By locator) throws Throwable {
		try{
			String eleTxt = eleTxtGet(locator);
			if(!eleTxt.equalsIgnoreCase("")){
				logger.info("点击" + eleTxt);
			}else{
				String eleValue = eleAttrValueGet(locator, "value");
				logger.info("点击" + eleValue);
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		driver.findElement(locator).click();
    }
	
	/**
	 * 元素双击
	 * @param locator
	 * @throws Throwable
	 */
	public void eleDoubleClickBy(By locator) throws Throwable {
		String eleTxt = eleTxtGet(locator);
		if(!eleTxt.equalsIgnoreCase("")){
			logger.info("双击" + eleTxt);
		}else{
			String eleValue = eleAttrValueGet(locator, "value");
			logger.info("双击" + eleValue);
		}
		Actions act = new Actions(driver);
		act.doubleClick(driver.findElement(locator)).build().perform();
		Thread.sleep(1000);
	}
	
	public void eleContextClickBy(By locator) throws Throwable {
		WebDriver wd=driver;
		Actions action = new Actions(wd); 
		if(eleDisplayChk(locator))
		{
			action.contextClick(wd.findElement(locator)).perform();// 鼠标右键点击指定的元素
			waitPageLoad(10);		
		}
	}
	
	/**
	 * 元素点击
	 * @param locator
	 * @throws Throwable
	 */
	public void eleClickByDriver(By locator) throws Throwable {
		String eleTxt = driver.findElement(locator).getText();
		if(!eleTxt.equalsIgnoreCase("")){
			logger.info("点击" + eleTxt);
		}else{
			String eleValue = driver.findElement(locator).getAttribute("value");
			logger.info("点击" + eleValue);
		}
		driver.findElement(locator).click();
		Thread.sleep(1000);
    }
	
	/**
	 * 元素点击
	 * @param locator, it is the unique
	 * @throws Throwable
	 */
	public void eleClickBy(By locator) throws Throwable {
		String eleTxt = eleTxtGet(locator);
		if(!eleTxt.equalsIgnoreCase("")){
			logger.info("点击" + eleTxt);
		}else{
			String eleValue = eleAttrValueGet(locator, "value");
			logger.info("点击" + eleValue);
		}
		driver.findElement(locator).click();
		Thread.sleep(1000);
    }

	/**
	 * 文本输入框内容获取
	 * @param locator
	 * @return
	 * @throws Throwable
	 */
	public String txtBoxEleValueGet(By locator) throws Throwable {
		String txtBoxValue = "";
		try{
			if(eleDisplayChk(locator))
			{
				txtBoxValue = eleAttrValueGet(locator, "value");
			}
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return txtBoxValue;
	}

	/**
	 * 对新打开的页面窗口，页面跳转至新的页面
	 * @return
	 * @throws Throwable
	 */
	public void switchToNewHandle(Object[] handlerArray, String oriHandler) throws Throwable {
		if(handlerArray.length > 1)
		{
			for(Object handlerA:handlerArray)
			{
				if(handlerA.equals(oriHandler))
				{
					continue;
				}
				else
				{
					driver.switchTo().window(handlerA.toString());
				}
			}
		}
	}
	
	/**
	 * alert是否存在检查
	 * @return
	 * @throws Throwable
	 */
	public boolean alertIsPresent() throws Throwable {
		try{
			driver.switchTo().alert();
			return true;
		}
		catch(Exception ex)
		{
			//log.error("no alert is appeared");
			return false;
		}
	}

	/**
	 * alert文本检查
	 * @param expectedTxt
	 * @return
	 * @throws Throwable
	 */
    public String alertTextGet() throws Throwable {
		return driver.switchTo().alert().getText().trim();
	}
	
	/**
	 * alert文本检查
	 * @param expectedTxt
	 * @return
	 * @throws Throwable
	 */
    public boolean alertTextChk(String expectedTxt) throws Throwable {
		boolean alertIsMatch = false;
		try{
			String alertText = driver.switchTo().alert().getText().trim();
			if(alertText.equalsIgnoreCase(expectedTxt))
			{
				alertIsMatch = true;
			}
		}
		catch(Exception ex)
		{
			exceptionErrorHandle(ex);
		}
		return alertIsMatch;
	}
	
    /**
     * alert弹出框，accept
     * @param isAccept
     * @throws Throwable
     */
	public void alertAccept(boolean isAccept) throws Throwable {
		try{
			if(isAccept)
			{
				logger.info(driver.switchTo().alert().getText());
				driver.switchTo().alert().accept();
			}
			else
			{
				logger.info(driver.switchTo().alert().getText());
				driver.switchTo().alert().dismiss();
			}
		}
		catch(Exception ex)
		{
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * 异常处理，截图并写入日志文件
	 * @param ex
	 * @throws Throwable
	 */
	public void exceptionErrorHandle(FrameworkException ex) throws FrameworkException {
		logger.error(ex.getMessage());
		throw new FrameworkException(ex.getMessage());
	}
	
	/**
	 * 异常处理，截图并写入日志文件
	 * @param ex
	 * @throws Throwable
	 */
	public void exceptionErrorHandle(Throwable ex) throws Throwable {
		String exceptionErrorScreenShot = caseName + "exceptionError" + "_screenShot" + 
				(new Random()).nextInt(10000000) + ".jpg";
		takeSnapShot(exceptionErrorScreenShot);
		logger.error(ex.getMessage());
	}
	
	/**
	 * 对整个桌面进行截图
	 * @param downFile
	 */
	 public void takeSnapShot(String deskTopSnapShot) {
		 try {
			 Date dateTimeNow = new Date();
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			 Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
			 Robot robot = new Robot();
			 BufferedImage bi=robot.createScreenCapture(new Rectangle(screenSize.width,screenSize.height)); // 抓取整个桌面
			 (new File("D:\\failCaseSnapShot")).mkdir();
			 String desktopImageName = deskTopSnapShot + "_" + sdf.format(dateTimeNow) + ".jpg";
			 ImageIO.write(bi, "jpg", new File("D:/failCaseSnapShot/" + desktopImageName)); //把抓取到的内容写入到一个jpg文件中
			 caseScreenShotList1.add(desktopImageName);
		 }catch (Exception ex) {
			 System.out.println(ex);
		 }
	}

	/**
	 * 页面加载等待（最多30秒）
	 * @param waitSeconds
	 * @author panyongjun
	 * @throws FrameworkException
	 */
	public void waitPageLoad(int waitSeconds) throws FrameworkException {
		try{
			WebDriver wd = driver;
			Object result = ((JavascriptExecutor)wd).executeScript("return document['readyState']" ); 
			int iCount = 0;      
			while (iCount < waitSeconds && iCount < 30){
				result = ((JavascriptExecutor) wd).executeScript("return document['readyState']" );
				if(result.toString().equalsIgnoreCase("complete")){
					return;
				}
				iCount++;
				Thread.sleep(1000);
			}
			throw new Exception("Page load time out");
		} catch(Exception e) {
			throw new FrameworkException(e.getMessage());
		}
	}
	
	public void navigateToUrl(String url) throws Throwable {
		driver.navigate().to(url);
	}
	
	/**
	 * execute js with none return value
	 * @param js
	 * @author 爱吃苹果的鱼
	 */
	public void executeJS(String js) throws Throwable {
		if (null != js && js.length() > 0) {
			javascriptExecutor.executeScript(js);
		}
	}
	
	/**
	 *execute js and return a value of string
	 * @param js
	 * @author 爱吃苹果的鱼
	 * @throws
	 * @return
	 */
	public String executeJsReturnString(String js) throws Throwable {
		String result = null;
		if (!StringUtils.isEmpty(js)) {
			result = (String) javascriptExecutor.executeScript(js);
		}
		return result;
	}
	
	/**
	 *open a new borrower window
	 *@author 爱吃苹果的鱼
	 */
	public void openNewWindow() throws Throwable {
		String js = "var result = window.open(\"about:blank\")";
		String result = executeJsReturnString(js);
		System.out.println(result);
	}
	
	/**
	 * close window
	 * @author 爱吃苹果的鱼
	 */
	public void closeWindow() throws Throwable {
		String js = "var result = window.close()";
		String result = executeJsReturnString(js);
		System.out.println(result);
	}
	
	/**
	 * judge an element is visible or not
	 * @param locator
	 * @author 爱吃苹果的鱼
	 */
	public boolean isElementVisible(By locator) {

		try {
			wait=new WebDriverWait(driver, 30);
			wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
			//log.info("[" + locator + "],element is visible!");
			return true;
		} catch (Exception e) {
			//log.error("[" + locator + "],element is invisible!");
			return false;
		}
	}
	
	/**
	 * 浏览器放大/缩小显示
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void zoomInOrOut(int zoomTime) throws Throwable {
		Actions action = new Actions(driver);
		for(int i=0; i<Math.abs(zoomTime); i++){
			if(Math.abs(zoomTime) == zoomTime){
				action.keyDown(Keys.CONTROL).sendKeys(Keys.ADD).keyUp(Keys.CONTROL).perform(); 
			}else{
				action.keyDown(Keys.CONTROL).sendKeys(Keys.SUBTRACT).keyUp(Keys.CONTROL).perform(); 
			}
		}
		Thread.sleep(3000);
	}
	
	/**
	 * 向左移动
	 * @param locator
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollToLeft(By locator) throws Throwable {
		WebElement element = driver.findElement(locator);
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft=0;", element);
	}
	
	/**
	 * 向右移动
	 * @param locator
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollToRight(By locator) throws Throwable {
		WebElement element = driver.findElement(locator);
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollLeft=10000;", element);
	}
	
	/**
	 * 向下移动
	 * @param locator
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollToDown(By locator) throws Throwable {
		WebElement element = driver.findElement(locator);
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop=10000;", element);
	}
	
	/**
	 * 向上移动
	 * @param locator
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollToTop(By locator) throws Throwable {
		WebElement element = driver.findElement(locator);
	    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollTop=0;", element);
	}
	
	/**
	 * 根据指定元素id滚动
	 * @param id
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollToElementById(String id) throws Throwable{
		((JavascriptExecutor) driver).executeScript("document.getElementById(\"" + id + "\").scrollIntoView()");
	}
	
	/**
	 * 向下滚动
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollToDown() throws Throwable {
		((JavascriptExecutor)driver).
		executeScript("window.scrollTo(0,10000)");			
	}
	
	/**
	 * 向上滚动
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollToTop() throws Throwable {
		((JavascriptExecutor)driver).
		executeScript("window.scrollTo(0,0)");			
	}
	
	/**
	 * 向右滚动
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollToRight() throws Throwable {
		((JavascriptExecutor)driver).
		executeScript("window.scrollTo(10000,0)");			
	}
	
	/**
	 * 滚动到指定位置
	 * @param x
	 * @param y
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void scrollTo(int x, int y) throws Throwable {
		((JavascriptExecutor)driver).
		executeScript("window.scrollTo(" + x + "," + y + ")");		
	}
}
