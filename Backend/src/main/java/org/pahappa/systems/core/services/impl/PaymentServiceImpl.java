package org.pahappa.systems.core.services.impl;

import com.googlecode.genericdao.search.Search;
import org.pahappa.systems.core.constants.InvoiceStatus;
import org.pahappa.systems.core.constants.PaymentMethod;
import org.pahappa.systems.core.constants.PaymentStatus;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.payment.Payment;
import org.pahappa.systems.core.sendInvoice.SendInvoice;
import org.pahappa.systems.core.services.InvoiceService;
import org.pahappa.systems.core.services.PaymentService;
import org.pahappa.systems.core.services.base.impl.GenericServiceImpl;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;
import org.sers.webutils.server.core.utils.ApplicationContextProvider;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;


@Transactional
@Service
public class PaymentServiceImpl extends GenericServiceImpl<Payment> implements PaymentService {

    Payment savedPayment = null;
    private InvoiceService invoiceService;
    @PostConstruct
    public void init(){
        this.invoiceService = ApplicationContextProvider.getBean(InvoiceService.class);
    }

    @Override
    public Payment saveInstance(Payment payment) throws ValidationFailedException, OperationFailedException {
        try {

            if(payment.getStatus().equals(PaymentStatus.PENDING)){
                this.invoiceService.changeStatusToPendingApproval(payment.getInvoice());
                savedPayment = save(payment);
            }
            else if(payment.getStatus().equals(PaymentStatus.APPROVED)){

                if(payment.getAmountPaid() == payment.getInvoice().getInvoiceTotalAmount()) {
                    this.invoiceService.changeStatusToPaid(payment.getInvoice(), payment.getAmountPaid());
                }
                else {
                    this.invoiceService.changeStatusToPartiallyPaid(payment.getInvoice(), payment.getAmountPaid());
                }

                savedPayment = save(payment);
               String receipt = generateReceiptHtml(payment);
               SendInvoice.sendInvoice(receipt,payment.getInvoice());

            }



            return savedPayment;
        } catch (Exception e) {
            throw new OperationFailedException("Failed to save payment.", e);
        }
    }

    public List<Payment> getPaymentsWithPendingApprovalInvoices(){
        Search search = new Search();
        search.addFilterEqual("invoice.invoiceStatus", InvoiceStatus.PENDING_APPROVAL);
        return super.search(search);
    }

    @Override
    public boolean isDeletable(Payment instance) throws OperationFailedException {
        return false;
    }

    public String generateReceiptHtml(Payment payment) {
        String template = loadTemplate("templates/receipt_template.xhtml");; // Read the template from a file or store it as a resource
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Replace placeholders with actual data
        System.out.println("okay. here we are");
        String receiptHtml = template
                .replace("{date}", new Date().toString())
                .replace("{receiptNo}", payment.getInvoice().getInvoiceNumber())
                .replace("{receivedFrom}", payment.getInvoice().getClientSubscription().getClient().getClientFirstName() +" " +payment.getInvoice().getClientSubscription().getClient().getClientLastName())
                .replace("{paidThrough}", payment.getPaymentMethod().toString())
                .replace("{amountPaid}", String.valueOf(payment.getAmountPaid()))
                .replace("{balanceDue}", String.valueOf(payment.getInvoice().getInvoiceBalance()))
                .replace("{receivedBy}", "Pahappa Limited");


        return receiptHtml;
    }

    private String loadTemplate(String templateFileName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(templateFileName);
            if (inputStream != null) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder template = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    template.append(line).append("\n");
                }
                return template.toString();
            } else {
                // Handle error if template file is not found
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
    
}
