package org.pahappa.systems.core.constants;

import org.apache.commons.lang.StringUtils;

public enum TemplateType {
    /**
     *
     */
    NEW_SUBSCRIPTION("Client received a new subscription"),

    PARTIAL_PAYMENT("Client made a partial payment on a subscription"),

    FULL_PAYMENT("Client paid all the subscription fees"),
    SUBSCRIPTION_EXPIRED("Client subscription has expired"),
    SUBSCRIPTION_OVERDUE("Client subscription expired and is overdue");

    private String name;

    TemplateType(String name) {
        this.name = name;
    }

    public static final TemplateType getEnumObject(String value) {
        if (StringUtils.isBlank(value))
            return null;
        for (TemplateType object : TemplateType.values()) {
            if (object.getName().equals(value))
                return object;
        }
        return null;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
