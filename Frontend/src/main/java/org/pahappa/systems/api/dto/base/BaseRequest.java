package org.pahappa.systems.api.dto.base;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * This class represents the base request data transfer object
 * It is used to hold the common fields that are used in the request body such as when searching, sorting, pagination
 *
 * @version 1.0
 */
@Getter
@Setter
public class BaseRequest {
    private String searchTerm; // used for searching /query
    private String sortBy; // used for sorting
    private int offset = 0; // used for pagination, the number of records to skip
    private int limit = 0; // used for pagination, the number of records to return
    private boolean sortAsc = true; // used for sorting, whether to sort ascending or descending
    private Date dateCreated; // used for filtering, the date the record was created
}
