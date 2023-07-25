import files.Payload;
import io.restassured.path.json.JsonPath;

public class ComplexJsonParse {

	public static void main(String[] args) {
		System.out.println("hello");
		JsonPath js = new JsonPath(Payload.CoursePrice());
		System.out.println(js.getInt("dashboard.purchaseAmount")+"");
		System.out.println(js.getString("dashboard.website"));
		System.out.println(js.getInt("courses.size()"));
		System.out.println(js.getString("courses[0].title"));
		System.out.println(js.getString("courses[0].price"));
		
		
		for(int i = 0; i < js.getInt("courses.size()"); i++ ) {
			System.out.println(js.getString("courses["+i+"].title") +": "+js.get("courses["+i+"].price").toString());
		}
		
		for(int i = 0; i < js.getInt("courses.size()"); i++ ) {
			if(js.getString("courses["+i+"].title").equalsIgnoreCase("RPA")) {
				System.out.println(js.get("courses["+i+"].copies").toString());
				break;
			}
		}
		
		int TotalpurchaseAmount = js.getInt("dashboard.purchaseAmount");
		int sum = 0;
		for(int i = 0; i < js.getInt("courses.size()"); i++ ) {
			sum += js.getInt("courses["+i+"].price") * js.getInt("courses["+i+"].copies");
		}
		System.out.println(TotalpurchaseAmount == sum);
	}
}
