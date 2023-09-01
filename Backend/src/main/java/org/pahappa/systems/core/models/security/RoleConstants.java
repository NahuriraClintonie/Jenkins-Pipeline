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

    @SystemRole(name = "Sales Agent", description = "Has role for sales agents")
    public static final String ROLE_SALES_AGENT = "Sales Agent";

    @SystemRole(name = "Accountant", description = "Has role of an accountant")
    public static final String ROLE_ACCOUNTANT = "Accountant";

    //Add your roles here
    //e.g public static final String SALES_AGENT = "Sales Agent";

    private RoleConstants() {

    }


}
