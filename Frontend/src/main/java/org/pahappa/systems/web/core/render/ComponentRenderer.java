package org.pahappa.systems.web.core.render;

import lombok.Getter;
import lombok.Setter;
//import org.sers.webutils.model.security.PermissionConstants;

//import org.apache.poi.ss.formula.functions.Hyperlink;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.shared.SharedAppData;
import org.pahappa.systems.core.models.security.PermissionConstants;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.view.facelets.FaceletContext;
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
    private boolean canViewProduct = false;
    private boolean canViewClient = false;
    private boolean canEditClient = false;
    private boolean canEditInvoice = false;
    private boolean canViewInvoice = false;
    private boolean canAddUser = false;
    private boolean canEditUser = false;
    private boolean canEditProduct = false;
    private boolean canCapturePayment = false;
    private boolean canApprovePayment = false;
    private boolean canViewPayment = false;
    private boolean canViewClientSubscription = false;
    private boolean canAddClientSubscription = false;
    private boolean canAddProductSubscription = false;
    private boolean canEditProductSubscription = false;
    private boolean canViewEmailTemplates = false;
    private boolean canAddEmailTemplate = false;
    private boolean canEditEmailTemplate = false;
    private boolean canViewGeneralSettings = false;
    private boolean canEditGeneralSettings = false;
    private boolean canDeleteEmailTemplate = false;
    private User loggedInUser;
    private boolean canViewProductSubscription;

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

            this.canAddProduct = loggedInUser.hasPermission(PermissionConstants.PERM_ADD_PRODUCT);

            this.canViewProduct = loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_PRODUCT);

            this.canViewInvoice = loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_INVOICE);

            this.canAddUser = loggedInUser.hasPermission(PermissionConstants.PERM_ADD_USER);

            this.canEditProduct= loggedInUser.hasPermission(PermissionConstants.PERM_EDIT_PRODUCT);

            this.canEditInvoice = loggedInUser.hasPermission(PermissionConstants.PERM_EDIT_INVOICE);

            this.canCapturePayment= loggedInUser.hasPermission(PermissionConstants.PERM_CAPTURE_PAYMENT);

            this.canApprovePayment= loggedInUser.hasPermission(PermissionConstants.PERM_APPROVE_PAYMENT);

            this.canViewPayment= loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_PAYMENT);

            this.canAddUser= loggedInUser.hasPermission(PermissionConstants.PERM_ADD_USER);

            this.canEditUser = loggedInUser.hasPermission(PermissionConstants.PERM_EDIT_USER);

            this.canAddClientSubscription= loggedInUser.hasPermission(PermissionConstants.PERM_ADD_CLIENT_SUBSCRIPTION);

            this.canViewClientSubscription = loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_CLIENT_SUBSCRIPTION);

            this.canAddProductSubscription = loggedInUser.hasPermission(PermissionConstants.PERM_ADD_PRODUCT_SUBSCRIPTION);

            this.canEditProductSubscription = loggedInUser.hasPermission(PermissionConstants.PERM_EDIT_PRODUCT_SUBSCRIPTION);

            this.canViewProductSubscription = loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_PRODUCT_SUBSCRIPTION);

            this.canViewEmailTemplates = loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_EMAIL_TEMPLATES);

            this.canAddEmailTemplate = loggedInUser.hasPermission(PermissionConstants.PERM_ADD_EMAIL_TEMPLATE);

            this.canViewGeneralSettings = loggedInUser.hasPermission(PermissionConstants.PERM_VIEW_GENERAL_SETTINGS);

            this.canEditGeneralSettings = loggedInUser.hasPermission(PermissionConstants.PERM_EDIT_GENERAL_SETTINGS);

            this.canEditEmailTemplate = loggedInUser.hasPermission(PermissionConstants.PERM_EDIT_EMAIL_TEMPLATE);

            this.canDeleteEmailTemplate = loggedInUser.hasPermission(PermissionConstants.PERM_DELETE_EMAIL_TEMPLATE);

            this.administrator = loggedInUser.hasAdministrativePrivileges();

            this.setAdministrator(loggedInUser.hasPermission(org.sers.webutils.model.security.PermissionConstants.PERM_ADMINISTRATOR));



        }
    }

    public static void redirectToLogin(boolean value){
        if (value){

        }
    }
}