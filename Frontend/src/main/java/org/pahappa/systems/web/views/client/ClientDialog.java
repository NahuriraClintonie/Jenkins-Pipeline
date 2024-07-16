package org.pahappa.systems.web.views.client;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.gender.Gender;
import org.pahappa.systems.core.models.security.RoleConstants;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.GenderService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.core.dialogs.MessageComposer;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.PrimeFaces;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.sers.webutils.server.shared.SharedAppData;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import java.util.List;

@ManagedBean(name="clientDialog")
@SessionScoped
//@ViewScoped
@Setter
@Getter
public class ClientDialog extends DialogForm<Client> {

    private ClientService clientService;

    private UserService userService;

    private GenderService genderService;

    private List<User> userList;

    private List<Gender> genderList;

    User currentUser;
    private boolean saveSuccessful;



    public ClientDialog() {
        super(HyperLinks.CLIENT_DIALOG, 700, 410);
    }

    @PostConstruct
    public void init(){
        clientService= ApplicationContextProvider.getBean(ClientService.class);
        genderService= ApplicationContextProvider.getBean(GenderService.class);
        userService= ApplicationContextProvider.getBean(UserService.class);
        this.userList = userService.getUsersByRoleName(RoleConstants.ROLE_SALES_AGENT);
        this.genderList = genderService.getAllInstances();
        currentUser = SharedAppData.getLoggedInUser();
    }
    @Override
    public void persist() throws Exception {
        if (super.model.getAttachedTo() == null) {
            super.model.setAttachedTo(currentUser);
        }

        try {
            this.clientService.saveInstance(super.model);
            saveSuccessful = true; // Set flag for successful save
            super.hide();
        } catch (Exception e) {
            saveSuccessful = false; // Set flag for unsuccessful save
        }
    }

    public void onDialogReturn() {
        if(saveSuccessful){
            MessageComposer.compose("Success", "Client Added successfully");
        }
        else {
            MessageComposer.compose("Error", "Failed to Add client");
        }
    }


    public void resetModal(){
        super.resetModal();
        super.model = new Client();
    }

    public boolean isSaveSuccessful() {
        return saveSuccessful;
    }

    public void setSaveSuccessful(boolean saveSuccessful) {
        this.saveSuccessful = saveSuccessful;
    }
}