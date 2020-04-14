package auto.web.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

import com.automation.common.WebCommon;

public class BaiduWebPage extends WebCommon{
	
	public BaiduWebPage(RemoteWebDriver driver) {
		super(driver);
	}
	
	By searchIputBy = By.id("kw");//百度输入框
	By searchBtnBy = By.id("su");//百度一下
	public By resultLinkBy = By.xpath("//div[@class='result c-container ']//div[@class='f13']/a");//搜索结果中链接
	
	/**
	 * 根据输入内容百度一下
	 * @param searchTxt
	 * @throws Throwable                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
	 */
	public void webDriverSearch(String searchTxt) throws Throwable {
		toURL("https://www.baidu.com");
		txtBoxSendValue(searchIputBy, searchTxt);
		eleClickBy(searchBtnBy);
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
			System.out.println(item.getText());
			if(item.getText().contains(searchTxt)) {
				item.click();
				result = true;
				break;
			}
		}
		return result;
	}
}
