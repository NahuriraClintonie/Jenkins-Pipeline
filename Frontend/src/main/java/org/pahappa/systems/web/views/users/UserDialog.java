package org.pahappa.systems.web.views.users;

import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.web.core.dialogs.DialogForm;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.security.Role;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.RoleService;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

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

    public UserDialog() {
        super(DIALOG_NAME, 800, 400);
        this.setUserService(ApplicationContextProvider.getBean(UserService.class));
        this.rolesList = ApplicationContextProvider.getApplicationContext().getBean(RoleService.class).getRoles();
        cleanRoleList();
        this.genders = Arrays.asList(Gender.values());
    }

    @Override
    public void persist() throws Exception {

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

}


