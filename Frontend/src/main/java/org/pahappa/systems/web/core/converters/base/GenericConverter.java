package org.pahappa.systems.web.core.converters.base;

import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.BaseEntity;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Generic converter for all entities that extends BaseEntity, and GenericService
 * <p>
 * Usage
 *  creates a converter class that extends this class and pass the entity and service class as parameters
 * e.g.
 *  <p>
 * <code>
 * public class UserConverter extends GenericConverter<User, UserService> { // do nothing here }
 * </code>
 * </p>
 *
 * @param <T> the entity class
 * @param <K> the service class
 */
public class GenericConverter<T extends BaseEntity, K extends GenericService<T>> implements Converter {

    private static final int INDEX_OF_PARAMETERIZED_SERVICE = 1;
    private Class<?> serviceType;

    public GenericConverter() {
        try {
            this.serviceType = getGenericClass(this.serviceType);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Class<?> getGenericClass(Class<?> inferedClass) throws ClassNotFoundException {
        Type mySuperclass = getClass().getGenericSuperclass();
        Type tType = ((ParameterizedType) mySuperclass).getActualTypeArguments()[INDEX_OF_PARAMETERIZED_SERVICE];
        String className = tType.toString().split(" ")[1];
        inferedClass = Class.forName(className);

        return inferedClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.convert.Converter#getAsObject(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, java.lang.String)
     */
    @Override
    public Object getAsObject(FacesContext arg0, UIComponent arg1, String arg2) {
        if (arg2 == null || arg2.isEmpty()) return null;

        return ((K) ApplicationContextProvider.getBean(this.serviceType)).getInstanceByID(arg2);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * javax.faces.convert.Converter#getAsString(javax.faces.context.FacesContext,
     * javax.faces.component.UIComponent, java.lang.Object)
     */
    @Override
    public String getAsString(FacesContext arg0, UIComponent arg1, Object object) {
        if (object == null || object instanceof String) return null;

        return ((T) object).getId();
    }
}
