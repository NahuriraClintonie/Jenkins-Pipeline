package org.pahappa.systems.core.services.impl;


import com.googlecode.genericdao.search.Search;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientAccount.ClientAccount;
import org.pahappa.systems.core.services.ClientAccountService;
import org.pahappa.systems.core.services.ClientService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.sers.webutils.server.shared.CustomLogger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {

    @PersistenceContext
    private EntityManager entityManager;
    private ClientAccountService clientAccountService;

    @PostConstruct
    public void init(){
        this.clientAccountService = ApplicationContextProvider.getBean(ClientAccountService.class);
    }

    @Override
    public Client saveInstance(Client entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");
        Client savedClient = save(entityInstance);
        Search search = new Search();
        search.addFilterEqual("clientId", savedClient);
        ClientAccount clientAccount = clientAccountService.getParticularClientAccount(search);
        if (clientAccount == null){
            createClientAccount(savedClient);
        }
        return savedClient;
    }

    @Override
    public boolean isDeletable(Client instance) throws OperationFailedException {
        return true;
    }

    @Override
    public void updateAutoSendStatus(Client client) {
        // Merge the updated client entity into the persistence context
        System.out.println("Client auto status is: " + client.getAutoSendStatus());

        entityManager.merge(client);

        //changing the auto send status of the matching client's app email object
        Session session = entityManager.unwrap(Session.class);

        // Create a Criteria instance for AppEmail class
        Criteria criteria = session.createCriteria(AppEmail.class);

        // Add restrictions to filter by client's email and email status
        criteria.add(Restrictions.eq("receiverEmail", client.getClientEmail()));
        criteria.add(Restrictions.eq("emailStatus", false));

        // Get the list of matching AppEmail objects
        List<AppEmail> appEmails = criteria.list();

        // Update auto send status for each retrieved AppEmail
        for (AppEmail appEmail : appEmails) {
            System.out.println("App email's email is: "+appEmail.getReceiverEmail()+" Email status is: "+appEmail.getEmailStatus());
            appEmail.setAutoSendStatusAppEmail(client.getAutoSendStatus()); // Set auto-send status to true
            entityManager.merge(appEmail); // Save the updated AppEmail object
        }
    }

    @Override
    public void createClientAccount(Client client) throws ValidationFailedException, OperationFailedException {
        ClientAccount clientAccount = new ClientAccount();
        clientAccount.setClientId(client);
        clientAccountService.saveInstance(clientAccount);
    }

    @Override
    public List<Client> returnAllRequiredInstances(Search search) {
        search.addSortDesc("dateCreated");
        return super.search(search);
    }


}
