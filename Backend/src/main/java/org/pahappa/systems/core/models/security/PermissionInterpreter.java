package org.pahappa.systems.core.models.security;

import org.sers.webutils.model.security.Permission;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for interpreting the permissions in the system and returning them as a list of {@link Permission}s
 * It process the {@link SystemPermission} annotation and returns a list of {@link Permission}s
 *
 * @version 1.0
 * @see SystemPermission
 * @see Permission
 * @see PermissionConstants
 */
public final class PermissionInterpreter {
    public static List<Permission> reflectivelyGetPermissions() {

        List<Permission> permissions = new ArrayList<>(); // The list of permissions to return

        // Loop through all the fields in the PermissionConstants class and create a permission for each field that has the SystemPermission annotation
        //read the fields in the PermissionConstants class by checking if they have the SystemPermission annotation
        for (Field field : PermissionConstants.class.getFields()) {
            if (field.isAnnotationPresent(SystemPermission.class)) { //if the field has the SystemPermission annotation
                SystemPermission permissionAnnotation = field.getAnnotation(SystemPermission.class);
                Permission permission = new Permission(); // Create a new permission
                permission.setName(permissionAnnotation.name()); // Set the name of the permission
                permission.setDescription(permissionAnnotation.description()); // Set the description of the permission
                permissions.add(permission); // Add the permission to the list of permissions
            }
        }
        return permissions; // Return the list of permissions
    }
}
