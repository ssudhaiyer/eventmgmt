package com.gss.eventmgmt.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.gss.eventmgmt.backend.data.Registration;
import com.gss.eventmgmt.backend.repositories.RegistrationRepository;

@Service
public class RegisterationService implements UserDetailsService {
	@Autowired
	RegistrationRepository registrationRepository;

	public Optional<Registration> registerUser(Registration registrationInfo) {
		System.out.println("Registring user " + registrationInfo.getPhoneNumber());
		Optional<Registration> reg = registrationRepository.findByPhoneNumber(registrationInfo.getPhoneNumber());
		if (reg.isPresent()) {
			System.out.println("**** REGISTRATION IS PRESENT ALREADY *****");
			return reg;
		}
		Optional<Registration> regManaged = Optional.ofNullable(registrationRepository.save(registrationInfo));
		return regManaged;
	}
	

	@Override
	public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
		System.out.println("loadUserByUsername " + phoneNumber);
		Optional<Registration> registrationInfo = registrationRepository.findByPhoneNumber(phoneNumber);
		if (!registrationInfo.isPresent()) {
			System.out.println("Registration info for " + phoneNumber + " not found");
			throw new UsernameNotFoundException("Phone number" + phoneNumber + " is unknown");
		}
		System.out.println("Registration Info " + registrationInfo.get().getEmail());
		return registrationInfo.get();
	}

}
