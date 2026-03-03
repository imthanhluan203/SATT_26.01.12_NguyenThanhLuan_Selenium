package Railway;

import Common.JsonReader;
import Common.Utilities;

import Constant.Constant;
import Enum.PageTitle;
import Enum.Tab;
import Enum.ObjectType;

public abstract class GeneralPage {
	private static JsonReader generalReader = new JsonReader(ObjectType.Railway, GeneralPage.class);

    public Boolean checkTabPageExist(Tab tabName) {
    	try {
    		Constant.WEBDRIVER.findElement(generalReader.getLocator("dynamicTabXpath", tabName.getValue()));
    		return true;
		} catch (Exception e) {
			return false;
		}
    }
    
	public <T extends GeneralPage> T gotoPage(Tab tab,PageTitle page ,Class<T> pageClass)  {
		Utilities.click(generalReader.getLocator( "dynamicTabXpath", tab.getValue()));
		Utilities.waitForPageFullyLoad(page);
		try {
			return pageClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
