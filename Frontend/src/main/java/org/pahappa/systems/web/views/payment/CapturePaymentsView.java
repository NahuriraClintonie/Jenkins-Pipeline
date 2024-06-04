package org.pahappa.systems.web.views.payment;
//imports
import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.sers.webutils.server.shared.SharedAppData;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.util.*;

@Getter
@Setter
@ManagedBean(name="capturePaymentsView")
@ViewScoped
public class CapturePaymentsView extends PaginatedTableView<Invoice, CapturePaymentsView, CapturePaymentsView> {

    private InvoiceService invoiceService;
    private int totalRecords;
    private User currentUser;
    private String searchTerm;
    private Search search;
    private String requestedInvoiceNumber;
    private Date createdFrom, createdTo;
    private List<SearchField> searchFields;
    private List<Invoice> searchResults= new ArrayList<>();

    @PostConstruct
    public void init(){
        invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        currentUser = SharedAppData.getLoggedInUser();
        try {
            reloadFilterReset();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> map) throws Exception {
        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);

        if(!currentUser.hasAdministrativePrivileges()){
            this.search.addFilterEqual("createdBy", currentUser);
        }

        this.search.addFilterNotEqual("invoiceStatus", InvoiceStatus.PAID);

        super.setDataModels(this.invoiceService.getInvoiceByStatus(this.search));
        this.totalRecords = this.invoiceService.countInstances(this.search);
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
                new SearchField("ClientLastName", "clientSubscription.client.clientLastName")
        );
        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);
        this.search.addFilterEqual("invoiceNumber",requestedInvoiceNumber);
        System.out.println("the invoice number is "+requestedInvoiceNumber);
        totalRecords = this.invoiceService.countInstances(this.search);
        try {
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
