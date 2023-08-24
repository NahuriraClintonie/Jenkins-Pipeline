package org.pahappa.systems.web.controllers;

import lombok.Getter;
import org.sers.webutils.server.core.utils.DateUtils;
import org.sers.webutils.server.shared.CustomLogger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
@ManagedBean(name = "genericController", eager = true)
@ApplicationScoped
public class GenericController implements Serializable {

    /**
     *
     */
    private final String APPLICATION_NAME = "Automated Invoicing";
    private static final long serialVersionUID = 1L;
    private static final String systemName = "APP NAME";
    private static final String searchPlaceHolder = "Enter text to search";
    private static final String dataEmptyMessage = "No records found";
    private static final String landingPageLabel = "";
    private int currentYear;
    private Date currentDate;
    private String currentDateAsString;
    private String contactNumbers;
    private String contactEmail;

    @PostConstruct
    public void init() {
        this.currentDate = new Date();
        this.currentYear = DateUtils.currentYear();
        this.currentDateAsString = new SimpleDateFormat("EEE, dd MMM, yyyy HH:mm:ss").format(currentDate);
        CustomLogger.printConsoleLogs(true);
    }

}
