package com.gss.eventmgmt.backend.data;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Transient;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;

@Entity(name = "Registration")
@Table(name = "registration")
@Data
public class Registration implements UserDetails {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(name = "phone_number")
	private String phoneNumber;
	private String password;
	private String name;
	private String email;
	private String role;
	@Transient
	private boolean registered;
	@Column(name = "created_on")
	private Date createdOn;
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "registration", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Member> members = new HashSet<Member>();
	@Embedded
	private Address address;
	@Transient
	private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();;

	public Registration() {

	}

	public Registration(String phoneNumber, String email, String role, Date createOn, Address address) {
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.setPassword(phoneNumber);
		this.role = role;
		this.createdOn = createOn;
		this.address = address;
	}

	public void addMember(Member member) {
		members.add(member);
	}

	public void removeMember(Member member) {
		members.remove(member);
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return null;
	}

	public void setPassword(String password) {
		this.password = passwordEncoder.encode(phoneNumber);
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.phoneNumber;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	@Override
	public String toString() {
		return "registration.id=" + this.id + " reg.phoneNumber=" + this.getPhoneNumber() + "reg.Email=" + this.getEmail() + "reg.Password="+ this.getPassword();
	}
}
