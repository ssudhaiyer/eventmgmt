package com.gss.eventmgmt.backend.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Data
@Table(name = "activity")
public class Activity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String description;
	private Date activityDate;
	@ManyToOne
	private Event event;
	private Integer maxUsers;
	private String type;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "activity")
	private List<MemberActivity> members = new ArrayList<MemberActivity>();

	public Activity() {
	}

	public Activity(String desc, Date activityDate, Event event, Integer maxUsers, String type) {
		this.description = desc;
		this.activityDate = activityDate;
		this.event = event;
		this.maxUsers = maxUsers;
		this.type = type;
	}

	public void addMember(Member member) {
		MemberActivity memberActivity = new MemberActivity(member, this);
		members.add(memberActivity);
		member.getActivities().add(memberActivity);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Activity activity = (Activity) o;
		return Objects.equals(type, activity.type);
	}

	@Override
	public int hashCode() {
		return Objects.hash(type);
	}
}
