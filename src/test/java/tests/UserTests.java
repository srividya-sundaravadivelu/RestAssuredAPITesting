package tests;


import config.BaseTest;
import dataproviders.CSVDataProvider;
import io.restassured.response.Response;
import models.User;
import models.UserAddress;
import utils.LogHelper;

import java.util.Map;

import org.testng.Assert;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.containsString;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasToString;


public class UserTests extends BaseTest {
	
	private static int newUserId;
	private static String newUserFirstName;
	
	
    @Test(priority = 1, dataProvider = "getUsersData", dataProviderClass = CSVDataProvider.class)
    public void testGetUsers(Map<String, String> data) {
    	expectedStatusCode = Integer.parseInt(data.get("expected_status_code"));
    	schemaPath = "users_schema.json";
    	
    		
    	response = request
	    	.given()
			.when()
				.get(data.get("endpoint"))
			.then()		
				 .statusCode(expectedStatusCode) // Status Code validation
				 .header("Content-Type", containsString("application/json")) // Header validation
				 .extract().response();     	
    	
    	
    	if (response.statusCode() == 200)
    	{
    		LogHelper.info("Get All Users successful");
    		
    		// Schema validation
    		response.then().body(matchesJsonSchemaInClasspath(schemaPath));
    	}
    	
    	
			
    }
    
    @Test(priority = 2, dataProvider = "createNewUserData", dataProviderClass = CSVDataProvider.class)
    public void testCreateNewUser(Map<String, String> data) {
    	expectedStatusCode = Integer.parseInt(data.get("expected_status_code"));
    	schemaPath = "user_schema.json";
    	
        // Create a user object with the data
        User user = new User();
        user.setUserContactNumber(data.get("user_contact_number"));
        user.setUserEmailId(data.get("user_email_id"));
        user.setUserFirstName(data.get("user_first_name"));
        user.setUserLastName(data.get("user_last_name"));
        
        UserAddress address = new UserAddress();
        address.setStreet(data.get("street"));
        address.setPlotNumber(data.get("plotNumber"));
        address.setState(data.get("state"));
        address.setCountry(data.get("country"));
        address.setZipCode(data.get("zipCode"));
        
        user.setUserAddress(address);   
	        
        // Send the post request
        response = request
        						.given()
									.body(user)
								.when()
									.post(data.get("endpoint"))
								.then()
									.statusCode(expectedStatusCode) // Status code validation
									.header("Content-Type", containsString("application/json")) // Header validation
									.extract().response(); 
        
        // Get the response
        if (response.statusCode() == 201) {
        	newUserId = response.jsonPath().getInt("user_id");
        	Assert.assertNotNull(newUserId, "User ID should not be null!");	  
        	LogHelper.info("Created User ID: " + newUserId);
        	
        	newUserFirstName = response.jsonPath().getString("user_first_name");
        	Assert.assertNotNull(newUserFirstName, "User First Name should not be null!");	          	
        	LogHelper.info("Created User First Name: " + newUserFirstName);	  
        	
        	// check for valid schema 
			response.then().body(matchesJsonSchemaInClasspath(schemaPath));			
			
			// Data Validation
			validateResponseData(response, user);		    
        }
    }
    
    @Test(priority = 3, dependsOnMethods = "testCreateNewUser", dataProvider = "getUserByIDData", dataProviderClass = CSVDataProvider.class)
    public void testGetUserById(Map<String, String> data) {
    	
    	expectedStatusCode = Integer.parseInt(data.get("expected_status_code"));
    	schemaPath = "user_schema.json";
    	
        response = request
        	.given()
            	.pathParam("userId", newUserId)
	        .when()
	            .get(data.get("endpoint") + "/{userId}")  
	        .then()
	            .statusCode(expectedStatusCode) // Status Code validation
	            .header("Content-Type", containsString("application/json")) // Header validation
	            .extract().response(); 
        
        
        if (response.statusCode() == 200) {
        	
        	LogHelper.info("Get user details for ID: " + newUserId);
        	
        	// Schema Validation
        	response.then().body(matchesJsonSchemaInClasspath(schemaPath));
        	
        	// Data Validation
			response.then().body("user_id", equalTo(newUserId));
        }
        
    }
    
    @Test(priority = 4, dependsOnMethods = "testCreateNewUser", dataProvider = "getUserByFirstNameData", dataProviderClass = CSVDataProvider.class)
    public void testGetUserByFirstName(Map<String, String> data) {
    	expectedStatusCode = Integer.parseInt(data.get("expected_status_code"));
    	schemaPath = "users_schema.json";
    	
    	response = request
	    	.given()
	        	.pathParam("userName", newUserFirstName)
		    .when()
		        .get(data.get("endpoint") + "/{userName}")  
		    .then()
		        .statusCode(expectedStatusCode) // Status code validation
		        .header("Content-Type", containsString("application/json")) // Header validation
	            .extract().response();     	
    	
        if (response.statusCode() == 200) {
        	
        	LogHelper.info("Get user details for first name: " + newUserFirstName);
        	
        	// Schema Validation
        	response.then().body(matchesJsonSchemaInClasspath(schemaPath));
        	
        	// Data Validation
        	response.then().body("user_first_name", everyItem(equalTo(newUserFirstName)));        	
        }
    }
    
