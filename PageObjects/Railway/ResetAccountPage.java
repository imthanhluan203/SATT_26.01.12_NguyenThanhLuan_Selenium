package Railway;

import Common.JsonReader;
import Common.Utilities;
import Enum.PageTitle;
import Enum.ObjectType;

public class ResetAccountPage extends GeneralPage {	
	private static JsonReader resetReader = new JsonReader(ObjectType.Railway, ResetAccountPage.class);
	public void resetPassWord(String password,String confirmPassword) {
		Utilities.enter(resetReader.getLocator("txtNewPassword"), password);
		Utilities.enter(resetReader.getLocator( "txtConfirmPassword"), confirmPassword);
		Utilities.click(resetReader.getLocator( "btnSubmitReset"));
	}
	
	public String getMessageReset() {
		return Utilities.getTextElement(resetReader.getLocator( "txtMessageReset"));
	}
	
	public String getResetoken() {
		return Utilities.getValueElement(resetReader.getLocator( "txtResetToken"));
	}
	
	public String getConfirmPasswordErr() {
		return Utilities.getTextElement(resetReader.getLocator( "lblConfirmPassword"));
	}
	
}
