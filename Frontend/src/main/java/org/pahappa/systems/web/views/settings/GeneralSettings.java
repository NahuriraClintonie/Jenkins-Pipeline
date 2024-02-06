package org.pahappa.systems.web.views.settings;
//imports
import lombok.Getter;
import lombok.Setter;
import org.pahappa.systems.core.models.appEmail.EmailSetup;
import org.pahappa.systems.core.models.companyLogo.CompanyLogo;
import org.pahappa.systems.core.models.invoice.InvoiceTax;
import org.pahappa.systems.core.services.ApplicationEmailService;
import org.pahappa.systems.core.services.CompanyLogoService;
import org.pahappa.systems.core.services.EmailSetupService;
import org.pahappa.systems.core.services.InvoiceTaxService;

import org.pahappa.systems.core.services.impl.ApplicationEmailServiceImpl;
import org.pahappa.systems.web.views.UiUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;
import org.sers.webutils.client.views.presenters.WebFormView;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "generalSettingsView")
@SessionScoped
@Getter
@Setter
public class GeneralSettings extends WebFormView<EmailSetup, GeneralSettings, GeneralSettings> {
    private EmailSetupService emailSetupService;
    private InvoiceTaxService invoiceTaxService;
    private InvoiceTax invoiceTax;
    private CompanyLogo companyLogo;
    private ApplicationEmailService applicationEmailService;
    private CompanyLogoService companyLogoService;


    @Override
    public void persist() throws Exception {

    }

    public void save() throws ValidationFailedException, OperationFailedException {
        applicationEmailService =new ApplicationEmailServiceImpl();
        this.emailSetupService.saveInstance(super.model);
        UiUtils.showMessageBox("Action Successful", "EmailSetup is successful");
    }

    public void saveCompanyLogo() throws ValidationFailedException, OperationFailedException {
        this.companyLogoService.saveInstance(companyLogo);
        UiUtils.showMessageBox("Action Successful", "Company Logos saved successful");
        companyLogo = new CompanyLogo();
    }

    public void saveTaxToBeUsed() throws ValidationFailedException, OperationFailedException {
        this.invoiceTaxService.saveInstance(invoiceTax);
        UiUtils.showMessageBox("Action Successful", "Tax saved successful");
    }

    @Override
    public void beanInit() {
        emailSetupService = ApplicationContextProvider.getBean(EmailSetupService.class);
        invoiceTaxService = ApplicationContextProvider.getBean(InvoiceTaxService.class);
        companyLogoService = ApplicationContextProvider.getBean(CompanyLogoService.class);

        if(invoiceTaxService.getAllInstances().isEmpty()) {
            invoiceTax = new InvoiceTax();
        }else {
            invoiceTax = invoiceTaxService.getAllInstances().get(0);
        }

        if (emailSetupService.getActiveEmail()==null){
            resetModal();
        }else{
            super.model = emailSetupService.getActiveEmail();
        }

        if(companyLogoService.getAllInstances().isEmpty()) {
            companyLogo = new CompanyLogo();
        }else {
            companyLogo = companyLogoService.getAllInstances().get(0);
        }

    }

    @Override
    public void pageLoadInit() {

    }

    public void logOut(){
        invalidateSession();
    }

    public void resetModal(){
        super.resetModal();
        super.model = new EmailSetup();
    }

    public void handleLogoUpload(FileUploadEvent event){
        System.out.println("Starting image upload");
        UploadedFile uploadedFile = event.getFile();

        if (isValidContentType(uploadedFile.getContentType())) {
            byte[] receiptImageBytes = uploadedFile.getContents();
            String fileName = uploadedFile.getFileName();
            System.out.println("file name "+fileName);

            companyLogo.setLogoName(receiptImageBytes);
            companyLogo.setLogoPath(fileName);

            System.out.println("payment attachment file name "+ companyLogo.getLogoPath());

            System.out.println("File is uploaded");
        } else {
            System.out.println("File ain't of the required type");
        }


    }

    public void handleWaterMarkUpload(FileUploadEvent event){
        System.out.println("Starting waterMark upload");
        UploadedFile uploadedFile = event.getFile();

        if (isValidContentType(uploadedFile.getContentType())) {
            byte[] waterMarkImageBytes = uploadedFile.getContents();
            String fileName = uploadedFile.getFileName();
            System.out.println("file name "+fileName);

            companyLogo.setWaterMarkName(waterMarkImageBytes);
            companyLogo.setWaterMarkPath(fileName);

            System.out.println("payment attachment file name "+ companyLogo.getLogoPath());

            System.out.println("File is uploaded");
        } else {
            System.out.println("File ain't of the required type");
        }


    }

    private boolean isValidContentType(String contentType) {
        // Implement your logic to validate content type, e.g., check if it's an image
        return contentType != null && contentType.startsWith("image/") && (contentType.endsWith("jpeg") || contentType.endsWith("jpg") || contentType.endsWith("png") || contentType.endsWith("gif"));
    }
}
