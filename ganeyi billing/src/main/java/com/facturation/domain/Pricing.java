package com.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Pricing.
 */
@Document(collection = "pricing")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pricing implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("value")
    private Double value;

    @DBRef
    @Field("productLicense")
    @JsonIgnoreProperties(value = { "products", "pricings" }, allowSetters = true)
    private ProductLicense productLicense;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Pricing id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Double getValue() {
        return this.value;
    }

    public Pricing value(Double value) {
        this.setValue(value);
        return this;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public ProductLicense getProductLicense() {
        return this.productLicense;
    }

    public void setProductLicense(ProductLicense productLicense) {
        this.productLicense = productLicense;
    }

    public Pricing productLicense(ProductLicense productLicense) {
        this.setProductLicense(productLicense);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pricing)) {
            return false;
        }
        return id != null && id.equals(((Pricing) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pricing{" +
            "id=" + getId() +
            ", value=" + getValue() +
            "}";
    }
}
