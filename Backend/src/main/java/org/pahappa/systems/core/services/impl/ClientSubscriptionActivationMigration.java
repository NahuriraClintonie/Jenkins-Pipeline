package org.pahappa.systems.core.services.impl;

import org.sers.webutils.model.bgtasks.BackgroundTask;
import org.sers.webutils.model.bgtasks.constants.TaskType;
import org.sers.webutils.model.migrations.Migration;
import org.sers.webutils.server.core.service.BackgroundTaskService;
import org.sers.webutils.server.core.service.TaskCreatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ClientSubscriptionActivationMigration {
    @Autowired
    BackgroundTaskService backgroundTaskService;

    @Autowired
    TaskCreatorService taskCreatorService;

    @Migration
    public void activateClientSubscription() {
        System.out.println("\n\n\nActivating client subscriptions\n\n\n");
        // Add your code here
        BackgroundTask backgroundTask = new BackgroundTask();
        backgroundTask.setName("Task to activate client subscriptions");
        backgroundTask.setTaskType(TaskType.Interval);
        backgroundTask.setIntervalInMinutes(1);
        backgroundTask.setPackageName("org.pahappa.systems.core.services");
        backgroundTask.setClassName("ClientSubscriptionService");
        backgroundTask.setMethodName("backgroundActivateClientSubscription");

        try{
            backgroundTaskService.saveOutsideSecurityContext(backgroundTask);
            taskCreatorService.ensureExecuted();
        } catch (Exception e){
            System.out.println("Exception in the client subscription Activation Migration"+ e.getMessage());
        }
    }

}
