package com.gss.eventmgmt.backend.repositories;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.gss.eventmgmt.backend.data.Activity;

@Transactional
public interface ActivityRepository extends CrudRepository<Activity, Long>{

}
