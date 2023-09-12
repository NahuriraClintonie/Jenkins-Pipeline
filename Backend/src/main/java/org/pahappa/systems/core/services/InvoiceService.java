package org.pahappa.systems.core.services;

import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.services.base.GenericService;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public interface InvoiceService extends GenericService<Invoice> {
    void changeStatusToPendingApproval(Invoice invoice);
    void changeStatusToPaid(Invoice invoice, double amount);

    void changeStatusToPartiallyPaid(Invoice invoice, double amount);
    public String INVOICE_TEMPLATE =  "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Invoice</title>\n" +
            "    <style>\n" +
            "        .about {\n" +
            "            border-style: hidden;\n" +
            "            font-family: 'Times New Roman', Times, serif;\n" +
            "            color:  blue;\n" +
            "        }\n" +
            "        .about tr {\n" +
            "            border-style: hidden;\n" +
            "        }\n" +
            "\n" +
            "        table, th, td {\n" +
            "            width: 900px;\n" +
            "            border: 1px solid black;\n" +
            "               \n" +
            "        }\n" +
            "        .owner {\n" +
            "            border-style:hidden;\n" +
            "        }\n" +
            "\n" +
            "        table {\n" +
            "            border-collapse: collapse;\n" +
            "        }\n" +
            "        \n" +
            "        .invoice-details tr:nth-child(odd) {background-color: grey;} \n" +
            "            \n" +
            "        .invoice-faqs {\n" +
            "            font-size: small;\n" +
            "        }\n" +
            "        .invoice-faqs tr {\n" +
            "            border-style:hidden;\n" +
            "\n" +
            "        }\n" +
            "        .invoice-sum {\n" +
            "            color: blue;\n" +
           "            width: 400px;\n" +
            "           right:400px"+
            "        }\n" +
            "\n" +
            "       .info{\n"+
            "           width:900px;\n"+
            "        }\n" +
            "\n" +
            "    </style>\n" +
            "</head>\n" +
            "\n" +
            "<body>\n" +
            "        <table class=\"about\">\n" +
            "            <tr><td>Muteesa 2 Road</td></tr>\n" +
            "            <tr><td>Ntinda</td></tr>\n" +
            "            <tr><td>Kampala</td></tr>\n" +
            "            <tr><td><br/></td></tr>\n" +
            "            <tr><td>Tel- </td></tr>\n" +
            "            <tr><td>TIN- </td></tr>\n" +
            "        </table>\n" +
            "        <br/>\n" +
            "        <div >\n" +
            "        <table class=\"owner\">\n" +
            "            <tr>\n" +
            "                <thead style=\"background-color:#4169e1;color:white;\">\n" +
            "                <th style=\"text-align: left;\">BILL TO</th>\n" +
            "                <th>Date</th>\n" +
            "                <th>Invoice No.</th>\n" +
            "            </thead>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <td>%s</td>\n" +
            "                <td>%s</td>\n" +
            "                <td>%s</td>\n" +
            "            </tr>\n" +
            "        </table>\n" +
            "        <br/>\n" +
            "        <table class=\"owner\">\n" +
            "            <tr>\n" +
            "                <thead style=\"background-color:#4169e1; color:white\">\n" +
            "                <th style=\"text-align: left;\">P.O No. </th>\n" +
            "                <th>Terms</th>\n" +
            "                <th>Due Date</td>\n" +
            "                <th style=\"width:fit-content\">Account No.</th>\n" +
            "                <th>Project</th>\n" +
            "            </thead>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <td></td>\n" +
            "                <td></td>\n" +
            "                <td>%s</td>\n" +
            "                <td></td>\n" +
            "                <td></td>\n" +
            "            </tr>\n" +
            "        </table>\n" +
            "    </div>\n" +
            "        <br/>\n" +
            "        <div>\n" +
            "        <table class=\"invoice-details\">\n" +
            "            <tr>\n" +
            "                <th style=\"text-align: left;\">Description</th>\n" +
            "                <th>Quantity</th>\n" +
            "                <th>Rate</th>\n" +
            "                <th>Amount</th>\n" +
            "            </tr>\n" +
            "            \n" +
            "            <tr>\n" +
            "                <td>%s</td>\n" +
            "                <td>1</td>\n" +
            "                <td>%d</td>\n" +
            "                <td>%f</td>\n" +
            "            </tr>\n" +
            "        \n" +
            "        </table>\n" +
            "    </div>\n" +
            "        <br/>\n" +
            "\n" +
            "        <div class=\"info\"  style=\"display:flex\">\n" +
            "        <table class=\"invoice-faqs\">\n" +
            "            <tr><td>Terms & Conditions</td></tr>\n" +
            "            <tr><td>Currency: UGX</td></tr>\n" +
            "            <tr><td>Bank: Centenary Bank</td></tr>\n" +
            "            <tr><td>Account Name: PAHAPPA LIMITED</td></tr>\n" +
            "            <tr><td>Account No. 3100062448</td></tr>\n" +
            "            <tr><td>Bank Code CERBUGKA</td></tr>\n" +
            "            <tr><td>Make ALL Cheques payable to PAHAPPA LIMITED</td></tr>\n" +
            "            <tr><td>Airtel Pay 1190866</td></tr>\n" +
            "            <tr><td>Momo Pay 608913</td></tr>\n" +
            "        </table>\n" +
            "        <br/>\n" +
            "        <table class=\"invoice-sum\">\n" +
            "            <tr>\n" +
            "                <th>Total</th>\n" +
            "                <td>%f</td>\n" +
            "            </tr>\n" +
            "            <tr>\n" +
            "                <th>Payments/ Credits</th>\n" +
            "                <td>0</td>\n" +
            "            </tr>   \n" +
            "            <tr>\n" +
            "                <th>Balance Due</th>\n" +
            "                <td>%f</td>\n" +
            "            </tr>   \n" +
            "        </table> \n" +
            "    </div>  \n" +
            "</body>\n" +
            "</html>" ;

     static String generateInvoice(Invoice invoice){

      SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
         String invoiceDate = dateFormat.format(invoice.getDateCreated());
         String dueDate = dateFormat.format(invoice.getInvoiceDueDate());
         String firstName = invoice.getClientSubscription().getClient().getClientFirstName();
         String productName = invoice.getClientSubscription().getSubscription().getProduct().getProductName();
        int duration = invoice.getClientSubscription().getSubscription().getSubscriptionDuration();
        double price = invoice.getClientSubscription().getSubscriptionPrice();

         return String.format(INVOICE_TEMPLATE,
                 firstName,
            invoiceDate,
            invoice.getInvoiceNumber(),
            dueDate,
            //invoice.getClientSubscription().getSubscription().getProduct().getProductName(),
                 productName,
            //invoice.getClientSubscription().getSubscription().getSubscriptionDuration(),
                 duration,
            //invoice.getClientSubscription().getSubscription_price(),
                 price,
            invoice.getInvoiceTotalAmount(),
            invoice.getInvoiceBalance()
         );
    }

     List<Invoice> getInvoiceByStatus();



    public Invoice getInvoiceByClientSubscriptionId(String id);

    public List<Invoice> getInvoiceByStatusPaid(Date startDate);


}
