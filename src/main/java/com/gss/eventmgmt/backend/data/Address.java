package com.gss.eventmgmt.backend.data;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class Address {
	private String street1;
	private String street2;
	private String city;
	private String country;
	@Column(name="zip_code")
	private String zipCode;

	public Address() {

	}

	public Address(String street1, String street2, String city, String country, String zipCode) {
		this.zipCode = zipCode;
		this.country = country;
		this.city = city;
		this.street2 = street2;
		this.street1 = street1;
	}
}
