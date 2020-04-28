package com.automation.framework;

import java.awt.Robot;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Platform;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.config.LogReConfig;
import com.automation.listener.Log4jLogger;
import com.automation.utils.FileUtil;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.MobileElement;

/**
 * auto test 基础类
 * @author panyongjun
 * July 12, 2016
 */

public class AbastractBase {
	public static String url = "";//测试链接
	Log4jLogger logger = new Log4jLogger();
	FileUtil fileUtil = new FileUtil();
	public RemoteWebDriver driver = null;
	public AppiumDriver<MobileElement> appHandler;
	public  WebDriverWait wait = null;
	protected JavascriptExecutor javascriptExecutor	= null;
	public static String caseName = "";//用例名
	public static final long MAX_TIME = 5;
	public static final long MAX_EXCUTE_TIME = 5 * 10 * 6000;
	public static final String TEST_REPORT_OUTPUT_FOLDER = "test-output";
	public static List<String> caseScreenShotList1 = new ArrayList<String>();//测试结果截图
	public String testReportName = "";//测试report	
	public static String emulationName = "";//H5 emulation
	public String testEnv = "";//test-测试环境，pre-预生产环境，pro-生产环境
	

	/**
	 * 强制关闭进程
	 * @param processName
	 * @throws Throwable
	 */
    public void killProcess(String processName) throws Throwable {
    	BufferedReader bufferedreader = null;
        Process proc = Runtime.getRuntime().exec("cmd.exe /c start taskkill /F /IM " + processName);
        bufferedreader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
        String line = null;
        while ((line = bufferedreader.readLine()) != null) {
                System.out.println(line);
        }
    }
	
