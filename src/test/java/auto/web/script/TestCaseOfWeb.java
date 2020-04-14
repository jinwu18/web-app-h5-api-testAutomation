package auto.web.script;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import com.automation.common.WebCommon;
import com.automation.exception.FrameworkException;
import com.automation.framework.WebTestNGBase;

import auto.web.page.BaiduWebPage;

public class TestCaseOfWeb extends WebTestNGBase{

	@Test(alwaysRun=true, groups={"auto.demo.test.web"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfWebDriverSearchAndOpenSeleniumHq()throws FrameworkException{
		try{
			caseName = "使用百度web检索selenium，并打开官方网站";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			BaiduWebPage baidu = new BaiduWebPage(driver);
			baidu.webDriverSearch("selenium");//检索		
			
			WebCommon web = new WebCommon(driver);
			List<WebElement> resultList = web.eleListsGet(baidu.resultLinkBy);//获取检索结果中第一页链接列表	
			result = baidu.searchResultChk("seleniumhq.org", resultList);//比对结果并打开官方网址
			
			assertEquals(result, true);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
}