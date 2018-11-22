package com.gss.eventmgmt.backend.repositories;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.gss.eventmgmt.backend.data.Event;

@Transactional
public interface EventRepository extends CrudRepository<Event, Long> {

}
