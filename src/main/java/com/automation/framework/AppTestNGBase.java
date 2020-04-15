package com.automation.framework;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import java.net.URL;
import java.text.SimpleDateFormat;

import com.automation.exception.FrameworkException;
import com.automation.listener.Log4jLogger;

import io.appium.java_client.android.AndroidDriver;

/**
 * testng app 基础类
 * @author 爱吃苹果的鱼
 *
 */
public class AppTestNGBase extends AbastractBase{

	public static String currentClassName = "";
	public static String testName = "";
	public static String testOn = "";//test-测试环境，pre-预生产环境，pro-生产环境
	public static String testNGsuiteName = "";
	public static String testNGTestName = "";
	List<String> methodList = new ArrayList<String>();
	Log4jLogger logger = new Log4jLogger();

	ArrayList<ArrayList<Object>> testCaseArray = new ArrayList<ArrayList<Object>>();
	
	@Parameters({"handlerMode", "testFor"})
	@BeforeSuite(groups = "all")
	public void beforeSuite(String handlerMode, String testFor) throws Throwable{
		try {
			testOn = testFor;
			ArrayList<Object> caseFileHeaderList = new ArrayList<Object>();
			caseFileHeaderList.add("用例名");
			caseFileHeaderList.add("方法名");
			caseFileHeaderList.add("测试结果");
			testCaseArray.add(caseFileHeaderList);
		} catch (Exception e) {
			logger.error("Exception happened: " + e.getMessage());
			throw new FrameworkException(e);
		}
	}

	/**
	 * class执行前停止所有appium服务，并根据testng.xml重新启动appium
	 * author:panyongjun
	 * date:2019年12月27日
	 */
	@Parameters({"port", "udid"})
	@BeforeClass(groups = "all")
	public void beforeClass(String port, String udid) throws Throwable{
		try {
			stopAppium();
			startAppium(Integer.valueOf(port), udid);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		    String startAppiumLogcat = "cmd.exe /c \"adb -s " + udid + " logcat >d:/logs/" + udid.replace(":",  "_") + "_" + sdf.format(new Date()) + ".log\"";
		    System.out.println(startAppiumLogcat);
		    Runtime.getRuntime().exec(startAppiumLogcat);
			Thread.sleep(10000);
		} catch (Exception e) {
			logger.error("Exception happened: " + e.getMessage());
			throw new FrameworkException(e);
		}
	}	
	
	/**
	 * driver initialize
	 * author:panyongjun
	 * date:2019年12月27日
	 */
	@Parameters({"user", "pwd", "port", "udid", "noReset", "appPackage", "appActivity"})
	@BeforeMethod(groups = "all")
	public void beforeMethod(String user, String pwd, String port, String udid, 
			boolean noReset, String appPackage, String appActivity) throws Throwable{
		try {
			DesiredCapabilities cap = new DesiredCapabilities();
			cap.setCapability("unicodeKeyboard", true);//中文
//			cap.setCapability("resetKeyboard", true);
//			cap.setCapability("newCommandTimeout", "10");
//			cap.setCapability("nosign", true);
			cap.setCapability("noReset", noReset);
//			cap.setCapability("app", "");
//			cap.setCapability("browserName", "h5");//h5
			cap.setCapability("platformName", "Android");
			cap.setCapability("platformVersion", "5.1.1");
			cap.setCapability("udid", udid);
			cap.setCapability("deviceName", udid);
			cap.setCapability("appPackage", appPackage);
			cap.setCapability("appActivity", appActivity);			
			appHandler = new AndroidDriver<>(new URL("http://127.0.0.1:" + port + "/wd/hub"), cap);
			Thread.sleep(3000);
		} catch (Exception e) {
			logger.error("Exception happened: " + e.getMessage());
			throw new FrameworkException(e);
		}
	}
	
	/**
	 * quit driver
	 * author:panyongjun
	 * date:2019年12月27日
	 */
	@AfterMethod(groups = "all")
	public void afterMethod() throws Throwable {
		try{
			appHandler.quit();
		}catch(Exception e){
//			exceptionErrorHandle(e);
		}
	}
	
	/**
	 * class执行后，停止appium服务
	 * author:panyongjun
	 * date:2019年12月27日
	 */
	@AfterClass(groups = "all")
	public void afterClass() throws Throwable{
		stopAppium();
	}
	
	/**
	 * start appium
	 * author:panyongjun
	 * date:2019年12月27日
	 */
	@Parameters({"port", "udid"})
    public void startAppium(int port, String udid) throws Throwable {
	    int bpPort = port + 1;
	    String startAppiumCommand = "cmd.exe /c start cmd.exe /k \"" + "appium -a 127.0.0.1 -p " + port + " -bp " + bpPort + " -U " + udid + "\"";
	    System.out.println(startAppiumCommand);
	    Runtime.getRuntime().exec(startAppiumCommand);
    }
    
    /**
     * stop appium
     * author:panyongjun
     * date:2019年12月27日
     */
    public void stopAppium() throws Throwable {
    	killProcess("node.exe");
    	killProcess("cmd.exe");
    }
}
