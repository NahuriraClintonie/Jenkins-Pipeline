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
public class CreatingNewClientSubscriptionMigration {
    @Autowired
    BackgroundTaskService backgroundTaskService;

    @Autowired
    TaskCreatorService taskCreatorService;

    @Migration
    public void sendClientReminder(){
        System.out.println("\n\n\nCheck ing for expired subscription\n\n\n");
        BackgroundTask backgroundTask = new BackgroundTask();

        backgroundTask.setName("Task to checking for expired subscriptions");

        backgroundTask.setTaskType(TaskType.Interval);

        backgroundTask.setIntervalInMinutes(1440);

        backgroundTask.setPackageName("org.pahappa.systems.core.services");

        backgroundTask.setClassName("ApplicationEmailService");

        backgroundTask.setMethodName("generateInvoiceForNewClientSubscription");

        try{
            backgroundTaskService.saveOutsideSecurityContext(backgroundTask);

            taskCreatorService.ensureExecuted();
        } catch (Exception e){
            System.out.println("Exception in the appEmailMigration ClientReminder"+ e.getMessage());
        }

    }
}
