package org.pahappa.systems.core.models.security;

import org.sers.webutils.model.security.Role;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for interpreting the roles in the system and returning them as a list of {@link Role}s
 * It process the {@link SystemRole} annotation and returns a list of {@link Role}s
 *
 * @version 1.0
 */
public final class RolesInterpreter {

    /**
     * Function to reflectively get the roles in the system
     * It reads the fields in the RoleConstants class by checking if they have the SystemRole annotation and returns a list of roles
     *
     * @return - The list of roles
     */
    public static List<Role> reflectivelyGetRoles() {
        List<Role> roles = new ArrayList<>();

        for (Field field : RoleConstants.class.getFields()) {
            if (field.isAnnotationPresent(SystemRole.class)) {
                SystemRole roleAnnotation = field.getAnnotation(SystemRole.class);
                Role role = new Role();
                role.setName(roleAnnotation.name());
                role.setDescription(roleAnnotation.description());
                roles.add(role);
            }
        }

        return roles;
    }
}
