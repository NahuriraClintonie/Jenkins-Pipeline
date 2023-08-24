package org.pahappa.systems.core.services.impl;

import org.sers.webutils.server.core.utils.ApplicationSettingsUtils;
import org.sers.webutils.server.core.utils.DefaultApplicationSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.List;

@Service
public class CustomApplicationSettings extends DefaultApplicationSettings {
    public static final String BG_JOBS_CHECKSUM = "automated-invoicing";
    private static final NumberFormat DOUBLE_FORMATTER = new DecimalFormat("##,###,###,###.0000");
    private static final NumberFormat INTEGER_FORMATER = new DecimalFormat("##,###,###,###");
    private static final boolean EXECUTE_BACKGROUND_TASKS = true;
    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PostConstruct
    public void init() {
        ApplicationSettingsUtils.Util.createApplicationSettings(this);
        super.setExecuteBackgroundJobs(EXECUTE_BACKGROUND_TASKS);
        super.setBgJobsCheckSum(BG_JOBS_CHECKSUM);
        log.info("Application settings initialized");
    }

    @Override
    public String getOrganizationName() {
        return "Automated Invoicing";
    }

    @Override
    public int getMaximumRecordsPerPage() {
        return 10;
    }

    @Override
    public List<String> getErrorMessageRecipients() {
        return Arrays.asList(new String[]{"user@gmail.com"});
    }

    @Override
    public String getDefaultMailSenderAddress() {
        return "";
    }

    @Override
    public String getDefaultMailSenderPassword() {

        return "";
    }

    @Override
    public String getDefaultMailSenderSmtpHost() {
        return "";
    }

    @Override
    public String getDefaultMailSenderSmtpPort() {
        return "";
    }

    @Override
    public String getDefaultClientFeedbackMail() {
        return "xyz@gmail.com";
    }

    @Override
    public String getDefaultSuperUserEmail() {
        return "admin@appname.com";
    }

    @Override
    public String getDefaultSuperUserPhoneNumber() {
        return "070000000";
    }

    @Override
    public NumberFormat getDoubleNumberFormater() {
        return DOUBLE_FORMATTER;
    }

    @Override
    public NumberFormat getIntegerNumberFormater() {
        return INTEGER_FORMATER;
    }

    @Override
    public String getPasswordChangeToken() {
        return super.passwordChangeToken;
    }
}
