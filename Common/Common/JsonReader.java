package Common;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.By;

import Enum.*;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;
public class JsonReader {
	private Map<String,String> LOCATORS;
	public <T> JsonReader(ObjectType fileSource, Class<T> myClass){
		String folderName = fileSource.name();
		String fileName = myClass.getSimpleName();
		try {
			LOCATORS = new HashMap<>();
            ObjectMapper mapper = new ObjectMapper();
			String filepath = String.format("Resource/Locators/%s/%s.json",folderName,fileName);
			System.out.println(filepath);
			LOCATORS = mapper.readValue(new File(filepath),Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	public By getLocator(String typeOfLocator, Object... values) {
		String element = LOCATORS.get(typeOfLocator);
		if(values.length == 0) {
			return By.xpath(element);
		}
		return By.xpath(String.format(element, values));
	}

}
