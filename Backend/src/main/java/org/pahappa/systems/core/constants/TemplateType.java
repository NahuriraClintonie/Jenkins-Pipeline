package org.pahappa.systems.core.constants;

import org.apache.commons.lang.StringUtils;

public enum TemplateType {
    /**
     *
     */
    ORDER_CONFIRMED("Order Confirmed"),

    ORDER_REJECTED("Order Rejected"),

    ORDER_DELIVERED("Order Delivered"),
    AWAITING_PICKUP("Order Waiting For Pick Up");

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
