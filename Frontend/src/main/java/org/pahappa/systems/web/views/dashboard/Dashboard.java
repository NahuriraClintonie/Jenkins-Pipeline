package org.pahappa.systems.web.views.dashboard;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.sers.webutils.client.controllers.WebAppExceptionHandler;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;
import org.pahappa.systems.core.constants.InvoiceStatus;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


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
    private int totalClients;
    private int totalClientSubscriptions;
    private int totalPendingInvoices;
    private int totalNationalSuppliers;
    private int totalAgroDealers;
    private int totalSystemUsers;
    private int totalUserAccounts;
    private int totalProductCategories;
    private int totalProductSubcategories;
    private int totalProducts;

    public void setPieModel(PieChartModel pieModel) {
        this.pieModel = pieModel;
    }

    @Getter
    private PieChartModel pieModel;

    @SuppressWarnings("unused")
    private String viewPath;

    private transient UserService userService;

    private ClientSubscriptionService clientSubscriptionService;

    private InvoiceService invoiceService;

    private ClientService clientService;

    @PostConstruct
    public void init() {
        this.userService = ApplicationContextProvider.getBean(UserService.class);
        this.clientSubscriptionService= ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        this.invoiceService= ApplicationContextProvider.getBean(InvoiceService.class);
        this.clientService= ApplicationContextProvider.getBean(ClientService.class);
        loggedinUser = SharedAppData.getLoggedInUser();
        countAll();
        createPieModel();
    }

    public void countAll(){
        this.totalClientSubscriptions= clientSubscriptionService.countInstances(new Search());
        this.totalClients=clientService.countInstances(new Search());
        this.totalClientSubscriptions = clientSubscriptionService.countInstances(new Search());
        this.totalUserAccounts= userService.countUsers(new Search());

    }

    private void createPieModel() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(300);
        values.add(50);
        values.add(100);
        dataSet.setData(values);

        List<String> bgColors = new ArrayList<>();
        bgColors.add("rgb(255, 99, 132)");
        bgColors.add("rgb(54, 162, 235)");
        bgColors.add("rgb(255, 205, 86)");
        dataSet.setBackgroundColor(bgColors);

        data.addChartDataSet(dataSet);
        List<String> labels = new ArrayList<>();
        labels.add("PAID");
        labels.add("PARTIALLY PAID");
        labels.add("UNPAID");
        data.setLabels(labels);

        pieModel.setData(data);
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public ClientSubscriptionService getClientSubscriptionService() {
        return clientSubscriptionService;
    }

    public void setClientSubscriptionService(ClientSubscriptionService clientSubscriptionService) {
        this.clientSubscriptionService = clientSubscriptionService;
    }

    public InvoiceService getInvoiceService() {
        return invoiceService;
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    public int getTotalClients() {
        return totalClients;
    }

    public void setTotalClients(int totalClients) {
        this.totalClients = totalClients;
    }

    public int getTotalClientSubscriptions() {
        return totalClientSubscriptions;
    }

    public void setTotalClientSubscriptions(int totalClientSubscriptions) {
        this.totalClientSubscriptions = totalClientSubscriptions;
    }

    public int getTotalPendingInvoices() {
        return totalPendingInvoices;
    }

    public void setTotalPendingInvoices(int totalPendingInvoices) {
        this.totalPendingInvoices = totalPendingInvoices;
    }
}
