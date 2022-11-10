package com.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Request.
 */
@Document(collection = "request")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Request implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("duration")
    private Integer duration;

    @Field("status")
    private String status;

    @Field("request_date")
    private LocalDate requestDate;

    @DBRef
    @Field("resultat")
    private Resultat resultat;

    @DBRef
    @Field("product")
    @JsonIgnoreProperties(value = { "apis", "request", "productLicense", "clients" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @DBRef
    @Field("forfait")
    @JsonIgnoreProperties(value = { "requests", "facture", "clients" }, allowSetters = true)
    private Forfait forfait;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Request id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public Request duration(Integer duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getStatus() {
        return this.status;
    }

    public Request status(String status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDate getRequestDate() {
        return this.requestDate;
    }

    public Request requestDate(LocalDate requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(LocalDate requestDate) {
        this.requestDate = requestDate;
    }

    public Resultat getResultat() {
        return this.resultat;
    }

    public void setResultat(Resultat resultat) {
        this.resultat = resultat;
    }

    public Request resultat(Resultat resultat) {
        this.setResultat(resultat);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setRequest(null));
        }
        if (products != null) {
            products.forEach(i -> i.setRequest(this));
        }
        this.products = products;
    }

    public Request products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Request addProduct(Product product) {
        this.products.add(product);
        product.setRequest(this);
        return this;
    }

    public Request removeProduct(Product product) {
        this.products.remove(product);
        product.setRequest(null);
        return this;
    }

    public Forfait getForfait() {
        return this.forfait;
    }

    public void setForfait(Forfait forfait) {
        this.forfait = forfait;
    }

    public Request forfait(Forfait forfait) {
        this.setForfait(forfait);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Request)) {
            return false;
        }
        return id != null && id.equals(((Request) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Request{" +
            "id=" + getId() +
            ", duration=" + getDuration() +
            ", status='" + getStatus() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            "}";
    }
}
