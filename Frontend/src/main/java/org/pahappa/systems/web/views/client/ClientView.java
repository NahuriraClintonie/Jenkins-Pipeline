package org.pahappa.systems.web.views.client;

import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.utils.GeneralSearchUtils;
import org.pahappa.systems.web.core.dialogs.MessageComposer;
import org.pahappa.systems.web.views.HyperLinks;
import org.pahappa.systems.web.views.UiUtils;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.SortMeta;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.security.User;
import org.sers.webutils.model.utils.SearchField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean(name = "clientView")
@ViewScoped
@Getter
@Setter
@ViewPath(path = HyperLinks.CLIENT_VIEW)
public class ClientView extends PaginatedTableView<Client, ClientView, ClientView> {

    private ClientService clientService;

    private String searchTerm;
    private List<SearchField> searchFields, selectedSearchFields;
    private Search search;
    private Date createdFrom, createdTo;

    private List<Gender> genders;

    private Gender selectedGender;

    private int numberOfMales;

    private int numberOfFemales;

    private int numberOfTotalClientSubscriptions;

    private int numberOfUnknown;

    User currentUser;

    @PostConstruct
    public void init(){
        clientService= ApplicationContextProvider.getBean(ClientService.class);
        this.genders = Arrays.asList(Gender.values());
        currentUser = SharedAppData.getLoggedInUser();
        this.reloadFilterReset();
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> map) {
        if(currentUser.hasAdministrativePrivileges()){
            System.out.println("Has administrative privileges");
            super.setDataModels(clientService.returnAllRequiredInstances(search));
        }else{
            System.out.println("Doesnt have administrative privileges");
            search.addFilterEqual("attachedTo", currentUser);
            super.setDataModels(clientService.returnAllRequiredInstances(search));
        }

        super.setTotalRecords(clientService.countInstances(this.search));
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        return null;
    }

    @Override
    public String getFileName() {
        return null;
    }


    public List<Client> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
        return getDataModels();
    }

    public void deleteClient(Client client) {
        try {
            clientService.deleteInstance(client);
            super.reloadFilterReset();
            UiUtils.showMessageBox("Action Successful", "Client deleted successfully");
        } catch (Exception e) {
            UiUtils.showMessageBox("Action Failed", "Failed to delete client");
            throw new RuntimeException(e);
        }
    }


    @Override
    public void reloadFilterReset(){
        this.searchFields = Arrays.asList(new SearchField("FirstName", "clientFirstName"), new SearchField("LastName", "clientLastName"),new SearchField("Email", "clientEmail"), new SearchField("Phone", "clientContact"));
        if (!currentUser.hasAdministrativePrivileges()) {
            this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("attachedTo", currentUser);
            this.numberOfFemales = clientService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("clientGender",Gender.FEMALE).addFilterEqual("attachedTo", currentUser));
            this.numberOfMales = clientService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("clientGender",Gender.MALE).addFilterEqual("attachedTo", currentUser));
            this.numberOfUnknown = clientService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("clientGender",Gender.UNKNOWN).addFilterEqual("attachedTo", currentUser));
        } else {
            this.search = GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo);
            this.numberOfFemales = clientService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("clientGender",Gender.FEMALE));
            this.numberOfMales = clientService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("clientGender",Gender.MALE));
            this.numberOfUnknown = clientService.countInstances(GeneralSearchUtils.composeUsersSearchForAll(searchFields, searchTerm, null, createdFrom, createdTo).addFilterEqual("clientGender",Gender.UNKNOWN));
        }

        super.setTotalRecords(clientService.countInstances(this.search));
        try{
            super.reloadFilterReset();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