    @Test(priority = 5, dependsOnMethods = "testCreateNewUser", dataProvider = "updateUserData", dataProviderClass = CSVDataProvider.class)
    public void testUpdateUser(Map<String, String> data) {
    	
    	expectedStatusCode = Integer.parseInt(data.get("expected_status_code"));
    	schemaPath = "user_schema.json";   	
    	
    	 // Fetch the existing user by ID
    	 User existingUser = request
    		        .given()
    		            .pathParam("userId", newUserId)  
    		        .when()
    		            .get("uap/user/{userId}")
    		        .then()
    		            .statusCode(200) 
    		            .extract()
    		            .as(User.class);  

	  	// Update user object with CSV data (only if new data is provided)
	    if (data.get("user_first_name") != null &&  !data.get("user_first_name").trim().isEmpty()) {
	        existingUser.setUserFirstName(data.get("user_first_name"));
	    }
	    if (data.get("user_last_name") != null  && !data.get("user_last_name").trim().isEmpty()) {
	        existingUser.setUserLastName(data.get("user_last_name"));
	    }
	    if (data.get("user_email_id") != null  && !data.get("user_email_id").trim().isEmpty()) {
	        existingUser.setUserEmailId(data.get("user_email_id"));
	    }
	    if (data.get("user_contact_number") != null  && !data.get("user_contact_number").trim().isEmpty()) {
	        existingUser.setUserContactNumber(data.get("user_contact_number"));
	    }
	    
	    UserAddress address = existingUser.getUserAddress();
	    if (data.get("plotNumber") != null  && !data.get("plotNumber").trim().isEmpty()) {
	    		address.setPlotNumber(data.get("plotNumber"));
	    }
	    if (data.get("street") != null  && !data.get("street").trim().isEmpty()) {
	    	address.setStreet(data.get("street"));
	    }
	    if (data.get("state") != null  && !data.get("state").trim().isEmpty()) {
	        address.setState(data.get("state"));
	    }
	    if (data.get("country") != null && !data.get("country").trim().isEmpty()) {
	        address.setCountry(data.get("country"));
	    }
	    if (data.get("zipCode") != null && !data.get("zipCode").trim().isEmpty()) {
	        address.setZipCode(data.get("zipCode"));
	    }
	    existingUser.setUserAddress(address);  

        //  Send PUT request to update the user
        response = request
            .given()
                .pathParam("userId", newUserId)
                .body(existingUser)  
            .when()
                .put(data.get("endpoint") + "/{userId}")
            .then()
                .statusCode(expectedStatusCode) // Status Code validation
                .header("Content-Type", containsString("application/json")) // Header validation
	            .extract().response();  
        
        if (response.statusCode() == 200) {
        	
        	LogHelper.info("Updated user details for ID: " + newUserId);
        	
        	// Schema validation
        	response.then().body(matchesJsonSchemaInClasspath(schemaPath));
        	
        	// Data validation
        	validateResponseData(response, existingUser);
        } 
	    
    }
    
    @Test(priority = 6, dependsOnMethods = "testCreateNewUser", dataProvider = "deleteUserByIDData", dataProviderClass = CSVDataProvider.class)
    public void testDeleteUserById(Map<String, String> data) {
    	
    	 expectedStatusCode = Integer.parseInt(data.get("expected_status_code"));
    	 schemaPath = "delete_user_schema.json";
    	 
    	 response = request
	        .given()
	            .pathParam("userId", newUserId)
	        .when()
	            .delete(data.get("endpoint") + "/{userId}")
	        .then()
	            .statusCode(expectedStatusCode) // Status Code validation
	            .header("Content-Type", containsString("application/json")) // Header validation
	            .extract().response();        
        
        if (response.statusCode() == 200) {       	
        	
        	LogHelper.info("Deleted user with ID: " + newUserId);   
        	
        	// Schema validation
	        response.then().body(matchesJsonSchemaInClasspath(schemaPath));		 
	        
	        // Data validation
        	response.then().body("message", equalToIgnoringCase("User is deleted successfully"));
        } 
    }
    
	@Test(priority = 7, dependsOnMethods = "testCreateNewUser", dataProvider = "deleteUserByFirstNameData", dataProviderClass = CSVDataProvider.class)
	public void testDeleteUserByFirstName(Map<String, String> data) {
		
		 expectedStatusCode = Integer.parseInt(data.get("expected_status_code"));
		 schemaPath = "delete_user_schema.json";
		 
		 String userFirstName = data.get("user_first_name");
		 response = request
		    .given()
		        .pathParam("userfirstname", userFirstName)
		    .when()
		        .delete(data.get("endpoint") + "/{userfirstname}")
		    .then()
		        .statusCode(expectedStatusCode) // Status Code validation
		        .header("Content-Type", containsString("application/json")) // Header validation
	            .extract().response();  
		 
		 if (response.statusCode() == 200) { 
			 
			 LogHelper.info("Deleted user with first name: " + userFirstName);
			 
			// Schema validation
	        response.then().body(matchesJsonSchemaInClasspath(schemaPath));		 
			 
			 // Data validation
			 response.then().body("message", equalToIgnoringCase("User is deleted successfully"));
		 }
	}
	
	private void validateResponseData(Response response, User user)
	{
		UserAddress address = user.getUserAddress();		
		
		response.then()
		    .body("user_id", notNullValue())  
		    .body("creation_time", notNullValue())
		    .body("last_mod_time", notNullValue())
		    .body("user_first_name", equalTo(user.getUserFirstName()))
		    .body("user_last_name", equalTo(user.getUserLastName()))
		    .body("user_email_id", equalTo(user.getUserEmailId()))
		    .body("user_contact_number",hasToString(user.getUserContactNumber()))		    
		    .body("userAddress.plotNumber", equalTo(address.getPlotNumber()))
		    .body("userAddress.street", equalTo(address.getStreet()))
		    .body("userAddress.state", equalTo(address.getState()))
		    .body("userAddress.country", equalTo(address.getCountry()))
		    .body("userAddress.zipCode", equalTo(Integer.parseInt(address.getZipCode())))
		    .body("userAddress.addressId", notNullValue());	
	}
    
}
