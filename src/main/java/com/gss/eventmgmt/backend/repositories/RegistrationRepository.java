package com.gss.eventmgmt.backend.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.repository.CrudRepository;

import com.gss.eventmgmt.backend.data.Registration;

@Transactional
public interface RegistrationRepository extends CrudRepository<Registration, Long>{
	public Optional<Registration> findByPhoneNumber(String phoneNumber);
}
