
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.Courses;
import pojo.GetCourse;
import pojo.Mobile;
import pojo.WebAutomation;


public class oAuth2Test {

	public static void main(String[] args) throws InterruptedException {
		

//		System.setProperty("webdriver.chrome.driver", "../driver114/chromedriver.exe");
//		WebDriver driver = new ChromeDriver();
//		driver.get("https://accounts.google.com/o/oauth2/v2/auth?scope=https://www.googleapis.com/auth/userinfo.email&auth_url=https://accounts.google.com/o/oauth2/v2/auth&client_id=692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com&response_type=code&redirect_uri=https://rahulshettyacademy.com/getCourse.php");
//		Thread.sleep(5000);
//		driver.findElement(By.xpath("//input[@type='email']")).sendKeys("<your email address>");
//		driver.findElement(By.xpath("//input[@type='email']")).sendKeys(Keys.ENTER);
//		Thread.sleep(3000);
//		driver.findElement(By.xpath("//input[@type='password']")).sendKeys("<password>");
//		driver.findElement(By.xpath("//input[@type='password']")).sendKeys(Keys.ENTER);
//		Thread.sleep(5000);
//		String url = driver.getCurrentUrl();

		// looks like one time use only
		//String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AZEOvhUHmVcJsEQGes3k38NUqfeuNVWvvBTDBV81cl2a74ISsNr36OiSBcuptDR4wt_A7w&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&hd=sjinnovation.com&prompt=consent";
		//String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AZEOvhVhn3xVPVclcdWGoiXuJPba9I8MqEH6ivRvY7JhYX1pw1fW4DxLxcjQ7N-Q8bzq_g&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=consent";
		//String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AZEOvhUyqp5VaMRDFg_KE9l_EAoeOC_z3aJaxj3RwZL9Fugnxze15XnzPkB4QveKYOY0WA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		//String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AZEOvhV6YdgpQPbVGC3ZMUqF3FnQGR_hVOX47044EQoacANUNFGxh0HuKwEGzO4Yb3ueuQ&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		//String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AZEOvhUb0ax79aGAnYehoBN6gUsuWNSi5CJab1R_DcPwpJr4OLWSh2NVpdtLpt5aYeKINA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		//String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AZEOvhWX5JhIXy1mbGt5diOqk2uJZ_GiBGKWs3xMELk507elGA5KKPJ8lU4q9KiG7ELFuA&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String url = "https://rahulshettyacademy.com/getCourse.php?code=4%2F0AZEOvhWJxuB1zEn_dEmLHY0L_QkyUEVZ0ULwSc6xb78ffkfVILgn1cPPJSi9bX1mDDJPbg&scope=email+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email+openid&authuser=0&prompt=none";
		String[] array = url.split("&");
		String code = "";
		for (String txt : array) {
			if (txt.contains("code=")) {
				code = txt.substring(txt.indexOf("code=")+5, txt.length());
				System.out.println(code);
				break;
			}
				
		}
		
		String accessTokenResponse = RestAssured.given().urlEncodingEnabled(false)
		.queryParam("code", code)
		.queryParam("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com")
		.queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W")
		.queryParams("grant_type", "authorization_code")
		.queryParam("redirect_uri", "https://rahulshettyacademy.com/getCourse.php")
		.when().post("https://www.googleapis.com/oauth2/v4/token").asString();
			
		System.out.println(accessTokenResponse);
		JsonPath js = new JsonPath(accessTokenResponse);
		String accessToken = js.getString("access_token");
		
//		String response  = RestAssured.given().queryParam("access_token", accessToken)
//		.when().get("https://rahulshettyacademy.com/getCourse.php").then()
//		.extract().response().asString();

		GetCourse gc  = RestAssured.given().queryParam("access_token", accessToken)
		.expect().defaultParser(Parser.JSON)		
		.when().get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);
	
		System.out.println(gc.getInstructor());
		System.out.println(gc.getUrl());
		System.out.println(gc.getServices());
		System.out.println(gc.getExpertise());
		
		Courses courses = gc.getCourses();
		List<WebAutomation> wa= courses.getWebAutomation();
		List<Api> ap= courses.getApi();
		List<Mobile> mo= courses.getMobile();
		
		for (WebAutomation webAutomation : wa) {
			System.out.println(webAutomation.getCourseTitle());
			System.out.println(webAutomation.getPrice());
		}
		
		for (Api api : ap) {
			System.out.println(api.getCourseTitle());
			System.out.println(api.getPrice());
		}
		
		for (Mobile mobile : mo) {
			System.out.println(mobile.getCourseTitle());
			System.out.println(mobile.getPrice());
		}
		
		System.out.println(gc.getLinkedIn());
		
	}

}

