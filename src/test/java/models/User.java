package models;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import utils.EmailGenerator;
import utils.PhoneNumberGenerator;

public class User {

    @JsonProperty("user_first_name")
    private String userFirstName;

    @JsonProperty("user_last_name")
    private String userLastName;

    @JsonProperty("user_contact_number")
    private String userContactNumber;

    @JsonProperty("user_email_id")
    private String userEmailId;

    @JsonProperty("userAddress")
    private UserAddress userAddress;
    
    @JsonProperty("user_id")
    @JsonSerialize(using = CustomFieldSerializer.class)
	private int userId; 
    
    @JsonProperty("creation_time")
    @JsonInclude(JsonInclude.Include.NON_NULL) 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime creationTime; 
    
    @JsonProperty("last_mod_time")
    @JsonInclude(JsonInclude.Include.NON_NULL) 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    private OffsetDateTime lastModifiedTime; 
   
    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserContactNumber() {
        return userContactNumber;
    }

    public void setUserContactNumber(String userContactNumber) {
    	 if (userContactNumber.equalsIgnoreCase("GENERATE_IN_CODE"))
    		 userContactNumber = PhoneNumberGenerator.generatePhoneNumber();
    	 
        this.userContactNumber = userContactNumber;
    }

    public String getUserEmailId() {
        return userEmailId;
    }

    public void setUserEmailId(String userEmailId) {
         if (userEmailId.equalsIgnoreCase("GENERATE_IN_CODE"))
        	 userEmailId = EmailGenerator.generateEmail();
         
        this.userEmailId = userEmailId;
    }

    public UserAddress getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(UserAddress userAddress) {
        this.userAddress = userAddress;
    }    
   
    public OffsetDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(OffsetDateTime creationTime) {
        this.creationTime = creationTime;
    }
    
    public OffsetDateTime getLastModifiedTime() {
        return creationTime;
    }

    public void setLastModifiedTime(OffsetDateTime lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }
}
