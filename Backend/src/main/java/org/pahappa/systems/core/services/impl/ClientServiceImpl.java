package org.pahappa.systems.core.services.impl;

import org.hibernate.criterion.Restrictions;
import org.pahappa.systems.core.models.appEmail.AppEmail;
import org.pahappa.systems.core.models.client.Client;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.services.*;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.hibernate.Session;
import org.hibernate.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class ClientServiceImpl extends GenericServiceImpl<Client> implements ClientService {

    @PersistenceContext
    private EntityManager entityManager;


    @Override
    public Client saveInstance(Client entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");
        return save(entityInstance);
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
}
