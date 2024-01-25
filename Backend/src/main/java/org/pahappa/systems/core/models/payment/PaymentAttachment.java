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
    private String imageName;

    private byte[] imageAttachment;

    @Column(name = "file_name")
    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    @Lob
    @Column(name = "payment_attachment", columnDefinition = "LONGBLOB")
    public byte[] getImageAttachment() {
        return imageAttachment;
    }

    public void setImageAttachment(byte[] imageAttachment) {
        this.imageAttachment = imageAttachment;
    }
}
