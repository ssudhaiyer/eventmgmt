package com.gss.eventmgmt.ui.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

public interface AdminController {
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	String adminOnlyEcho(String s);

	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_USER')")
	String echo(String s);
}
