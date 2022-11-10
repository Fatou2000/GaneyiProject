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
 * A ProductLicense.
 */
@Document(collection = "product_license")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductLicense implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("access_key")
    private String accessKey;

    @Field("start_date")
    private LocalDate startDate;

    @Field("end_date")
    private LocalDate endDate;

    @Field("is_active")
    private Boolean isActive;

    @DBRef
    @Field("product")
    @JsonIgnoreProperties(value = { "apis", "request", "productLicense", "clients" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @DBRef
    @Field("pricing")
    @JsonIgnoreProperties(value = { "productLicense" }, allowSetters = true)
    private Set<Pricing> pricings = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ProductLicense id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccessKey() {
        return this.accessKey;
    }

    public ProductLicense accessKey(String accessKey) {
        this.setAccessKey(accessKey);
        return this;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public ProductLicense startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public ProductLicense endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ProductLicense isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.setProductLicense(null));
        }
        if (products != null) {
            products.forEach(i -> i.setProductLicense(this));
        }
        this.products = products;
    }

    public ProductLicense products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public ProductLicense addProduct(Product product) {
        this.products.add(product);
        product.setProductLicense(this);
        return this;
    }

    public ProductLicense removeProduct(Product product) {
        this.products.remove(product);
        product.setProductLicense(null);
        return this;
    }

    public Set<Pricing> getPricings() {
        return this.pricings;
    }

    public void setPricings(Set<Pricing> pricings) {
        if (this.pricings != null) {
            this.pricings.forEach(i -> i.setProductLicense(null));
        }
        if (pricings != null) {
            pricings.forEach(i -> i.setProductLicense(this));
        }
        this.pricings = pricings;
    }

    public ProductLicense pricings(Set<Pricing> pricings) {
        this.setPricings(pricings);
        return this;
    }

    public ProductLicense addPricing(Pricing pricing) {
        this.pricings.add(pricing);
        pricing.setProductLicense(this);
        return this;
    }

    public ProductLicense removePricing(Pricing pricing) {
        this.pricings.remove(pricing);
        pricing.setProductLicense(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductLicense)) {
            return false;
        }
        return id != null && id.equals(((ProductLicense) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductLicense{" +
            "id=" + getId() +
            ", accessKey='" + getAccessKey() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
