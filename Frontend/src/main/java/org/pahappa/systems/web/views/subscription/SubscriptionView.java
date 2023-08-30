package org.pahappa.systems.web.views.subscription;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.subscription.Subscription;
import org.pahappa.systems.core.services.SubscriptionService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
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

@Getter
@Setter
@ManagedBean(name="subscriptionView")
@ViewScoped
public class SubscriptionView extends PaginatedTableView<Subscription,SubscriptionView,SubscriptionView> {
    private SubscriptionService subscriptionService;
    private Search search;
    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Date createdFrom, createdTo;

    @PostConstruct
    public void init(){
        subscriptionService= ApplicationContextProvider.getBean(SubscriptionService.class);
        reloadFilterReset();
    }
    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> map) throws Exception {
        super.setDataModels(subscriptionService.getInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm,null,createdFrom , createdTo), offset, limit));
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }

    public List<Subscription> load(int i, int i1, Map<String, SortMeta> map, Map<String, FilterMeta> map1) {
        return getDataModels();
    }

    public void reloadFilterReset(){
        this.searchFields = Arrays.asList(
                new SearchField("Product Name", "productName"),
                new SearchField("Product Description", "productDescription"),
                new SearchField("Subscription Time Units", "subscriptionTimeUnits"),
                new SearchField("Subscription Duration", "subscriptionDuration")
        );
        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);

        super.setTotalRecords(subscriptionService.countInstances(this.search));

        try{
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
