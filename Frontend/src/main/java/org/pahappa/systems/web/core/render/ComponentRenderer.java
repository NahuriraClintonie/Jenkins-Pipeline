package org.pahappa.systems.web.core.render;

import lombok.Getter;
import lombok.Setter;
//import org.sers.webutils.model.security.PermissionConstants;

import org.sers.webutils.model.security.User;
import org.sers.webutils.server.shared.SharedAppData;
import org.pahappa.systems.core.models.security.PermissionConstants;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.Serializable;

/**
 * Managed bean that renders components based on the user's permissions
 *
 * @apiNote You can use this bean to render components based on the user's permissions
 */
@Getter
@Setter
@ManagedBean(name = "componentRenderer")
@SessionScoped
public class ComponentRenderer implements Serializable {

    private static final long serialVersionUID = 1L;
    private boolean administrator = false;
    private boolean salesAgent = false;
    private boolean canAddClient = false;
    private boolean canAddProduct = false;
    private boolean canViewClient = false;
    private boolean canEditClient = false;
    private User loggedInUser;

    /*
    How to use this bean:
    1.
    Define your permissions here
    e.g.,
    private boolean canViewClients = false; // default to false

    2.
    And then in the init method, set the value of the permission
    e.g.,
    ...other init code...
    this.canViewClients = loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_CLIENTS);
    ...other init code...

    3.
    Then in your xhtml file, you can use the rendered attribute to render components based on the permission
    e.g.,
    <p:commandButton rendered="#{componentRenderer.canViewClients}" ...other attributes... />
    <p:commandButton disabled="#{componentRenderer.administrator}" ...other attributes... />
     */

    @PostConstruct
    public void init() {
        this.loggedInUser = SharedAppData.getLoggedInUser();

        if (this.loggedInUser != null) {

            this.canViewClient = loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_CLIENT);
            System.out.println("Can view client"+ this.canViewClient);

            this.canAddClient = loggedInUser.hasPermission(PermissionConstants.PERM_ADD_CLIENT);

            this.canEditClient = loggedInUser.hasPermission(PermissionConstants.PERM_EDIT_CLIENT);
            System.out.println("Can edit client"+ this.canEditClient);

            this.administrator = loggedInUser.hasAdministrativePrivileges();


            this.setAdministrator(loggedInUser.hasPermission(org.sers.webutils.model.security.PermissionConstants.PERM_ADMINISTRATOR));
        }
    }
}