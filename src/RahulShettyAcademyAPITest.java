import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.testng.Assert;
import org.testng.annotations.Test;

import files.Payload;
import files.ReUsableMethods;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlace;
import pojo.Location;
import pojo.Types;

import java.util.ArrayList;
import java.util.List;
public class RahulShettyAcademyAPITest {

	@Test
	public void test_04() throws IOException {
		
		AddPlace addPlace =  new AddPlace();
		Location loca = new Location();
		loca.setLat(-91.383494);
		loca.setLng(45.427362);
		addPlace.setLocation(loca);
		addPlace.setAccuracy(45);
		addPlace.setName("Old Book Store");
		addPlace.setPhone_number("(+1) 983 893 3007");
		addPlace.setAddress("200 North lane, Old Bookie");
		Types types = new Types();
		List<String> myList = new ArrayList<>();
		myList.add("Lost bookes");
		myList.add("lost pen");
		types.setTypes(myList);
		addPlace.setTypes(types);
		addPlace.setWebsite("www.lostsomething.com");
		addPlace.setLanguage("English_NG");
		
		
		RequestSpecification reqs = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
		ResponseSpecification reps = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		
		RequestSpecification res = RestAssured.given().spec(reqs).body(addPlace);
		
		Response response = res.when().post("/maps/api/place/add/json").then().spec(reps).extract().response();
				
		String output = response.asString();
		
//		JsonPath jsPath = ReUsableMethods.rawToJson(output);
//		String placeId = jsPath.getString("place_id");
		System.out.println(output);
	}
	
	//@Test
	public void test_03() throws IOException {
		
		AddPlace addPlace =  new AddPlace();
		Location loca = new Location();
		loca.setLat(-91.383494);
		loca.setLng(45.427362);
		addPlace.setLocation(loca);
		addPlace.setAccuracy(45);
		addPlace.setName("new Book Store");
		addPlace.setPhone_number("(+1) 983 893 3937");
		addPlace.setAddress("21 North lane, Bookie");
		Types types = new Types();
		List<String> myList = new ArrayList<>();
		myList.add("file holder");
		myList.add("pen");
		
		types.setTypes(myList);
		addPlace.setTypes(types);
		addPlace.setWebsite("www.something.com");
		addPlace.setLanguage("Franch_FR");
		
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String output = RestAssured.given().queryParam("key", "qaclick123")
		.header("Content-Type","application/json")
		.body(addPlace)
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath jsPath = ReUsableMethods.rawToJson(output);
		String placeId = jsPath.getString("place_id");
		System.out.println(placeId);
	}
	
	//@Test
	public void test_02() throws IOException {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
		String output = RestAssured.given().queryParam("key", "qaclick123")
		.header("Content-Type","application/json")
		.body(new String(Files.readAllBytes(Paths.get("./src/files/addPlace.json"))))
		.when().post("/maps/api/place/add/json")
		.then().assertThat().statusCode(200)
		.extract().response().asString();
		
		JsonPath jsPath = ReUsableMethods.rawToJson(output);
		String placeId = jsPath.getString("place_id");
		System.out.println(placeId);
	}
	
	
	//@Test
	public void test_01() {
		RestAssured.baseURI = "https://rahulshettyacademy.com";
		
	String output = given().log().all().queryParam("key", "qaclick123")
				.header("Content-Type","application/json")
				.body(Payload.addPalce())
				.when().post("/maps/api/place/add/json")
				.then().assertThat().statusCode(200)
				.header("Server", "Apache/2.4.52 (Ubuntu)")
				.body("scope", equalTo("APP"))
				.log().all().extract().response().asString();
	 
		
		JsonPath jsPath = ReUsableMethods.rawToJson(output);
		String placeId = jsPath.getString("place_id");
		System.out.println(placeId);
		
		// Update Place
		String place = "70 Side layout, cohen 10";
		given().log().all().queryParam("key", "qaclick123")
			.header("Content-Type","application/json")
			.body(Payload.editPlace(placeId, place))
			.when().put("/maps/api/place/update/json")
			.then().assertThat().statusCode(200)
			.log().all().body("msg", equalTo("Address successfully updated"));
		
		// Get Place
		String placeResponse = given().log().all()
				.queryParam("key", "qaclick123")
				.queryParam("place_id", placeId)
				.when().get("/maps/api/place/get/json")
				.then().assertThat().log().all().statusCode(200)
				.extract().response().asString();
		
		jsPath = ReUsableMethods.rawToJson(placeResponse);
		String address = jsPath.getString("address");
		System.out.println(address);
		
		Assert.assertEquals(address, place);
		
		// Delete place
		String res = given().queryParam("key", "qaclick123")
				.header("Content-Type","application/json")
				.body(Payload.placeID(placeId))
				.when().delete("/maps/api/place/delete/json")
				.then().assertThat().statusCode(200)
				.extract().response().asString();
		
		jsPath = ReUsableMethods.rawToJson(res);
		System.out.println(jsPath.getString("status"));
		
	}
	

}
