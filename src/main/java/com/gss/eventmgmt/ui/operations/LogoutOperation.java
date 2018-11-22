package com.gss.eventmgmt.ui.operations;

import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.spring.security.VaadinSecurity;
import org.vaadin.spring.sidebar.annotation.FontAwesomeIcon;
import org.vaadin.spring.sidebar.annotation.SideBarItem;

import com.gss.eventmgmt.ui.Sections;
import com.vaadin.server.FontAwesome;
import com.vaadin.spring.annotation.SpringComponent;

@SpringComponent
@SideBarItem(sectionId = Sections.OPERATIONS, caption = "Logout")
@FontAwesomeIcon(FontAwesome.POWER_OFF)
public class LogoutOperation implements Runnable {

    private final VaadinSecurity vaadinSecurity;

    @Autowired
    public LogoutOperation(VaadinSecurity vaadinSecurity) {
        this.vaadinSecurity = vaadinSecurity;
    }

    @Override
    public void run() {
        vaadinSecurity.logout();
    }
}
