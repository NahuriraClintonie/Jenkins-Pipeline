package org.pahappa.systems.web.views.payment;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@ManagedBean(name="capturePaymentsView")
@ViewScoped
public class CapturePaymentsView extends PaginatedTableView<Invoice, CapturePaymentsView, CapturePaymentsView> {

    private InvoiceService invoiceService;

    private String searchTerm;
    private Search search;
    private Date createdFrom, createdTo;
    private List<SearchField> searchFields;

    @PostConstruct
    public void init(){
        invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
        try {
            reloadFilterReset();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void reloadFromDB(int i, int i1, Map<String, Object> map) throws Exception {
//        System.out.println("List");
        super.setDataModels(this.invoiceService.getInvoiceByStatus());
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

        try {
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

//    public void approve(Invoice model){
//        System.out.println(model);
//        this.invoiceService.changeStatusToUnpaid(model);
//
//    }
}
