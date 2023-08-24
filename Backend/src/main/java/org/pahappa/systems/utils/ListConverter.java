package org.pahappa.systems.utils;

import org.apache.commons.lang.StringUtils;
import org.sers.webutils.model.security.Permission;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * This class is responsible for converting lists and sets into well formatted strings with commas.
 * @author ttc
 *
 */
public class ListConverter {
	private ListConverter() {
		// Prevent instantiation
	}

	public static String permissionListConverter(Set<Permission> permissions) {
    	if(!permissions.isEmpty()) {
    		List<String> newPermissions = new ArrayList<>();
        	for(Permission thisPermission : permissions) {
        		newPermissions.add(thisPermission.getName());
        	}
        	return StringUtils.join(newPermissions, ", ");
    	}else {
    		return "No Access Controls Available";
    	}
    	
    }
	
}
