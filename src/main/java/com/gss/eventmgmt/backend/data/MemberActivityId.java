package com.gss.eventmgmt.backend.data;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class MemberActivityId implements Serializable {
	@Column(name = "member_id")
	private Long memberId;
	@Column(name = "activity_id")
	private Long activityId;

	public MemberActivityId() {
	}

	public MemberActivityId(Long memberId, Long activityId) {
		this.memberId = memberId;
		this.activityId = activityId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;

		if (o == null || getClass() != o.getClass())
			return false;

		MemberActivityId that = (MemberActivityId) o;
		return Objects.equals(memberId, that.memberId) && Objects.equals(activityId, that.activityId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(memberId, activityId);
	}
}
