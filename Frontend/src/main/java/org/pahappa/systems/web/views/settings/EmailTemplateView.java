package org.pahappa.systems.web.views.settings;

import org.pahappa.systems.core.models.emailTemplate.EmailTemplate;
import org.pahappa.systems.core.services.EmailTemplateService;
import org.pahappa.systems.web.views.HyperLinks;
import org.pahappa.systems.web.views.UiUtils;
import org.sers.webutils.client.views.presenters.PaginatedTableView;
import org.sers.webutils.client.views.presenters.ViewPath;
import org.sers.webutils.model.utils.SortField;
import org.sers.webutils.server.core.service.excel.reports.ExcelReport;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.Date;
import java.util.List;
import java.util.Map;

@ManagedBean(name="emailTemplateView")
@ViewScoped
@ViewPath(path = HyperLinks.EMAIL_TEMPLATE_VIEW)
public class EmailTemplateView extends PaginatedTableView<EmailTemplate, EmailTemplateView, EmailTemplateView> {

    private static final long serialVersionUID = 1L;

    private List<EmailTemplate> filteredTemplate;
    private EmailTemplateService emailTemplateService;

    private String searchTerm;
    private Date createdFrom;
    private Date createdTo;
    private SortField sortField = new SortField("dateCreated", "dateCreated", true);

    @PostConstruct
    public void init() {
        this.emailTemplateService = ApplicationContextProvider.getApplicationContext().getBean(EmailTemplateService.class);
        super.setMaximumresultsPerpage(10);
    }
    @Override
    public void reloadFromDB(int i, int i1, Map<String, Object> map) throws Exception {
        super.setDataModels(emailTemplateService.getTemplates());
    }

    @Override
    public List<ExcelReport> getExcelReportModels() {
        return null;
    }

    @Override
    public void reloadFilterReset() throws Exception {
        super.setTotalRecords(emailTemplateService.countTemplates());
        try {
            super.reloadFilterReset();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getFileName() {
        return null;
    }

    public void setFilteredTemplate(List<EmailTemplate> filteredTemplate) {
        this.filteredTemplate = filteredTemplate;
    }

    public void setSearchTerm(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public void setCreatedFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
    }

    public void setCreatedTo(Date createdTo) {
        this.createdTo = createdTo;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public Date getCreatedFrom() {
        return createdFrom;
    }

    public Date getCreatedTo() {
        return createdTo;
    }

    public SortField getSortField() {
        return sortField;
    }

    public void setSortField(SortField sortField) {
        this.sortField = sortField;
    }

    public List<EmailTemplate> getFilteredTemplate() {
        return filteredTemplate;
    }
    public void deleteEmailTemplate(EmailTemplate emailTemplate) {
        try {
            emailTemplateService.deleteInstance(emailTemplate);
            super.reloadFilterReset();
            UiUtils.showMessageBox("Action Successful", "Email template deleted successfully");
        } catch (Exception e) {
            UiUtils.showMessageBox("Action Failed", "Failed to delete template");
            throw new RuntimeException(e);
        }
    }

    public String stripHtmlTags(String input){
        return input.replaceAll("\\<.*?\\>", "");
    }

}
