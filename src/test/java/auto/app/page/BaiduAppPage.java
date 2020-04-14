package auto.app.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.automation.common.WebCommon;


public class BaiduAppPage extends WebCommon{
	
	public BaiduAppPage(RemoteWebDriver driver) {
		super(driver);
	}
	
	By searchIputBy = By.id("index-kw");//百度输入框
	public By resultLinkBy = By.xpath("//div[@class='c-result-content']/article");//搜索结果中标签
	
	/**
	 * 根据输入内容百度一下
	 * @param searchTxt
	 * @throws Throwable                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
	 */
	public void webDriverSearch(String searchTxt) throws Throwable {
		toURL("https://www.baidu.com");
		txtBoxSendValue(searchIputBy, searchTxt);
		pressKey(Keys.ENTER);
		Thread.sleep(3000);
	}
	
	/**
	 * 检索搜索结果
	 * @param searchTxt
	 * @param resultList
	 * @return
	 * @throws Throwable
	 */
	public boolean searchResultChk(String searchTxt, List<WebElement> resultList) throws Throwable {
		boolean result = false; 
		for(WebElement item:resultList) {
//			System.out.println(item.getText());
			if(item.getText().contains(searchTxt)) {
				System.out.println(item.getText());
				item.click();
				result = true;
				break;
			}
		}
		return result;
	}
}
