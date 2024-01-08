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

@ManagedBean(name="genderDialog")
@SessionScoped
@Setter
@Getter
public class GenderDialog extends DialogForm<Gender> {

    private GenderService genderService;


    public GenderDialog() {
        super(HyperLinks.ADD_GENDER_DIALOG, 400, 200);
    }

    @PostConstruct
    public void init(){
        genderService= ApplicationContextProvider.getBean(GenderService.class);
    }
    @Override
    public void persist() throws Exception {
        System.out.println(model);
        this.genderService.saveGender(model);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Gender();
    }
}
