package Railway;
import Common.JsonReader;
import Common.Utilities;
import Constant.Constant;
import DataObjects.UserInfo;
import Enum.PageTitle;
import Enum.ObjectType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class LoginPage extends GeneralPage {
	private static JsonReader loginReader = new JsonReader(ObjectType.Railway, LoginPage.class);

	public String getLblErrorLoginMessage() {
		return Utilities.getTextElement(loginReader.getLocator("lblErrorLoginMessage"));
	}
	
	public void clickForgotPassword() {
		Utilities.click(loginReader.getLocator("linkforgotPassword"));
	}
	
	public void sendRequestReset(String input) {
		Utilities.enter(loginReader.getLocator("txtEmailReset"), input);
		Utilities.click(loginReader.getLocator( "btnSubmitReset"));
	}
	
	@SuppressWarnings("unchecked")
	public <T extends GeneralPage> T login(UserInfo myUser) {
		Utilities.scrollToElement(loginReader.getLocator("btnLogin"));
		Utilities.enter(loginReader.getLocator("txtUsername"), myUser.getName());
		Utilities.enter(loginReader.getLocator( "txtPassword"), myUser.getPassword());
		Utilities.click(loginReader.getLocator( "btnLogin"));
		Utilities.waitForPageFullyLoad(PageTitle.HOME);
		if(Constant.WEBDRIVER.getTitle().equals(PageTitle.HOME.getValue())) {
			return (T) new HomePage();
		}
		return (T) this;
		
	}

}
