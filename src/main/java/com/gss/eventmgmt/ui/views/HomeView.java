package com.gss.eventmgmt.ui.views;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.shared.VaadinSharedSecurity;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gss.eventmgmt.backend.data.Activity;
import com.gss.eventmgmt.backend.data.Member;
import com.gss.eventmgmt.backend.data.Registration;
import com.gss.eventmgmt.backend.repositories.ActivityRepository;
import com.gss.eventmgmt.backend.repositories.EventRepository;
import com.gss.eventmgmt.backend.repositories.MemberRepository;
import com.gss.eventmgmt.backend.repositories.RegistrationRepository;
import com.gss.eventmgmt.backend.services.EnrollmentManager;
import com.gss.eventmgmt.backend.services.RegisterationService;
import com.gss.eventmgmt.ui.Sections;
import com.vaadin.annotations.Theme;
import com.vaadin.data.provider.DataProvider;
import com.vaadin.data.provider.ListDataProvider;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.ClassResource;
import com.vaadin.server.FileResource;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.ContentMode;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.spring.annotation.SpringView;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Grid.Column;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.renderers.ButtonRenderer;
import com.vaadin.ui.themes.ValoTheme;

@SpringView(name = "")
@SideBarItem(sectionId = Sections.VIEWS, caption = "Home", order = 0)
@FontAwesomeIcon(FontAwesome.HOME)
@Theme("mytheme")
public class HomeView extends VerticalLayout implements View {
	VaadinSharedSecurity vaadinSecurity;
	EnrollmentManager enrollmentManager;
	ActivityRepository activityRepo;
	Registration managedRegistration;
	RegistrationRepository registrationRepository;
	ListDataProvider<Member> dataProvider = null;
	private final Grid<Member> userGrid = new Grid<Member>();

	public HomeView(VaadinSharedSecurity vaadinSecurity, ActivityRepository activityRepo,
			EnrollmentManager enrollmentManager, RegistrationRepository registrationRepository,
			MemberRepository memberRepository) {
		this.enrollmentManager = enrollmentManager;
		this.activityRepo = activityRepo;
		this.registrationRepository = registrationRepository;
		ThemeResource resource = new ThemeResource("images/banner.png");

		setSpacing(true);
		setMargin(true);
		this.vaadinSecurity = vaadinSecurity;
		if (vaadinSecurity.getAuthentication().getPrincipal() instanceof Registration) {
			Registration registration = (Registration) vaadinSecurity.getAuthentication().getPrincipal();
			System.out.println("Registration info " + registration.toString());
			managedRegistration = registrationRepository.findByPhoneNumber(registration.getPhoneNumber()).get();
		}

		Activity managedActivity = activityRepo.findAll().iterator().next();
		
		Embedded bannerEmbed = new Embedded("", resource);
		bannerEmbed.setType(Embedded.TYPE_IMAGE);
		bannerEmbed.setWidth("100%");
		bannerEmbed.setStyleName("banner");
		addComponent(bannerEmbed);

		Button addSuhasiniBtn = new Button("Add Suvasini");
		addSuhasiniBtn.addStyleName(ValoTheme.BUTTON_PRIMARY);
		
		addSuhasiniBtn.addClickListener(new ClickListener() {

			@Override
			public void buttonClick(ClickEvent event) {
				final Window sub = new Window("");
				sub.setModal(true);
				sub.center();
				sub.setClosable(false);
				sub.setWidth("40%");
				sub.setHeight("40%");
				sub.setResizable(false);
				UI.getCurrent().addWindow(sub);
				VerticalLayout addSuhasiniLayout = new VerticalLayout();
				addSuhasiniLayout.setMargin(new MarginInfo(true, false, false, false));
				FormLayout addSuhasiniForm = new FormLayout();
				addSuhasiniForm.setMargin(new MarginInfo(true, false, false, true));
				TextField suhasiniName = new TextField("Suhasini Name");
				suhasiniName.setRequiredIndicatorVisible(true);
				TextField age = new TextField("Age");
				Button save = new Button("Save");
				Button cancel = new Button("Cancel");
				save.addStyleName(ValoTheme.BUTTON_PRIMARY);
				save.setDisableOnClick(true);
				save.setClickShortcut(ShortcutAction.KeyCode.ENTER);
				cancel.setDisableOnClick(true);

				save.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						String agevalue = age.getValue();
						if (agevalue == null || agevalue == "")
							agevalue = "Not available";
						Member m = new Member(managedRegistration, suhasiniName.getValue(), age.getValue());
						managedRegistration = enrollmentManager.enroll(managedRegistration, m, managedActivity);
						userGrid.setItems(managedRegistration.getMembers());
						userGrid.setHeightByRows(managedRegistration.getMembers().size());
						sub.close();
					}
				});

				cancel.addClickListener(new ClickListener() {
					@Override
					public void buttonClick(ClickEvent event) {
						sub.close();
					}
				});

				addSuhasiniForm.addComponent(suhasiniName);
				addSuhasiniForm.addComponent(age);
				addSuhasiniLayout.addComponent(addSuhasiniForm);
				HorizontalLayout actionBar = new HorizontalLayout();
				actionBar.setMargin(new MarginInfo(true, false, false, true));
				actionBar.addComponent(save);
				actionBar.addComponent(cancel);
				save.setClickShortcut(KeyCode.ENTER);
				addSuhasiniForm.addComponent(actionBar);
				suhasiniName.focus();
				sub.setContent(addSuhasiniLayout);

			}
		});

		Label body = new Label(
				"<p>Suvasini Registration Information for <b> " + managedRegistration.getPhoneNumber() + "</b> </p>");

		body.setContentMode(ContentMode.HTML);
		body.setStyleName("label");
		addComponent(body);

		if (managedRegistration.getMembers().size() == 0) {
			System.out.println("No registered member. Adding login user as a member");
			Member m = new Member(managedRegistration, managedRegistration.getName(), "Not available");
			managedRegistration = enrollmentManager.enroll(managedRegistration, m, managedActivity);
		}

		TextField memberAgeTextField = new TextField();
		VerticalLayout suhasiniViewLayout = new VerticalLayout();
		suhasiniViewLayout.setMargin(new MarginInfo(false, false, false, false));
		userGrid.setItems(managedRegistration.getMembers());

		// userGrid.setItems(regManaged.getMembers());
		Column<Member, String> nameClmn = userGrid.addColumn(Member::getMemberName).setCaption("Suvasini Name");
		Column<Member, String> ageClmn = userGrid.addColumn(Member::getMemberAge)
				.setEditorComponent(memberAgeTextField, Member::setMemberAge).setCaption("Suvasini Age (optional)");
		Column<Member, Button> deleteClmn = userGrid.addColumn(person -> "Delete", new ButtonRenderer(clickEvent -> {
			managedRegistration = enrollmentManager.unenroll(managedRegistration, (Member) clickEvent.getItem(),
					managedActivity);
			userGrid.setItems(managedRegistration.getMembers());
			userGrid.setHeightByRows(managedRegistration.getMembers().size());
		})).setCaption("Remove Suvasini");
		userGrid.getEditor().setEnabled(true);
		userGrid.setHeightByRows(managedRegistration.getMembers().size());
		suhasiniViewLayout.addComponent(userGrid);
		suhasiniViewLayout.addComponent(addSuhasiniBtn);
		addComponent(suhasiniViewLayout);

	}

	@Override
	public void enter(ViewChangeListener.ViewChangeEvent event) {
	}

}
