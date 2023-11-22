package org.pahappa.systems.core.services.impl;

import org.sers.webutils.model.bgtasks.BackgroundTask;
import org.sers.webutils.model.bgtasks.constants.TaskType;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.model.migrations.Migration;
import org.sers.webutils.server.core.service.BackgroundTaskService;
import org.sers.webutils.server.core.service.TaskCreatorService;
import org.sers.webutils.server.core.service.impl.TaskCreatiorServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationEmailMigration {
    @Autowired
    BackgroundTaskService backgroundTaskService;

    @Autowired
    TaskCreatorService taskCreatorService;

    @Migration
    public void sendMailsv2() {
        System.out.println("\n\n\nSending mails in migrations\n\n\n");
        BackgroundTask backgroundTask = new BackgroundTask();

        backgroundTask.setName("Task to send app mails");
        backgroundTask.setTaskType(TaskType.Interval);
        backgroundTask.setIntervalInMinutes(1);
        backgroundTask.setPackageName("org.pahappa.systems.core.services");
        backgroundTask.setClassName("ApplicationEmailService");
        backgroundTask.setMethodName("sendSavedInvoices");

        try{
            backgroundTaskService.saveOutsideSecurityContext(backgroundTask);
            taskCreatorService.ensureExecuted();
        } catch (Exception e){
            System.out.println("Exception in the appEmailMigration"+ e.getMessage());
        }

    }


}
