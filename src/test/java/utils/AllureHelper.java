package utils;

import io.qameta.allure.Allure;
import io.restassured.response.Response;

import org.testng.Assert;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

public class AllureHelper {

    // status code validation
    public static void verifyStatusCode(int actualStatusCode, int expectedStatusCode) {
        Allure.step("Verify Status Code", () -> {
            Assert.assertEquals(actualStatusCode, expectedStatusCode, 
                "Expected status code " + expectedStatusCode + " but got: " + actualStatusCode);
        });
    }

    // content-type validation
    public static void verifyContentType(String contentType) {
        Allure.step("Verify Content-Type", () -> {
            Assert.assertTrue(contentType.contains("application/json"), 
                "Unexpected Content-Type: " + contentType);
        });
    }

    // schema validation
    public static void validateJsonSchema(String schemaPath, Response response) {
        Allure.step("Validate Response Schema", () -> {
            // Check if the response status code is 200 or 201 before validating the schema
            if (response.statusCode() == 200 || response.statusCode() == 201) {
                response.then().body(matchesJsonSchemaInClasspath(schemaPath));
            }
        });
    }

    // Step for attaching the response body
    public static void attachResponseBody(Response response) {
        Allure.addAttachment("Response Body", "application/json", response.body().asPrettyString());
    }
}
