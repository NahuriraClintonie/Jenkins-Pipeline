package org.pahappa.systems.core.models.security;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * System role annotation. This annotation is used to annotate fields in the {@link RoleConstants} class to
 * indicate that the field is a system role.
 * <p>
 * The annotation has two attributes: name and description. The name attribute is the name of the role, and the
 * description attribute is the description of the role.
 *
 * @version 1.0
 * @see Target - The annotation that indicates where the annotation can be used
 * @see Retention - The annotation that indicates how long the annotation should be retained
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SystemRole {

    /**
     * The name of the role
     *
     * @return - The name of the role
     */
    String name();

    /**
     * The description of the role
     *
     * @return - The description of the role
     */
    String description() default "Not supplied";

}
