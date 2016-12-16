package com.andreiolar.abms.shared;

import java.util.Date;

import com.google.gwt.user.client.rpc.IsSerializable;

public class UserInfo implements IsSerializable {

	private String firstName;
	private String lastName;
	private Date dateOfBirth;
	private String email;
	private String mobileNumber;
	private String gender;
	private String address;
	private String city;
	private String country;
	private String personalNumber;
	private String idSeries;
	private String apartmentNumber;
	private String type;

	private String username;
	private String password;

	public UserInfo() {
	}

	public UserInfo(String firstName, String lastName, Date dateOfBirth, String email, String mobileNumber, String gender, String address,
			String city, String country, String personalNumber, String idSeries, String username, String password, String apartmentNumber) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.dateOfBirth = dateOfBirth;
		this.email = email;
		this.mobileNumber = mobileNumber;
		this.gender = gender;
		this.address = address;
		this.city = city;
		this.country = country;
		this.personalNumber = personalNumber;
		this.idSeries = idSeries;
		this.username = username;
		this.password = password;
		this.apartmentNumber = apartmentNumber;
	}

	public String getApartmentNumber() {
		return apartmentNumber;
	}

	public void setApartmentNumber(String apartmentNumber) {
		this.apartmentNumber = apartmentNumber;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public String getEmail() {
		return email;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public String getGender() {
		return gender;
	}

	public String getAddress() {
		return address;
	}

	public String getCity() {
		return city;
	}

	public String getCountry() {
		return country;
	}

	public String getPersonalNumber() {
		return personalNumber;
	}

	public String getIdSeries() {
		return idSeries;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public void setPersonalNumber(String personalNumber) {
		this.personalNumber = personalNumber;
	}

	public void setIdSeries(String idSeries) {
		this.idSeries = idSeries;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
