import com.google.gson.Gson;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.User;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import utils.Auth;

public class GraphQLTests {
    private static RequestSpecification request;
    public static String baseURI = "https://gorest.co.in/public/v2";

    @BeforeClass
    public void setUp() {
        request = new RequestSpecBuilder()
                .setBaseUri(baseURI)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "*/*")
                .addHeader("Authorization", "Bearer " + Auth.getTokenFromPropertiesFile())
                .build();
    }

    @Test
    public void getAllUsers() {
        //Create the graphql query in JSON format
        String bodyQuery = "{\n" +
                "  \"query\": \"query Users { users { nodes { email gender id name status } }}\"\n" +
                "}";
        Response response = RestAssured.given().log().all()
                .spec(request)
                .body(bodyQuery)
                .when()
                .post("/graphql");

        response.then().log().all(true).assertThat().statusCode(200);
    }

    @Test
    public void getAUserByID() {
        //Create the graphql query in JSON format
        String bodyQuery = "{\n" +
                "  \"query\": \"query User { user(id: \\\"7281392\\\") { email gender id name status }}\"\n" +
                "}";
        Response response = RestAssured.given().log().all()
                .spec(request)
                .body(bodyQuery)
                .when()
                .post("/graphql");

        response.then().log().all(true).assertThat().statusCode(200);
    }

    @Test
    public void createUser() {
        User userObj = new User(2323234,"Jane Feld", "jane@gmail.com", "female", "active");
        String jsonObj = new Gson().toJson(userObj);
        System.out.println(jsonObj);
        //Create the graphql query in JSON format
        String bodyQuery = "{\n" +
                "  \"query\": \"mutation CreateUser { createUser( input: { name: \\\"Alec Great\\\" gender: \\\"male\\\" email: \\\"alec@mail.com\\\" status: \\\"active\\\" } ) { user { email gender id name status } }}\"\n" +
                "}";

        Response response = RestAssured.given().log().all()
                .spec(request)
                .body(bodyQuery)
                .when()
                .post("/graphql");

        response.then().log().all(true).assertThat().statusCode(200);
    }
}
