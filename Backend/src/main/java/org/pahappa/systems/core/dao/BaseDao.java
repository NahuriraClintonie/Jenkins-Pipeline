package org.pahappa.systems.core.dao;

import com.googlecode.genericdao.dao.jpa.GenericDAO;
import org.sers.webutils.model.RecordStatus;

import java.util.List;

/**
 * * A Base Interface for all Data Access interfaces that enables the Data
 * Access Layer objects to use generics <br/>
 * <br/>
 * <p>
 * The BaseDao interface extends the {@link GenericDAO} interface to provide the
 * generalization of the Data Access objects. <br/>
 * <br/>
 * <p>
 * Usage of this interface can be seen below <br/>
 * <code>
 * public interface UserDAO extends BaseDao{@code <UserAccount>}
 * </code>
 *
 * @param <T>
 * @author Deo
 * @apiNote You can add custom methods to this interface
 */
public interface BaseDao<T> extends GenericDAO<T, String> {
    /**
     * retrieves a list of entities of type T
     * given property value is equal to the given value
     *
     * @param property - the property to be matched
     * @param value    - the value to be matched
     * @return the list of entities
     */
    List<T> searchByPropertyEqual(String property, Object value);

    /**
     * retrieves an instance of type T given property whose value is equal to the given value
     *
     * @return the instance of type T
     */
    T searchUniqueByPropertyEqual(String property, Object value);

    /**
     * searches for a list of entities of a given record status with a given
     * property whose value is equal to the given value
     *
     * @param property     - the property to be matched
     * @param value        - the value to be matched
     * @param recordStatus - the record status to be matched
     * @return the list of entities
     */
    List<T> searchByPropertyEqual(String property, Object value, RecordStatus recordStatus);

    /**
     * searches for a list of entities matching the given record status
     *
     * @param recordStatus - the record status to be matched
     * @return the list of entities
     */
    List<T> searchByRecordStatus(RecordStatus recordStatus);

    /**
     * searches for an entity of a given record status with a given property whose
     * value is equals to the given value
     *
     * @param property     - the property to be matched
     * @param value        - the value to be matched
     * @param recordStatus - the record status to be matched
     * @return the entity
     */
    T searchUniqueByPropertyEqual(String property, Object value, RecordStatus recordStatus);

    /**
     * deletes a given entity. This doesn't remove the entity entirely from the
     * system, but marks the record as deleted and leave it intact for historical
     * purposes
     *
     * @param entity - the entity to be deleted
     */
    void delete(T entity);

    /**
     * updates a given entity
     *
     * @param entity - the entity to be updated
     */
    void update(T entity);

    /**
     * adds a given entity
     *
     * @param entity - the entity to be added
     */
    void add(T entity);

    T directSave(T entity);

    T directMerge(T entity);

    /**
     * This method is used to
     *
     * @param entity
     */
    void directUpdate(T entity);

    void saveBG(T obj);

    void updateBG(Object obj);

    T mergeBG(T obj);

}
