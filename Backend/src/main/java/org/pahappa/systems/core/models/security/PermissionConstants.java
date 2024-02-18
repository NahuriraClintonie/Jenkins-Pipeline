package org.pahappa.systems.core.models.security;

/**
 * This class contains all the permissions that are used in the system
 * Permissions are used to control access to certain parts of the system
 *
 * @see SystemPermission
 * @version 1.0
 */
public final class PermissionConstants {
    private PermissionConstants() { // Prevent instantiation: this class is only for storing constants
    }

    //Base permissions
    @SystemPermission(name = "Api user", description = "Has ability to access API endpoints")
    public static final String PERM_API_USER = "Api User";
    @SystemPermission(name = "Add User", description = "Has ability to add user")
    public static final String PERM_ADD_USER = "Add User";

    @SystemPermission(name = "Capture Payment", description = "Has ability to capture payment details")
    public static final String PERM_CAPTURE_PAYMENT = "Capture Payment";

    @SystemPermission(name = "Approve Payment", description = "Has ability to approve a payment")
    public static final String PERM_APPROVE_PAYMENT = "Approve Payment";

    @SystemPermission(name = "View Payment", description = "Has ability to view payment details")
    public static final String PERM_VIEW_PAYMENT = "View Payment";

    @SystemPermission(name = "Add Client", description = "Has ability to add client")
    public static final String PERM_ADD_CLIENT = "Add Client";

    @SystemPermission(name = "Edit User", description = "Has ability to edit user")
    public static final String PERM_EDIT_USER = "Edit User";

    @SystemPermission(name = "Add Product", description = "Has ability to add product")
    public static final String PERM_ADD_PRODUCT = "Add Product";

    @SystemPermission(name = "View Clients", description = "Has ability to view client")
    public static final String PERM_VIEW_CLIENT = "View Clients";

    @SystemPermission(name = "View Product", description = "Has ability to view product")
    public static final String PERM_VIEW_PRODUCT = "Add Product";

    @SystemPermission(name = "View Invoice", description = "Has ability to view invoice")
    public static final String PERM_VIEW_INVOICE = "View Invoice";

    @SystemPermission(name = "Edit Invoice", description = "Has ability to view invoice")
    public static final String PERM_EDIT_INVOICE = "Edit Invoice";

    @SystemPermission(name = "Edit Product", description = "Has ability to edit product")
    public static final String PERM_EDIT_PRODUCT = "Edit Product";

    @SystemPermission(name = "Edit Client", description = "Has ability to edit client")
    public static final String PERM_EDIT_CLIENT = "Edit Client";

    @SystemPermission(name = "Add product subscription", description = "Has ability to add product subscription")
    public static final String PERM_ADD_PRODUCT_SUBSCRIPTION = "Add product subscription";

    @SystemPermission(name = "View product subscription", description = "Has ability to view product subscription")
    public static final String PERM_VIEW_PRODUCT_SUBSCRIPTION = "View product subscription";

    @SystemPermission(name = "Edit product subscription", description = "Has ability to edit product subscription")
    public static final String PERM_EDIT_PRODUCT_SUBSCRIPTION = "Edit product subscription";

    @SystemPermission(name = "Add client subscription", description = "Has ability to add client subscription")
    public static final String PERM_ADD_CLIENT_SUBSCRIPTION = "Add client subscription";

    @SystemPermission(name = "View client subscription", description = "Has ability to view client subscription")
    public static final String PERM_VIEW_CLIENT_SUBSCRIPTION = "View client subscription";

    @SystemPermission(name = "View general settings", description = "Has ability to view general settings")
    public static final String PERM_VIEW_GENERAL_SETTINGS = "View general settings";

    @SystemPermission(name = "View email templates", description = "View email templates")
    public static final String PERM_VIEW_EMAIL_TEMPLATES = "View email templates";

    @SystemPermission(name = "Add email template", description = "Add email template")
    public static final String PERM_ADD_EMAIL_TEMPLATE= "Add email template";

    @SystemPermission(name = "Edit email template", description = "Edit email template")
    public static final String PERM_EDIT_EMAIL_TEMPLATE= "Edit email template";

    @SystemPermission(name = "Edit general settings", description = "Edit general settings")
    public static final String PERM_EDIT_GENERAL_SETTINGS= "Edit general settings";

    @SystemPermission(name = "Delete email template", description = "Delete email template")
    public static final String PERM_DELETE_EMAIL_TEMPLATE= "Delete email template";


    //Add your permissions here
    //e.g public static final String PERM_ADD_USER = "Add User";

}
