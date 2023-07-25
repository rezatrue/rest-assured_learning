package files;

import io.restassured.path.json.JsonPath;

public class ReUsableMethods {
	
	public static JsonPath rawToJson(String input) {
		JsonPath jsPath = new JsonPath(input);
		return jsPath;
	}

}
