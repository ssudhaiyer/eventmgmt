package com.gss.eventmgmt.ui.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gss.eventmgmt.ui.Sections;
import com.gss.eventmgmt.ui.controller.AdminController;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;

@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Admin operation", order = 1)
@FontAwesomeIcon(FontAwesome.WRENCH)
public class AdminOperation implements Runnable {

	private final AdminController adminController;

	@Autowired
	public AdminOperation(AdminController adminController) {
		this.adminController = adminController;
	}

	@Override
	public void run() {
		Notification.show(adminController.adminOnlyEcho("Hello Admin World"));
	}
}
