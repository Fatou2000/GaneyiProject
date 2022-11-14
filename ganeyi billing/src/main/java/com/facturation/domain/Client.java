package com.facturation.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Client.
 */
@Document(collection = "client")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Client implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("account_id")
    private String accountId;

    @Field("company_name")
    private String companyName;

    @Field("first_name")
    private String firstName;

    @Field("address")
    private String address;

    @Field("phone_number")
    private String phoneNumber;

    @DBRef
    @Field("facture")
    @JsonIgnoreProperties(value = { "forfait", "payment", "client" }, allowSetters = true)
    private Set<Facture> factures = new HashSet<>();

    @DBRef
    @Field("products")
    @JsonIgnoreProperties(value = { "apis", "request", "productLicense", "clients" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    @DBRef
    @Field("forfaits")
    @JsonIgnoreProperties(value = { "requests", "facture", "clients" }, allowSetters = true)
    private Set<Forfait> forfaits = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Client id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountId() {
        return this.accountId;
    }

    public Client accountId(String accountId) {
        this.setAccountId(accountId);
        return this;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Client companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public Client firstName(String firstName) {
        this.setFirstName(firstName);
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAddress() {
        return this.address;
    }

    public Client address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public Client phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Set<Facture> getFactures() {
        return this.factures;
    }

    public void setFactures(Set<Facture> factures) {
        if (this.factures != null) {
            this.factures.forEach(i -> i.setClient(null));
        }
        if (factures != null) {
            factures.forEach(i -> i.setClient(this));
        }
        this.factures = factures;
    }

    public Client factures(Set<Facture> factures) {
        this.setFactures(factures);
        return this;
    }

    public Client addFacture(Facture facture) {
        this.factures.add(facture);
        facture.setClient(this);
        return this;
    }

    public Client removeFacture(Facture facture) {
        this.factures.remove(facture);
        facture.setClient(null);
        return this;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }

    public Client products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Client addProduct(Product product) {
        this.products.add(product);
        product.getClients().add(this);
        return this;
    }

    public Client removeProduct(Product product) {
        this.products.remove(product);
        product.getClients().remove(this);
        return this;
    }

    public Set<Forfait> getForfaits() {
        return this.forfaits;
    }

    public void setForfaits(Set<Forfait> forfaits) {
        this.forfaits = forfaits;
    }

    public Client forfaits(Set<Forfait> forfaits) {
        this.setForfaits(forfaits);
        return this;
    }

    public Client addForfait(Forfait forfait) {
        this.forfaits.add(forfait);
        forfait.getClients().add(this);
        return this;
    }

    public Client removeForfait(Forfait forfait) {
        this.forfaits.remove(forfait);
        forfait.getClients().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Client)) {
            return false;
        }
        return id != null && id.equals(((Client) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Client{" +
            "id=" + getId() +
            ", accountId='" + getAccountId() + "'" +
            ", companyName='" + getCompanyName() + "'" +
            ", firstName='" + getFirstName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            "}";
    }
}
