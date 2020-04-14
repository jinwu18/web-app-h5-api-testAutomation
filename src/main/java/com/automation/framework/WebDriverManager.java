package com.automation.framework;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.Platform;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.automation.config.WebConstant;
import com.automation.listener.Log4jLogger;

/**
 * web/h5页面初始化类
 * @author 爱吃苹果的鱼
 *
 */
public class WebDriverManager {
	
	public static RemoteWebDriver driver;
	Log4jLogger logger = new Log4jLogger();
	AbastractBase baseAct = new AbastractBase();
	
	/**
	 * broser setting and driver create
	 * @param browserType
	 * @return
	 * @author: 爱吃苹果的鱼   
	 */
	private RemoteWebDriver browserSet(String browserType){
		if (browserType.equalsIgnoreCase("firefox")) {
			driver = createGeckodriver();
		} 
		if (browserType.equalsIgnoreCase("ie")) {
			driver = createIEDriver();
		} 
		if (browserType.equalsIgnoreCase("chrome")) {
			driver = createChromeDriver();
		} 
		if (browserType.equalsIgnoreCase("opera")) {
			driver = createOperaDriver();
		} 
		if(browserType.equalsIgnoreCase("H5")){
			driver = createH5ChromeDriver();
		}
		return driver;
	}
	
	/**
	 * opera driver create
	 * @return
	 * @author: 爱吃苹果的鱼   
	 * @date: 2020年4月13日
	 */
	@SuppressWarnings("deprecation")
	private RemoteWebDriver createOperaDriver(){
		DesiredCapabilities dr = DesiredCapabilities.operaBlink();
		ChromeOptions options = new ChromeOptions();
		options.setBinary(System.getProperty("browserPath"));
		dr.setCapability(OperaOptions.CAPABILITY, options);
		System.setProperty("webdriver.opera.driver", System.getProperty("user.dir") + "/lib/opreadriver.exe");
		dr.setPlatform(Platform.WINDOWS);
		driver = new OperaDriver(dr);
		return driver;
	}
	
	/**
	 * h5 in chrome driver create
	 * @return
	 * @author: 爱吃苹果的鱼   
	 */
	@SuppressWarnings({ "deprecation", "static-access" })
	private RemoteWebDriver createH5ChromeDriver(){
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/lib/chromedriver.exe");
        Map<String, String> mobileEmulation = new HashMap<String, String>();
        mobileEmulation.put("deviceName", baseAct.emulationName);
        Map<String, Object> chromeOptions = new HashMap<String, Object>();     
        chromeOptions.put("mobileEmulation", mobileEmulation);     
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();       
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
		driver = new ChromeDriver(capabilities);
		return driver;
	}
	
	/**
	 * chrome driver create
	 * @return
	 * @author: 爱吃苹果的鱼   
	 */
	private RemoteWebDriver createChromeDriver(){
		ChromeOptions options = new ChromeOptions();		
		Map<String, Object> prefs = new HashMap<String, Object>();
		prefs.put("download.prompt_for_download", true);
		options.setExperimentalOption("prefs", prefs);
		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir") + "/lib/chromedriver.exe");
		driver = new ChromeDriver(options);
		return driver;
	}
	
	/**
	 * firefox driver create
	 * @return
	 * @author: 爱吃苹果的鱼   
	 */
	private RemoteWebDriver createGeckodriver() {
		System.setProperty("webdriver.gecko.driver", System.getProperty("user.dir") + "/lib/geckodriver.exe");
		driver = new FirefoxDriver();
		return driver;
	}
	
	/**
	 * ie driver create
	 * @return
	 * @author: 爱吃苹果的鱼   
	 */
	@SuppressWarnings("deprecation")
	private InternetExplorerDriver createIEDriver(){
		System.setProperty("webdriver.ie.driver", System.getProperty("user.dir")+"/lib/IEDriverServer.exe");  //32 bit
		DesiredCapabilities ieCapabilities = DesiredCapabilities.internetExplorer();  
		ieCapabilities.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
		return new InternetExplorerDriver(InternetExplorerDriverService.createDefaultService(), ieCapabilities);
	}
	
	/**
	 * web driver initialize
	 * @param browserType
	 * @throws MalformedURLException
	 * @author: 爱吃苹果的鱼   
	 */
	public void initNewWebDriver(final String browserType) throws MalformedURLException {
		logger.info("Will start the web driver for the WEB case");
		driver = browserSet(browserType);
		
		driver.manage().timeouts().implicitlyWait(WebConstant.implicitlyWait, TimeUnit.SECONDS);
		if(!browserType.equalsIgnoreCase("chrome")){
			driver.manage().timeouts().pageLoadTimeout(WebConstant.pageLoadTimeout, TimeUnit.SECONDS);
		}
		driver.manage().timeouts().setScriptTimeout(WebConstant.scriptTimeout, TimeUnit.SECONDS);
		driver.manage().deleteAllCookies();
		driver.manage().window().maximize();
	}
	
	/**
	 * web driver close
	 * @author: 爱吃苹果的鱼   
	 */
    public void closeDriver() {
    	try{
        	if(driver != null) {
				driver.close();
        		driver = null;
        		logger.info("All windows have been closed and cookies have been deleted.");
        		}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    }
    
    /**
     * web driver return 
     * @param browserType
     * @return
     * @throws Throwable
     * @author: 爱吃苹果的鱼   
     */
    public RemoteWebDriver getCurrentWebDriver(String browserType) throws Throwable {
    	// Get driver mode
    	String mode = System.getProperty("HANDLE_MODE");
    	if (StringUtils.isBlank(mode)) {
    		mode = WebConstant.handleMode;
    	}
    	if ("Selenium".equalsIgnoreCase(mode)){
    		driver = null;
    		initNewWebDriver(browserType);
    		int tryTime=0;
    		while(driver==null && tryTime<3){
    			tryTime++;
    			AbastractBase baseAct = new AbastractBase();
    			baseAct.browserKill(browserType);
    			initNewWebDriver(browserType);
    		}
    	}
        return driver;
    }
}