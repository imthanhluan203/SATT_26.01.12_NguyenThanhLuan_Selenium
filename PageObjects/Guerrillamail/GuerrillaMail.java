package Guerrillamail;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WindowType;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import Common.JsonReader;
import Common.Utilities;
import Constant.Constant;
import DataObjects.UserInfo;
import Enum.PageTitle;
import Enum.ObjectType;

public class GuerrillaMail {
	private static JsonReader guerrillaMailReader = new JsonReader(ObjectType.Guerrillamail, GuerrillaMail.class);
	private UserInfo userInfo;
	
	public GuerrillaMail(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	
	public void cleanAllOldMail() {
		WebDriverWait wait = new WebDriverWait(Constant.WEBDRIVER, Duration.ofSeconds(5));
		JavascriptExecutor js = (JavascriptExecutor) Constant.WEBDRIVER;
		try {
			wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(guerrillaMailReader.getLocator("checkboxMail"), 2));
			List<WebElement> listCheckBox = Constant.WEBDRIVER.findElements(guerrillaMailReader.getLocator("checkboxMail"));
			listCheckBox.forEach(x ->{
				js.executeScript("arguments[0].click();", x);
			});
			Utilities.click(guerrillaMailReader.getLocator( "btnDelete"));
		} catch (Exception e) {
			List<WebElement> listCheckBox = Constant.WEBDRIVER.findElements(guerrillaMailReader.getLocator( "checkboxMail"));
			listCheckBox.forEach(x ->{
				js.executeScript("arguments[0].click();", x);
				System.out.println(x);
			});
			Utilities.click(guerrillaMailReader.getLocator( "btnDelete"));
		}
		
		
	}
	
	public void createAnEmail() {
		Constant.WEBDRIVER.get(Constant.EMAIL_URL);
		this.setAnEmail();	
		Constant.WEBDRIVER.switchTo().newWindow(WindowType.TAB);
	    Constant.WEBDRIVER.get(Constant.RAILWAY_URL);   
	}
	
	public void setAnEmail() {
		Utilities.click(guerrillaMailReader.getLocator( "btnEdit"));
		Utilities.enter(guerrillaMailReader.getLocator( "txtMail"), userInfo.getUserName());
		Utilities.click(guerrillaMailReader.getLocator( "btnSetMail"));
	}
	
	public void waitAndClickConfirmEmail() {
		Utilities.click(guerrillaMailReader.getLocator( "linkMailconfirm"));
		Utilities.click(guerrillaMailReader.getLocator( "linkConfirm"));
		Utilities.waitForPageFullyLoad(PageTitle.REGISTER_CONFIRM);
	}
	
	
}
