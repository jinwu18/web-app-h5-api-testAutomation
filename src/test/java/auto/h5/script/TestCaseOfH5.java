package auto.h5.script;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.automation.common.WebCommon;
import com.automation.exception.FrameworkException;
import com.automation.framework.WebTestNGBase;

import auto.h5.page.BaiduH5Page;

public class TestCaseOfH5 extends WebTestNGBase{

	@Test(alwaysRun=true, groups={"auto.demo.test.h5"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfH5WebDriverSearchAndOpenSeleniumHq()throws FrameworkException{
		try{
			caseName = "使用百度H5搜素selenium, 并打开官方网站";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			BaiduH5Page baidu = new BaiduH5Page(driver);
			baidu.webDriverSearch("selenium");
			WebCommon web = new WebCommon(driver);
			List<WebElement> resultList = web.eleListsGet(baidu.resultLinkBy);//获取检索结果中第一页列表	
			result = baidu.searchResultChk("Selenium automates browsers. That's it!", resultList);//比对结果并打开官方网址
			
			assertEquals(result, true);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}	
}