	/**
	 * 本机ip地址获取
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String localHostIPGet() throws Throwable{
		String ip = new String();
		InetAddress inetAdd = InetAddress.getLocalHost();
		ip = inetAdd.getHostAddress().toString();
		return ip;
	}
	
	/**
	 * 杀死指定浏览器进程
	 * @param browserType
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void browserKill(String browserType) throws Throwable {
		switch(browserType.toLowerCase())
		{
			case "firefox":Runtime.getRuntime().exec("taskkill /F /IM firefox.exe").waitFor();
				Runtime.getRuntime().exec("taskkill /F /IM firefoxdriver.exe").waitFor();break;
			case "chrome":Runtime.getRuntime().exec("taskkill /F /IM chrome.exe").waitFor();
				Runtime.getRuntime().exec("taskkill /F /IM chromedriver.exe").waitFor();break;
			case "ie":Runtime.getRuntime().exec("taskkill /F /IM iexplore.exe").waitFor();
				Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer.exe").waitFor();
				//Runtime.getRuntime().exec("taskkill /F /IM IEDriverServer_64.exe").waitFor();
				break;
			default:break;
		}
		Thread.sleep(5000);
	}
	
	/**
	 * 焦点移动到指定元素
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void moveToEle(WebElement ele) throws Throwable{
		Actions act = new Actions(driver);
		act.moveByOffset(ele.getLocation().x, ele.getLocation().y).build().perform();
		Thread.sleep(1000);
	}
	
	/**
	 * 焦点移动到指定元素
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public void moveToEle(By locator) throws Throwable{
		Actions act = new Actions(driver);
		act.moveToElement(driver.findElement(locator)).perform();
		Thread.sleep(1000);
	}
	
	/**
	 * 判断字符串是否为日期
	 * @param str
	 * @param dateFormat
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean isDate(String str, String dateFormat) throws Throwable {
		if(str.equals("")){
			logger.info("日期为空");
			return false;
		}else{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			if(sdf.parse(str) != null){
				return true;
			}else {
				return false;
			}
		}
    }
	
	/**
	 * 日期格式转换
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	@SuppressWarnings("deprecation")
	public String dateFormatTransfer(String date, String format) throws Throwable {
		if(!date.equalsIgnoreCase("")){
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			return sdf.format(new Date(date));
		}else {
			return "日期为空";
		}
	}
	
	/**
	 * 日期格式转换，从格式dateFormat1转换成dataFormat2
	 * @param date
	 * @param dateFormat1
	 * @param dateFormat2
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String dateFormatTransfer(String date, String oriFormat, String newFormat) throws Throwable {
		if(!date.equalsIgnoreCase("")){
			SimpleDateFormat sdf1 = new SimpleDateFormat(oriFormat);
			SimpleDateFormat sdf2 = new SimpleDateFormat(newFormat);
			return sdf2.format(sdf1.parse(date));
		}else {
			return "日期为空";
		}
	}
	
	/**
	 * 阿拉伯数字月份转换为中文月份
	 * @param locator
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String monthTransferToZh(String monthNum) throws Throwable {
		switch(monthNum){
		case "01": return "一月";
		case "03": return "三月";
		case "04": return "四月";
		case "05": return "五月";
		case "06": return "六月";
		case "07": return "七月";
		case "08": return "八月";
		case "09": return "九月";
		case "10": return "十月";
		case "11": return "十一月";
		case "12": return "十二月";
		default: return "月份输入非法";
	}
	}
	
	/**
	 * txt文件写入信息
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void writeToTxt(String txtFilePath, String fileStreamStr, boolean isAppend) throws Throwable {
		File txtFile = new File(txtFilePath);
		if(!txtFile.exists()){
			txtFile.createNewFile();
		}
		BufferedReader br = new BufferedReader(new FileReader(txtFile));
		BufferedWriter bw = new BufferedWriter(new FileWriter(txtFile, isAppend));
		try{
			if(br.readLine() != null){
				bw.write("\r\n");
				br.close();
			}
			bw.write(fileStreamStr);
	
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}finally{
			bw.flush();
			bw.close();	
		}
	}
	
	/**
	 * 根据进程名，检查进程是否存在
	 * @param processName
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean processIsExisted(String processName) throws Throwable {
		BufferedReader bufferedReader = null;  
		try{
			Process proc = Runtime.getRuntime().exec("tasklist");
			bufferedReader = new BufferedReader(new InputStreamReader(proc.getInputStream()));
			String line = null;
			while((line = bufferedReader.readLine()) != null){
				if(line.contains(processName)){
					return true;
				}
			}
			return false;
		}catch(Exception ex){
			exceptionErrorHandle(ex);
			return false;
		}finally{
			if(bufferedReader != null){
				bufferedReader.close();
			}
		}
	}
	
	/**
	 * 终止可能出现的上传下载autoit进程
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void taskKill(String processName) throws Throwable {
		Runtime.getRuntime().exec("taskkill /F /IM " + processName).waitFor();
	}
	
	public int eleIndexGetFromEleList(List<WebElement> list, WebElement ele) throws Throwable{
		int index = 0;
		for(Object item:list) {
			if(item.equals(ele)) {
				break;
			}
			index++;
		}
		return index;
	}
	
	/**
	 * 获取str在list中的索引
	 * @param list
	 * @param strInList
	 * @author panyongjun
	 * @return
	 * @throws Throwable
	 */
	public int strIndexGetFromList(List<String> list, String strInList) throws Throwable {
		int index = 0;
		try{
			for(String str:list){
				if(str.equalsIgnoreCase(strInList)){
					logger.info("当前字符串在list中的索引为" + index);
					break;
				}
				index++;
				if(index == list.size()){
					logger.info("当前list中没有" + strInList);
					index = -1;
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return index;
	}
	
	/**
	 * 获取桌面分辨率，并返回，格式为1920 x 1080
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String desktopSizeGet() throws Throwable {
		Toolkit tk = Toolkit.getDefaultToolkit();
        int width = tk.getScreenSize().width; //返回宽度
        int height = tk.getScreenSize().height;//高度        
        logger.info("desktopSize");
        return width + " x " + height;
	}
	
	/**
	 * 删除list中指定的字符串
	 * @param oriList
	 * @param removeStr
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public List<String> listRemoveStr(List<String> oriList, String removeStr) throws Throwable {
		try{
			for(int i = 0; i < oriList.size(); i++){
			    if(oriList.get(i).replaceAll("\\s+", "").equalsIgnoreCase(removeStr)){
			    	oriList.remove(i);
			    }
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return oriList;
	}
	
	/**
	 * 删除list中重复的item
	 * @param oriList
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public List<String> listRemoveDuplicate(List<String> oriList) throws Throwable {
		ArrayList<String> newList = new ArrayList<String>();
		try{
			for(String s: oriList){
			    if(Collections.frequency(newList, s) < 1){
			    	newList.add(s);
			    }
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return newList;
	}
	
	/**
	 * 返回两个list的组合，并去重
	 * @param list1
	 * @param list2
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public List<String> listReturnMerge(List<String> list1, List<String> list2) throws Throwable {
		List<String> mergeList = new ArrayList<String>();
		try{
			for(String str1:list1){
				mergeList.add(str1);
			}
			for(String str2:list2){
				mergeList.add(str2);
			}
			mergeList = listRemoveDuplicate(mergeList);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return mergeList;
	}
	
	/**
	 * 返回两个list中不同
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public List<String> list2ReturnDiff(List<String> list1, List<String> list2) throws Throwable {
		List<String> diffList = new ArrayList<String>();
		try{
			for(String str2:list2){
				if(!list1.contains(str2)){
					diffList.add(str2);
				}
			}
			diffList = listRemoveDuplicate(diffList);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return diffList;
	}
	
	/**
	 * 返回两个list中不同
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public List<String> list1ReturnDiff(List<String> list1, List<String> list2) throws Throwable {
		List<String> diffList = new ArrayList<String>();
		try{
			for(String str1:list1){
				if(!list2.contains(str1)){
					diffList.add(str1);
				}
			}
			diffList = listRemoveDuplicate(diffList);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return diffList;
	}
	
	/**
	 * 返回两个list中不相等的字符串
	 * @param list1
	 * @param list2
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public List<String> listReturnDiff(List<String> list1, List<String> list2) throws Throwable {
		List<String> diffList = new ArrayList<String>();
		try{
			for(String str1:list1){
				if(!list2.contains(str1)){
					diffList.add(str1);
				}
			}
			for(String str2:list2){
				if(!list1.contains(str2)){
					diffList.add(str2);
				}
			}
			diffList = listRemoveDuplicate(diffList);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return diffList;
	}
	
	/**
	 * 返回两个list中相等的字符串
	 * @param list1
	 * @param list2
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public List<String> listReturnSame(List<String> list1, List<String> list2) throws Throwable {
		List<String> sameList = new ArrayList<String>();
		try{
			for(String str1:list1){
				if(list2.contains(str1)){
					sameList.add(str1);
				}
			}
			for(String str2:list2){
				if(list1.contains(str2)){
					sameList.add(str2);
				}
			}
			sameList = listRemoveDuplicate(sameList);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return sameList;
	}
	
	/**
	 * 检查两个list是否相等
	 * @param list1
	 * @param list2
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean listCompare(List<String> list1, List<String> list2) throws Throwable {
		boolean isSame = false;
		try{
			if(list1.equals(list2)){
				isSame = true;
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return isSame;
	}
	
	/**
	 * list与array比对
	 * @param testList
	 * @param testArray
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean listArrayCompare(List<String> testList, String[] testArray) throws Throwable {
		boolean isSame = false;
		try{
			if(testList.size()==testArray.length){
				for(int i=0; i<testList.size(); i++){
					if(!testList.get(i).equals(testArray[i])){
						isSame = false;
						break;
					}
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return isSame;
	}
	
	/**
	 * 数据meger
	 * @param array1
	 * @param array2
	 * @author panyongjun
	 */
    public String[] arrayMerge(String[] array1, String[] array2) throws Throwable {
    	String[] tempArray = new String[array1.length + array2.length];
        for(int i = 0;i < array1.length;i++){
        	tempArray[i] = array1[i];
        }
        for(int j = 0;j < array2.length;j++){
        	tempArray[array1.length+j] = array2[j];
        }
        return tempArray;
    }
	
	/**
	 * list转为array
	 * @param strList
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public Object[] listToArray(ArrayList<Object> strList) throws Throwable {
		Object[] newArray = null;
		for(int i=0; i<strList.size(); i++){
			System.out.println(strList.get(i));
			if(i==0){
				Object[] testArray = {strList.get(0)};
				newArray = testArray;
			}else{
				Object[] tempArray = new Object[newArray.length + 1];
				System.arraycopy(newArray, 0, tempArray, 0, newArray.length);
				tempArray[newArray.length] = strList.get(i);
				newArray = tempArray;
			}
		}
		return newArray;
	}
	
	/**
	 * array转为list
	 * @param strArray
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public ArrayList<Object> arrayToList(Object[] strArray) throws Throwable {
		ArrayList<Object> newList = new ArrayList<Object>();
		for(Object str:strArray){
			newList.add(str);
		}
		return newList;
	}
	
	/**
	 * 检查list2是否包含list1
	 * @param list1
	 * @param list2
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean listIsIncludedInList(List<String> list1, List<String> list2) throws Throwable {
		for(String item:list1){
			if(list2.contains(item)){
				continue;
			}else{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 在输入框按回车
	 * @param locator
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void txtBoxEnterPress(By locator) throws Throwable {
		Actions action = new Actions(driver);
		action.sendKeys(driver.findElement(locator), Keys.ENTER).perform();
		Thread.sleep(2000);
	}
	
	/**
	 * 随进从一个数组中提取一个字符串
	 * @param objects
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public int strIndexGetFromArray(Object[] objects, String expectedStr) throws Throwable {
		int returnStrIndex = 0;
		for(Object str:objects){
			if(str.toString().equalsIgnoreCase(expectedStr)){
				break;
			}
			returnStrIndex++;
		}
		return returnStrIndex;
	}
	
	/**
	 * 随进从一个数组中提取一个字符串
	 * @param strArray
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String strRandomGetFromArray(ArrayList<String> strArray) throws Throwable {
		Random rand = new Random();
		if(strArray.size() != 0 && strArray.size() > 1){
			return strArray.get(rand.nextInt(strArray.size()));
		}else if(strArray.size() == 1){
			return strArray.get(0);
		}else {
			return "数组为空";
		}
	}
	
	/**
	 * return random str from list
	 * @param lists
	 * @return
	 * @throws Throwable
	 */
	public String strGetFromList(List<String> lists) throws Throwable{
		Random rand = new Random();
		if(lists.size() != 0 && lists.size() > 1) {
			return lists.get(rand.nextInt(lists.size()));
		}else if(lists.size() == 1){
			return lists.get(0);
		}else {
			return "列表为空";
		}
	}
	
	/**
	 * 随进从一个数组中提取一个字符串
	 * @param strArray
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String strRandomGetFromArray(String[] strArray) throws Throwable {
		Random rand = new Random();
		if(strArray.length != 0 && strArray.length > 1){
			return strArray[rand.nextInt(strArray.length)];
		}else if(strArray.length == 1){
			return strArray[0];
		}else {
			return "数组为空";
		}
	}
	
	/**
	 * 验证日期格式是否正确
	 * @param dateFormat
	 * @param dateStrList
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean dateFormatChk(String dateFormat, List<String> dateStrList) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		for(String dateStr:dateStrList){
			Date date = sdf.parse(dateStr);
			boolean isMatch = dateStr.equalsIgnoreCase(sdf.format(date));
			if(!isMatch){
				return false;
			}else {
				continue;
			}
		}
		return true;
	}
	
	/**
	 * 当前是星期几获取
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public int weekDayGet() throws Throwable {
		Calendar dateNow = Calendar.getInstance();
		return dateNow.get(Calendar.DAY_OF_WEEK) - 1;
	}
	
	/**
	 * 获取当前日期之前一周的日期列表
	 * @param dateFormat, yyyy-MM-dd
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public List<String> dateListGet(String dateFormat, int latestDays) throws Throwable {
		List<String> dateList = new ArrayList<String>();
		try{
			boolean isSubtract = Integer.valueOf(latestDays).toString().contains("-");
			if(isSubtract){
				latestDays = Math.abs(latestDays);
			}
			for(int i=0; i<latestDays; i++){
				Calendar cal = Calendar.getInstance();
				if(isSubtract){
					cal.add(Calendar.DAY_OF_YEAR, -i);
				}else{
					cal.add(Calendar.DAY_OF_YEAR, i);
				}
				Date dateUserWant = cal.getTime();
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
				String date = sdf.format(dateUserWant);
				dateList.add(date);
			}
			Collections.sort(dateList);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return dateList;
	}
	
	/**
	 * 获取指定格式的系统当前日期，  e.g. yyyy-MM-dd, 2017-04-18
	 * @param dateFormat
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String systemCurrentDate(String dateFormat) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		return sdf.format(new Date());
	}
	
	/**
	 * 根据当前日期加/减天数，获取xx天前或xx天后的相应日期
	 * @param addDay
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String systemCurrentDate(int addDay) throws Throwable {
		GregorianCalendar gcal = new GregorianCalendar();
		gcal.add(Calendar.DAY_OF_MONTH, addDay);
		String year = Integer.valueOf(gcal.get(Calendar.YEAR)).toString();
		String month = "";
		if((gcal.get(Calendar.MONTH) + 1) < 10){
			month = "0" + Integer.valueOf(gcal.get(Calendar.MONTH) + 1).toString();
		}else{
			month = Integer.valueOf(gcal.get(Calendar.MONTH) + 1).toString();
		}
		String day = "";
		if((gcal.get(Calendar.DAY_OF_MONTH)) < 10){
			day = "0" + Integer.valueOf(gcal.get(Calendar.DAY_OF_MONTH)).toString();
		}else{
			day = Integer.valueOf(gcal.get(Calendar.DAY_OF_MONTH)).toString();
		}
		return year + "-" + month + "-" + day;
	}
	
	/**
	 * 获取系统当前日期 e.g. 2017-4-18
	 * @return
	 * @throws Throwable
	 * @author panyongjun
	 */
	public String systemCurrentDate() throws Throwable {
		return systemCurrentYear() + "-" +
				systemCurrentMonth() + "-" +
				systemCurrentDay();
	}
	
	/**
	 * 获得某个月最大天数
	 * 
	 * @param year 年份
	 * @param month 月份 (1-12)
	 * @return 某个月最大天数
	 */
	public int maxDayGet(int year, int month) {
	  Calendar calendar = Calendar.getInstance();
	  calendar.set(year, month, 1);
	  calendar.add(Calendar.DAY_OF_YEAR, -1);
	  return calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	/**
	 * 获取系统当前日
	 * @return
	 * @throws Throwable
	 * @author panyongjun
	 */
	public String systemCurrentDay() throws Throwable {
		GregorianCalendar gcal = new GregorianCalendar();
		return Integer.valueOf(gcal.get(Calendar.DAY_OF_MONTH)).toString();
	}
	
	/**
	 * 获取系统当前月
	 * @return
	 * @throws Throwable
	 * @author panyongjun
	 */
	public String systemCurrentMonth() throws Throwable {
		GregorianCalendar gcal = new GregorianCalendar();
		return Integer.valueOf(gcal.get(Calendar.MONTH) + 1).toString();
	}
	
	/**
	 * 获取系统当前年
	 * @return
	 * @throws Throwable
	 * @author panyongjun
	 */
	public String systemCurrentYear() throws Throwable {
		GregorianCalendar gcal = new GregorianCalendar();
		return Integer.valueOf(gcal.get(Calendar.YEAR)).toString();
	}
	
	/**
	 * 日期比对（常用于检查查询结果集检查）
	 * @param dateStr1
	 * @param dateStr2
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean dateCompare(String dateStr1, String dateStr2) throws Throwable {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if(sdf.parse(dateStr1).compareTo(sdf.parse(dateStr2)) <= 0){
			logger.info("两个日期相等/第二个日期在第一个日期之后");
			return true;			
		}
		else{
			logger.info("第二个日期小于第一个日期");
			return false;		
		}
	}
	
	/**
	 * 两个日期的年份差值
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public int dateDiffByYear(String dateStr1, String dateStr2) throws Throwable{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(dateStr2));
		int value2 = cal.get(Calendar.YEAR);
		cal.setTime(sdf.parse(dateStr1));
		int value1 = cal.get(Calendar.YEAR);
		return value2-value1;
	}
	
	/**
	 * 两个日期的月份差值
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public int dateDiffByMonth(String dateStr1, String dateStr2) throws Throwable{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(dateStr2));
		int value2 = cal.get(Calendar.MONTH);
		cal.setTime(sdf.parse(dateStr1));
		int value1 = cal.get(Calendar.MONTH);
		return value2-value1;
	}

	/**
	 * 两个日期的天差值
	 * author:panyongjun
	 * date:2019年8月9日
	 */
	public int dateDayGet(String date) throws Throwable{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		cal.setTime(sdf.parse(date));
		return cal.get(Calendar.DAY_OF_MONTH);
	}
	/**
	 * 日期比对（常用于检查查询结果集检查）
	 * @param dateStrList
	 * @param theCompareDate
	 * @return
	 * @throws Throwable
	 */
	public boolean dateListCompare(List<String> dateStrList, String compare, String theCompareDate) throws Throwable {
		boolean compareResult = false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			if(compare.equalsIgnoreCase("lessOrEqualThan")){
				for(String dateStr:dateStrList){
					if(sdf.parse(dateStr).compareTo(sdf.parse(theCompareDate + " 23:59:59")) <= 0){
						compareResult = true;
					}
					else{
						compareResult = false;
						logger.info("日期比对结果为假，日期列表中有" + dateStr + "不小于" + theCompareDate + " 23:59:59");
						break;
					}
				}
				if(compareResult){
					logger.info("日期比对结果为真，日期列表中所有的日期都小于" + theCompareDate + " 23:59:59");
				}else{
					logger.info("日期比对结果为假，日期列表中有日期不小于" + theCompareDate + " 23:59:59");
				}
			}
			if(compare.equalsIgnoreCase("moreOrEqualThan")){
				for(String dateStr:dateStrList){
					if(sdf.parse(dateStr).compareTo(sdf.parse(theCompareDate)) >= 0){
						compareResult = true;
					}
					else{
						compareResult = false;
						logger.info("日期比对结果为假，日期列表中有" + dateStr + "不大于" + theCompareDate);
						break;
					}
				}
				if(compareResult){
					logger.info("日期比对结果为真，日期列表中所有的日期都大于" + theCompareDate);
				}else{
					logger.info("日期比对结果为假，日期列表中有日期不大于" + theCompareDate);
				}
			}

		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return compareResult;
	}
	
	/**
	 * 日期排序检查
	 * @param dateList
	 * @param sortType， “升序” 或  “降序”
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean dateListSortByChk(List<String> dateStrList, String sortType, String dateFormat) throws Throwable {
		boolean sortChk = false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
			if(dateStrList.size() == 1){
				logger.warn("当前只有一条记录，无法判断排序方式");
			}
			if(dateStrList.size() > 1){
				for(int i=0; i < dateStrList.size() - 1; i++)
				{
					int compareResult = sdf.parse(dateStrList.get(i)).compareTo(
							sdf.parse(dateStrList.get(i+1)));
					if(sortType.equalsIgnoreCase("升序") && compareResult <= 0 ){
						sortChk = true;
					}
					if(sortType.equalsIgnoreCase("升序") && compareResult > 0 ){
						sortChk = false;
						logger.info(dateStrList.get(i) + "在" + dateStrList.get(i + 1) + "之后");
						break;
					}
					if(sortType.equalsIgnoreCase("降序") && compareResult >= 0 ){
						sortChk = true;
					}
					if(sortType.equalsIgnoreCase("降序") && compareResult < 0 ){
						sortChk = false;
						logger.info(dateStrList.get(i) + "在" + dateStrList.get(i + 1) + "之前");
						break;
					}	
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return sortChk;
	}
	
	/**
	 * 日期排序检查
	 * @param dateList
	 * @param sortType， “升序” 或  “降序”
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean dateListSortByChk(List<String> dateStrList, String sortType) throws Throwable {
		boolean sortChk = false;
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			if(dateStrList.size() == 1){
				logger.warn("当前只有一条记录，无法判断排序方式");
			}
			if(dateStrList.size() > 1){
				for(int i=0; i < dateStrList.size() - 1; i++)
				{
					int compareResult = sdf.parse(dateStrList.get(i)).compareTo(
							sdf.parse(dateStrList.get(i+1)));
					if(sortType.equalsIgnoreCase("升序") && compareResult <= 0 ){
						sortChk = true;
					}
					if(sortType.equalsIgnoreCase("升序") && compareResult > 0 ){
						sortChk = false;
						logger.info(dateStrList.get(i) + "在" + dateStrList.get(i + 1) + "之后");
						break;
					}
					if(sortType.equalsIgnoreCase("降序") && compareResult >= 0 ){
						sortChk = true;
					}
					if(sortType.equalsIgnoreCase("降序") && compareResult < 0 ){
						sortChk = false;
						logger.info(dateStrList.get(i) + "在" + dateStrList.get(i + 1) + "之前");
						break;
					}	
				}
			}
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
		return sortChk;
	}
	
	/**
	 * 异常处理，截图并写入日志文件
	 * @param ex
	 * @throws Throwable
	 */
	public void exceptionErrorHandle(Exception ex) throws Throwable {
		logger.error(ex.getMessage());
	}
	
	/**
	 * 根据当前执行case，对日志进行分割，以便于单独case执行日志的检查
	 * @param caseName
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void whichCaseIsRun(String testCaseName) throws Throwable {
		caseName = testCaseName;
		LogReConfig.getLog4jConfig(caseName);
		logger.info("case " + caseName + " is start\n");
	}
	
	/**
	 * 全角转半角
	 * @param str
	 * @return
	 * @author panyongjun
	 */
    public static String toDBC(String str) {
        char c[] = str.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == '\u3000') {
                c[i] = ' ';
            } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                c[i] = (char) (c[i] - 65248);

            }
        }
        String returnString = new String(c);
        return returnString;
    }
	
	/**
	 * 删除多余的空格
	 * @param str
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String spaceRemove(String str) throws Throwable {
		return str.replace(" ", "").replace("　", "");
	}

	/**
	 * email地址生成
	 * @param phoneNum
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String emailGet(String phoneNum) throws Throwable {
		return phoneNum + "@139.com";
	}
	
	/**
	 * 电话号码生成
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String telNumberGet() throws Throwable {
		String phoneSuffixStr = "";
		Random rand = new Random();
		int phoneSuffixIndex = 0;
		String[] phoneSuffix = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		for(int i=0; i<8; i++)
		{
			phoneSuffixIndex = rand.nextInt(phoneSuffix.length);
			phoneSuffixStr += phoneSuffix[phoneSuffixIndex];
			if(i==0){
				while(phoneSuffixStr.equalsIgnoreCase("0") || phoneSuffixStr.equalsIgnoreCase("1") || 
						phoneSuffixStr.equalsIgnoreCase("2")){
					phoneSuffixStr = "";
					phoneSuffixIndex = rand.nextInt(phoneSuffix.length);
					phoneSuffixStr += phoneSuffix[phoneSuffixIndex];
				}
			}
		}
		return phoneSuffixStr;
	}
	
	/**
	 * 注册人名生成
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String userNameGet() throws Throwable {
        String userName = "";
        Random rand = new Random();
        String[] passengerFstName = { "赵", "钱", "孙", "李", "周", "吴", "郑", "王", "冯", "陈", "褚", "卫", "蒋", "沈", "韩", "杨", "朱", "秦", "尤", "许", "何", "吕", "施", "张" };
        String[] passengerSecName = { "永", "祥", "荣", "玉", "明", "树", "近", "伯", "晓", "正", "福", "佩", "中", "泽", "润", "智", "礼", "振", "岐", "跃", "良", "啸", "志", "英" };
        String[] passengerLastName = { "林", "江", "山", "春", "秋", "夏", "冬", "国", "凤", "龙", "呈", "风", "雷", "发", "强", "刚", "贵", "才", "亮", "阳", "宝", "唐", "杰", "海",
                                         "磊", "蕾", "风", "岩", "炎", "申", "珏", "超", "宇", "岳", "枫", "锋", "田", "光", "克", "文", "永", "祥", "荣", "玉", "明", "树", "近", 
                                         "伯", "晓", "正", "福", "佩", "中", "泽", "润", "智", "礼", "振", "岐", "跃", "良", "啸", "志", "英"};
        
        String fstName = passengerFstName[rand.nextInt(passengerFstName.length)];
        String secName = passengerSecName[rand.nextInt(passengerSecName.length)];
        String lastName = passengerLastName[rand.nextInt(passengerLastName.length)];
        while(lastName.equals(secName))
        {
        	lastName = passengerLastName[rand.nextInt(passengerLastName.length)];
        }
        userName = fstName + secName + lastName;
        return userName;
	}
	
	/**
	 * 手机号码生成
	 * @return
	 * @author panyongjun
	 * @throws Throwable
	 */
	public String phoneNumberGet() throws Throwable {
		String phoneNumber = "";
		Random rand = new Random();
		String[][] phonePrefix =  {{"130", "131", "132", "155", "156", "185", "186"},
				{"133", "153", "180", "181", "189"}, 
				{"134", "135", "136", "137", "138", "139", "150", "151", "152", 
					"153", "155", "156", "157", "158", "159", "186", "187", "188"}};
		String[] phoneSuffix = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
		int inWhichComIndex = rand.nextInt(phonePrefix.length);
		int inWhichComPrefix = rand.nextInt(phonePrefix[inWhichComIndex].length);
		int phoneSuffixIndex = 0;
		String phoneSuffixStr = "";
		for(int i=0; i<8; i++)
		{
			phoneSuffixIndex = rand.nextInt(phoneSuffix.length);
			phoneSuffixStr += phoneSuffix[phoneSuffixIndex];
		}
		
		phoneNumber = phonePrefix[inWhichComIndex][inWhichComPrefix] + phoneSuffixStr;
		return phoneNumber;
	}
	
	/**
	 * 对弹出的下载窗口，传入键盘按键，取消或确定下载
	 * @param fileName
	 * @param sendKeyPress
	 * @author panyongjun
	 * @throws Throwable
	 */
	public void downLoad(int sendKeyPress) throws Throwable {
		Robot robot = new Robot();
		Thread.sleep(5000);
		robot.keyPress(sendKeyPress);     
		Thread.sleep(5000);
	}
	
	/**
	 * List中的所有记录都包含指定的字符串
	 * @param expectedStr
	 * @param strList
	 * @return 
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean strIsIncludedInAllItems(String expectedStr, List<String> strList) throws Throwable {
		for(String str:strList){
			if(!str.toLowerCase().contains(expectedStr.toLowerCase())){
				return false;
			}else{
				continue;
			}
		}
		return true;
	}
	
	/**
	 * 验证指定的字符串是否与List的item相等
	 * @param expectedStr
	 * @param strList
	 * @return 
	 * @author panyongjun
	 * @throws Throwable
	 */
	public boolean strIsSameInList(String expectedStr, List<String> strList) throws Throwable {
		for(String str:strList){
			if(!str.toLowerCase().equalsIgnoreCase(expectedStr.toLowerCase())){
				return false;
			}else{
				continue;
			}
		}
		return true;
	}
	
	/**
	 * 判断list所有值都不为空
	 * @param strList
	 * @return
	 * @throws Throwable
	 */
	public boolean allListItemIsNotEmpty(List<String> strList) throws Throwable {
		for(String str:strList){
			if(str.replace(" ", "").equals("")){
				return false;
			}else{
				continue;
			}
		}
		return true;
	}
	
	/**
	 * 判断list所有值都为空
	 * @param strList
	 * @return
	 * @throws Throwable
	 */
	public boolean allListItemIsEmpty(List<String> strList) throws Throwable {
		for(String str:strList){
			if(str.replace(" ", "").equals("")){
				continue;
			}else{
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 验证指定的字符串是否被List中所有的item包含
	 * @param expectedStr
	 * @param strList
	 * @return 
	 * @throws Throwable
	 */
	public boolean strIsIncludedInList(String expectedStr, List<String> strList) throws Throwable {
		for(String str:strList){
			if(str.toLowerCase().contains(expectedStr.toLowerCase())){
				continue;
			}else {
				return false;
			}
		}
		return true;
	}
	
	public boolean strIsIncludedInArray(String expectedStr, Object[] objArray) throws Throwable {
		for(Object str:objArray){
			if(!str.toString().toLowerCase().contains(expectedStr.toLowerCase())) {
				return false;
			}else {
				continue;
			}
		}
		return true;
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
	
	public String testEnv(String testOn){
		String testEnv = "--";
		switch(testOn.toLowerCase()){
			case "pro": testEnv = "生产环境执行"; break;
			case "pre": testEnv = "预生产环境执行"; break;
			case "test": testEnv = "测试环境执行"; break;
			default: testEnv = "--执行"; break;
		}
		return testEnv;
	}
	
	public List<String> listObjectToString(List<Object> objectList) throws Throwable {
		List<String> strList = new ArrayList<String>();
		for(Object obj:objectList){
			strList.add(obj.toString());
		}
		return strList;
	}
	
	
	public String listToString(List<String> list, char separator) {
		StringBuilder sb = new StringBuilder();
		if(list==null)
			return null;
		for (int i = 0; i < list.size(); i++) {
			if(separator!='\0')
				sb.append(list.get(i)).append(separator);
			else {
				sb.append(list.get(i));
			}
		}
		if(separator!='\0')
			return sb.toString().substring(0, sb.toString().length() - 1);
		else {
			return sb.toString();
		}

	}
	
	/**
	 * 数字判断
	 * @param str
	 * @return
	 * @throws Throwable
	 */
	public boolean isDigital(String str) throws Throwable {
	   Pattern pattern = Pattern.compile("[0-9]*"); 
	   Matcher isNum = pattern.matcher(str);
	   return isNum.matches();
	}	

	public String readResponse(InputStream is) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        return sb.toString();
    }
	
	public int generateRandom() {
        return (int) (Math.random() * 1000000);
    }

	public String getFilePathSeprator() {
		if (Platform.WINDOWS.is(Platform.getCurrent()) || Platform.VISTA.is(Platform.getCurrent()) 
				|| Platform.WIN8.is(Platform.getCurrent()) || Platform.XP.is(Platform.getCurrent())) {
			return "\\";
		} else {
			return "/";
		}
	}
	
	/**
	 * 随机生产指定长度的中文字符串
	 * @param strLength
	 * @return
	 * @throws Throwable
	 */
	public String chineseStrGet(int strLength) throws Throwable{
		StringBuffer buffer = new StringBuffer();
		for(int i=0; i<strLength; i++){
			buffer.append(chineseCharGet());
		}
		return buffer.toString();
	}
	
	/**
	 * 随机生产单个中文字符
	 * @throws Throwable
	 */
	public String chineseCharGet() throws Throwable{
		Random random = new Random();///随机数
		String[] rBase = { "0", "1", "2", "3", "4", "5", "6", "7", "8","9", "a", "b", "c", "d", "e", "f" };
		// 生成第1位的区码
		int r1 = random.nextInt(3) + 11; //生成11到14之间的随机数
		String str_r1 = rBase[r1];
		// 生成第2位的区码
		int r2;
		if (r1 == 13) {
		r2 = random.nextInt(7); //生成0到7之间的随机数
		} else {
		r2 = random.nextInt(16); //生成0到16之间的随机数
		}
		String str_r2 = rBase[r2];
		// 生成第1位的位码
		int r3 = random.nextInt(6) + 10; //生成10到16之间的随机数
		String str_r3 = rBase[r3];
		// 生成第2位的位码
		int r4;
		if (r3 == 10) {
		r4 = random.nextInt(15) + 1; //生成1到16之间的随机数
		} else if (r3 == 15) {
		r4 = random.nextInt(15); //生成0到15之间的随机数
		} else {
		r4 = random.nextInt(16); //生成0到16之间的随机数
		}
		String str_r4 = rBase[r4];
		//System.out.println(str_r1 + str_r2 + str_r3 + str_r4);

		// 将生成机内码转换为汉字
		byte[] bytes = new byte[2];
		//将生成的区码保存到字节数组的第1个元素中
		String str_r12 = str_r1 + str_r2;
		int tempLow = Integer.parseInt(str_r12, 16);
		bytes[0] = (byte) tempLow;
		//将生成的位码保存到字节数组的第2个元素中
		String str_r34 = str_r3 + str_r4;
		int tempHigh = Integer.parseInt(str_r34, 16);
		bytes[1] = (byte) tempHigh;
		String ctmp = new String(bytes,"gb2312"); //根据字节数组生成汉字
		//System.out.println("生成汉字:" + ctmp);
		return ctmp;
	}
	
	/**
	 * 部分特殊字符生成
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public String generateSpecialString(int length) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String characters = "~!@#$%^&*()_+:\\;.,/?\\|\"\'";
		int charactersLength = characters.length();
		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}
	
	/**
	 * 随机生成英文及数字字符串
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public String generateRandomStringAndNumber(int length){
		StringBuffer buffer = new StringBuffer();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "0123456789";
		int charactersLength = characters.length();
		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}
	
	/**
	 * 对字符串中的一些特殊字符进行转义
	 * @param includedEscapeCharStr
	 * @return
	 * @throws Throwable
	 */
	public String escapeCharTransfer(String includedEscapeCharStr){
		String newStr = "";
		newStr = includedEscapeCharStr.replaceAll("\\|", "");
		newStr = newStr.replaceAll("\r", "");
		newStr = newStr.replaceAll("\n", "");
		newStr = newStr.replaceAll("\t", "");
		newStr = newStr.replaceAll("\\\\", "");
		newStr = newStr.replaceAll("&nbsp;", "");
		newStr = newStr.replaceAll("'", "&#39;");
		newStr = newStr.replaceAll("<", "&lt;");
		newStr = newStr.replaceAll(">", "&gt;");
		newStr = newStr.replaceAll("\\(", "&#40;");
		newStr = newStr.replaceAll("\\)", "&#41;");
		newStr = newStr.replaceAll("\\+", "&#43;");
		newStr = newStr.replaceAll("\"", "&guot;");
		newStr = newStr.replaceAll("\\\"", "&guot;");
		newStr = newStr.replaceAll("\\\'", "&#39;");
		return newStr;
	}
	
	/**
	 * 随机生成英文、数字及特殊字符字符串
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public String generateRandomStringNumAndSpecialChar(int length) throws Exception {
		StringBuffer buffer = new StringBuffer();
		Random rand = new Random();		
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
				+ "0123456789";
		String[] specialCharArray = {"~", "!", "@", "#", "%", "$", "^", "&", "*", "(", ")", " ", 
				"_", "+", ":", "\\", "\\\\", "/", ".", ";", "?", "|", "\"", "\'", "\\r", "\\n", "\\t", "&nbsp;"};
		int specialCharLength = length/10;
		int charactersLength = characters.length();
		if(specialCharLength <= 1){
			int specialCharIndex = rand.nextInt(specialCharArray.length);
//			int offsetIndex = rand.nextInt(length - specialCharLength);
			buffer.append(specialCharArray[specialCharIndex]);
			int specialBufferLength = buffer.length();
			for (int i = 0; i < length - specialBufferLength; i++) {
				double index = Math.random() * charactersLength;
				int offsetIndex = rand.nextInt(buffer.length());
				buffer.insert(offsetIndex, characters.charAt((int) index));
			}
		}
		if(specialCharLength > 1){
			for(int i = 0; i < specialCharLength; i++){
				int specialCharIndex = rand.nextInt(specialCharArray.length);
				buffer.append(specialCharArray[specialCharIndex]);
			}
			int specialBufferLength = buffer.length();
			for (int i = 0; i < length - specialBufferLength; i++) {
				double index = Math.random() * charactersLength;
				int offsetIndex = rand.nextInt(buffer.length());
				buffer.insert(offsetIndex, characters.charAt((int) index));
			}
		}
		return buffer.toString();
	}
	
	/**
	 * 随机生成英文字符串
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public String generateRandomString(int length) throws Exception {
		StringBuffer buffer = new StringBuffer();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";		
		int charactersLength = characters.length();
		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}
	
	/**
	 * 随机生成指定位数的数字字符串
	 * @param length
	 * @return
	 * @throws Exception
	 */
	public String generateRandomInteger(int length){
		StringBuffer buffer = new StringBuffer();
		String characters = "0123456789";		
		int charactersLength = characters.length();
		for (int i = 0; i < length; i++) {
			double index = Math.random() * charactersLength;
			buffer.append(characters.charAt((int) index));
		}
		return buffer.toString();
	}

	public String generateActivationCode(String userId) throws Exception {
		return encodeHash(userId);
	}
	
	private String encodeHash(String str) {
        BigInteger hash = BigInteger.valueOf(0);
        for (int i = 0; i < str.length(); i++){
            hash = (hash.multiply(BigInteger.valueOf(127l))).add(BigInteger.valueOf(str.charAt(i)));
        }   
        return hash.toString() ;
	}
	
}
