package com.gss.eventmgmt.ui.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gss.eventmgmt.ui.Sections;
import com.gss.eventmgmt.ui.controller.UserController;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Notification;

@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "User operation", order = 0)
@FontAwesomeIcon(FontAwesome.ANCHOR)
public class UserOperation implements Runnable {

	private final UserController userController;

	@Autowired
	public UserOperation(UserController userController) {
		this.userController = userController;
	}

	@Override
	public void run() {
		Notification.show(userController.echo("Hello World"));
	}
}
