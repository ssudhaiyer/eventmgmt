package com.gss.eventmgmt.backend.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "event")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@Embedded
	private Address address;
	@Column(name="created_on")
	private Date createdOn;
	@OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Activity> activities = new ArrayList<Activity>();
	
	public Event() {
		
	}
	
	public Event(Address address, Date createdOn) {
		this.address = address;
		this.createdOn = createdOn;
	}
}
