package com.gss.eventmgmt.backend.repositories;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.gss.eventmgmt.backend.data.Member;
import com.gss.eventmgmt.backend.data.Registration;

@Transactional
public interface MemberRepository extends CrudRepository<Member, Long>{

}
