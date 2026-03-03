package Railway;
import Common.JsonReader;
import Common.Utilities;
import Constant.Constant;
import Enum.PageTitle;
import Enum.ObjectType;

public class HomePage extends GeneralPage {
	private JsonReader homeReader = new JsonReader(ObjectType.Railway, HomePage.class);
	
	public HomePage open() {
		Constant.WEBDRIVER.navigate().to(Constant.RAILWAY_URL);
		return this;
	}
	
	public String getWelcomeMessage() {
		return Utilities.getTextElementByJS(homeReader.getLocator( "txtWelcomeMessage"));
	}
	
	public RegisterPage getCreateAccountPage() {
		Utilities.click(homeReader.getLocator( "linkCreateAccount"));
		return new RegisterPage();
	}
	
	public LoginPage getLoginPage() {
		return new LoginPage();
	}
	
}
