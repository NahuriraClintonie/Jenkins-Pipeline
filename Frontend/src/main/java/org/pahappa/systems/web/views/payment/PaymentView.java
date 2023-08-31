package org.pahappa.systems.web.views.payment;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.views.UiUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "paymentView")
@Getter
@Setter
public class PaymentView extends PaginatedTableView<Payment, PaymentView, PaymentView> {

    private PaymentService paymentService;
    private ClientService clientService;

    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Search search;
    private Date createdFrom, createdTo;

    private List<Gender> genders;

    private Gender selectedGender;

    @PostConstruct
    public void init(){
        paymentService= ApplicationContextProvider.getBean(PaymentService.class);
        this.genders = Arrays.asList(Gender.values());
        reloadFilterReset();
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> map) throws Exception {

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
        this.searchFields = Arrays.asList(new SearchField("FirstName", "firstName"), new SearchField("LastName", "lastName"),new SearchField("Email", "clientEmail"), new SearchField("Phone", "clientContact"));
        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);

        super.setTotalRecords(clientService.countInstances(this.search));

        try{
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
