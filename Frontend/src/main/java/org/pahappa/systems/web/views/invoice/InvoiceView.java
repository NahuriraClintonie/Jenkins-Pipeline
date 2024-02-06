package org.pahappa.systems.web.views.invoice;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;

import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.models.payment.PaymentAttachment;
import org.pahappa.systems.core.models.security.RoleConstants;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.ClientSubscriptionService;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.model.*;
import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.pie.PieChartDataSet;
import org.primefaces.model.charts.pie.PieChartModel;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.*;

@Getter
@Setter
@ManagedBean(name="invoiceView")
@ViewScoped
@ViewPath(path = HyperLinks.PAYMENT_VIEW)
public class InvoiceView extends PaginatedTableView<Invoice, InvoiceView, InvoiceView> {
    private InvoiceService invoiceService;
    private ClientSubscriptionService clientSubscriptionService;
    private PaymentService paymentService;

    private String searchTerm;
    private Search search;
    private Date createdFrom, createdTo;
    private List<SearchField> searchFields;
    private PieChartModel pieModel;
    private ClientService clientService;
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
    private Payment selectedPayment;

    private List<InvoiceStatus> invoiceStatuses;



    @PostConstruct
    public void init(){
        invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        invoiceStatuses = Arrays.asList(InvoiceStatus.values());
        clientService = ApplicationContextProvider.getBean(ClientService.class);
        clientSubscriptionService = ApplicationContextProvider.getBean(ClientSubscriptionService.class);
        paymentService = ApplicationContextProvider.getBean(PaymentService.class);
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
        super.setDataModels(invoiceService.getInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo), i, i1));
        this.setTotalRecords(invoiceService.countInstances(this.search));
    }

    public void particularClientInvoices(Client client){
        System.out.println("client is"+ client.getClientFirstName());
        this.particularClientInvoiceList = new ArrayList<>();
        particularClientInvoiceList = invoiceService.getInvoiceByClientSubscriptionId(clientSubscriptionService.getParticularClientSubscriptions(client));
        System.out.println("The size is " +particularClientInvoiceList.size());
    }

    public void particularInvoicePayments(Invoice invoice){
        System.out.println("invoice is"+ invoice.getInvoiceNumber());
        particularInvoicePaymentList = new ArrayList<>();
        particularInvoicePaymentList = paymentService.getAllPaymentsOfParticularInvoice(invoice.getInvoiceNumber());
        System.out.println("The size is " +particularInvoicePaymentList.size());
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

        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);
        this.setTotalRecords(invoiceService.countInstances(this.search));
        this.numberOfPaidInvoices= invoiceService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("invoiceStatus", InvoiceStatus.PAID));
        this.numberOfPartiallyPaidInvoices= invoiceService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("invoiceStatus", InvoiceStatus.PARTIALLY_PAID));
        this.numberOfUnPaidInvoices= invoiceService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("invoiceStatus", InvoiceStatus.UNPAID));

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


    public StreamedContent buildDownloadableFile(PaymentAttachment paymentAttachment){
        InputStream inputStream = new ByteArrayInputStream(paymentAttachment.getImageAttachment());
        return new DefaultStreamedContent(inputStream, paymentAttachment.getImageName());
    }

    public void setSelectedPayment(Payment selectedPayment) {
        this.selectedPayment = selectedPayment;
        buildDownloadableFile(this.selectedPayment.getPaymentAttachment());
    }

}