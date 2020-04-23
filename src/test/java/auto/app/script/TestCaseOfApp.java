package auto.app.script;

import static org.testng.Assert.assertEquals;

import org.testng.annotations.Test;

import com.automation.common.AppCommon;
import com.automation.exception.FrameworkException;
import com.automation.framework.AppTestNGBase;

import auto.app.page.ContactAppPage;

public class TestCaseOfApp extends AppTestNGBase{
	
	String newContactor = "";
	String newPhone = "";

	@Test(alwaysRun=true, groups={"auto.demo.test.app"}, timeOut=MAX_EXCUTE_TIME)
	public void caseOfAppiumAndroidContact()throws FrameworkException{
		try{
			caseName = "打开安卓电话簿，并添加一个新联系人";
			whichCaseIsRun(caseName);
			boolean result = false;
			
			ContactAppPage contact = new ContactAppPage(appHandler);
			contact.addNewContact(newContactor, newPhone);//联系人添加
			
			AppCommon app = new AppCommon(appHandler);
			result = app.toastChk("联系人已保存");//toast信息比对
			
			assertEquals(result, true);
		}catch (Throwable e) {
			e.printStackTrace();
			throw new FrameworkException(e.getMessage());
		}
	}
}
