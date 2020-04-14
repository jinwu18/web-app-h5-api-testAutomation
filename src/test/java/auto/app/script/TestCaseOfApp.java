package auto.app.script;

import static org.testng.Assert.assertEquals;

import java.util.List;

import org.testng.annotations.Test;

import com.automation.common.WebCommon;
import com.automation.exception.FrameworkException;
import com.automation.framework.WebTestNGBase;

import auto.app.page.BaiduAppPage;

public class TestCaseOfApp extends WebTestNGBase{

	@Test(alwaysRun=true, groups={"auto.demo.test.app"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfAppiumSearchAndOpenSeleniumHq()throws FrameworkException{
		try{
			caseName = "使用百度App搜素selenium，并打开官方网站";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			BaiduAppPage baidu = new BaiduAppPage(driver);
			baidu.webDriverSearch("selenium");
			
			WebCommon web = new WebCommon(driver);
			List<String> resultList = web.eleTxtListsGet(baidu.resultLinkBy); 
			
			result = strIsIncludedInList("www.seleniumhq.org", resultList);
			
			assertEquals(result, true);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
}