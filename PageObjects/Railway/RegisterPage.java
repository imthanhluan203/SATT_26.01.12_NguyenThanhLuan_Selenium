package Railway;
import Common.JsonReader;
import Common.Utilities;
import DataObjects.UserInfo;
import Enum.PageTitle;
import Enum.ObjectType;

public class RegisterPage extends GeneralPage {
	private static JsonReader registerReader = new JsonReader(ObjectType.Railway , RegisterPage.class);
	public String getThankyouMessage() {
		return Utilities.getTextElement(registerReader.getLocator( "lblThankyouMessage"));
	}
	
	public String getWelcomeMessage() {
		return Utilities.getTextElement(registerReader.getLocator( "lblWelcomeMessage"));
	}
	
	public String getErrorMessage() {
		return Utilities.getTextElement(registerReader.getLocator( "lblErrorMessage"));
	}
	
	public String getErrorInvalidPassword() {
		return Utilities.getTextElement(registerReader.getLocator( "lblErrorInvalidPassword"));
	}
	
	public String getErrorInvalidPid() {
		return Utilities.getTextElement(registerReader.getLocator( "lblErrorInvalidPid"));
	}
	
	public RegisterPage register(UserInfo myInfo)  {
		Utilities.scrollToElement(registerReader.getLocator( "btnSubmit"));
		Utilities.enter(registerReader.getLocator( "txtUsername"), myInfo.getName());
		Utilities.enter(registerReader.getLocator( "txtPassword"), myInfo.getPassword());
		Utilities.enter(registerReader.getLocator( "txtConfirmPassword"), myInfo.getPassword());
		Utilities.enter(registerReader.getLocator( "txtPassport"), myInfo.getPid());
		Utilities.click(registerReader.getLocator( "btnSubmit"));
		Utilities.waitForPageFullyLoad(PageTitle.REGISTER_THANK);
		return new RegisterPage();
	}
	
}
