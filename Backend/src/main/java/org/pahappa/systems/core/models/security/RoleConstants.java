package org.pahappa.systems.core.models.security;

/**
 * This class contains all the constants for the roles in the system
 * In case your system has predefined roles, you can add them here and annotate them with the {@link SystemRole} annotation
 * A migration will be created for you to add the roles to the database.
 *
 * @version 1.0
 * @see SystemRole - The annotation used to annotate the roles
 */
public final class RoleConstants {
    @SystemRole(name = "Api user", description = "Has role for api users")
    public static final String ROLE_API_USER = "Api User";

    //Add your roles here
    //e.g public static final String SALES_AGENT = "Sales Agent";

    private RoleConstants() {

    }


}
