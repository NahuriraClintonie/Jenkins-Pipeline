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
    @SystemPermission(name = "View Setting", description = "Has ability to view Application Settings")
    public static final String PERM_VIEW_SETTING = "View Setting";

    //Add your permissions here
    //e.g public static final String PERM_ADD_USER = "Add User";

}
