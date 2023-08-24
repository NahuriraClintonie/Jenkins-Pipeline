package org.pahappa.systems.web.views;

/**
 * This class contains all the hyperlinks to the pages in the application
 */
public class HyperLinks {
    public static final String DASHBOARD = "/pages/dashboard/Dashboard.xhtml?faces-redirect=true";
    public static final String PROFILE_VIEW = "/pages/users/ProfileView.xhtml?faces-redirect=true";
    public static final String USERS_VIEW = "/pages/users/UsersView.xhtml?faces-redirect=true";
    public static final String USER_FORM = "/pages/users/UserForm.xhtml?faces-redirect=true";
    public static final String ROLES_VIEW = "/pages/users/RolesView.xhtml?faces-redirect=true";
    public static final String PERMISSIONS_VIEW = "/pages/users/PermissionsView.xhtml?faces-redirect=true";
    public static final String ROLE_FORM = "/pages/users/RolesForm.xhtml?faces-redirect=true";
    public static final String LOGIN_FORM = "/ExternalViews/Login.xhtml?faces-redirect=true";

    private HyperLinks() {
    }

}
