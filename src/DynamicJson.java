import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import files.Payload;
import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

public class DynamicJson {

	@Test(dataProvider="BookData")
	public void addBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = RestAssured.given().header("Content-Type", "application/json")
		.body(Payload.addBook(isbn, aisle))
		.when().get("/Library/Addbook.php")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		System.out.println(response);
		
		JsonPath js = ReUsableMethods.rawToJson(response);
		String msg = js.get("Msg");
		String id = js.get("ID");
		System.out.println(msg +" : "+id);
		
	}
	
	@Test(dataProvider="BookData")
	public void deleteBook(String isbn, String aisle) {
		RestAssured.baseURI = "http://216.10.245.166";
		String response = RestAssured.given().header("Content-Type", "application/json")
		.body(Payload.deleteBook(isbn, aisle))
		.when().post("/Library/DeleteBook.php")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		System.out.println(response);
		
		JsonPath js = ReUsableMethods.rawToJson(response);
		String msg = js.get("msg");
		System.out.println(msg);
		
	}
	
	@DataProvider(name="BookData")
	public Object[][] getData() {
		return new Object[][] {{"abc", "123"}, {"abd", "124"}, {"abe", "125"}};
	}
}
