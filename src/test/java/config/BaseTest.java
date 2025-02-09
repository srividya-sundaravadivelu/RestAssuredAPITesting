package config;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import utils.AllureHelper;
import utils.ConfigReader;
import utils.LogHelper;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import filters.RequestResponseLoggingFilter;

public class BaseTest {

	protected RequestSpecification request;
	protected static Response response;
	protected static int expectedStatusCode;
	protected static String schemaPath;

	@BeforeMethod
	public void setUp() {
		RestAssured.baseURI = ConfigReader.getBaseUri();
		request = RestAssured.given().auth().basic(ConfigReader.getUserName(), ConfigReader.getPassword())
				.header("Content-Type", "application/json").given().filter(new RequestResponseLoggingFilter());

	}

	@AfterMethod
	public void tearDown() {
		if (response != null) {

			AllureHelper.verifyStatusCode(response.statusCode(), expectedStatusCode);
			AllureHelper.verifyContentType(response.getHeader("Content-Type"));
			AllureHelper.validateJsonSchema(schemaPath, response);
			AllureHelper.attachResponseBody(response);

			// Clear the response and expectedStatusCode for the next test
			response = null;
			expectedStatusCode = 0;
			schemaPath = "";

		}
	}

}
