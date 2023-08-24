package org.pahappa.systems.core.models.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * System permission annotation. This annotation is used to annotate fields in the {@link PermissionConstants} class to
 * indicate that the field is a system permission.
 * <p>
 * The annotation has two attributes: name and description. The name attribute is the name of the permission, and the
 * description attribute is the description of the permission.
 *
 * @version 1.0
 * @see Target - The annotation that indicates where the annotation can be used
 * @see Retention - The annotation that indicates how long the annotation should be retained
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemPermission {
    /**
     * The name of the permission
     *
     * @return - The name of the permission
     */
    String name();

    /**
     * The description of the permission
     *
     * @return - The description of the permission
     */
    String description() default "Not supplied";

}
