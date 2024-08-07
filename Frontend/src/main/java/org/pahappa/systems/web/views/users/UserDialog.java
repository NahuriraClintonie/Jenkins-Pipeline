package org.pahappa.systems.web.views.users;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.utils.Validate;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.pahappa.systems.web.core.dialogs.MessageComposer;
import org.pahappa.systems.web.views.HyperLinks;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.security.Role;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.RoleService;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@Setter
@ManagedBean(name = "dialogUsers")
@SessionScoped
public class UserDialog extends DialogForm<User> {

    private static final long serialVersionUID = 1L;
    private static final String DIALOG_NAME = "dialogUsers";
    private UserService userService;
    private Set<Role> selectedRolesList = new HashSet<>();
    private List<Role> rolesList = new ArrayList<Role>();
    private boolean disableOtherFields;
    private List<Gender> genders = new ArrayList<>();
    private Boolean savedSuccessfully = null;

    public UserDialog() {
        super(HyperLinks.DIALOG_USERS, 800, 400);
    }

    @PostConstruct
    public void init(){
        this.userService = ApplicationContextProvider.getBean(UserService.class);
        this.rolesList = ApplicationContextProvider.getApplicationContext().getBean(RoleService.class).getRoles();
        cleanRoleList();
        this.genders = Arrays.asList(Gender.values());
        resetModal();
    }

    @Override
    public void persist() throws Exception {
        try{
            Validate.notNull(super.model, "Some fields are null");
            this.model.setRoles(selectedRolesList);
            this.userService.saveUser(super.model);
            savedSuccessfully = true;
            hide();
        }catch (Exception e){
            savedSuccessfully = false;
        }
    }

    public void resetModal() {
        super.resetModal();
        super.model = new User();
        this.selectedRolesList = new HashSet<>();
    }

    public void setFormProperties() {
        super.setFormProperties();
        if (super.model != null) {
            this.selectedRolesList = new HashSet<>(userService.getRoles(super.model, 0, 0));
        }
    }

    private void cleanRoleList() {
        if (this.rolesList != null && !this.rolesList.isEmpty()) {
            List<Role> dupRoles = new CopyOnWriteArrayList<>(this.rolesList);
            for (Role role : dupRoles) {
                if (role.getName().equalsIgnoreCase("Normal User")) this.rolesList.remove(role);
            }
        }
    }

    public void onDialogReturn(){
        if (savedSuccessfully != null){
            if (savedSuccessfully)
                MessageComposer.compose("Success", "User Saved successfully");
            else
                MessageComposer.compose("Success", "Failed to Save User");
        }
    }

}


