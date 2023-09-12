package org.pahappa.systems.core.gender;

import org.pahappa.systems.core.models.gender.Gender;
import org.pahappa.systems.core.services.GenderService;
import org.pahappa.systems.core.services.impl.GenderServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class initGender {
    private GenderService genderService;
    private Gender gender = new Gender();
    @PostConstruct
    public void init() throws ValidationFailedException, OperationFailedException {
        genderService= ApplicationContextProvider.getBean(GenderService.class);

    }

    public void saveGenders() throws ValidationFailedException, OperationFailedException {
        gender.setGenderName("MALE");
        genderService.saveGender(gender);
        Gender gender2= new Gender();
        gender2.setGenderName("FEMALE");
        genderService.saveGender(gender2);

    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

}
