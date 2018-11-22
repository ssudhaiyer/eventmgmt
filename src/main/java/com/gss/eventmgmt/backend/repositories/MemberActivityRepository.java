package com.gss.eventmgmt.backend.repositories;

import org.springframework.data.repository.CrudRepository;

import com.gss.eventmgmt.backend.data.MemberActivity;
import com.gss.eventmgmt.backend.data.MemberActivityId;

public interface MemberActivityRepository extends CrudRepository<MemberActivity, MemberActivityId>{

}
