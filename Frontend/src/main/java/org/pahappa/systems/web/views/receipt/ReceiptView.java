package org.pahappa.systems.web.views.receipt;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.*;
import java.text.SimpleDateFormat;
import org.pahappa.systems.core.models.receipt.Receipt;
import java.util.Date;

@ManagedBean(name="receiptView")
@ViewScoped
public class ReceiptView implements Serializable {
    private Receipt receipt;

    public String generateReceiptHtml() {
        String template = loadTemplate("templates/receipt_template.xhtml");; // Read the template from a file or store it as a resource
        // SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        // Replace placeholders with actual data
        System.out.println("okay. here we are");
        String receiptHtml = template
                .replace("{date}", new Date().toString())
                .replace("{receiptNo}", "receipt.getReceiptNo()")
                .replace("{receivedFrom}", "receipt.getReceivedFrom()")
                .replace("{paidThrough}", "receipt.getPaymentMethod()")
                .replace("{amountPaid}", "String.valueOf(receipt.getAmountPaid())")
                .replace("{balanceDue}", "String.valueOf(receipt.getBalanceDue())")
                .replace("{receivedBy}", "receipt.getReceivedBy()")
                .replace("{signature}", "receipt.getSignature()");

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
