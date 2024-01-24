package org.pahappa.systems.web.views.salesAgentReminder;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.product.Product;
import org.pahappa.systems.core.models.salesAgentReminder.SalesAgentReminder;
import org.pahappa.systems.core.services.SalesAgentReminderService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;

@SessionScoped
@ManagedBean(name="salesAgentReminderView")

public class SalesAgentReminderView extends PaginatedTableView<SalesAgentReminder,SalesAgentReminderView,SalesAgentReminderView> {
   private SalesAgentReminderService salesAgentReminderService;

    private Search search;

    private User currentUser;
    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Date createdFrom, createdTo;
    @PostConstruct
    public void init(){
        salesAgentReminderService = ApplicationContextProvider.getBean(SalesAgentReminderService.class);
        currentUser = SharedAppData.getLoggedInUser();
        super.setMaximumresultsPerpage(1000);
        reloadFilterReset();
    }
    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> map) throws Exception {
        if(!currentUser.hasAdministrativePrivileges()){
            super.setDataModels(salesAgentReminderService.getInstances(this.search.addFilterEqual("attachedTo", currentUser),offset, limit));
        }else {
            super.setDataModels(salesAgentReminderService.getInstances(this.search,offset, limit));
        }
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }

    public List<SalesAgentReminder> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

       return  getDataModels();

    }

    public void reloadFilterReset(){
        this.searchFields = Arrays.asList(
                new SearchField("Invoice Due Date", "invoiceDueDate")

        );
        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);

        super.setTotalRecords(salesAgentReminderService.countInstances(this.search));

        try{
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
