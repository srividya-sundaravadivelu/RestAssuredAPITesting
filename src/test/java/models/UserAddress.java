package models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class UserAddress {

	@JsonProperty("plotNumber")
    private String plotNumber;
	
	@JsonProperty("street")
    private String street;
	
	@JsonProperty("state")
    private String state;
	
	@JsonProperty("country")
    private String country;
	
	@JsonProperty("zipCode")
    private String zipCode;
	
	@JsonProperty("addressId")
	@JsonSerialize(using = CustomFieldSerializer.class)
	private String addressId; 

    // Getters and Setters
    public String getPlotNumber() {
        return plotNumber;
    }

    public void setPlotNumber(String plotNumber) {
        this.plotNumber = plotNumber;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
    	this.zipCode = zipCode;
    }
}

