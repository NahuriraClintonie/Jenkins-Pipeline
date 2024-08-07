package org.pahappa.systems.web.views.invoice;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;

import org.jetbrains.annotations.NotNull;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.views.HyperLinks;
import org.pahappa.systems.web.views.UiUtils;
import org.primefaces.model.*;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.io.IOException;
import java.util.*;

@Getter
@Setter
@ManagedBean(name="invoiceView")
@SessionScoped
public class InvoiceView extends PaginatedTableView<Invoice, InvoiceView, InvoiceView> {
    private InvoiceService invoiceService;
    private ClientSubscriptionService clientSubscriptionService;
    private PaymentService paymentService;
    private String searchTerm;
    private Search search;
    private Date dateFrom, dateTo;
    private List<SearchField> searchFields;
    private PieChartModel pieModel;
    private ClientService clientService;

    public void setSelectedClient(Client selectedClient) {
        this.selectedClient = selectedClient;
        System.out.println("Client is "+ selectedClient.getClientFirstName()+" in particularClientInvoices");
    }

    private Client selectedClient;
    private List<Invoice> filteredInvoices;

    private int numberOfPaidInvoices;
    private int numberOfUnPaidInvoices;
    private int numberOfPartiallyPaidInvoices;
    private int numberOfInvoices;

    private List<Invoice> salesAgentInvoiceList;
    private List<Invoice> accountantInvoiceList;
    private List<Invoice> particularClientInvoiceList;
    private List<Payment> particularInvoicePaymentList;

    private List<InvoiceStatus> invoiceStatuses;

    private boolean autoSendEnabled;

    @PostConstruct
    public void init(){
        invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        invoiceStatuses = Arrays.asList(InvoiceStatus.values());
        clientService = ApplicationContextProvider.getBean(ClientService.class);
        clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        paymentService = ApplicationContextProvider.getBean(PaymentService.class);
        updateAutoSendEnabled();
        createPieModel();
        try {
            reloadFilterReset();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        createPieModel();
    }
    @Override
    public void reloadFromDB(int i, int i1, Map<String, Object> map) throws Exception {
        super.setDataModels(invoiceService.getInstances(GeneralSearchUtils.composeUsersSearchForInvoicesBetweenDate(searchFields, searchTerm, null, dateFrom, dateTo).addFilterEqual("clientSubscription.client", selectedClient ).addSortDesc("dateCreated"), i, i1));
        this.setTotalRecords(invoiceService.countInstances(this.search));
    }

    public void particularClientInvoices(Client client) throws IOException {
        this.selectedClient = client;
        System.out.println("client is"+ selectedClient.getClientFirstName());
        this.particularClientInvoiceList = new ArrayList<>();
        particularClientInvoiceList = invoiceService.getInvoiceByClientSubscriptionId(clientSubscriptionService.getParticularClientSubscriptions(client));
        System.out.println("The size is " +particularClientInvoiceList.size());
        redirectTo(HyperLinks.PARTICULAR_CLIENT_INVOICE_VIEW);

    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }


    public List<Invoice> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        return getDataModels();
    }

    @Override
    public void reloadFilterReset() throws Exception {

        this.searchFields = Arrays.asList(new SearchField("clientName", "clientSubscription.client.clientFirstName"),
                new SearchField("ClientLastName", "clientSubscription.client.clientLastName"));

        this.search = GeneralSearchUtils.composeUsersSearchForInvoicesBetweenDate(searchFields, searchTerm, null, dateFrom, dateTo);
        this.setTotalRecords(invoiceService.countInstances(this.search));
        this.numberOfPaidInvoices= invoiceService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, dateFrom, dateTo).addFilterEqual("invoiceStatus", InvoiceStatus.PAID));
        this.numberOfPartiallyPaidInvoices= invoiceService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, dateFrom, dateTo).addFilterEqual("invoiceStatus", InvoiceStatus.PARTIALLY_PAID));
        this.numberOfUnPaidInvoices= invoiceService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, dateFrom, dateTo).addFilterEqual("invoiceStatus", InvoiceStatus.UNPAID));

        try {
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void createPieModel() {
        pieModel = new PieChartModel();
        ChartData data = new ChartData();

        PieChartDataSet dataSet = new PieChartDataSet();
        List<Number> values = new ArrayList<>();
        values.add(this.numberOfPaidInvoices);
        values.add(this.numberOfPartiallyPaidInvoices);
        values.add(this.numberOfUnPaidInvoices);
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

    public void redirectToInvoiceView() throws IOException {
        redirectTo(HyperLinks.INVOICE_VIEW);
    }

    public void toggleAutoSend(@NotNull Client client) {
        // Toggle the state of auto sending for the specific client
        client.setAutoSendStatus(!client.getAutoSendStatus());
        autoSendEnabled = client.getAutoSendStatus();

        // Update the client entity in the database
        clientService.updateAutoSendStatus(client);

        // Display a message to inform the user about the change
        UiUtils.showMessageBox("Auto Send Status",
                client.getAutoSendStatus() ? "Auto sending activated." : "Auto sending deactivated.");
    }

    public void updateAutoSendEnabled(){
        autoSendEnabled = true;
    }

}