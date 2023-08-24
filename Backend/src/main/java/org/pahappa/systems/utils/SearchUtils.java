package org.pahappa.systems.utils;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.utils.SortField;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Utility class for custom searches.
 *
 * @version 1.0
 */
public class SearchUtils {
    /**
     * The minimum number of characters for a search term.
     */
    private static final int MINIMUM_CHARACTERS_FOR_SEARCH_TERM = 2;

    private SearchUtils() { // Prevent instantiation
    }


    /**
     * Check if the search term satisfies the query criteria.
     *
     * @param query - the query
     * @return - true if the search term satisfies the query criteria, false otherwise
     */
    public static boolean searchTermSatisfiesQueryCriteria(String query) {
        if (StringUtils.isBlank(query)) {
            return false;
        }
        return query.length() >= MINIMUM_CHARACTERS_FOR_SEARCH_TERM;
    }

    /**
     * Generate search terms for the given search fields and query.
     *
     * @param query        - the query
     * @param searchFields - the search fields
     * @return - the search object
     */
    private static Search generateSearchTerms(String query, List<String> searchFields) {
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

        if (StringUtils.isNotBlank(query) && searchTermSatisfiesQueryCriteria(query)) {
            ArrayList<Filter> filters = new ArrayList<>();
            generateSearchTerms(searchFields, query, filters);
            search.addFilterAnd(filters.toArray(new Filter[filters.size()]));
        }
        return search;
    }

    /**
     * Generate search terms for the given search fields and query.
     *
     * @param searchFields - the search fields
     * @param query        - the query
     * @param filters      - the filters
     */
    private static void generateSearchTerms(List<String> searchFields, String query, List<Filter> filters) {
        if (searchFields != null && !searchFields.isEmpty()) {
            for (String token : query.replaceAll("  ", " ").split(" ")) {
                String searchTerm = "%" + StringEscapeUtils.escapeSql(token) + "%";
                Filter[] orFilters = new Filter[searchFields.size()];
                int counter = 0;
                for (String searchField : searchFields) {
                    orFilters[counter] = Filter.like(searchField, searchTerm);
                    counter++;
                }
                filters.add(Filter.or(orFilters));
            }
        }
    }

    /**
     * Add a sort field to the search object.
     *
     * @param sortField - the sort field
     * @param search    - the search object
     * @return - the search object
     */
    public static Search addSortField(SortField sortField, Search search) {
        if (sortField != null) {
            search.addSort(sortField.getSort());
        } else {
            search.addSort(new Sort("dateCreated", true));
        }

        return search;
    }

    /**
     * Generate a search object for the users. The search object will search for the query in the username, first name, last name, email address and phone number.
     *
     * @param query     - the query to search for e.g. "John Doe"
     * @param sortField - the selected sort field
     * @return - the search object
     */
    public static Search generateSearchObjectForUsers(String query, SortField sortField) {
        Search search = generateSearchTerms(query, Arrays.asList("username", "firstName", "lastName", "emailAddress", "phoneNumber"));
        return addSortField(sortField, search);
    }

    /**
     * Generate a search object for the roles. The search object will search for the query in the name and description.
     *
     * @param query     - the query to search for e.g. "Admin"
     * @param sortField - the selected sort field
     * @return - the search object
     */
    public static Search generateSearchObjectForRoles(String query, SortField sortField) {
        Search search = generateSearchTerms(query, Arrays.asList("name", "description"));
        return addSortField(sortField, search);
    }

    /**
     * Generate a search object for the permissions. The search object will search for the query in the name and description.
     *
     * @param query     - the query to search for e.g. "Add User"
     * @param sortField - the selected sort field
     * @return - the search object
     */
    public static Search generateSearchObjectForPermissions(String query, SortField sortField) {
        Search search = generateSearchTerms(query, Arrays.asList("name", "description"));
        return addSortField(sortField, search);
    }


    /**
     * Add date filters to the search object.
     *
     * @param search        - the search object
     * @param selectedDates - the selected dates
     * @param startDate     - the start date
     * @param endDate       - the end date
     * @return - the search object
     */
    public static Search generateSearchForDates(Search search, List<String> selectedDates, Date startDate, Date endDate) {
        if (selectedDates != null) {
            if (!selectedDates.isEmpty()) {
                for (String name : selectedDates) {
                    search.addFilterGreaterOrEqual(name, startDate);
                    search.addFilterLessOrEqual(name, endDate);
                }
            } else {
                selectedDates = Arrays.asList("expectedDateOfDelivery", "actualDateOfDelivery", "dateCreated");
                for (String name : selectedDates) {
                    search.addFilterGreaterOrEqual(name, startDate);
                    search.addFilterLessOrEqual(name, endDate);
                }
            }
        }
        return search;
    }

}
