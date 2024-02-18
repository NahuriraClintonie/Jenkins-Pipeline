package org.pahappa.systems.core.services.impl;

import org.sers.webutils.model.bgtasks.BackgroundTask;
import org.sers.webutils.model.bgtasks.constants.TaskType;
import org.sers.webutils.model.migrations.Migration;
import org.sers.webutils.server.core.service.BackgroundTaskService;
import org.sers.webutils.server.core.service.TaskCreatorService;
import org.sers.webutils.server.core.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ApplicationReminderMigration {
    @Autowired
    BackgroundTaskService backgroundTaskService;

    @Autowired
    TaskCreatorService taskCreatorService;

    @Migration
    public void sendClientReminder(){
        System.out.println("\n\n\nSending clientReminder in migrations\n\n\n");
        BackgroundTask backgroundTask = new BackgroundTask();

        backgroundTask.setName("Task to send clientReminder");

        backgroundTask.setTaskType(TaskType.Once);

        backgroundTask.setOneTimeExecution(DateUtils.getDateAfterHrs(24));

        backgroundTask.setPackageName("org.pahappa.systems.core.services");

        backgroundTask.setClassName("ApplicationEmailService");

        backgroundTask.setMethodName("sendClientReminder");

        try{
            backgroundTaskService.saveOutsideSecurityContext(backgroundTask);

            taskCreatorService.ensureExecuted();
        } catch (Exception e){
            System.out.println("Exception in the appEmailMigration ClientReminder"+ e.getMessage());
        }

    }
}
