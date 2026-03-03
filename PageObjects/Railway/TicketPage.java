package Railway;

import Common.JsonReader;
import Common.Utilities;
import Constant.Constant;
import Enum.PageTitle;
import Enum.TableHeader;
import Enum.ObjectType;

public class TicketPage extends GeneralPage {

	private static JsonReader tickerReader = new JsonReader(ObjectType.Railway, TicketPage.class);

	public String getCellValue(String noOfTicket, TableHeader typeOfData) {
		Utilities.waitForElementLocated(tickerReader.getLocator("lblTicketHeader"));
		return Utilities.getTextElement(tickerReader.getLocator("dynamicXpathCellValueOfTable", noOfTicket, typeOfData.getValue()));
	}

	public void clickCancelButton(String noOfTicket) {
		Utilities.click(tickerReader.getLocator( "dynamicXpathCancel", noOfTicket));
	}

	public void clickOke() {
		Constant.WEBDRIVER.switchTo().alert().accept();
	}

}
