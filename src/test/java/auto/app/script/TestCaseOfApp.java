package auto.app.script;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.automation.exception.FrameworkException;
import com.automation.framework.AppTestNGBase;

import auto.app.page.BaiduAppPage;

public class TestCaseOfApp extends AppTestNGBase{

	@Test(alwaysRun=true, groups={"auto.demo.test.app"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfAppiumAndroidContact()throws FrameworkException{
		try{
			caseName = "打开安卓电话簿，并添加一个新联系人";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			BaiduAppPage baidu = new BaiduAppPage(appHandler);
			baidu.addNewContact("tester", "13402202055");
			
			assertEquals(result, true);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
}
