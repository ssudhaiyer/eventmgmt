package com.gss.eventmgmt.ui.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gss.eventmgmt.ui.Sections;
import com.gss.eventmgmt.ui.controller.UserController;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Notification;

@Secured({ "ROLE_USER", "ROLE_ADMIN" })
@SpringView(name = "user")
@SideBarItem(sectionId = Sections.VIEWS, caption = "User View")
@FontAwesomeIcon(FontAwesome.ARCHIVE)
public class UserView extends CustomComponent implements View {

	private final UserController userController;

	@Autowired
	public UserView(UserController userController) {
		this.userController = userController;
		Button button = new Button("Call user backend", new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				Notification.show(UserView.this.userController.echo("Hello User World!"));
			}
		});
		setCompositionRoot(button);
	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
	}
}
