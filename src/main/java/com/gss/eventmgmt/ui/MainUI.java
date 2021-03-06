package com.gss.eventmgmt.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.security.util.SecurityExceptionUtils;
import org.vaadin.spring.sidebar.components.ValoSideBar;
import org.vaadin.spring.sidebar.security.VaadinSecurityItemFilter;

import com.gss.eventmgmt.ui.views.AccessDeniedView;
import com.gss.eventmgmt.ui.views.ErrorView;
import com.vaadin.annotations.Push;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.DefaultErrorHandler;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ui.Transport;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI
@Theme("mytheme")
@Push(transport = Transport.WEBSOCKET)
public class MainUI extends UI {

	@Autowired
	ApplicationContext applicationContext;

	@Autowired
	VaadinSecurity vaadinSecurity;

	@Autowired
	SpringViewProvider springViewProvider;

	@Autowired
	ValoSideBar sideBar;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Suhasini Pooja");
		// Let's register a custom error handler to make the 'access denied' messages a
		// bit friendlier.
		setErrorHandler(new DefaultErrorHandler() {
			@Override
			public void error(com.vaadin.server.ErrorEvent event) {
				if (SecurityExceptionUtils.isAccessDeniedException(event.getThrowable())) {
					Notification.show("Sorry, you don't have access to do that.");
				} else {
					super.error(event);
				}
			}
		});
		HorizontalLayout layout = new HorizontalLayout();
		layout.setSizeFull();

		// By adding a security item filter, only views that are accessible to the user
		// will show up in the side bar.
		sideBar.setItemFilter(new VaadinSecurityItemFilter(vaadinSecurity));
		layout.addComponent(sideBar);

		CssLayout viewContainer = new CssLayout();
		viewContainer.setSizeFull();
		layout.addComponent(viewContainer);
		layout.setExpandRatio(viewContainer, 1f);

		Navigator navigator = new Navigator(this, viewContainer);
		// Without an AccessDeniedView, the view provider would act like the restricted
		// views did not exist at all.
		springViewProvider.setAccessDeniedViewClass(AccessDeniedView.class);
		navigator.addProvider(springViewProvider);
		navigator.setErrorView(ErrorView.class);
		navigator.navigateTo(navigator.getState());
		setNavigator(navigator);
		setContent(layout); // Call this here because the Navigator must have been configured before the
							// Side Bar can be attached to a UI.
	}
}
