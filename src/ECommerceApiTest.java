import java.io.File;
import java.util.ArrayList;

import org.testng.Assert;
import org.testng.annotations.Test;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.Credential;
import pojo.LoginResponse;
import pojo.OrderDetail;
import pojo.Orders;

public class ECommerceApiTest {

	@Test
	public void login() {
		
		Credential credential = new Credential();
		credential.setUserEmail("testerone@mail.com");
		credential.setUserPassword("Tester123@");
		
//		String res = RestAssured.given().baseUri("https://rahulshettyacademy.com").header("Content-Type","application/json").body(credential)
//				.when().post("/api/ecom/auth/login").then().extract().response().asString();
		
		RequestSpecification baseReqSepc = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").setContentType(ContentType.JSON).build();
		ResponseSpecification reps = new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
		RequestSpecification loginRequest = RestAssured.given().spec(baseReqSepc).body(credential);
		LoginResponse loginResponse = loginRequest.when().post("/api/ecom/auth/login").then().spec(reps).extract().response().as(LoginResponse.class);
		System.out.println(loginResponse.getUserId());
		
		
		// adding product
		
		RequestSpecification addReqSepc = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").addHeader("authorization", loginResponse.getToken()).build();

		
		RequestSpecification addRequest = RestAssured.given().spec(addReqSepc)
				.param("productName", "Windows 10").param("productAddedBy", loginResponse.getUserId())
				.param("productCategory", "IT").param("productSubCategory", "OS")
				.param("productPrice", "10000")
				.param("productDescription", "MicroSoft OS").param("productFor", "Human")
				.multiPart("productImage", new File("C:\\Users\\JAVA_USER\\Desktop\\win_logo.png"));
		
		
		String addProductResponse = addRequest.when().post("/api/ecom/product/add-product").then().extract().asString();
		
		JsonPath jp = new JsonPath(addProductResponse);
		String productId = jp.getString("productId");
		System.out.println(productId);
		
		// Create Order
		
		OrderDetail orderDetail = new OrderDetail();
		orderDetail.setCountry("Bangladesh");
		orderDetail.setProductOrderedId(productId);
		ArrayList<OrderDetail> list  = new ArrayList<>();
		list.add(orderDetail);
		
		Orders orders = new  Orders();
		orders.setOrders(list);
		
		RequestSpecification orderReqSepc = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", loginResponse.getToken()).setContentType(ContentType.JSON).build();
		
		
		RequestSpecification orderRequest = RestAssured.given().spec(orderReqSepc).body(orders);
		String orderResponse = orderRequest.when().post("/api/ecom/order/create-order").then().extract().response().asString();
		System.out.println(orderResponse);
		
		// Delete Product
		
		RequestSpecification deleteReqSepc = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com")
				.addHeader("authorization", loginResponse.getToken()).setContentType(ContentType.JSON).build();
		
		
		RequestSpecification deleteRequest = RestAssured.given().spec(deleteReqSepc).pathParam("productId", productId);
		String deleteResponse = deleteRequest.when().delete("/api/ecom/product/delete-product/{productId}").then().extract().response().asString();
		
		JsonPath jsonPath = new JsonPath(deleteResponse);
		
		Assert.assertEquals("Product Deleted Successfully", jsonPath.getString("message"));
		
	}
}
