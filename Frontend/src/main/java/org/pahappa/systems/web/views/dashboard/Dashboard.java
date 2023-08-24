package org.pahappa.systems.web.views.dashboard;

import com.googlecode.genericdao.search.Search;
;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.client.controllers.WebAppExceptionHandler;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.shared.SharedAppData;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;


@ManagedBean(name = "dashboard")
@ViewScoped
@ViewPath(path = HyperLinks.DASHBOARD)
public class Dashboard extends WebAppExceptionHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    private User loggedinUser;

    Search search = new Search();
    private String searchTerm;
    private SortField selectedSortField;
    private int totalSubscribers;
    private int totalNationalSuppliers;
    private int totalAgroDealers;
    private int totalSystemUsers;
    private int totalUserAccounts;
    private int totalProductCategories;
    private int totalProductSubcategories;
    private int totalProducts;

    @SuppressWarnings("unused")
    private String viewPath;

    @PostConstruct
    public void init() {
        loggedinUser = SharedAppData.getLoggedInUser();
    }

    public User getLoggedinUser() {
        return loggedinUser;
    }

    public void setLoggedinUser(User loggedinUser) {
        this.loggedinUser = loggedinUser;
    }

    /**
     * @return the viewPath
     */
    public String getViewPath() {
        return Dashboard.class.getAnnotation(ViewPath.class).path();
    }

    /**
     * @param viewPath the viewPath to set
     */
    public void setViewPath(String viewPath) {
        this.viewPath = viewPath;
    }

    public int getTotalSubscribers() {
        return totalSubscribers;
    }

    public void setTotalSubscribers(int totalSubscribers) {
        this.totalSubscribers = totalSubscribers;
    }

    public int getTotalNationalSuppliers() {
        return totalNationalSuppliers;
    }

    public void setTotalNationalSuppliers(int totalNationalSuppliers) {
        this.totalNationalSuppliers = totalNationalSuppliers;
    }

    public int getTotalAgroDealers() {
        return totalAgroDealers;
    }

    public void setTotalAgroDealers(int totalAgroDealers) {
        this.totalAgroDealers = totalAgroDealers;
    }

    public int getTotalSystemUsers() {
        return totalSystemUsers;
    }

    public void setTotalSystemUsers(int totalSystemUsers) {
        this.totalSystemUsers = totalSystemUsers;
    }

    public int getTotalUserAccounts() {
        return totalUserAccounts;
    }

    public void setTotalUserAccounts(int totalUserAccounts) {
        this.totalUserAccounts = totalUserAccounts;
    }

    public int getTotalProductCategories() {
        return totalProductCategories;
    }

    public void setTotalProductCategories(int totalProductCategories) {
        this.totalProductCategories = totalProductCategories;
    }

    public int getTotalProductSubcategories() {
        return totalProductSubcategories;
    }

    public void setTotalProductSubcategories(int totalProductSubcategories) {
        this.totalProductSubcategories = totalProductSubcategories;
    }

    public int getTotalProducts() {
        return totalProducts;
    }

    public void setTotalProducts(int totalProducts) {
        this.totalProducts = totalProducts;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public SortField getSelectedSortField() {
        return selectedSortField;
    }

    public void setSelectedSortField(SortField selectedSortField) {
        this.selectedSortField = selectedSortField;
    }

}
