package org.pahappa.systems.web.views.client;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.gender.Gender;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.GenderService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

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

    private GenderService genderService;

    private List<Gender> genderList;



    public ClientDialog() {
        super(HyperLinks.CLIENT_DIALOG, 700, 410);
    }

    @PostConstruct
    public void init(){
        clientService= ApplicationContextProvider.getBean(ClientService.class);
        genderService= ApplicationContextProvider.getBean(GenderService.class);
        this.genderList = genderService.getAllInstances();
    }
    @Override
    public void persist() throws Exception {
        this.clientService.saveInstance(super.model);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Client();
    }
}
