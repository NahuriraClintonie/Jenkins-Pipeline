package org.pahappa.systems.core.services;
//imports
import com.googlecode.genericdao.search.Search;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.DashedBorder;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import org.pahappa.systems.core.models.clientSubscription.ClientSubscription;
import org.pahappa.systems.core.models.invoice.Invoice;
import org.pahappa.systems.core.models.paymentTerms.PaymentTerms;
import org.pahappa.systems.core.services.base.GenericService;
import org.sers.webutils.model.exception.OperationFailedException;
import org.sers.webutils.model.exception.ValidationFailedException;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface InvoiceService extends GenericService<Invoice> {

    void changeStatusToPendingApproval(Invoice invoice);
    void changeStatusToPaid(Invoice invoice, double amount);

    void changeStatusToUnpaid(Invoice instance);

    void changeStatusToPartiallyPaid(Invoice invoice, double amount) throws ValidationFailedException, OperationFailedException;

    byte[] generateInvoicePdf(Invoice invoice, PaymentTerms paymentTerms);


    static Cell getHeaderTextCell(String textValue){
        return new Cell().add(textValue).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT);
    }

    static Cell getHeaderTextValue(String textValue){
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getHeaderTextValue1(String textValue){
        return new Cell().add(textValue).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getBillingAndShippingCell(String textValue){
        return new Cell().add(textValue).setFontSize(12f).setBold().setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
    }

    static Cell getCell10fLeft(String textValue, Boolean isBold){
        Cell myCell = new Cell().add(textValue).setFontSize(10f).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.LEFT);
        return isBold?myCell.setBold():myCell;
    }

     List<Invoice> getInvoiceByStatus();



    public List<Invoice> getInvoiceByClientSubscriptionId(List<ClientSubscription> clientSubscriptions);

    public List<Invoice> getInvoiceByStatusPaid(Date startDate);


    List<Invoice> getInstances(Search search);
}
