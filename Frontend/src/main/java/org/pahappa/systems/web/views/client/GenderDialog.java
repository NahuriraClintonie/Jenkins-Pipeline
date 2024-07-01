package org.pahappa.systems.web.views.client;

import lombok.Getter;
import lombok.Setter;

import org.pahappa.systems.core.models.gender.Gender;
import org.pahappa.systems.core.services.GenderService;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean(name="genderDialog")
@RequestScoped
@Setter
@Getter
public class GenderDialog extends DialogForm<Gender> {

    private GenderService genderService;

    private Gender gender;

    public GenderDialog() {
        super(HyperLinks.ADD_GENDER_DIALOG, 400, 200);
    }

    @PostConstruct
    public void init(){
        genderService= ApplicationContextProvider.getBean(GenderService.class);
        gender = new Gender();
    }

    @Override
    public void persist() throws Exception {
        System.out.println("Added gendername"+ this.gender.getGenderName());
//        System.out.println(super.model);
        this.genderService.saveGender(this.gender);
    }

    public void resetModal(){
        super.resetModal();
        super.model = new Gender();
    }
}
