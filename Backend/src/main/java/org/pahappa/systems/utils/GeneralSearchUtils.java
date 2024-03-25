/**
 * 
 */
package org.pahappa.systems.utils;

import com.googlecode.genericdao.search.Filter;
import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.pahappa.systems.core.models.security.RoleConstants;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.utils.DateUtils;
import org.sers.webutils.server.shared.SharedAppData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Contains the general search utility/helper functions used to query models
 * from the DB.
 * 
 * @author Mzee Sr.
 *
 */
@Getter
@Setter
public class GeneralSearchUtils {

	private static final int MINIMUM_CHARACTERS_FOR_SEARCH_TERM = 1;

	private static User loggedInUser;

	public static boolean searchTermSatisfiesQueryCriteria(String query) {
		if (StringUtils.isBlank(query))
			return false;
		return query.length() >= MINIMUM_CHARACTERS_FOR_SEARCH_TERM;
	}

	public static boolean generateSearchTerms(List<SearchField> searchFields, String query, List<Filter> filters) {
		if (searchFields != null && !searchFields.isEmpty()) {
			for (String token : query.replaceAll("  ", " ").split(" ")) {
				String searchTerm = "%" + StringEscapeUtils.escapeSql(token) + "%";
				Filter[] orFilters = new Filter[searchFields.size()];
				int counter = 0;
				for (SearchField searchField : searchFields) {
					orFilters[counter] = Filter.like(searchField.getPath(), searchTerm);
					counter++;
				}
				filters.add(Filter.or(orFilters));
			}
			return true;
		}
		return false;
	}
	
	public static boolean generateSearchTerms(String commaSeparatedsearchFields, String query, List<Filter> filters) {
		if (StringUtils.isBlank(commaSeparatedsearchFields))
			return false;

		List<String> searchFields = Arrays.asList(commaSeparatedsearchFields.split(","));
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
			return true;
		}
		return false;
	}
	
	public static Search composeUsersSearch(List<SearchField> searchFields, String query) {
		Search search = new Search();
		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		search.addSortAsc("username");
		search.addFilterNotIn("username", User.DEFAULT_ADMIN);

		return getSearch(searchFields, query, search);
	}

	public static Search composeUsersSearchForAll(List<SearchField> searchFields, String query, Gender gender, Date createdFrom, Date createdTo) {
		Search search = new Search();
		loggedInUser = SharedAppData.getLoggedInUser();

		search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);

		if(loggedInUser.hasRole(RoleConstants.ROLE_ACCOUNTANT )){
			search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
		}

		if(gender != null)
			search.addFilterEqual("gender", gender);
		if (createdFrom != null)
			search.addFilterGreaterOrEqual("dateCreated", DateUtils.getMinimumDate(createdFrom));
		if (createdTo != null)
			search.addFilterLessOrEqual("dateCreated", DateUtils.getMaximumDate(createdTo));

		return getSearch(searchFields, query, search);
	}

	private static Search getSearch(List<SearchField> searchFields, String query, Search search) {
		if (StringUtils.isNotBlank(query) && GeneralSearchUtils.searchTermSatisfiesQueryCriteria(query)) {
			ArrayList<Filter> filters = new ArrayList<Filter>();
			GeneralSearchUtils.generateSearchTerms(searchFields, query, filters);
			search.addFilterAnd(filters.toArray(new Filter[filters.size()]));
		}
		return search;
	}

}
