package Railway;

import Common.JsonReader;
import Common.Utilities;
import Enum.PageTitle;
import Enum.SeatType;
import Enum.ObjectType;

public class TicketPricePage extends GeneralPage {

	private static JsonReader ticketPriceReader = new JsonReader(ObjectType.Railway, TicketPricePage.class);
	public String getPrice(SeatType type) {
		return Utilities.getTextElement(ticketPriceReader.getLocator( "dynamicXpathPriceBySeat", type));
	}
	
	public String getTableName() {
		return Utilities.getTextElement(ticketPriceReader.getLocator( "txtTableName"));
	}
	
}
