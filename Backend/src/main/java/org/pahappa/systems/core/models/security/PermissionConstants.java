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

    @SystemPermission(name = "Add Client", description = "Has ability to add client")
    public static final String PERM_ADD_CLIENT = "Add Client";

    @SystemPermission(name = "Add Product", description = "Has ability to add product")
    public static final String PERM_ADD_PRODUCT = "Add Product";

    @SystemPermission(name = "Generate Invoice", description = "Has ability to generate an invoice")
    public static final String PERM_GENERATE_INVOICE = "Generate Invoice";

    @SystemPermission(name = "View Clients", description = "Has ability to view client")
    public static final String PERM_VIEW_CLIENT = "View Client";

    @SystemPermission(name = "View Product", description = "Has ability to view product")
    public static final String PERM_VIEW_PRODUCT = "Add Product";

    @SystemPermission(name = "View Invoice", description = "Has ability to view invoice")
    public static final String PERM_VIEW_INVOICE = "View Invoice";

    @SystemPermission(name = "Edit Product", description = "Has ability to edit product")
    public static final String PERM_EDIT_PRODUCT = "Edit Product";

    @SystemPermission(name = "Edit Client", description = "Has ability to edit client")
    public static final String PERM_EDIT_CLIENT = "Edit Client";

    //Add your permissions here
    //e.g public static final String PERM_ADD_USER = "Add User";

}
