package org.pahappa.systems.web.views.users;


import com.googlecode.genericdao.search.Search;
import lombok.Getter;
import org.pahappa.systems.utils.SearchUtils;
import org.pahappa.systems.web.core.dialogs.MessageComposer;
import org.pahappa.systems.web.views.HyperLinks;
import org.primefaces.PrimeFaces;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.Gender;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.security.Role;
import org.sers.webutils.model.security.User;
import org.sers.webutils.server.core.service.RoleService;
import org.sers.webutils.server.core.service.UserService;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.core.utils.TelephoneNumberUtils;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
@ManagedBean(name = "usersView")
@SessionScoped
@ViewPath(path = HyperLinks.USERS_VIEW)
public class UserView extends PaginatedTableView<User, UserView, UserView> {
    private static final long serialVersionUID = 1L;
    private transient UserService userService;
    private String searchTerm;
    private List<Gender> genders = new ArrayList<>();
    private User selectedUser;
    private double customPropOneNumber;
    private List<Role> rolesList = new ArrayList<>();
    private Set<Role> selectedRolesList = new HashSet<>();
    private Search search = new Search();

    @PostConstruct
    @Override
    public void init() {
        this.userService = ApplicationContextProvider.getApplicationContext().getBean(UserService.class);
        this.rolesList = ApplicationContextProvider.getApplicationContext().getBean(RoleService.class).getRoles();
        super.setMaximumresultsPerpage(10);
        this.genders = Arrays.asList(Gender.values());
        this.customPropOneNumber = 0;
    }



    //    Remove the "Normal User" role from the role list to display on the user form
    private void cleanRoleList() {
        if (this.rolesList != null && !this.rolesList.isEmpty()) {
            List<Role> dupRoles = new CopyOnWriteArrayList<>(this.rolesList);
            for (Role role : dupRoles) {
                if (role.getName().equalsIgnoreCase("Normal User")) this.rolesList.remove(role);
            }
        }
    }

    @Override
    public void reloadFromDB(int offset, int limit, Map<String, Object> filters) throws Exception {
        search = SearchUtils.generateSearchObjectForUsers(this.searchTerm, null);
        super.setDataModels(userService.getUsers(search, offset, limit));
        super.setTotalRecords(userService.countUsers(search));
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getFileName() {
        // TODO Auto-generated method stub
        return null;
    }

    public void saveSelectedUser() {
        this.selectedUser.setRoles(this.selectedRolesList);
        try {
            this.selectedUser.setPhoneNumber(TelephoneNumberUtils.getValidTelephoneNumber(this.selectedUser.getPhoneNumber()));

            //save the user
            User savedUser = userService.saveUser(this.selectedUser);


            PrimeFaces.current().executeScript("PF('selected_user_dialog').hide()");
            PrimeFaces.current().ajax().update("usersView:usersTable");
            MessageComposer.compose("Action successful", "User account created. Log-in details sent to user's email");

        } catch (ValidationFailedException ex) {
            MessageComposer.error("Action failed", ex.getLocalizedMessage());
        }
    }


    public void deleteSelectedUser(User user) throws OperationFailedException {
        try {
            user.setUsername(user.getUsername() + "_deleted");
            userService.deleteUser(user);
            MessageComposer.compose("Action successful", "User account deleted");
        } catch (Exception e) {
            MessageComposer.error("Action failed", e.getLocalizedMessage());
        }
    }

    public void loadSelectedUser(User user) {
        if (user != null) {
            this.selectedUser = user;
        } else {
            this.selectedUser = new User();
            this.customPropOneNumber = 0;
        }
    }


    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }


    public void setSelectedUser(User selectedUser) {
        this.selectedUser = selectedUser;
    }

    public void setGenders(List<Gender> genders) {
        this.genders = genders;
    }

    public void setCustomPropOneNumber(double customPropOneNumber) {
        this.customPropOneNumber = customPropOneNumber;
    }

}