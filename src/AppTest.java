import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.response.Response;

public class AppTest {

	@Test
	public void test_01() {
		Response response = RestAssured.get("https://reqres.in/api/users?page=2");
		System.out.println(response.getStatusCode());
		System.out.println(response.getTime());
		
		
	}
}
