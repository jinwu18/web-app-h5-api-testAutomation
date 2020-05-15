package com.automation.framework;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.automation.listener.Log4jLogger;

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

	
	public void pressKey(Keys k) throws Throwable {
		Actions action=new Actions(driver);
		action.sendKeys(k).perform();
		Thread.sleep(1000);
	}	
	
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
//		LogReConfig.getLog4jConfig(caseName);
		logger.info("case " + caseName + " is start\n");
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
}
