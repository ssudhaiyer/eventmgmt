package com.gss.eventmgmt.backend.services;


import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gss.eventmgmt.backend.data.Registration;
import com.gss.eventmgmt.backend.repositories.RegistrationRepository;

@Service
public class LoginService {
	@Autowired
	RegistrationRepository registrationRepository;
	
	public Optional<Registration> loginUser(String phoneNumber) {
		if( phoneNumber == null || phoneNumber == "")
			return Optional.ofNullable(null);
		
		Optional<Registration> reg = registrationRepository.findByPhoneNumber(phoneNumber);
		return reg;
	}
}
