package org.pahappa.systems.web.controllers;

import java.io.Serializable;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

import lombok.Getter;
import org.pahappa.systems.web.views.HyperLinks;

/**
 * Central place for URLs to xhtml pages.
 * <p>
 * Restrict the name of the bean, in case the name of the class is changed,
 * references to the class can still access the right bean.
 * <p>
 * Its application scoped to ensure that only one instance of the bean exists in
 * the entire life cycle of the application.
 */
@Getter
@ManagedBean(name = "navigationController")
@ApplicationScoped
public class NavigationController implements Serializable {
    /**
     * Add your navigation links here
     */
    private static final long serialVersionUID = 1L;
    private final String userForm = HyperLinks.USER_FORM;
    private final String rolesView = HyperLinks.ROLES_VIEW;
    private final String profileView = HyperLinks.PROFILE_VIEW;
    private final String loginForm = HyperLinks.LOGIN_FORM;
    private final String homePage = HyperLinks.DASHBOARD;


}
