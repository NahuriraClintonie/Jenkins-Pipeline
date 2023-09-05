package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import com.googlecode.genericdao.search.Sort;
import org.pahappa.systems.core.models.salesAgentReminder.SalesAgentReminder;
import org.pahappa.systems.core.services.SalesAgentReminderService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.pahappa.systems.utils.Validate;
import org.sers.webutils.model.RecordStatus;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SalesAgentReminderServiceImpl extends GenericServiceImpl<SalesAgentReminder> implements SalesAgentReminderService {
    @Override
    public SalesAgentReminder saveInstance(SalesAgentReminder entityInstance) throws ValidationFailedException, OperationFailedException {
        Validate.notNull(entityInstance, "Missing entity instance");
        return save(entityInstance);
    }

    @Override
    public boolean isDeletable(SalesAgentReminder instance) throws OperationFailedException {
        return false;
    }

    public List<SalesAgentReminder> getAllRemindersByDate(){
        Search search = new Search();
        search.addFilterEqual("recordStatus", RecordStatus.ACTIVE);
        search.addSort(Sort.desc("sentDate"));
        return super.search(search);
    }

}
