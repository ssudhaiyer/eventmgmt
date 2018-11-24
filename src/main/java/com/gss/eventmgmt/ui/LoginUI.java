package com.gss.eventmgmt.ui;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;

import com.gss.eventmgmt.backend.data.Address;
import com.gss.eventmgmt.backend.data.Registration;
import com.gss.eventmgmt.backend.repositories.RegistrationRepository;
import com.gss.eventmgmt.backend.services.RegisterationService;
import com.gss.eventmgmt.ui.views.AccessDeniedView;
import com.vaadin.annotations.Theme;
import com.vaadin.data.Binder;
import com.vaadin.event.ShortcutAction;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.navigator.SpringViewProvider;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "/login")
@Theme("mytheme")
public class LoginUI extends UI {
	private static final Logger log = LoggerFactory.getLogger(LoginUI.class);
	@Autowired
	VaadinSharedSecurity vaadinSecurity;

	private TextField userName;

	private CheckBox rememberMe;

	private Button login;
	private Button register;
	private Label loginFailedLabel;
	private Label loggedOutLabel;
	private Label phoneNumberLabel;

	@Autowired
	private RegisterationService registrationService;

	@Override
	protected void init(VaadinRequest request) {
		getPage().setTitle("Vaadin Shared Security Demo Login");

		FormLayout loginForm = new FormLayout();
		loginForm.setSizeUndefined();
		phoneNumberLabel = new Label("Please enter your phone number to begin");
		userName = new TextField("");
		rememberMe = new CheckBox("Remember me");
		login = new Button("Login");
		register = new Button("Register");

		loginForm.addComponent(phoneNumberLabel);
		loginForm.addComponent(userName);
		loginForm.addComponent(rememberMe);

		HorizontalLayout loginComponents = new HorizontalLayout();
		loginComponents.addComponent(login);
		loginComponents.addComponent(register);
		loginForm.addComponent(loginComponents);

		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		login.setDisableOnClick(true);
		login.setClickShortcut(ShortcutAction.KeyCode.ENTER);
		login.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				login();
			}
		});

		register.setDisableOnClick(true);
		register.addClickListener(new Button.ClickListener() {
			@Override
			public void buttonClick(Button.ClickEvent event) {
				try {
					registrationService.loadUserByUsername(userName.getValue());
					login();
				} catch (UsernameNotFoundException ue) {
					// flow through registration
					System.out.println("User not found, registering");
				}
				final Window sub = new Window("");
				sub.setModal(true);
				sub.center();
				sub.setClosable(false);
				sub.setWidth("80%");
				sub.setHeight("80%");
				sub.setResizable(false);
				UI.getCurrent().addWindow(sub);

				VerticalLayout registrationLayout = new VerticalLayout();
				
				registrationLayout.setMargin(new MarginInfo(false, false, false, true));
				Label registerLabel = new Label("Please provide the following details to register. <br> <b>Address is optional.<p>",
						ContentMode.HTML);
				
				registerLabel.setStyleName("center50withpadding");
				registrationLayout.addComponent(registerLabel);

				FormLayout registrationForm = new FormLayout();
				registrationForm.setMargin(new MarginInfo(false, false, false, true));

				HorizontalLayout horizon = new HorizontalLayout();
				horizon.setMargin(new MarginInfo(false, false, false, false));

				VerticalLayout ver1 = new VerticalLayout();
				ver1.setMargin(new MarginInfo(false, true, false, false));

				TextField phoneNumber = new TextField("Phone Number");
				phoneNumber.setRequiredIndicatorVisible(true);
				phoneNumber.setValue(userName.getValue());
				TextField name = new TextField("Name");
				name.setRequiredIndicatorVisible(true);
				TextField email = new TextField("Email");
				email.setRequiredIndicatorVisible(true);
				TextField street1 = new TextField("Address");
				ver1.addComponent(phoneNumber);
				ver1.addComponent(name);
				ver1.addComponent(email);
				ver1.addComponent(street1);

				horizon.addComponent(ver1);
				horizon.setExpandRatio(ver1, 1.0f);

				VerticalLayout ver2 = new VerticalLayout();
				ver2.setMargin(new MarginInfo(false, false, false, true));
				TextField city = new TextField("City");
				TextField state = new TextField("State");
				TextField zip = new TextField("Zip");
				TextField country = new TextField("Country");
				ver2.addComponent(city);
				ver2.addComponent(state);
				ver2.addComponent(zip);
				ver2.addComponent(country);

				horizon.addComponent(ver2);
				horizon.setExpandRatio(ver2, 1.0f);

				registrationForm.addComponent(horizon);
				registrationForm.addStyleName("center50");
				registrationForm.setWidth("50%");
				registrationLayout.addComponent(registrationForm);

				HorizontalLayout actionBar = new HorizontalLayout();
				actionBar.setMargin(new MarginInfo(true, false, false, true));
				Button save = new Button("Save");
				Button cancel = new Button("Cancel");
				save.addStyleName(ValoTheme.BUTTON_PRIMARY);
				save.setDisableOnClick(true);
				save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
				cancel.setDisableOnClick(true);
				actionBar.setStyleName("center20");
				
				actionBar.addComponent(save);
				actionBar.addComponent(cancel);
				cancel.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						register.setEnabled(true);
						sub.close();
					}
				});
				
				save.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						Registration registrationInfo = new Registration();
						if( nullCheck(name.getValue()) || nullCheck(email.getValue()) || nullCheck(phoneNumber.getValue()))
						{
							Notification.show("Missing data", "Name, email and phone number are mandatory!", Notification.Type.ERROR_MESSAGE);
							save.setEnabled(true);
							return;
						}
						registrationInfo.setPhoneNumber(userName.getValue());
						registrationInfo.setAddress(new Address(street1.getValue(), state.getValue(), city.getValue(),
								country.getValue(), zip.getValue()));
						registrationInfo.setCreatedOn(new Date());
						registrationInfo.setEmail(email.getValue());
						registrationInfo.setName(name.getValue());
						registrationInfo.setPassword(userName.getValue());
						registrationInfo.setRegistered(true);
						registrationInfo.setRole("USER");
						registrationService.registerUser(registrationInfo);
						login();
					}
				});
				registrationLayout.addComponent(actionBar);
				
				sub.setContent(registrationLayout);
			}
		});

		VerticalLayout loginLayout = new VerticalLayout();
		loginLayout.setSpacing(true);
		loginLayout.setSizeUndefined();

		if (request.getParameter("logout") != null) {
			loggedOutLabel = new Label("You have been logged out!");
			loggedOutLabel.addStyleName(ValoTheme.LABEL_SUCCESS);
			loggedOutLabel.setSizeUndefined();
			loginLayout.addComponent(loggedOutLabel);
			loginLayout.setComponentAlignment(loggedOutLabel, Alignment.BOTTOM_CENTER);
		}

		loginLayout.addComponent(loginFailedLabel = new Label());
		loginLayout.setComponentAlignment(loginFailedLabel, Alignment.BOTTOM_CENTER);
		loginFailedLabel.setSizeUndefined();
		loginFailedLabel.addStyleName(ValoTheme.LABEL_FAILURE);
		loginFailedLabel.setVisible(false);

		loginLayout.addComponent(loginForm);
		loginLayout.setComponentAlignment(loginForm, Alignment.TOP_CENTER);

		VerticalLayout rootLayout = new VerticalLayout(loginLayout);
		rootLayout.setSizeFull();
		rootLayout.setComponentAlignment(loginLayout, Alignment.MIDDLE_CENTER);

		setContent(rootLayout);
	}

	private void login() {
		try {
			vaadinSecurity.login(userName.getValue(), userName.getValue(), rememberMe.getValue());
			System.out.println("Logged in");
		} catch (AuthenticationException ex) {
			userName.focus();
			userName.selectAll();
			loginFailedLabel.setValue(String.format("Login failed: %s", ex.getMessage()));
			loginFailedLabel.setVisible(true);
			if (loggedOutLabel != null) {
				loggedOutLabel.setVisible(false);
			}
		} catch (Exception ex) {
			Notification.show("An unexpected error occurred", ex.getMessage(), Notification.Type.ERROR_MESSAGE);
			LoggerFactory.getLogger(getClass()).error("Unexpected error while logging in", ex);
		} finally {
			login.setEnabled(true);
		}
	}
	
	public static boolean nullCheck(String str) {
		return (str == null || str == "");
	}
}
