package com.gss.eventmgmt;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.vaadin.spring.security.annotation.EnableVaadinManagedSecurity;
import org.vaadin.spring.security.config.AuthenticationManagerConfigurer;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionFixationProtectionStrategy;
import org.vaadin.spring.events.EventBus;
import org.vaadin.spring.events.support.ApplicationContextEventBroker;
import org.vaadin.spring.http.HttpService;
import org.vaadin.spring.security.annotation.EnableVaadinSharedSecurity;
import org.vaadin.spring.security.config.VaadinSharedSecurityConfiguration;
import org.vaadin.spring.security.shared.VaadinAuthenticationSuccessHandler;
import org.vaadin.spring.security.shared.VaadinSessionClosingLogoutHandler;
import org.vaadin.spring.security.web.VaadinRedirectStrategy;
import org.vaadin.spring.security.shared.VaadinUrlAuthenticationSuccessHandler;

import com.gss.eventmgmt.backend.data.Activity;
import com.gss.eventmgmt.backend.data.Address;
import com.gss.eventmgmt.backend.data.Event;
import com.gss.eventmgmt.backend.data.Member;
import com.gss.eventmgmt.backend.data.Registration;
import com.gss.eventmgmt.backend.repositories.ActivityRepository;
import com.gss.eventmgmt.backend.repositories.EventRepository;
import com.gss.eventmgmt.backend.repositories.MemberRepository;
import com.gss.eventmgmt.backend.repositories.RegistrationRepository;
import com.gss.eventmgmt.backend.services.EnrollmentManager;
import com.gss.eventmgmt.backend.services.RegisterationService;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaRepositories
public class EventmgmtApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventmgmtApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(EventRepository eventRepo, ActivityRepository activityRepo,
			EnrollmentManager enrollmentManager, RegisterationService registrationService,
			MemberRepository memberRepository) {
		return (args) -> {
			Date today = new Date();
			Address address = new Address("SVC Temple", "SVC Temple", "Fremont", "CA", "94536");
			// save an event
			Event event = new Event(address, new Date());
			Event managedEvent = eventRepo.save(event);

			// save activity for event
			Activity suhasiniPooja = new Activity("Suhasini Pooja", today, managedEvent, 100, "SUHASINI_POOJA");
			Activity managedActivity = activityRepo.save(suhasiniPooja);

			// register user
			Address myAddress = new Address("429 Silvercrownway", "", "San Ramon", "CA", "94532");
			Registration registration = new Registration("5105419066", "sudha.hs@gmail.com", "ADMIN", today, myAddress);
			Optional<Registration> optionalManagedRegistration = registrationService.registerUser(registration);
			optionalManagedRegistration = registrationService.registerUser(registration);
			if (optionalManagedRegistration.isPresent()) {
				Registration managedRegistration = optionalManagedRegistration.get();
				System.out.println("==> **** REGISTERED USER WITH ID *****" + managedRegistration.getId());
				System.out.println("==> ***** MANAGED ACTIVITY ID *****" + managedActivity.getId());
				Member member1 = new Member(managedRegistration, "sudha", "40");
				managedRegistration = enrollmentManager.enroll(managedRegistration, member1, managedActivity);
				Member member2 = new Member(managedRegistration, "jeyanthi", "35");
				managedRegistration = enrollmentManager.enroll(managedRegistration, member2, managedActivity);
				Member member3 = new Member(managedRegistration, "Prema", "70");
				managedRegistration = enrollmentManager.enroll(managedRegistration, member3, managedActivity);
				System.out.println("==> ***** NUMBER OF REGISTERED MEMBERS **** " + managedRegistration.getMembers().size());
				managedRegistration = enrollmentManager.unenroll(managedRegistration, member3, managedActivity);
				System.out.println("==> *** NUMBER OF REGISTERED MEMEBERS AFTER REMOVING LAST USER ****" + managedRegistration.getMembers().size());
				Member member4 = new Member(managedRegistration, "Shantha", "50");
				managedRegistration = enrollmentManager.enroll(managedRegistration, member4, managedActivity);
				System.out.println("==>  *** NUMBER OF REGISTERED MEMEBERS REPLACING REMOVED USER ***" + managedRegistration.getMembers().size());
				managedRegistration.getMembers().forEach(m-> System.out.println(m.toString()));
			}
		};
	}

	/**
	 * Configure Spring Security.
	 */
	@Configuration
	@EnableWebSecurity
	@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true, proxyTargetClass = true)
	@EnableVaadinSharedSecurity
	static class SecurityConfiguration extends WebSecurityConfigurerAdapter {

		@Autowired
		RegistrationRepository registrationRepository;

		@Autowired
		private UserDetailsService userDetailService;

		@Autowired
		public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
			final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
			authenticationProvider.setUserDetailsService(userDetailService);
			authenticationProvider.setPasswordEncoder(passwordEncoder());
			auth.userDetailsService(userDetailService).and().authenticationProvider(authenticationProvider);
		}

		@Bean
		public static PasswordEncoder passwordEncoder() {
			return PasswordEncoderFactories.createDelegatingPasswordEncoder();
		}

		@Override
		protected void configure(HttpSecurity http) throws Exception {
			http.csrf().disable(); // Use Vaadin's built-in CSRF protection instead
			http.authorizeRequests().antMatchers("/login/**").anonymous().antMatchers("/vaadinServlet/UIDL/**")
					.permitAll().antMatchers("/vaadinServlet/HEARTBEAT/**").permitAll().anyRequest().authenticated();
			http.httpBasic().disable();
			http.formLogin().disable();
			// Remember to add the VaadinSessionClosingLogoutHandler
			http.logout().addLogoutHandler(new VaadinSessionClosingLogoutHandler()).logoutUrl("/logout")
					.logoutSuccessUrl("/login?logout").permitAll();
			http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"));
			// Instruct Spring Security to use the same RememberMeServices as Vaadin4Spring.
			// Also remember the key.
			http.rememberMe().rememberMeServices(rememberMeServices()).key("myAppKey");
			// Instruct Spring Security to use the same authentication strategy as
			// Vaadin4Spring
			http.sessionManagement().sessionAuthenticationStrategy(sessionAuthenticationStrategy());
		}

		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers("/VAADIN/**");
		}

		/**
		 * The {@link AuthenticationManager} must be available as a Spring bean for
		 * Vaadin4Spring.
		 */
		@Override
		@Bean
		public AuthenticationManager authenticationManagerBean() throws Exception {
			return super.authenticationManagerBean();
		}

		/**
		 * The {@link RememberMeServices} must be available as a Spring bean for
		 * Vaadin4Spring.
		 */
		@Bean
		public RememberMeServices rememberMeServices() {
			return new TokenBasedRememberMeServices("myAppKey", userDetailsService());
		}

		/**
		 * The {@link SessionAuthenticationStrategy} must be available as a Spring bean
		 * for Vaadin4Spring.
		 */
		@Bean
		public SessionAuthenticationStrategy sessionAuthenticationStrategy() {
			return new SessionFixationProtectionStrategy();
		}

		@Bean(name = VaadinSharedSecurityConfiguration.VAADIN_AUTHENTICATION_SUCCESS_HANDLER_BEAN)
		VaadinAuthenticationSuccessHandler vaadinAuthenticationSuccessHandler(HttpService httpService,
				VaadinRedirectStrategy vaadinRedirectStrategy) {
			return new VaadinUrlAuthenticationSuccessHandler(httpService, vaadinRedirectStrategy, "/");
		}
	}

}
