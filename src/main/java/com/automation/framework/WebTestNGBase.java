package com.automation.framework;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.testng.ITestContext;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Parameters;
import java.lang.reflect.Method;

import com.automation.common.AppCommon;
import com.automation.common.WebCommon;
import com.automation.exception.FrameworkException;
import com.automation.listener.Log4jLogger;

/**
 * testng web 基础类
 * @author 爱吃苹果的鱼
 */
public class WebTestNGBase extends AbastractBase{
	
	public static String driverBrowserType = "";
	public static String driverBrowserPath = "";
	public static String driverHandlerMode = "";
	public static String currentClassName = "";
	public static String testName = "";	
	public static String testNGsuiteName = "";
	public static String testNGTestName = "";
	
	List<String> methodList = new ArrayList<String>();
	ArrayList<ArrayList<Object>> testCaseArray = new ArrayList<ArrayList<Object>>();
	
	Log4jLogger logger = new Log4jLogger();	
	WebDriverManager webDriverManager = new WebDriverManager();
	AppCommon app = new AppCommon(appHandler);
	WebCommon web = new WebCommon(driver);
	
	/**
	 * test environment get
	 * @param itc
	 * @param testOn
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月13日
	 */
	@Parameters("testOn")
	@BeforeSuite(groups = "all")
	public void beforeSuite(ITestContext itc, String testOn) throws Throwable {
		testEnv = testOn;
		testNGsuiteName = itc.getSuite().getName();
	}
	
	/**
	 * broser setting
	 * @param itc
	 * @param handlerMode
	 * @param browserType
	 * @param browserPath
	 * @param emulationName
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 */
	@SuppressWarnings("static-access")
	@Parameters({"handlerMode", "browserType", "browserPath", "emulationName"})
	@BeforeTest(groups = "all")
	public void beforeTest(ITestContext itc, String handlerMode, String browserType, String browserPath, String emulationName) throws Throwable{
		try {
			testNGsuiteName = itc.getSuite().getName();
			testNGTestName = itc.getName();
			(new AbastractBase()).emulationName = emulationName;
			driverBrowserType = browserType;
			driverBrowserPath = browserPath;
			driverHandlerMode = handlerMode;
		} catch (Exception e) {
			logger.error("Exception happened: " + e.getMessage());
			throw new FrameworkException(e);
		}
	}
	
	/**
	 * browser setting
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 */
	@BeforeClass(groups = "all") 
	public void beforeClass() throws Throwable {
		handlerModeGet(driverHandlerMode);
		browserPathSet(driverBrowserPath);
	}
	
	/**
	 * browser initialize
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 */
	@BeforeMethod(groups = "all")
	public void beforeMethod() throws Throwable{
		try {
			switch(testNGTestName.toLowerCase()) {
			case "h5":
			case "web":{
				driver = webDriverManager.getCurrentWebDriver(driverBrowserType);
				driver.manage().window().maximize();
				driver.manage().deleteAllCookies();
				break;
			}
			default:break;
			}
		} catch (Exception e) {
			logger.error("Exception happened: " + e.getMessage());
			throw new FrameworkException(e);
		}
	}
	
	/**
	 * test case quit
	 * @param method
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 */
	@AfterMethod(groups = "all")
	public void afterMethod(Method method) throws Throwable {
		try {
			switch(testNGTestName.toLowerCase()) {
			case "api":{
				break;
			}
			case "app":{
				break;
			}
			default:{
				if(web.alertIsPresent()){
					web.alertAccept(false);
				}
				driver.manage().deleteAllCookies();
				driver.quit();				
			}}
		} catch(Exception e){
			exceptionErrorHandle(e);
		}
	}
	
	/**
	 * testng report open
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 */
//	@AfterSuite(groups = "all")
	public void afterSuite() throws Throwable {
		try{
			String reportHtml = System.getProperty("user.dir") + File.separator + "test-output" + File.separator + "index.html";
			Runtime.getRuntime().exec("cmd   /c   start  " + reportHtml);
		}catch(Exception ex){
			exceptionErrorHandle(ex);
		}
	}
	
	/**
	 * browser path get
	 * @param browserPath
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 */
	private void browserPathSet(String browserPath) throws Throwable {
		try{
			if (!StringUtils.isBlank(browserPath)) {
				System.setProperty("browserPath", browserPath);
			}
		} catch(Exception e)
		{
			exceptionErrorHandle(e);
		}
	}
	
	/**
	 * handler mode get
	 * @param handlerMode
	 * @throws Throwable
	 * @author: 爱吃苹果的鱼   
	 */
	private void handlerModeGet(String handlerMode) throws Throwable {
		try{
			if (!StringUtils.isBlank(System.getProperty("HANDLE_MODE"))) {
				// prefer to use Jenkins handler mode
				logger.info("Jenkins Parameter Handler Mode is : " + System.getProperty("HANDLE_MODE"));
			} 
			else if (!StringUtils.isBlank(handlerMode)) {
				System.setProperty("HANDLE_MODE", handlerMode);
			}
		} catch(Exception e)
		{
			exceptionErrorHandle(e);
		}
	}
}
