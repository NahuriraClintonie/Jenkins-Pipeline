package org.pahappa.systems.web.views.salesAgentReminder;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.appEmail.EmailsToCc;
import org.pahappa.systems.core.models.salesAgentReminder.SalesAgentReminder;
import org.pahappa.systems.core.services.ApplicationEmailService;
import org.pahappa.systems.core.services.EmailsToCcService;
import org.pahappa.systems.core.services.SalesAgentReminderService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.model.RecordStatus;
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
@ViewScoped
@ManagedBean(name="salesAgentReminderView")

public class SalesAgentReminderView extends PaginatedTableView<AppEmail,SalesAgentReminderView,SalesAgentReminderView> {
    private ApplicationEmailService applicationEmailService;
    private EmailsToCcService emailsToCcService;
    private Search search;
    private String searchTerm;
    private List<SearchField> searchFields;
    private Date createdFrom, createdTo;
    private User currentUser;
    private List<EmailsToCc> emailsToCcList = new ArrayList<>();

    @PostConstruct
    public void init(){
        currentUser = SharedAppData.getLoggedInUser();
        if (!currentUser.hasAdministrativePrivileges()){
            search.addFilterEqual("createdBy", currentUser);
        }
        applicationEmailService = ApplicationContextProvider.getBean(ApplicationEmailService.class);
        emailsToCcService = ApplicationContextProvider.getBean(EmailsToCcService.class);
        reloadFilterReset();
    }
    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> map) throws Exception {
        super.setDataModels(applicationEmailService.getParticularSalesAgentEmails(this.search));
        CustomLogger.log("The size is"+ super.getTotalRecords());
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }

    public List<AppEmail> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {

       return  getDataModels();

    }

    public void reloadFilterReset(){
        this.searchFields = Arrays.asList(
                new SearchField("Email Subject", "emailSubject"),
                new SearchField("Receiver Email", "receiverEmail"),
                new SearchField("Invoice Number", "invoiceObject.invoiceNumber"),
                new SearchField("clientFirstName", "invoiceObject.clientSubscription.client.clientFirstName"),
                new SearchField("clientLastName", "invoiceObject.clientSubscription.client.clientLastName")

        );
        this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);
        this.search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        super.setTotalRecords(applicationEmailService.getParticularSalesAgentEmails(this.search).size());

        try{
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public List<EmailsToCc> ccEmails(AppEmail appEmail){
        System.out.println("We are filling the list");
        emailsToCcList = emailsToCcService.getByClientSubscritpion(appEmail.getInvoiceObject().getClientSubscription().getId());
        System.out.println("List size "+ emailsToCcList.size());
        return emailsToCcList;
    }

    public List<EmailsToCc> getEmailsToCcList() {
        System.out.println("the size is" +emailsToCcList.size());
        return emailsToCcList;
    }
}
