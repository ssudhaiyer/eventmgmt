package com.gss.eventmgmt.backend.data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import lombok.Data;
import lombok.ToString;

@Entity(name = "Member")
@Table(name = "member")
@Data
public class Member {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "registration_id")
	private Registration registration;

	@Column(name = "member_name")
	private String memberName;

	@Column(name = "member_age")
	private String memberAge;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "member", cascade= CascadeType.ALL, orphanRemoval = true)
	private List<MemberActivity> activities = new ArrayList<MemberActivity>();

	public Member() {
	}

	public Member(Registration registration, String memberName, String memberAge) {
		this.memberAge = memberAge;
		this.memberName = memberName;
		this.registration = registration;
	}

	public void addActivity(Activity activity) {
		MemberActivity memberActivity = new MemberActivity(this, activity);
		activities.add(memberActivity);
		activity.getMembers().add(memberActivity);
	}

	

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		Member member = (Member) o;
		return Objects.equals(memberName, member.memberName) && Objects.equals(memberAge, member.memberAge);
	}

	@Override
	public int hashCode() {
		return Objects.hash(memberName, memberAge);
	}

	@Override
	public String toString() {
		return " member.id = " + this.getId() + ", member.name=" + this.getMemberName() + ", member.age="
				+ this.getMemberAge();
	}
}
