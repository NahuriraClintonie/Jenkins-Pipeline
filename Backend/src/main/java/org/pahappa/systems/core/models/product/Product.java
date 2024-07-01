package org.pahappa.systems.core.models.product;

import org.sers.webutils.model.BaseEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name="products")
public class Product extends BaseEntity {
    private String productName;

    private String productDescription;

    @Column(name="product_name")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @Column(name="product_description")
    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Product && (super.getId() != null) ? super.getId().equals(((Product) o).getId()) : (o == this);
    }

    @Override
    public int hashCode() {
        return super.getId() != null ? Objects.hash(super.getId()) : super.hashCode();
    }

    @Override
    public String toString() {
        return productName;
    }
}
