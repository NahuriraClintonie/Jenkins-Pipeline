package org.pahappa.systems.core.models.companyLogo;

import org.sers.webutils.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "company_logo")
public class CompanyLogo extends BaseEntity {
    private byte[] logoName;
    private String logoPath;

    private byte[] waterMarkName;
    private String waterMarkPath;

    //generate getters and setters
    @Column(name = "logo_name", columnDefinition = "LONGBLOB")
    public byte[] getLogoName() {
        return logoName;
    }
    public void setLogoName(byte[] logoName) {
        this.logoName = logoName;
    }
    @Column(name = "logo_path")
    public String getLogoPath() {
        return logoPath;
    }
    public void setLogoPath(String logoPath) {
        this.logoPath = logoPath;
    }
    @Column(name = "water_mark_name", columnDefinition = "LONGBLOB")
    public byte[] getWaterMarkName() {
        return waterMarkName;
    }
    public void setWaterMarkName(byte[] waterMarkName) {
        this.waterMarkName = waterMarkName;
    }
    @Column(name = "water_mark_path")
    public String getWaterMarkPath() {
        return waterMarkPath;
    }
    public void setWaterMarkPath(String waterMarkPath) {
        this.waterMarkPath = waterMarkPath;
    }

}
