package com.gss.eventmgmt.backend.services;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gss.eventmgmt.backend.data.Activity;
import com.gss.eventmgmt.backend.data.Member;
import com.gss.eventmgmt.backend.data.MemberActivity;
import com.gss.eventmgmt.backend.data.Registration;
import com.gss.eventmgmt.backend.repositories.ActivityRepository;
import com.gss.eventmgmt.backend.repositories.MemberActivityRepository;
import com.gss.eventmgmt.backend.repositories.MemberRepository;
import com.gss.eventmgmt.backend.repositories.RegistrationRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class EnrollmentManager {
	@Autowired
	RegistrationRepository registrationRepository;
	@Autowired
	MemberRepository memberRepository;
	@Autowired
	MemberActivityRepository memberActivityRepository;
	@Autowired
	ActivityRepository activityRepository;

	@Transactional
	public Registration enroll(Registration registration, Member member, Activity activity) {
		Registration managedRegistration = null;
		if (activity.getMembers().size() >= activity.getMaxUsers()) {
			// TODO: add member to waitlist here
		} else {
			member.addActivity(activity);
			registration.addMember(member);
			managedRegistration = registrationRepository.save(registration);
		}

		return managedRegistration;
	}

	/**
	 * This is not considering other activities the member can be signed up for. If
	 * we need to add that functionality, we need to do the following -> Not remove
	 * the member from registration if he/she is signed up for different activity ->
	 * Homeview should show list of members enrolled for specific activity instead
	 * of showing all members of a registration
	 * 
	 * @param registration
	 * @param member
	 * @param activity
	 * @return
	 */
	public Registration unenroll(Registration registration, Member member, Activity activity) {
		Set<Member> filteredMember = registration.getMembers().stream().filter(
				m -> (m.getMemberName().equals(member.getMemberName()) && m.getMemberAge() == member.getMemberAge()))
				.collect(Collectors.toSet());
		if (filteredMember.size() == 0)
			return registration;
		Member managedMember = filteredMember.iterator().next();
		System.out.println("*** REMOVE MEMBER " + managedMember.toString());
		removeActivity(activity, managedMember.getActivities(), managedMember);
		registration.removeMember(managedMember);
		managedMember.setRegistration(null);
		Registration reg = registrationRepository.save(registration);
		System.out.println(" *** NUMBER OF REGISTERED MEMBERS **** " + reg.getMembers().size());
		return reg;
	}

	public void removeActivity(Activity activity, List<MemberActivity> activities, Member managedMember) {
		for (Iterator<MemberActivity> iterator = activities.iterator(); iterator.hasNext();) {
			MemberActivity memberActivity = iterator.next();
			if (memberActivity.getMember().equals(managedMember) && memberActivity.getActivity().equals(activity)) {
				managedMember.getActivities().remove(memberActivity);
				activity.getMembers().remove(memberActivity);
				memberActivity.setMember(null);
				memberActivity.setActivity(null);
				activityRepository.save(activity);
				break;
			}

		}

	}
}
