package org.pahappa.systems.core.models.payment;
//imports
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.sers.webutils.model.BaseEntity;

@Entity
@Table(name="payment_attachment")
public class PaymentAttachment extends BaseEntity {
    private String name;

    private byte[] imageAttachment;

    private byte[] pdfAttachment;

    @Column(name = "file_name")
    public String getName() {
        return name;
    }

    public void setName(String imageName) {
        this.name = imageName;
    }

    @Lob
    @Column(name = "image_payment_attachment", columnDefinition = "LONGBLOB", nullable = true)
    public byte[] getImageAttachment() {
        return imageAttachment;
    }

    public void setImageAttachment(byte[] imageAttachment) {
        this.imageAttachment = imageAttachment;
    }

    @Lob
    @Column(name = "pdf_payment_attachment", columnDefinition = "LONGBLOB", nullable = true)
    public byte[] getPdfAttachment() {
        return pdfAttachment;
    }

    public void setPdfAttachment(byte[] pdfAttachment) {
        this.pdfAttachment = pdfAttachment;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof PaymentAttachment && (super.getId() != null)
                ?super.getId().equals(((PaymentAttachment) obj).getId())
                :(obj == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null? this.getClass().hashCode() + super.getId().hashCode():super.hashCode();
    }
}
