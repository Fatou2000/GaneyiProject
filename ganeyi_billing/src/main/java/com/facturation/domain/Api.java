package com.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Api.
 */
@Document(collection = "api")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Api implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("version")
    private String version;

    @Field("service_url")
    private String serviceURL;

    @Field("doc_url")
    private String docURL;

    @Field("is_actice")
    private Boolean isActice;

    @DBRef
    @Field("product")
    @JsonIgnoreProperties(value = { "apis", "request", "productLicense", "clients" }, allowSetters = true)
    private Product product;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Api id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVersion() {
        return this.version;
    }

    public Api version(String version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getServiceURL() {
        return this.serviceURL;
    }

    public Api serviceURL(String serviceURL) {
        this.setServiceURL(serviceURL);
        return this;
    }

    public void setServiceURL(String serviceURL) {
        this.serviceURL = serviceURL;
    }

    public String getDocURL() {
        return this.docURL;
    }

    public Api docURL(String docURL) {
        this.setDocURL(docURL);
        return this;
    }

    public void setDocURL(String docURL) {
        this.docURL = docURL;
    }

    public Boolean getIsActice() {
        return this.isActice;
    }

    public Api isActice(Boolean isActice) {
        this.setIsActice(isActice);
        return this;
    }

    public void setIsActice(Boolean isActice) {
        this.isActice = isActice;
    }

    public Product getProduct() {
        return this.product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Api product(Product product) {
        this.setProduct(product);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Api)) {
            return false;
        }
        return id != null && id.equals(((Api) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Api{" +
            "id=" + getId() +
            ", version='" + getVersion() + "'" +
            ", serviceURL='" + getServiceURL() + "'" +
            ", docURL='" + getDocURL() + "'" +
            ", isActice='" + getIsActice() + "'" +
            "}";
    }
}
