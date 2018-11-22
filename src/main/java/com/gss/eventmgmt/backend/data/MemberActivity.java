package com.gss.eventmgmt.backend.data;

import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

import lombok.Data;

@Entity(name = "MemberActivity")
@Table(name = "member_activity")
@Data
public class MemberActivity {
	@EmbeddedId
	private MemberActivityId id;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("memberId")
	private Member member;

	@ManyToOne(fetch = FetchType.EAGER)
	@MapsId("activityId")
	private Activity activity;

	@Column(name = "created_on")
	private Date createdOn = new Date();

	public MemberActivity() {

	}

	public MemberActivity(Member member, Activity activity) {
		this.member = member;
		this.activity = activity;
		this.id = new MemberActivityId(member.getId(), activity.getId());
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		MemberActivity that = (MemberActivity) o;
		return Objects.equals(member.getMemberName(), that.member.getMemberName()) && Objects.equals(activity.getDescription(), that.activity.getDescription());
	}

	@Override
	public int hashCode() {
		return Objects.hash(member, activity);
	}
}
