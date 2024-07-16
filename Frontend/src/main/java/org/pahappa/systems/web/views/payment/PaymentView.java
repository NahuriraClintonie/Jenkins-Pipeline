package org.pahappa.systems.web.views.payment;
//imports

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.core.dialogs.MessageComposer;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "paymentView")
@ViewScoped
@Getter
@Setter
public class PaymentView extends PaginatedTableView<Payment, PaymentView, PaymentView> {

    private PaymentService paymentService;
    private ClientService clientService;
    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Search search;
    private Date createdFrom, createdTo;

    @PostConstruct
    public void init(){
        paymentService= ApplicationContextProvider.getBean(PaymentService.class);
        reloadFilterReset();
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> map) throws Exception {
        search.addFilterEqual("status", PaymentStatus.PENDING_APPROVAL);
        super.setDataModels(paymentService.returnAllRequiredPayments(search));
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }


    public List<Payment> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        return getDataModels();
    }


    @Override
    public void reloadFilterReset(){
        System.out.println("Calling the reloadFilterReset");
        this.searchFields = Arrays.asList(new SearchField("FirstName", "invoice.clientSubscription.client.clientFirstName"),
                new SearchField("LastName", "invoice.clientSubscription.client.clientFirstName"),
                new SearchField("invoiceNumber", "invoice.invoiceNumber"));
        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);
        this.search = new Search().addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        super.setTotalRecords(paymentService.countInstances(this.search));
        try{
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
