package org.pahappa.systems.web.views.client;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.gender.Gender;
import org.pahappa.systems.core.models.security.RoleConstants;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.GenderService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.SharedAppData;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.List;

@ManagedBean(name="clientDialog")
@SessionScoped
@Setter
@Getter
public class ClientDialog extends DialogForm<Client> {

    private ClientService clientService;

    private UserService userService;

    private GenderService genderService;

    private List<User> userList;

    private List<Gender> genderList;

    User currentUser;



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
        this.clientService.saveInstance(super.model);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Client();
    }
}